/*
 * Copyright 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.dev.jjs;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.CompilationUnitDeclaration;
import org.eclipse.jdt.internal.compiler.ast.TypeDeclaration;

import rocket.compiler.Compiler;
import rocket.compiler.JavaCompilationWorker;
import rocket.compiler.JavaScriptCompilationWorker;
import rocket.compiler.JavaScriptSourceChecker;
import rocket.compiler.NullJavaCompilationWorker;
import rocket.compiler.NullJavaScriptCompilationWorker;
import rocket.compiler.StaticFieldClinitRemover;
import rocket.compiler.StaticMethodClinitRemover;
import rocket.logging.compiler.LoggerOptimiser;
import rocket.logging.compiler.LoggingLevelByNameAssigner;
import rocket.logging.compiler.NoneLoggingFactoryGetLoggerOptimiser;
import rocket.logging.util.LoggingPropertyReader;
import rocket.util.client.Checker;

import com.google.gwt.core.ext.BadPropertyValueException;
import com.google.gwt.core.ext.PropertyOracle;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.CompilationUnitProvider;
import com.google.gwt.dev.jdt.ICompilationUnitAdapter;
import com.google.gwt.dev.jdt.RebindOracle;
import com.google.gwt.dev.jdt.RebindPermutationOracle;
import com.google.gwt.dev.jdt.WebModeCompilerFrontEnd;
import com.google.gwt.dev.jjs.InternalCompilerException.NodeInfo;
import com.google.gwt.dev.jjs.ast.JClassType;
import com.google.gwt.dev.jjs.ast.JExpression;
import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JMethodBody;
import com.google.gwt.dev.jjs.ast.JMethodCall;
import com.google.gwt.dev.jjs.ast.JNewInstance;
import com.google.gwt.dev.jjs.ast.JProgram;
import com.google.gwt.dev.jjs.ast.JReferenceType;
import com.google.gwt.dev.jjs.impl.ArrayNormalizer;
import com.google.gwt.dev.jjs.impl.AssertionRemover;
import com.google.gwt.dev.jjs.impl.BuildTypeMap;
import com.google.gwt.dev.jjs.impl.CastNormalizer;
import com.google.gwt.dev.jjs.impl.CatchBlockNormalizer;
import com.google.gwt.dev.jjs.impl.CompoundAssignmentNormalizer;
import com.google.gwt.dev.jjs.impl.DeadCodeElimination;
import com.google.gwt.dev.jjs.impl.GenerateJavaAST;
import com.google.gwt.dev.jjs.impl.GenerateJavaScriptAST;
import com.google.gwt.dev.jjs.impl.JavaScriptObjectCaster;
import com.google.gwt.dev.jjs.impl.MakeCallsStatic;
import com.google.gwt.dev.jjs.impl.MethodAndClassFinalizer;
import com.google.gwt.dev.jjs.impl.MethodCallTightener;
import com.google.gwt.dev.jjs.impl.MethodInliner;
import com.google.gwt.dev.jjs.impl.Pruner;
import com.google.gwt.dev.jjs.impl.ReplaceRebinds;
import com.google.gwt.dev.jjs.impl.TypeMap;
import com.google.gwt.dev.jjs.impl.TypeTightener;
import com.google.gwt.dev.js.JsNormalizer;
import com.google.gwt.dev.js.JsObfuscateNamer;
import com.google.gwt.dev.js.JsPrettyNamer;
import com.google.gwt.dev.js.JsSourceGenerationVisitor;
import com.google.gwt.dev.js.JsSymbolResolver;
import com.google.gwt.dev.js.JsVerboseNamer;
import com.google.gwt.dev.js.ast.JsProgram;
import com.google.gwt.dev.util.DefaultTextOutput;
import com.google.gwt.dev.util.Util;

/**
 * Compiles the Java <code>JProgram</code> representation into its
 * corresponding JavaScript source.
 */
public class JavaToJavaScriptCompiler {

