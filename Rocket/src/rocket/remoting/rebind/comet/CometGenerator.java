/*
 * Copyright Miroslav Pokorny
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http:www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rocket.remoting.rebind.comet;

import java.util.Collections;
import java.util.List;

import rocket.generator.rebind.Generator;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.GeneratorContextImpl;
import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.methodparameter.NewMethodParameter;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.NewNestedInterfaceType;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

import com.google.gwt.core.ext.TreeLogger;

/**
 * This generator sub-classes the given Comet and adds a method that generates
 * the necessary
 * 
 * This generator reads the createDeserializer method of CometClient and creates
 * two GWT RPC interfaces for the annotated type.
 * 
 * @author Miroslav Pokorny
 */
public class CometGenerator extends Generator {

	protected NewConcreteType assembleNewType(final Type cometClient, final String newTypeName) {
		this.verifyEnvironment();

		final NewConcreteType generated = this.subClassCometClient(newTypeName, cometClient);

		this.createRpcServiceInterface(generated);
		this.createRpcAsyncServiceInterface(generated);

		this.implementCreateProxy(generated);
		return generated;
	}

	/**
	 * Verifies that the customised ProxyCreator is used rather than the vanilla
	 * version. If not an exception is thrown, resulting in an error being
	 * reported so the developer can fix the classpath issue.
	 */
	protected void verifyEnvironment() {
		try {
			final Class proxyCreator = Class.forName(Constants.PROXY_CREATOR);
			proxyCreator.getField(Constants.CUSTOMISED_PROXY_CREATOR_MARKER_FIELD);
			this
					.getGeneratorContext()
					.info(
							"Rocket.jar appears in front of gwt*.jar files! Problems relating to the classpath should not be happen (Comet deserialization of incoming payload should work).");

		} catch (final NoSuchFieldException fieldMissing) {
			fieldMissing.printStackTrace();
			throw new CometClientGeneratorException("The classpath appears to be loading the vanilla " + Constants.PROXY_CREATOR
					+ " instead of the customised version. Make sure that rocket.jar appears in before any gwt-*.jar.");
		} catch (final Throwable classNotFound) {
			classNotFound.printStackTrace();
			throw new CometClientGeneratorException("Unable to locate " + Constants.PROXY_CREATOR
					+ " gwt-user.jar appears to be missing from classpath, corrupted or broken.", classNotFound);
		}
	}

	/**
	 * Creates a new type that sub classes the given comet client.
	 * 
	 * @param newTypeName
	 * @param cometClient
	 * @return
	 */
	protected NewConcreteType subClassCometClient(final String newTypeName, final Type cometClient) {
		GeneratorHelper.checkJavaTypeName("parameter:newTypeName", newTypeName);
		ObjectHelper.checkNotNull("parameter:cometClient", cometClient);

		final GeneratorContext context = this.getGeneratorContext();
		context.info("Creating new sub class of " + cometClient.getName() + " called " + newTypeName);

		final NewConcreteType subClass = context.newConcreteType();
		subClass.setAbstract(false);
		subClass.setFinal(true);
		subClass.setName(newTypeName);
		subClass.setSuperType(cometClient);

		return subClass;
	}

	/**
	 * Creates a new GWT RPC service interface with a dummy method with a return
	 * type that is set to the annotated payload type
	 * 
	 * @param newType
	 *            The enclosing type that will contain the service interface
	 */
	protected void createRpcServiceInterface(final NewConcreteType newType) {
		ObjectHelper.checkNotNull("parameter:newType", newType);

		this.getGeneratorContext().info("Generating Rpc service interface which will provide the payload serializer.");

		final NewNestedInterfaceType serviceInterface = newType.newNestedInterfaceType();
		serviceInterface.setName(Constants.RPC_SERVICE_INTERFACE);
		serviceInterface.setStatic(true);
		serviceInterface.setSuperType(this.getRemoteService());
		serviceInterface.setVisibility(Visibility.PUBLIC);

		final NewMethod newMethod = serviceInterface.newMethod();
		newMethod.setAbstract(true);
		newMethod.setFinal(false);
		newMethod.setName(Constants.PAYLOAD_DECLARATION_METHOD);
		newMethod.setNative(false);

		final Type payloadType = this.getPayloadType(newType.getSuperType());
		newMethod.setReturnType(payloadType);

		newMethod.setStatic(false);
		newMethod.setVisibility(Visibility.PUBLIC);
	}

