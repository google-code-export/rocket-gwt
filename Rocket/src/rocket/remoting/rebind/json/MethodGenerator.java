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

import rocket.generator.rebind.RebindHelper;
import rocket.json.client.JsonSerializer;
import rocket.remoting.client.json.RemoteGetJsonServiceInvoker;
import rocket.remoting.client.json.RemoteJsonServiceInvoker;
import rocket.remoting.client.json.RemotePostJsonServiceInvoker;
import rocket.util.client.HttpHelper;
import rocket.util.client.ObjectHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Instances of this class hold a single service interface method. This class
 * not only checks parameters for validity but also generates the service method
 * itself which invokes the json service. The resulting JSON encoded string is
 * then parsed using the GWT json libraries with the JSONObject given to the
 * appropriate deserializer for the method's return type.
 * 
 * @author Miroslav Pokorny
 */
public class MethodGenerator {

	/**
	 * Requests that this method writes its implementation.
	 * 
	 * @param writer
	 */
	protected void write(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		this.writeMethodDeclaration(writer);
		writer.indent();

		this.writeNewRemoteJsonServiceMethodInvokerStatement(writer);
		this.writeAddParametersToRequestStatements(writer);
		this.writeSetUrlStatement(writer);
		this.writeSetCallbackStatement(writer);
		this.writeMakeRequestStatement(writer);

		writer.outdent();
		writer.println("}");
	}

	/**
	 * Inserts a statement that copies the serviceEntryPoint from the outter
	 * class to url property The generated code will look like this
	 * 
	 * <pre>
	 * invoker.setUrl(this.getServiceEntryPoint());
	 * </pre>
	 * 
	 * @param writer
	 */
	protected void writeSetUrlStatement(final SourceWriter writer) {
		writer.println("invoker.setUrl( this.getServiceEntryPoint());");
	}

	/**
	 * Inserts a statement that sets the given callback parameter upon the
	 * invoker. The generated code will look like this
	 * 
	 * <pre>
	 * invoker.setCallback(callback);
	 * </pre>
	 * 
	 * @param writer
	 */
	protected void writeSetCallbackStatement(final SourceWriter writer) {
		writer.println("invoker.setCallback( callback );");
	}

	/**
	 * Inserts a statement that initiates the request via invoke.makeRequest.
	 * The generated code will look like this
	 * 
	 * <pre>
	 * invoker.makeRequest(this);
	 * </pre>
	 * 
	 * @param writer
	 */
	protected void writeMakeRequestStatement(final SourceWriter writer) {
		writer.println("invoker.makeRequest( this );");
	}

	/**
	 * This method writes some code that creates a
	 * RemoteJsonServiceXXXMethodInvoker anonymous inner class where xxx is
	 * either GET or POST. The generated code should look something like this.
	 * 
	 * <pre>
	 * final RemoteJsonServiceMethodInvoker invoker = new RemoteJsonServiceXXXMethodInvoker(){
	 *      ...
	 * };
	 * </pre>
	 * 
	 * @param writer
	 */
	protected void writeNewRemoteJsonServiceMethodInvokerStatement(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		final StringBuffer newInvokerInstance = new StringBuffer();
		newInvokerInstance.append("final ");
		newInvokerInstance.append(RemoteJsonServiceInvoker.class.getName());
		newInvokerInstance.append(" invoker = new ");

		final String httpMethod = this.getHttpRequestMethod();
		final Class getOrPostInvoker = HttpHelper.isGet(httpMethod) ? RemoteGetJsonServiceInvoker.class
				: RemotePostJsonServiceInvoker.class;
		newInvokerInstance.append(getOrPostInvoker.getName());
		newInvokerInstance.append("(){");
		writer.println(newInvokerInstance.toString());
		writer.println();
		writer.indent();

		this.writeAsObjectMethod(writer);

		// end of anonymous inner class.
		writer.outdent();
		writer.println("};");
	}