  private static void findEntryPoints(TreeLogger logger,
      RebindOracle rebindOracle, String[] mainClassNames, JProgram program)
      throws UnableToCompleteException {
    JMethod bootStrapMethod = program.createMethod(null, "init".toCharArray(), null, program.getTypeVoid(), false, true, true, false, false);
    bootStrapMethod.freezeParamTypes();

    for (int i = 0; i < mainClassNames.length; ++i) {
      String mainClassName = mainClassNames[i];
      JReferenceType referenceType = program.getFromTypeMap(mainClassName);

      if (referenceType == null) {
        logger.log(TreeLogger.ERROR,
            "Could not find module entry point class '" + mainClassName + "'",
            null);
        throw new UnableToCompleteException();
      }

      JExpression qualifier = null;
      JMethod mainMethod = findMainMethod(referenceType);
      if (mainMethod == null || !mainMethod.isStatic()) {
        // Couldn't find a static main method; must rebind the class
        String originalClassName = mainClassName;
        mainClassName = rebindOracle.rebind(logger, originalClassName);
        referenceType = program.getFromTypeMap(mainClassName);
        if (referenceType == null) {
          logger.log(TreeLogger.ERROR,
              "Could not find module entry point class '" + mainClassName
                  + "' after rebinding from '" + originalClassName + "'", null);
          throw new UnableToCompleteException();
        }

        if (!(referenceType instanceof JClassType)) {
          logger.log(TreeLogger.ERROR, "Module entry point class '"
              + mainClassName + "' must be a class", null);
          throw new UnableToCompleteException();
        }

        JClassType mainClass = (JClassType) referenceType;
        if (mainClass.isAbstract()) {
          logger.log(TreeLogger.ERROR, "Module entry point class '"
              + mainClassName + "' must not be abstract", null);
          throw new UnableToCompleteException();
        }

        mainMethod = findMainMethodRecurse(referenceType);
        if (mainMethod == null) {
          logger.log(TreeLogger.ERROR,
              "Could not find entry method 'onModuleLoad()' method in entry point class '"
                  + mainClassName + "'", null);
          throw new UnableToCompleteException();
        }

        if (mainMethod.isAbstract()) {
          logger.log(TreeLogger.ERROR,
              "Entry method 'onModuleLoad' in entry point class '"
                  + mainClassName + "' must not be abstract", null);
          throw new UnableToCompleteException();
        }

        if (!mainMethod.isStatic()) {
          // Find the appropriate (noArg) constructor
          JMethod noArgCtor = null;
          for (int j = 0; j < mainClass.methods.size(); ++j) {
            JMethod ctor = (JMethod) mainClass.methods.get(j);
            if (ctor.getName().equals(mainClass.getShortName())) {
              if (ctor.params.size() == 0) {
                noArgCtor = ctor;
              }
            }
          }
          if (noArgCtor == null) {
            logger.log(
                TreeLogger.ERROR,
                "No default (zero argument) constructor could be found in entry point class '"
                    + mainClassName
                    + "' to qualify a call to non-static entry method 'onModuleLoad'",
                null);
            throw new UnableToCompleteException();
          }

          // Construct a new instance of the class to qualify the non-static
          // call
          JNewInstance newInstance = new JNewInstance(program, null, mainClass);
          qualifier = new JMethodCall(program, null, newInstance, noArgCtor);
        }
      }

      // qualifier will be null if onModuleLoad is static
      JMethodCall onModuleLoadCall = new JMethodCall(program, null, qualifier,
          mainMethod);
      JMethodBody body = (JMethodBody) bootStrapMethod.getBody();
      body.getStatements().add(onModuleLoadCall.makeStatement());
    }
    program.addEntryMethod(bootStrapMethod);
  }

  private static JMethod findMainMethod(JReferenceType referenceType) {
    for (int j = 0; j < referenceType.methods.size(); ++j) {
      JMethod method = (JMethod) referenceType.methods.get(j);
      if (method.getName().equals("onModuleLoad")) {
        if (method.params.size() == 0) {
          return method;
        }
      }
    }
    return null;
  }

  private static JMethod findMainMethodRecurse(JReferenceType referenceType) {
    for (JReferenceType it = referenceType; it != null; it = it.extnds) {
      JMethod result = findMainMethod(it);
      if (result != null) {
        return result;
      }
    }
    return null;
  }