	/**
	 * Creates the async service interface counterpart for the rpc service
	 * interface generated by
	 * {@link #createRpcServiceInterface(NewConcreteType)}
	 * 
	 * @param newType
	 *            The enclosing type that will contain the async service
	 *            interface
	 */
	protected void createRpcAsyncServiceInterface(final NewConcreteType newType) {
		ObjectHelper.checkNotNull("parameter:newType", newType);

		this.getGeneratorContext().info("Generating Rpc async service interface counterpart for the rpc service interface.");

		final NewNestedInterfaceType asyncServiceInterface = newType.newNestedInterfaceType();
		asyncServiceInterface.setName(Constants.RPC_ASYNC_SERVICE_INTERFACE);
		asyncServiceInterface.setStatic(true);
		asyncServiceInterface.setVisibility(Visibility.PUBLIC);

		final NewMethod newMethod = asyncServiceInterface.newMethod();
		newMethod.setAbstract(true);
		newMethod.setFinal(false);
		newMethod.setName(Constants.PAYLOAD_DECLARATION_METHOD);
		newMethod.setNative(false);
		newMethod.setReturnType(this.getGeneratorContext().getVoid());
		newMethod.setStatic(false);
		newMethod.setVisibility(Visibility.PUBLIC);

		final NewMethodParameter callback = newMethod.newParameter();
		callback.setFinal(true);
		callback.setName(Constants.ASYNC_CALLBACK_PARAMETER_NAME);
		callback.setType(this.getAsyncCallback());
	}

	/**
	 * Retrieves the type after reading the type from an annotation belonging to
	 * the createDeserializer method.
	 * 
	 * @param list
	 * @param annotation
	 * @return
	 */
	protected Type getPayloadType(final Type type) {
		ObjectHelper.checkNotNull("parameter:list", type);

		final Method createDeserializer = type.findMostDerivedMethod(Constants.CREATE_PROXY_METHOD, Collections.EMPTY_LIST);
		if (null == createDeserializer) {
			throwUnableToFindAbstractCreateProxy(type);
		}
		if (false == createDeserializer.isAbstract()) {
			throwUnableToFindAbstractCreateProxy(type);
		}
		final List values = createDeserializer.getMetadataValues(Constants.COMET_PAYLOAD_TYPE_ANNOTATION);
		if (values.size() != 1) {
			throwUnableToFindAnnotation(type);
		}

		final String typeName = (String) values.get(0);
		if (null == typeName) {
			this.throwUnableToFindPayloadType(typeName);
		}

		return this.getGeneratorContext().getType(typeName);
	}

	protected void throwUnableToFindAbstractCreateProxy(final Type type) {
		throw new CometClientGeneratorException("Unable to find an abstract " + Constants.CREATE_PROXY_METHOD + " upon the type " + type);
	}

	protected void throwUnableToFindAnnotation(final Type type) {
		throw new CometClientGeneratorException("Unable to find the annotation [" + Constants.COMET_PAYLOAD_TYPE_ANNOTATION + "] on the "
				+ type + "." + Constants.CREATE_PROXY_METHOD);
	}

	protected void throwUnableToFindPayloadType(final String type) {
		throw new CometClientGeneratorException("Unable to find the type [" + type + "] which was specified by the "
				+ Constants.CREATE_PROXY_METHOD + " annotation.");
	}

	/**
	 * Implements the outstanding createDeserializer method to return a
	 * reference to the payload serializer.
	 * 
	 * @param newType
	 *            The type that will contain the new realised createDeserializer
	 *            method.
	 */
	protected void implementCreateProxy(final NewConcreteType newType) {
		final Method createDeserializer = newType.getMostDerivedMethod(Constants.CREATE_PROXY_METHOD, Collections.EMPTY_LIST);
		final NewMethod newCreateProxy = createDeserializer.copy(newType);
		newCreateProxy.setAbstract(false);
		newCreateProxy.setFinal(true);

		final CreateProxyTemplatedFile template = new CreateProxyTemplatedFile();
		final Type serviceInterface = this.getGeneratorContext().getType(Constants.RPC_SERVICE_INTERFACE);
		template.setType(serviceInterface);
		newCreateProxy.setBody(template);
	}

	protected GeneratorContext createGeneratorContext( final com.google.gwt.core.ext.GeneratorContext generatorContext, final TreeLogger logger){
		final GeneratorContextImpl context = new GeneratorContextImpl() {
			protected String getGeneratedTypeNameSuffix() {
				return Constants.COMET_CLIENT_SUFFIX;
			}
		};
		context.setGenerator( this );
		context.setGeneratorContext( generatorContext );
		context.setLogger( logger );
		
		return context;
	}

	protected Type getAsyncCallback() {
		return this.getGeneratorContext().getType(Constants.ASYNC_CALLBACK);
	}

	protected Type getRemoteService() {
		return this.getGeneratorContext().getType(Constants.REMOTE_SERVICE);
	}
}