	/**
	 * Retrieves the httpRequestMethod annotation value for this method.
	 * 
	 * @return The annotation value.
	 */
	protected String getHttpRequestMethod() {
		final JMethod parameter = this.getMethod();
		final String method = RebindHelper.getAnnotationValue(parameter, Constants.HTTP_REQUEST_METHOD);
		if (false == HttpHelper.isGet(method) && false == HttpHelper.isPost(method)) {
			this.throwMissingAnnotationException("The annotation " + Constants.HTTP_REQUEST_METHOD + " must be POST or GET and not ["
					+ method + "]");
		}
		return method;
	}

	/**
	 * Overrides the abstract asObject( JSONObject jsonObject ) which accepts a
	 * JSONObject and attempts to set all the fields belonging to the type being
	 * deserialized.
	 * 
	 * <pre>
	 * protected Object asObject(final JSONObject jsonObject ) {
	 *     final JsonSerializer deserializer = (JsonSerializer) GWT.create( ${returnType}.class );
	 *     return deserializer.asObject( jsonObject );
	 * }
	 * </pre>
	 * 
	 * @param writer
	 */
	protected void writeAsObjectMethod(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		final JType returnType = (JType) this.getMethod().getReturnType();
		if (JPrimitiveType.VOID == returnType) {
			this.throwIncompatibleInterfacesException("The method " + this.getMethod().getReadableDeclaration()
					+ " returns void. Service methods must return " + Object.class.getName());
		}

		final StringBuffer declaration = new StringBuffer();
		declaration.append("protected Object asObject( final ");
		declaration.append(JSONValue.class.getName());
		declaration.append(" jsonValue ){");
		writer.println(declaration.toString());
		writer.indent();

		// final JsonSerializer deserializer = (JsonSerializer) GWT.create(
		// ${returnType}.class );
		final String jsonSerializerTypeName = JsonSerializer.class.getName();

		final StringBuffer invokeDeserializer = new StringBuffer();
		invokeDeserializer.append("final ");
		invokeDeserializer.append(jsonSerializerTypeName);
		invokeDeserializer.append(" deserializer =(");
		invokeDeserializer.append(jsonSerializerTypeName);
		invokeDeserializer.append(") ");
		invokeDeserializer.append(GWT.class.getName());
		invokeDeserializer.append(".create( ");
		invokeDeserializer.append(returnType.getQualifiedSourceName());
		invokeDeserializer.append(".class );");
		writer.println(invokeDeserializer.toString());

		// return deserializer.asObject( jsonObject );
		writer.println("return deserializer.asObject( jsonValue );");

		writer.outdent();
		writer.println("}");
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

	/**
	 * For each parameter (except for the AsyncCallback) this method writes a
	 * line that adds teh parameter (by name) to the previous created
	 * RemoteJsonServiceMethodInvoker.
	 * 
	 * The generated code will look like this
	 * 
	 * <pre>
	 * invoker.add(&quot;httpRequestParameterName&quot;, parameterName);
	 * </pre>
	 * 
	 * @param writer
	 */
	protected void writeAddParametersToRequestStatements(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		final JMethod method = this.getMethod();

		// TODO not sure if annotations should be fetched from JParameter or
		// JMethod
		final JParameter[] parameters = method.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			final JParameter parameter = parameters[i];
			final String parameterName = parameter.getName();

			final String httpRequestParameterName = this.getHttpRequestParameterName(i);

			final StringBuffer buf = new StringBuffer();
			buf.append("invoker.addParameter( \"");
			buf.append(httpRequestParameterName);
			buf.append("\", ");
			buf.append(parameterName);
			buf.append(");");

			writer.println(buf.toString());
		}
	}

	/**
	 * Helper which retrieves the annotation value for a method parameter.
	 * 
	 * @param parameterIndex
	 * @return
	 */
	protected String getHttpRequestParameterName(final int parameterIndex) {
		final String[][] values = this.getMethod().getMetaData(Constants.HTTP_REQUEST_PARAMETER_NAME);
		if (null == values) {
			this.throwHttpRequestParameterNameAnnotationMissing(parameterIndex);
		}
		if (parameterIndex > values.length) {
			this.throwHttpRequestParameterNameAnnotationMissing(parameterIndex);
		}
		return values[parameterIndex][0];
	}