  private final String[] declEntryPoints;
  private final CompilationUnitDeclaration[] goldenCuds;
  private long lastModified;
  private final boolean obfuscate;
  private final boolean prettyNames;
  private final Set/* <IProblem> */problemSet = new HashSet/* <IProblem> */();

  public JavaToJavaScriptCompiler(final TreeLogger logger,
      final WebModeCompilerFrontEnd compiler, final String[] declEntryPts)
      throws UnableToCompleteException {
    this(logger, compiler, declEntryPts, true, false);
  }

  public JavaToJavaScriptCompiler(final TreeLogger logger,
      final WebModeCompilerFrontEnd compiler, final String[] declEntryPts,
      boolean obfuscate, boolean prettyNames) throws UnableToCompleteException {

    if (declEntryPts.length == 0) {
      throw new IllegalArgumentException("entry point(s) required");
    }

    // Remember these for subsequent compiles.
    //
    this.declEntryPoints = declEntryPts;

    // Should we obfuscate or, if not, use pretty names?
    //
    this.obfuscate = obfuscate;
    this.prettyNames = prettyNames;

    // Find all the possible rebound entry points.
    //
    RebindPermutationOracle rpo = compiler.getRebindPermutationOracle();
    Set allEntryPoints = new HashSet();
    for (int i = 0; i < declEntryPts.length; i++) {
      String[] all = rpo.getAllPossibleRebindAnswers(logger, declEntryPts[i]);
      Util.addAll(allEntryPoints, all);
    }
    String[] entryPts = Util.toStringArray(allEntryPoints);

    // Add intrinsics needed for code generation.
    //
    int k = entryPts.length;
    String[] seedTypeNames = new String[k + 3];
    System.arraycopy(entryPts, 0, seedTypeNames, 0, k);
    seedTypeNames[k++] = "com.google.gwt.lang.Array";
    seedTypeNames[k++] = "com.google.gwt.lang.Cast";
    seedTypeNames[k++] = "com.google.gwt.lang.Exceptions";

    // Compile the source and get the compiler so we can get the parse tree
    //
    goldenCuds = compiler.getCompilationUnitDeclarations(logger, seedTypeNames);

    // Check for compilation problems. We don't log here because any problems
    // found here will have already been logged by AbstractCompiler.
    //
    checkForErrors(logger, false);

    // Find the newest of all these.
    //
    lastModified = 0;
    CompilationUnitProvider newestCup = null;
    for (int i = 0; i < goldenCuds.length; i++) {
      CompilationUnitDeclaration cud = goldenCuds[i];
      ICompilationUnitAdapter icua = (ICompilationUnitAdapter) cud.compilationResult.compilationUnit;
      CompilationUnitProvider cup = icua.getCompilationUnitProvider();
      long cupLastModified = cup.getLastModified();
      if (cupLastModified > lastModified) {
        newestCup = cup;
        lastModified = cupLastModified;
      }
    }
    if (newestCup != null) {
      String loc = newestCup.getLocation();
      String msg = "Newest compilation unit is '" + loc + "'";
      logger.log(TreeLogger.DEBUG, msg, null);
    }
  }

