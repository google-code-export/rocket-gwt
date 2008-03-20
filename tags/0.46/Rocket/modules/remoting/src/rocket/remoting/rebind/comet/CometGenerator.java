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
import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.methodparameter.NewMethodParameter;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.NewNestedInterfaceType;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * This generator sub-classes the given Comet and adds a method that generates
 * the necessary
 * 
 * @author Miroslav Pokorny
 */
public class CometGenerator extends Generator {

	protected NewConcreteType assembleNewType(final Type cometClient, final String newTypeName) {
		this.verifyEnvironment();

		final NewConcreteType generated = this.subClassCometClient(newTypeName, cometClient);

		final Type serviceInterface = this.createRpcServiceInterface(generated);
		this.createRpcAsyncServiceInterface(generated);

		this.implementCreateGwtRpcProxy(generated, serviceInterface );
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
			this.getGeneratorContext().warn(
							"Rocket.jar appears in front of gwt*.jar files! Classpath related problems should not happen (Comet deserialization of incoming payload should work).");

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
	 * @param newTypeName The name of the new type to generate
	 * @param cometClient The incoming type
	 * @return The new class.
	 */
	protected NewConcreteType subClassCometClient(final String newTypeName, final Type cometClient) {
		GeneratorHelper.checkJavaTypeName("parameter:newTypeName", newTypeName);
		Checker.notNull("parameter:cometClient", cometClient);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Creating new sub class of " + cometClient.getName() + " called " + newTypeName);

		final NewConcreteType subClass = context.newConcreteType( newTypeName );
		subClass.setAbstract(false);
		subClass.setFinal(true);
		subClass.setSuperType(cometClient);

		context.unbranch();
		
		return subClass;
	}

	/**
	 * Creates a new GWT RPC service interface with a dummy method with a return
	 * type that is set to the annotated payload type
	 * 
	 * @param newType A nested type that is a gwt rpc service interface
	 * @return The service interface
	 */
	protected Type createRpcServiceInterface(final NewConcreteType newType) {
		Checker.notNull("parameter:newType", newType);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Generating Rpc service interface which will provide the payload serializer.");

		final NewNestedInterfaceType serviceInterface = newType.newNestedInterfaceType();
		serviceInterface.setNestedName(Constants.RPC_SERVICE_INTERFACE);
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
		
		context.debug( newMethod.toString() );
		context.unbranch();
		
		return serviceInterface;
	}

	/**
	 * Creates the async service interface counterpart for the rpc service
	 * interface generated by
	 * {@link #createRpcServiceInterface(NewConcreteType)}
	 * 
	 * @param newType The nested type that will contain the async service interface
	 */
	protected void createRpcAsyncServiceInterface(final NewConcreteType newType) {
		Checker.notNull("parameter:newType", newType);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info("Generating Rpc async service interface counterpart for the rpc service interface.");

		final NewNestedInterfaceType asyncServiceInterface = newType.newNestedInterfaceType();
		asyncServiceInterface.setNestedName(Constants.RPC_ASYNC_SERVICE_INTERFACE);
		asyncServiceInterface.setStatic(true);
		asyncServiceInterface.setVisibility(Visibility.PUBLIC);

		final NewMethod newMethod = asyncServiceInterface.newMethod();
		newMethod.setAbstract(true);
		newMethod.setFinal(false);
		newMethod.setName(Constants.PAYLOAD_DECLARATION_METHOD);
		newMethod.setNative(false);
		newMethod.setReturnType( context.getVoid());
		newMethod.setStatic(false);
		newMethod.setVisibility(Visibility.PUBLIC);

		final NewMethodParameter callback = newMethod.newParameter();
		callback.setFinal(true);
		callback.setName(Constants.ASYNC_CALLBACK_PARAMETER_NAME);
		callback.setType(this.getAsyncCallback());
		
		context.debug( newMethod.toString() );
		context.unbranch();
	}

	/**
	 * Retrieves the incoming payload type from the annotation appearing on the given comet client.
	 * 
	 * @param The original comet client
	 * @return The incoming payload type
	 */
	protected Type getPayloadType(final Type type) {
		Checker.notNull("parameter:type", type);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		context.info( "Retrieving incoming payload type.");
		
		context.debug( "Reading \"" + Constants.COMET_PAYLOAD_TYPE_ANNOTATION + "\" annotation." );
		final List values = type.getMetadataValues(Constants.COMET_PAYLOAD_TYPE_ANNOTATION);
		if (values.size() != 1) {
			throwUnableToFindCometPayloadTypeAnnotation(type);
		}

		final String typeName = (String) values.get(0);
		if (null == typeName) {
			this.throwUnableToFindCometPayloadTypeAnnotation(type);
		}		
		context.debug( typeName );

		final Type payloadType = context.findType(typeName);
		if( payloadType == null ){
			this.throwUnableToFindPayloadType(typeName);
		}
		
		context.debug( payloadType.toString() );
		context.unbranch();
		
		return payloadType;
	}

	protected void throwUnableToFindCometPayloadTypeAnnotation(final Type type) {
		throw new CometClientGeneratorException("Unable to find the annotation \"" + Constants.COMET_PAYLOAD_TYPE_ANNOTATION + "\" on the "
				+ type + "." + Constants.CREATE_GWT_RPC_PROXY_METHOD);
	}

	protected void throwUnableToFindPayloadType(final String type) {
		throw new CometClientGeneratorException("Unable to find the type \"" + type + "\" which was specified by the \"" + Constants.COMET_PAYLOAD_TYPE_ANNOTATION + "\" annotation.");
	}

	/**
	 * Implements the outstanding createDeserializer method to return a
	 * reference to the payload serializer.
	 * 
	 * @param newType The type being constructed
	 *  @param serviceInterface The service interface recently created to trigger GWT to create a deserializer.
	 */
	protected void implementCreateGwtRpcProxy(final NewConcreteType newType, final Type serviceInterface ) {		
		final Method method = newType.getMostDerivedMethod(Constants.CREATE_GWT_RPC_PROXY_METHOD, Collections.EMPTY_LIST);
		final NewMethod newMethod = method.copy(newType);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);

		final CreateGwtRpcProxyTemplatedFile template = new CreateGwtRpcProxyTemplatedFile();
		template.setType(serviceInterface);
		newMethod.setBody(template);
	}

	protected String getGeneratedTypeNameSuffix() {
		return Constants.COMET_CLIENT_SUFFIX;
	}
	
	protected Type getAsyncCallback() {
		return this.getGeneratorContext().getType(Constants.ASYNC_CALLBACK);
	}

	protected Type getRemoteService() {
		return this.getGeneratorContext().getType(Constants.REMOTE_SERVICE);
	}
}
