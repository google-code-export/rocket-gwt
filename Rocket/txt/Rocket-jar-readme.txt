This archive contain the source and binaries for all of the components (except for the stacktrace support) 
within the Rocket library. The stacktrace support (found within Rocket-stacktrace.jar) has been separated
as it requires JDK1.5+.

Rocket.jar contains a number of compiler enhancements which may be individually or globally enabled via system properties. The goal of each and every optimiser is to make the outputted javascript smaller by transforming
whereever possible java statements from a more verbose form to a equivalent but less characters form.
	i=i+1	->	i++
	
Because compiler optimisation is a tricky process if your output is larger with the optimisations in place or something goes wrong simply disable the feature(s). 
For problems such as compile time exceptions please report them to the forum with a message describing the problem, stacktrace (if possible) and perhaps the source code form of the troublesome java. 

Optimiser													Goals
rocket.compiler.AlternateValuesAssignmentOptimiser		Changes if statements containing a then and else that assign the same variable a new value into a tenary statement equivalent. if(condition)then i=1; else i=2; becomes i=condition ? 1:2;
rocket.compiler.AlternateValuesReturnOptimiser			Changes if statements containing a then and else that return values into a tenary statement equivalent. if(condition)then return 1; else return 2; becomes return condition ? 1:2;
rocket.compiler.ConditionalAssignmentOptimiser			Changes if statements with only a then which updates a variable from if(condition)then i=1; into i=condition?1:i;
rocket.compiler.EmptyBlockRemover						Removes any empty else blocks hanging of if statements.
rocket.compiler.GenerateJavaScriptAST					Enables each of the 2 features below.
	nullAndNotNullTests									Takes advantage of javascript's coercision of null to boolean ie if(ref === null becomes if(ref)
	zeroAndNotZeroTests									Takes advantage of javascript's coercision of zero tests to boolean ie if(ref === null becomes if(ref)
rocket.compiler.IncrementOrDecrementByOneOptimiser		Transforms mathematical operations like i+=1 into i++.
rocket.compiler.InvokeClinitsAtStartupMover				This optimiser moves static initializers belonging to selected classes to startup time rather than leaving them to initialize lazily. 
														This saves significant program size, and improved runtime performance due to the lack of constant clinit calls within static methods.
														The downside is the change from having initializers lazily loading and a slightly slower startup due to extra clinit calls at startup.
	listRemainingTypesWithStaticInitializers			This option lists remaining types with static initializers that might be potential candidates for special treatment.													 
rocket.compiler.JsObfuscateNamer						An optimised form of the default JsObfuscateNamer
rocket.compiler.JsSourceGenerationVisitor				The default behaviour is often surround negative literal numbers with parenthesis. This class never surrounds negative literals. 
rocket.compiler.LocalVariableFinalMaker					Attempts to locate any variables that are only set once and makes them final. Helps with GWT inlining optimisations which occur for final variables.
rocket.compiler.LongNotifier							Produces warning level messages noting all usages of the long type within a program.
rocket.compiler.StaticFieldClinitRemover				Removes unnecessary clinit guards in front of static fields belonging to classes with static initializers wherever possible.
rocket.compiler.StaticMethodClinitRemover				Removes unnecessary clinit guards from private and static methods that are not called from outside the enclosing class.
rocket.compiler.TrailingReturnRemover					Removes the return statement providing it is the last statement of a void method.  
rocket.compiler.UnusedLocalVariableRemover				Removes any unused local variables.
rocket.compiler.VariableAssignedToSelfRemover			Removes any assignment of a variable to itself. i = i;
rocket.compiler.VariableUpdaterOptimiser				Transforms mathematical operations like i = i + 2 becomes i+=2. 

Some optimisers work in tandem with others eg VariableUpdaterOptimiser and IncrementOfDecrementByOneOptimiser. This allows java like this 
	i = i + 1 
to be modified to by VariableUpdaterOptimiser to
	i += 1
which then further optimised by IncrementOfDecrementByOneOptimiser to 
	i++

To enable all of the above enhancements add the following system property.
rocket.compiler=enabled

or to only enable LongNotifier specifically...
rocket.compiler.LongNotifier=enabled

Performance Improvements.
The StaticFieldClinitRemover and StaticMethodClinitRemover not only reduce code size but also improve performance due to the elimination of unnecessary/redundant static initializer calls.