  /**
   * Creates finished JavaScript source code from the specified Java compilation
   * units.
   */
  public String compile(TreeLogger logger, RebindOracle rebindOracle, final PropertyOracle propertyOracle)
      throws UnableToCompleteException {

    try {

      // (1) Build a flattened map of TypeDeclarations => JType.
      //

      // Note that all reference types (even nested and local ones) are in the
      // resulting type map. BuildTypeMap also parses all JSNI.
      //
      JProgram jprogram = new JProgram(logger, rebindOracle);
      TypeMap typeMap = new TypeMap(jprogram);
      JsProgram jsProgram = new JsProgram();
      TypeDeclaration[] allTypeDeclarations = BuildTypeMap.exec(typeMap,
          goldenCuds, jsProgram);

      // BuildTypeMap can uncover syntactic JSNI errors; report & abort
      // 
      checkForErrors(logger, true);

      // Compute all super type/sub type info
      jprogram.typeOracle.computeBeforeAST();

      // (3) Create a normalized Java AST using our own notation.
      //

      // Create the tree from JDT
      GenerateJavaAST.exec(allTypeDeclarations, typeMap, jprogram);

      // GenerateJavaAST can uncover semantic JSNI errors; report & abort
      // 
      checkForErrors(logger, true);

      // TODO: figure out how to have a debug mode.
      boolean isDebugEnabled = false;
      if (!isDebugEnabled) {
        // Remove all assert statements.
        AssertionRemover.exec(jprogram);
      }

      // Compute which classes have clinits
      jprogram.typeOracle.computeAfterAST();
      
		// TODO ROCKET When upgrading from GWT 1.4.60 reapply changes. 
		final LoggerOptimiser loggerOptimiser = this.getLoggerOptimiser(jprogram, propertyOracle, logger);

		loggerOptimiser.execute();

      // Fix up GWT.create() into new operations
      ReplaceRebinds.exec(jprogram);

      // Rebind each entry point.
      //
      findEntryPoints(logger, rebindOracle, declEntryPoints, jprogram);

      // (4) Optimize the normalized Java AST
      boolean didChange;
      
      // TODO ROCKET When upgrading from GWT 1.4.60 reapply changes.

      // warnings generator...
      final JavaCompilationWorker longNotifier = this.createJavaCompilationWorker( rocket.compiler.LongNotifier.class, logger );

      final JavaCompilationWorker testJavaCompilationWorker = this.createTestJavaCompilationWorker( logger );
      
      // actual optimisers
      final JavaCompilationWorker alternateValuesAssignmentOptimiser =  this.createJavaCompilationWorker(rocket.compiler.AlternateValuesAssignmentOptimiser.class,logger);
      final JavaCompilationWorker alternateValuesReturnedOptimiser = this.createJavaCompilationWorker( rocket.compiler.AlternateValuesReturnedOptimiser.class,logger);
      final JavaCompilationWorker conditionalAssignmentOptimiser = this.createJavaCompilationWorker( rocket.compiler.ConditionalAssignmentOptimiser.class,logger);
      final JavaCompilationWorker trailingReturnRemover = this.createJavaCompilationWorker( rocket.compiler.TrailingReturnRemover.class,logger);
      final JavaCompilationWorker variableAssignedToSelfRemover = this.createJavaCompilationWorker( rocket.compiler.VariableAssignedToSelfRemover.class,logger);
      final JavaCompilationWorker unusedLocalVariableRemover = this.createJavaCompilationWorker( rocket.compiler.UnusedLocalVariableRemover.class,logger);
      final JavaCompilationWorker localVariableFinalMaker = this.createJavaCompilationWorker( rocket.compiler.LocalVariableFinalMaker.class,logger);
      final JavaCompilationWorker variableUpdaterOptimiser = this.createJavaCompilationWorker( rocket.compiler.VariableUpdaterOptimiser.class,logger);
      final JavaCompilationWorker incrementOrDecrementByOneOptimiser = this.createJavaCompilationWorker( rocket.compiler.IncrementOrDecrementByOneOptimiser.class,logger);
      final JavaCompilationWorker emptyBlockRemover = this.createJavaCompilationWorker( rocket.compiler.EmptyBlockRemover.class,logger);
      
      int pass = 0;
      
      do {
        didChange = false;
        
        didChange = testJavaCompilationWorker.work(jprogram, logger) || didChange;
        
        if( pass == 0 ){
        	longNotifier.work( jprogram, logger); 
        }
    	pass++;
        
        didChange = alternateValuesAssignmentOptimiser.work(jprogram, logger) || didChange;
        didChange = alternateValuesReturnedOptimiser.work(jprogram, logger) || didChange;
        didChange = conditionalAssignmentOptimiser.work(jprogram, logger) || didChange;        
        didChange = trailingReturnRemover.work(jprogram, logger) || didChange;
        didChange = variableAssignedToSelfRemover.work(jprogram, logger) || didChange;  
        didChange = unusedLocalVariableRemover.work( jprogram, logger ) || didChange;
        didChange = localVariableFinalMaker.work( jprogram, logger ) || didChange;
        didChange = variableUpdaterOptimiser.work( jprogram, logger ) || didChange;
        didChange = incrementOrDecrementByOneOptimiser.work( jprogram, logger ) || didChange;
        didChange = emptyBlockRemover.work( jprogram, logger ) || didChange;
        
        // Remove unreferenced types, fields, methods, [params, locals]
        didChange = Pruner.exec(jprogram, true) || didChange;
        // finalize locals, params, fields, methods, classes
        didChange = MethodAndClassFinalizer.exec(jprogram) || didChange;
        // rewrite non-polymorphic calls as static calls; update all call sites
        didChange = MakeCallsStatic.exec(jprogram) || didChange;

        // type flow tightening
        // - fields, locals based on assignment
        // - params based on assignment and call sites
        // - method bodies based on return statements
        // - polymorphic methods based on return types of all implementors
        // - optimize casts and instance of
        didChange = TypeTightener.exec(jprogram) || didChange;

        // tighten method call bindings
        didChange = MethodCallTightener.exec(jprogram) || didChange;

        // dead code removal??
        didChange = DeadCodeElimination.exec(jprogram) || didChange;

        // inlining
        didChange = MethodInliner.exec(jprogram) || didChange;

        if (didChange) {
          // recompute clinits; some may now be empty
          jprogram.typeOracle.recomputeClinits();
        }

        // prove that any types that have been culled from the main tree are
        // unreferenced due to type tightening?
      } while (didChange);

      // TODO ROCKET When upgrading from GWT 1.4.6x reapply changes.
      Compiler.resetFieldReferencesNotRequiringClint();
      Compiler.resetStaticMethodsNotRequiringClint();
      
      final JavaCompilationWorker staticMethodClintRemover = this.createJavaCompilationWorker( StaticMethodClinitRemover.class, logger );
      final JavaCompilationWorker staticFieldClintRemover = this.createJavaCompilationWorker( StaticFieldClinitRemover.class, logger );
      
      staticMethodClintRemover.work(jprogram, logger);
      staticFieldClintRemover.work(jprogram, logger);
      
      // (5) "Normalize" the high-level Java tree into a lower-level tree more
      // suited for JavaScript code generation. Don't go reordering these
      // willy-nilly because there are some subtle interdependencies.
      if (isDebugEnabled) {
        // AssertionNormalizer.exec(jprogram);
      }
      CatchBlockNormalizer.exec(jprogram);
      CompoundAssignmentNormalizer.exec(jprogram);
      JavaScriptObjectCaster.exec(jprogram);
      CastNormalizer.exec(jprogram);
      ArrayNormalizer.exec(jprogram);

      // (6) Perform further post-normalization optimizations
      // Prune everything
      Pruner.exec(jprogram, false);

      // (7) Generate a JavaScript code DOM from the Java type declarations
      rocket.compiler.GenerateJavaScriptAST.exec(jprogram, jsProgram, obfuscate, prettyNames);
      
      
      // (8) Fix invalid constructs created during JS AST gen
      JsNormalizer.exec(jsProgram);

      // (9) Resolve all unresolved JsNameRefs
      JsSymbolResolver.exec(jsProgram);

      // (10) Obfuscate
      if (obfuscate) {
    	
    	  // ROCKET When upgrading from GWT 1.4.6x reapply changes
    	  if( Compiler.isEnabled( rocket.compiler.JsObfuscateNamer.class.getName() )){
    		  rocket.compiler.JsObfuscateNamer.exec(jsProgram);
    	  } else {
    		  JsObfuscateNamer.exec(jsProgram);
    	  }
        
      } else if (prettyNames) {
        JsPrettyNamer.exec(jsProgram);
      } else {
        JsVerboseNamer.exec(jsProgram);
      }
      
      // TODO ROCKET When upgrading from GWT 1.4.60 reapply changes.
      final JavaScriptCompilationWorker testJavaScriptCompilationWorker =  this.createTestJavaScriptCompilationWorker( logger );
      
      final JavaScriptCompilationWorker compareAgainstZero =  this.createJavaScriptCompilationWorker(rocket.compiler.CompareAgainstZeroOptimiser.class, logger );
      do {
          didChange = false;
          didChange = didChange || testJavaScriptCompilationWorker.work(jsProgram, logger);
          didChange = didChange || compareAgainstZero.work(jsProgram, logger);
          
      } while( didChange );
     

      DefaultTextOutput out = new DefaultTextOutput(obfuscate);
      
      // TODO ROCKET when upgrading from GWT 1.4.60 reapply changes.
      String javascript = null;
      if( Compiler.isEnabled( rocket.compiler.JsSourceGenerationVisitor.class.getName() )){
    	  rocket.compiler.JsSourceGenerationVisitor v = new rocket.compiler.JsSourceGenerationVisitor(out);
          v.accept(jsProgram);
          javascript = out.toString();  
      } else {
          JsSourceGenerationVisitor v = new JsSourceGenerationVisitor(out);
          v.accept(jsProgram);
          javascript = out.toString();    	  
      }
      
      // TODO ROCKET when upgrading from GWT 1.4.60 reapply changes.
      final JavaScriptSourceChecker javascriptSourceChecker = this.createJavaScriptSourceChecker( logger ); 
      javascriptSourceChecker.examine( javascript );

      return javascript;
    	} 
    catch (UnableToCompleteException e) {
      // just rethrow
      throw e;
    } catch (InternalCompilerException e) {
      TreeLogger topBranch = logger.branch(TreeLogger.ERROR,
          "An internal compiler exception occurred", e);
      List nodeTrace = e.getNodeTrace();
      for (Iterator it = nodeTrace.iterator(); it.hasNext();) {
        NodeInfo nodeInfo = (NodeInfo) it.next();
        SourceInfo info = nodeInfo.getSourceInfo();
        String msg;
        if (info != null) {
          String fileName = info.getFileName();
          fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
          fileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
          msg = "at " + fileName + "(" + info.getStartLine() + "): ";
        } else {
          msg = "<no source info>: ";
        }

        String description = nodeInfo.getDescription();
        if (description != null) {
          msg += description;
        } else {
          msg += "<no description available>";
        }
        TreeLogger nodeBranch = topBranch.branch(TreeLogger.ERROR, msg, null);
        String className = nodeInfo.getClassName();
        if (className != null) {
          nodeBranch.log(TreeLogger.INFO, className, null);
        }
      }
      throw new UnableToCompleteException();
    } catch (Throwable e) {
      logger.log(TreeLogger.ERROR, "Unexpected internal compiler error", e);
      throw new UnableToCompleteException();
    }
  }