	protected void throwHttpRequestParameterNameAnnotationMissing(final int parameterIndex) {
		final JParameter parameter = this.getMethod().getParameters()[parameterIndex];
		this.throwMissingAnnotationException("Unable to find the annotation [" + Constants.HTTP_REQUEST_PARAMETER_NAME
				+ "] for the parameter " + parameter.getName() + " which belongs to the method " + method.getReadableDeclaration());
	}

	/**
	 * Helper which throws a BadAnnotationException exception given the
	 * 
	 * @param message
	 */
	protected void throwMissingAnnotationException(final String message) {
		throw new MissingAnnotationException(message);
	}

	/**
	 * Writes the method declaration along with the parameters and a opening
	 * brace for the method body. This method also verifies that the parameter
	 * types are supported (primitives and String).
	 * 
	 * <pre>
	 * public void methodName( final parameterType parameterName, final AsyncCallback callback ){ 
	 * </pre>
	 * 
	 * @param writer
	 */
	protected void writeMethodDeclaration(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		final JMethod method = this.getMethod();

		final StringBuffer buf = new StringBuffer();
		buf.append("public void ");
		buf.append(method.getName());
		buf.append("( ");

		final JParameter[] parameters = method.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			final JParameter parameter = parameters[i];
			final JType parameterType = parameter.getType();

			this.verifySupportedParameterType(parameterType, method);

			buf.append("final ");
			buf.append(parameterType.getQualifiedSourceName());
			buf.append(" ");
			buf.append(parameter.getName());
			buf.append(", ");
		}

		buf.append("final ");
		buf.append(AsyncCallback.class.getName());
		buf.append(" callback ){");
		writer.println(buf.toString());
	}

	/**
	 * Verifies that the given parameter type is one of the supportd types
	 * throwing an exception if its not. Supported types include all the
	 * primitives and String.
	 * 
	 * @param parameterType
	 * @param method
	 */
	public void verifySupportedParameterType(final JType parameterType, final JMethod method) {
		if (false == this.isSupportedParameterType(parameterType, method)) {
			this.throwUnsupportedParameterTypeException(parameterType, method);
		}
	}

	/**
	 * Tests if the given parameter type is supported.
	 * 
	 * @param parameterType
	 * @return
	 */
	protected boolean isSupportedParameterType(final JType parameterType, final JMethod method) {
		ObjectHelper.checkNotNull("parameter:parameterType", parameterType);
		ObjectHelper.checkNotNull("parameter:method", method);

		boolean supported = false;

		while (true) {
			// if its a primitive pass
			if (null != parameterType.isPrimitive()) {
				supported = true;
				break;
			}
			// string is ok
			if (parameterType == this.getRemoteJsonServiceGeneratorContext().getJavaLangString()) {
				supported = true;
				break;
			}
			supported = false;
			break;
		}// while

		return supported;
	}

	protected void throwUnsupportedParameterTypeException(final JType parameterType, final JMethod method) {
		final String message = "The parameter type " + parameterType.getQualifiedSourceName() + " which belongs to the method "
				+ method.getReadableDeclaration() + " is not a supported type. Only primitives and String may be used.";
		throw new UnsupportedParameterTypeException(message);
	}

	private RemoteJsonServiceGeneratorContext remoteJsonServiceGeneratorContext;

	protected RemoteJsonServiceGeneratorContext getRemoteJsonServiceGeneratorContext() {
		ObjectHelper.checkNotNull("field:remoteJsonServiceGeneratorContext", remoteJsonServiceGeneratorContext);
		return this.remoteJsonServiceGeneratorContext;
	}

	public void setRemoteJsonServiceGeneratorContext(final RemoteJsonServiceGeneratorContext remoteJsonServiceGeneratorContext) {
		ObjectHelper.checkNotNull("parameter:remoteJsonServiceGeneratorContext", remoteJsonServiceGeneratorContext);
		this.remoteJsonServiceGeneratorContext = remoteJsonServiceGeneratorContext;
	}

	/**
	 * The method for which this generator is being run.
	 */
	private JMethod method;

	protected JMethod getMethod() {
		ObjectHelper.checkNotNull("field:method", method);
		return this.method;
	}

	protected void setMethod(final JMethod method) {
		ObjectHelper.checkNotNull("parameter:method", method);
		this.method = method;
	}
}
