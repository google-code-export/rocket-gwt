/*
 * Copyright Miroslav Pokorny
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
package rocket.remoting.rebind.json;

import java.io.PrintWriter;

import rocket.generator.rebind.JClassTypeMethodVisitor;
import rocket.remoting.client.json.RemoteJsonService;
import rocket.remoting.client.json.RemoteJsonServiceClient;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * This generator may be used to generate a proxy or client for a server based
 * resource that returns its result in the json format. The interface presented
 * to the user is the same as that found in regular rpcs. The only difference
 * being some annotations must be specificed to assist in the json to java
 * mapping process.
 * 
 * @author Miroslav Pokorny
 */
public class RemoteJsonServiceGenerator extends Generator {

	/**
	 * Begins the code generation process.
	 * 
	 * @param logger
	 * @param remoteJsonServiceGeneratorContext
	 * @param typeName
	 */
	public String generate(final TreeLogger logger, final GeneratorContext generatorContext, final String typeName)
			throws UnableToCompleteException {

		final RemoteJsonServiceGeneratorContext context = new RemoteJsonServiceGeneratorContext();
		context.setGeneratorContext(generatorContext);
		context.setLogger(logger);
		this.setRemoteJsonServiceGeneratorContext(context);

		this.setServiceInterfaceClassname(typeName);

		// will be null if generater has already been run.
		final String generatedClientClassname = this.getGeneratedProxyClassname();
		final String packageName = context.getPackageName(generatedClientClassname);
		final String simpleClassName = context.getSimpleClassName(generatedClientClassname);
		final PrintWriter printWriter = context.tryCreateTypePrintWriter(packageName, simpleClassName);
		final boolean alreadyExists = (null == printWriter);

		if (false == alreadyExists) {
			try {
				this.verifyInterfaceCompatibility();
				final SourceWriter writer = this.createSourceWriter(generatedClientClassname, printWriter);
				this.write(writer);

			} catch (final RemoteJsonServiceGeneratorException dontCatchRethrow) {
				dontCatchRethrow.printStackTrace();
				throw dontCatchRethrow;
			} catch (final Throwable caught) {
				caught.printStackTrace();
				throw new UnableToCompleteException();
			}
		}

		// return the name of the generated class
		return generatedClientClassname;
	}

	/**
	 * Factory field which creates a SourceWriter which will be used to write
	 * the generate code for the json client.
	 * 
	 * @param packageName
	 * @param simpleClassname
	 * @param printWriter
	 * @return
	 */
	protected SourceWriter createSourceWriter(final String classname, final PrintWriter printWriter) {
		final RemoteJsonServiceGeneratorContext context = this.getRemoteJsonServiceGeneratorContext();

		final String packageName = context.getPackageName(classname);
		final String simpleClassName = context.getSimpleClassName(classname);

		final ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(packageName, simpleClassName);
		composerFactory.addImplementedInterface(RemoteJsonService.class.getName());
		composerFactory.addImplementedInterface(ServiceDefTarget.class.getName());
		composerFactory.addImplementedInterface(this.getAsyncInterfaceClassname());
		composerFactory.setSuperclass(RemoteJsonServiceClient.class.getName());
		return context.createSourceWriter(composerFactory, printWriter);
	}

	/**
	 * Generates the methods that belong to the class being generated.
	 * 
	 * @param writer
	 */
	protected void write(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		final JClassType generated = this.getServiceInterfaceType();

		final RemoteJsonServiceGeneratorContext context = this.getRemoteJsonServiceGeneratorContext();
		
		final JClassTypeMethodVisitor publicMethodWalker = new JClassTypeMethodVisitor(){
			public boolean skipMethod( final JMethod method ){
				boolean skip = false;
				
				while( true ){
					if( method.isStatic() ){
						skip = true;
						break;
					}					
					final JClassType type = method.getEnclosingType();
					if( type == type.getOracle().getJavaLangObject() ){
						skip = true;
						break;
					}
					
					skip = false;
					break;
				}
				
				return skip;
			}
			public void visitMethod( final JMethod method ){
				final Method serviceMethod = new Method();
				serviceMethod.setMethod(method);
				serviceMethod.setRemoteJsonServiceGeneratorContext(context);
				serviceMethod.write(writer);				
			}
		};
		publicMethodWalker.setType( generated );
		publicMethodWalker.startVisit();
		context.commitWriter(writer);
	}

	/**
	 * A refernce to the context for this code generation context.
	 */
	private RemoteJsonServiceGeneratorContext remoteJsonServiceGeneratorContext;

	protected RemoteJsonServiceGeneratorContext getRemoteJsonServiceGeneratorContext() {
		ObjectHelper.checkNotNull("field:remoteJsonServiceGeneratorContext", remoteJsonServiceGeneratorContext);
		return this.remoteJsonServiceGeneratorContext;
	}

	protected void setRemoteJsonServiceGeneratorContext(final RemoteJsonServiceGeneratorContext generateSession) {
		ObjectHelper.checkNotNull("parameter:remoteJsonServiceGeneratorContext", generateSession);
		this.remoteJsonServiceGeneratorContext = generateSession;
	}

	/**
	 * The name of the service interface for which the proxy is being generated.
	 */
	private String serviceInterfaceClassname;

	protected String getServiceInterfaceClassname() {
		StringHelper.checkNotEmpty("field:serviceInterfaceClassname", serviceInterfaceClassname);
		return this.serviceInterfaceClassname;
	}