  public long getLastModifiedTimeOfNewestCompilationUnit() {
    return lastModified;
  }

  private void checkForErrors(final TreeLogger logger, boolean itemizeErrors)
      throws UnableToCompleteException {
    boolean compilationFailed = false;
    if (goldenCuds.length == 0) {
      compilationFailed = true;
    }
    for (int iCud = 0; iCud < goldenCuds.length; iCud++) {
      CompilationUnitDeclaration cud = goldenCuds[iCud];
      CompilationResult result = cud.compilationResult();
      if (result.hasErrors()) {
        compilationFailed = true;
        // Early out if we don't need to itemize.
        if (!itemizeErrors) {
          break;
        }
        TreeLogger branch = logger.branch(TreeLogger.ERROR, "Errors in "
            + String.valueOf(result.getFileName()), null);
        IProblem[] errors = result.getErrors();
        for (int i = 0; i < errors.length; i++) {
          IProblem problem = errors[i];
          if (problemSet.contains(problem)) {
            continue;
          }

          problemSet.add(problem);

          // Strip the initial code from each error.
          //
          String msg = problem.toString();
          msg = msg.substring(msg.indexOf(' '));

          // Append 'file (line): msg' to the error message.
          //
          int line = problem.getSourceLineNumber();
          StringBuffer msgBuf = new StringBuffer();
          msgBuf.append("Line ");
          msgBuf.append(line);
          msgBuf.append(": ");
          msgBuf.append(msg);
          branch.log(TreeLogger.ERROR, msgBuf.toString(), null);
        }
      }
    }
    if (compilationFailed) {
      logger.log(TreeLogger.ERROR, "Cannot proceed due to previous errors",
          null);
      throw new UnableToCompleteException();
    }
  }
  

	/**
	 * This method has been added by the rocket project to return the LoggerOptimiser implementation based upon the value of a property for the
	 * permutation being generated. 
	 * @param program The program or permutation being processed
	 * @param propertyOracle The PropertyOracle for the current permutation
	 * @param logger A logger
	 * @return The appropriate implementation
	 * 
	 * TODO ROCKET When upgrading from GWT 1.4.60 reapply changes.
	 */
	protected LoggerOptimiser getLoggerOptimiser(final JProgram program, final PropertyOracle propertyOracle, final TreeLogger logger) {
		final LoggingPropertyReader reader = new LoggingPropertyReader(){
			protected String getPropertyValue(){
				try{
				   return propertyOracle.getPropertyValue(logger, this.getPropertyName() );
				} catch ( final BadPropertyValueException badPropertyValueException){
					throw new RuntimeException( badPropertyValueException );
				}
			}

			protected void throwInvalidPropertyValue(final String propertyValue){
				throw new RuntimeException( "Invalid " + this.getPropertyName() + " value of \"" + propertyValue + "\" encountered.");
			}

			protected Object handleDisableLoggingValue(){
				final NoneLoggingFactoryGetLoggerOptimiser none = new NoneLoggingFactoryGetLoggerOptimiser();
				none.setProgram(program);
				none.setGetLogger(program.getLoggerFactoryGetLogger());
				
				return none;
			}

			protected Object handleEnableLoggingValue(){
				final LoggingLevelByNameAssigner loggingFactory = new LoggingLevelByNameAssigner();
				loggingFactory.setProgram(program);
				loggingFactory.setGetLogger(program.getLoggerFactoryGetLogger());

				loggingFactory.setLoggingFactoryConfig( this.createPropertiesFileLoggingConfig() );

				return loggingFactory;				
			}
		};
		
		return (LoggerOptimiser)reader.readProperty();
	}
	
	
	/**
	 * If this system property is present the value is used as the class name of a JavaCompilationWorker
	 */
	final static String JAVA_COMPILATION_WORKER_SYSTEM_PROPERTY = rocket.compiler.JavaCompilationWorker.class.getName();
	