	public JClassType getServiceInterfaceType() {
		return (JClassType) this.getRemoteJsonServiceGeneratorContext().getType(this.getServiceInterfaceClassname());
	}

	protected void setServiceInterfaceClassname(final String serviceInterfaceClassname) {
		StringHelper.checkNotEmpty("parameter:serviceInterfaceClassname", serviceInterfaceClassname);
		this.serviceInterfaceClassname = serviceInterfaceClassname;
	}

	protected String getAsyncInterfaceClassname() {
		return this.getServiceInterfaceClassname() + Constants.ASYNC_INTERFACE_SUFFIX;
	}

	protected JClassType getAsyncInterfaceType() {
		final String name = getAsyncInterfaceClassname();
		final JClassType type = (JClassType) this.getRemoteJsonServiceGeneratorContext().findType(name);
		if (null == type) {
			this.throwAsyncInterfaceNotFoundException(name);
		}
		return type;
	}

	protected void throwAsyncInterfaceNotFoundException(final String typeName) {
		throw new AsyncInterfaceNotFoundException("Unable to find [" + typeName + "].");
	}

	/**
	 * Returns the generated client name for
	 * 
	 * @return
	 */
	protected String getGeneratedProxyClassname() {
		final String serviceInterfaceClassname = this.getServiceInterfaceClassname();

		final RemoteJsonServiceGeneratorContext context = this.getRemoteJsonServiceGeneratorContext();
		final String packageName = context.getPackageName(serviceInterfaceClassname);
		final String simpleClassName = context.getSimpleClassName(serviceInterfaceClassname);
		return packageName + '.' + simpleClassName + Constants.CLIENT_SUFFIX;
	}

	/**
	 * Performs rundamentary checking that the service interface and async
	 * interface are rpc compatible.
	 */
	protected void verifyInterfaceCompatibility() {
		this.verifyServiceInterface();
		this.verifyAsyncMethods();
	}

	protected void verifyServiceInterface() {
		final JClassType serviceInterface = this.getServiceInterfaceType();

		// verify serviceInterface is an interface
		if (serviceInterface.isInterface() == null) {
			this.throwIncompatibleInterfacesException("The type [" + serviceInterface.getQualifiedSourceName() + "] is not an interface.");
		}

		// verify serviceInterface implements RemoteJsonService
		final JClassType remoteJsonServiceType = this.getRemoteJsonServiceGeneratorContext().getRemoteJsonService();
		if (false == serviceInterface.isAssignableTo(remoteJsonServiceType)) {
			this.throwIncompatibleInterfacesException("The type [" + serviceInterface.getQualifiedSourceName() + "] does not implement "
					+ RemoteJsonService.class.getName());
		}
	}

	/**
	 * Verifies that the methods parameters that appear on the async interface
	 * are compatible with the service interface as per GWT RPC rules.
	 */
	protected void verifyAsyncMethods() {
		final JMethod[] methods = this.getServiceInterfaceType().getMethods();
		final JClassType asyncInterface = this.getAsyncInterfaceType();
		final RemoteJsonServiceGeneratorContext context = this.getRemoteJsonServiceGeneratorContext();
		final JType asyncCallback = context.getAsyncCallbackType();

		for (int i = 0; i < methods.length; i++) {
			final JMethod method = methods[i];

			// verify method returns a serializable object and not void, List or
			// String.
			final JType methodReturnType = method.getReturnType();
			if (methodReturnType.isArray() != null) {
				this.throwIncompatibleInterfacesException("The service method " + method.getReadableDeclaration()
						+ " return type must not be an array.");
			}
			if (methodReturnType.isPrimitive() != null) {
				this.throwIncompatibleInterfacesException("The service method " + method.getReadableDeclaration()
						+ " return type must not be a primitive.");
			}
			if (methodReturnType == context.getJavaLangString()) {
				this.throwIncompatibleInterfacesException("The service method " + method.getReadableDeclaration()
						+ " return type must not be a String.");
			}
			if (methodReturnType == context.getJavaUtilList()) {
				this.throwIncompatibleInterfacesException("The service method " + method.getReadableDeclaration()
						+ " return type must not be a java.util.List.");
			}
			// doesnt test if return type is serializable etc this is done
			// during code generation...

			final JParameter[] parameters = method.getParameters();
			final int parameterCount = parameters.length;

			final JType[] asyncMethodParameterTypes = new JType[parameterCount + 1];
			for (int j = 0; j < parameterCount; j++) {
				asyncMethodParameterTypes[j] = parameters[j].getType();
			}
			asyncMethodParameterTypes[parameterCount] = asyncCallback;

			// try and find the matchinhg method..
			final JMethod asyncMethod = asyncInterface.findMethod(method.getName(), asyncMethodParameterTypes);
			if (null == asyncMethod) {
				this.throwIncompatibleInterfacesException("Cannot find the corresponding method for the service method "
						+ method.getReadableDeclaration() + " on the class " + asyncInterface.getQualifiedSourceName());
			}
			final JType returnType = asyncMethod.getReturnType();
			if (returnType != JPrimitiveType.VOID) {
				this.throwIncompatibleInterfacesException("The return type of the async method " + asyncMethod.getReadableDeclaration()
						+ " must be void.");
			}
		}
	}

	/**
	 * Throws an exception with the given message to notify that the service and
	 * async interfaces are not GWT rpc compatible.
	 * 
	 * @param message
	 */
	protected void throwIncompatibleInterfacesException(final String message) {
		throw new IncompatibleInterfacesException(message);
	}
}