	protected JavaCompilationWorker createTestJavaCompilationWorker(final TreeLogger logger ){
		JavaCompilationWorker worker = NullJavaCompilationWorker.instance;
		
		final String className = System.getProperty(JAVA_COMPILATION_WORKER_SYSTEM_PROPERTY );
		if( null != className ){
			try {
				worker = (JavaCompilationWorker) Class.forName(className).newInstance();
				logger.log(TreeLogger.WARN, "Using " + className, null );
				
			} catch (final Exception exception ) {
				throw new RuntimeException( "Unable to create JavaCompilationWorker \"" + className + "\", cause: " + exception.getMessage(), exception );
			}
		} else {
			logger.log(TreeLogger.WARN, "The system property \"" + JAVA_COMPILATION_WORKER_SYSTEM_PROPERTY + "\" was not set.", null);		
		}
		
		return worker;
	}
	
	/**
	 * Factory method which creates a JavaCompilationWorker given its class name. If its not found a NullJavaCompilationWorker instance is
	 * returned instead.
	 * @param c
	 * @return
	 */
	protected JavaCompilationWorker createJavaCompilationWorker(final Class c, final TreeLogger logger ) {
		Checker.notNull("parameter:class", c);
		Checker.notNull("parameter:logger", logger);
		
		JavaCompilationWorker worker = NullJavaCompilationWorker.instance;

		final String name = c.getName();
		if (Compiler.isEnabled( name )) {

			try {
				worker = (JavaCompilationWorker) c.newInstance();
				logger.log(TreeLogger.WARN, "Using " + c.getName(), null );
				
			} catch (final Exception exception ) {
				throw new RuntimeException( "Unable to create JavaCompilationWorker \"" + name + "\", cause: " + exception.getMessage(), exception );
			}
		}

		return worker;
	}

	/**
	 * If this system property is present the value is used as the class name of a JavaScriptCompilationWorker
	 */
	final static String JAVASCRIPT_COMPILATION_WORKER_SYSTEM_PROPERTY = rocket.compiler.JavaScriptCompilationWorker.class.getName();
	
	/**
	 * Factory method which loads a JavaScriptCompilationWorker after fetching its class name from a system property
	 * @return
	 */
	protected JavaScriptCompilationWorker createTestJavaScriptCompilationWorker(final TreeLogger logger ){
		JavaScriptCompilationWorker worker = NullJavaScriptCompilationWorker.instance;
		
		final String className = System.getProperty(JAVASCRIPT_COMPILATION_WORKER_SYSTEM_PROPERTY );
		if( null != className ){
			try {
				worker = (JavaScriptCompilationWorker) Class.forName( className ).newInstance();
				logger.log(TreeLogger.WARN, "Using " + className, null );
				
			} catch (final Exception exception ) {
				throw new RuntimeException( "Unable to create JavaScriptCompilationWorker \"" + className + "\", cause: " + exception.getMessage(), exception );
			}
		} else {
			logger.log(TreeLogger.WARN, "The system property \"" + JAVASCRIPT_COMPILATION_WORKER_SYSTEM_PROPERTY + "\" was not set.", null);		
		}
		
		return worker;
	}
	
	/**
	 * Factory method which creates a JavaScriptCompilationWorker given its class name. If its not found a NullJavaScriptCompilationWorker instance is
	 * returned instead.
	 * @param c
	 * @return
	 */
	protected JavaScriptCompilationWorker createJavaScriptCompilationWorker(final Class c, final TreeLogger logger ) {
		Checker.notNull("parameter:class", c);
		Checker.notNull("parameter:logger", logger);
		
		JavaScriptCompilationWorker worker = NullJavaScriptCompilationWorker.instance;

		final String name = c.getName();
		if (Compiler.isEnabled( name )) {

			try {
				worker = (JavaScriptCompilationWorker) c.newInstance();
				logger.log(TreeLogger.WARN, c.getName(), null );
				
			} catch (final Exception exception ) {
				throw new RuntimeException( "Unable to create JavaScriptCompilationWorker \"" + name + "\", cause: " + exception.getMessage(), exception );
			}
		}

		return worker;
	}
	
	/**
	 * If this system property is present the value is used as the class name of a JavaScriptSourceChecker
	 */
	final static String JAVASCRIPT_SOURCE_CHECKER_SYSTEM_PROPERTY = rocket.compiler.JavaScriptSourceChecker.class.getName();	
	
	/**
	 * Factory method which attempts to create a CompilationWorker instance
	 * after fetching the class name from a system property.
	 * 
	 * @param logger
	 * @return
	 * @throws UnableToCompleteException
	 *             if something goes wrong attempting to create the JavaScriptSourceChecker
	 */
	JavaScriptSourceChecker createJavaScriptSourceChecker(final TreeLogger logger) throws UnableToCompleteException {
		JavaScriptSourceChecker worker = new JavaScriptSourceChecker(){
			public void examine(final String string ){
			}
		};

		while (true) {
			// fetch the fqcn of the compilation worker from a system property.
			final String className = System.getProperty(JAVASCRIPT_SOURCE_CHECKER_SYSTEM_PROPERTY);
			if (null == className) {
				logger.log(TreeLogger.WARN, "The system property \"" + JAVASCRIPT_SOURCE_CHECKER_SYSTEM_PROPERTY + "\" was not set.", null);
				break;
			}

			// try and load the class

			try {
				final Class classs = Class.forName(className);
				worker = (JavaScriptSourceChecker) classs.newInstance();
			} catch (final Exception caught) {
				logger.log(TreeLogger.ERROR, "Unable to load the class \"" + className + "\", message: " + caught.getMessage(), null);
				throw new UnableToCompleteException();
			}
			break;
		}

		return worker;
	}
	
	/**
	 * Simple helper that fetches the class object for a particular class name wrapping any exceptions inside an
	 * AssertionError
	 * @param name
	 * @return
	 */
	Class classForName( final String name ){
		Checker.notEmpty("parameter:name", name );
		
		try{
			return Class.forName( name );
		} catch ( final Exception caught ){
			throw new AssertionError( "Unable to load the class \"" + name + "\".");
		}
	}
}
