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
package rocket.remoting.client.json;

import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONNull;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;

/**
 * The general base class for all json service stubs. Two sub-classes exist one
 * that handles POST and the other GET requests. The json response is
 * deserialized using the json serialization api, and then the given
 * AsyncCallback is invoked.
 * 
 * @author Miroslav Pokorny
 */
abstract public class RemoteJsonServiceInvoker implements RequestCallback {

	protected RemoteJsonServiceInvoker() {
		super();
	}

	/**
	 * Initiates a Http Request to the given url and provides a bridge between
	 * the {@link RequestCallback} and the given {@link AsyncCallback}.
	 * 
	 * @return
	 */
	public void makeRequest(final RemoteJsonServiceClient serviceImpl) {
		final RequestBuilder request = this.createRequestBuilder();

		if (serviceImpl.hasUsername()) {
			request.setUser(serviceImpl.getUsername());
			request.setPassword(serviceImpl.getPassword());
		}

		if (serviceImpl.hasTimeout()) {
			request.setTimeoutMillis(serviceImpl.getTimeout());
		}

		this.setHeaders(request);

		try {
			request.sendRequest(this.getRequestData(), this);
		} catch (final RequestException requestException) {
			this.handleFailedRequest(requestException);
		}
	}

	/**
	 * Factory method which creates a new RequestBuilder.
	 * 
	 * @return
	 */
	protected RequestBuilder createRequestBuilder() {
		return new RequestBuilder(this.getRequestMethod(), this.buildUrl());
	}

	/**
	 * This method will be used to build a url for the request that is about to
	 * be submitted.
	 * 
	 * @return
	 */
	abstract protected String buildUrl();

	/**
	 * This property will be set by copying the serviceEntryPoint property from
	 * the matching RemoteJsonServiceClient.
	 */
	private String url;

	protected String getUrl() {
		StringHelper.checkNotEmpty("field:url", url);
		return this.url;
	}

	public void setUrl(final String url) {
		StringHelper.checkNotEmpty("parameter:url", url);
		this.url = url;
	}

	/**
	 * THis method will be implemented by either
	 * {@link RemoteGetJsonServiceInvoker} or
	 * {@link RemotePostJsonServiceInvoker}.
	 * 
	 * @return
	 */
	abstract String getRequestData();

	/**
	 * THis method will be implemented by either
	 * {@link RemoteGetJsonServiceInvoker} or
	 * {@link RemotePostJsonServiceInvoker}.
	 * 
	 * @return
	 */
	abstract RequestBuilder.Method getRequestMethod();

	/**
	 * This method will be implemented by either
	 * {@link RemoteGetJsonServiceInvoker} or
	 * {@link RemotePostJsonServiceInvoker}.
	 */
	abstract protected void setHeaders(final RequestBuilder request);

	public void onResponseReceived(final Request request, final Response response) {
		final int status = response.getStatusCode();
		if (status == Constants.HTTP_RESPONSE_OK) {
			this.handleSuccessfulResponse(request, response);
		} else {
			this.handleFailedResponse(request, response);
		}
	}

	/**
	 * Takes the response and converts the json payload to a java instance using
	 * {@link #deserializeJsonPayload(String)}
	 * 
	 * @param request
	 * @param response
	 */
	protected void handleSuccessfulResponse(final Request request, final Response response) {
		final AsyncCallback callback = this.getCallback();
		Object object = null;
		boolean skipOnFailure = false;

		try {
			final String text = response.getText();
			final JSONValue jsonValue = JSONParser.parse(text);
			if (false == jsonValue.isNull() instanceof JSONNull) {
				object = this.readObject(jsonValue);
			}
			// invoke success! after deserializing...
			skipOnFailure = true;
			callback.onSuccess(object);

		} catch (final Throwable caught) {
			if (false == skipOnFailure) {
				// invoke failure if anything went wrong.
				callback.onFailure(caught);
			}
		}
	}

	/**
	 * This method is implemented by the generator and acts a bridge calling GWT
	 * to create a Serializer which will deserialize the incoming JSON stream
	 * into a java object.
	 * 
	 * @param jsonValue
	 * @return
	 */
	abstract protected Object readObject(final JSONValue jsonValue);

	/**
	 * Dispatches to one of two methods depending on the response code.
	 */
	public void onError(final Request request, final Throwable caught) {
		this.handleFailedRequest(caught);
	}

	/**
	 * This method is invoked if a request fails for any reason that is not the
	 * result of a server failure.
	 * 
	 * @param throwable
	 */
	protected void handleFailedRequest(final Throwable throwable) {
		final Throwable wrapper = new InvocationException("Call to server failed: " + throwable.getMessage(), throwable);
		this.getCallback().onFailure(wrapper);
	}

	/**
	 * Creates an exception that expresses the reason why the server invocation
	 * failed, and executes the {@link #AsyncCallback#callback}
	 * 
	 * @param request
	 * @param response
	 */
	protected void handleFailedResponse(final Request request, final Response response) {
		this.getCallback().onFailure(
				new InvocationException("Call failed on server, " + response.getStatusText() + "(" + response.getStatusCode() + ")"));
	}

	/**
	 * The final target callback that represents the public fascade of this rpc.
	 */
	private AsyncCallback callback;

	private AsyncCallback getCallback() {
		ObjectHelper.checkNotNull("field:callback", callback);
		return this.callback;
	}

	public void setCallback(final AsyncCallback callback) {
		ObjectHelper.checkNotNull("parameter:callback", callback);
		this.callback = callback;
	}

	/**
	 * Accumulates any parameters that accompany the request.
	 */
	private StringBuffer parameters = new StringBuffer();

	protected StringBuffer getParameters() {
		ObjectHelper.checkNotNull("field:parameters", parameters);
		return this.parameters;
	}

	protected void setParameter(final StringBuffer parameters) {
		ObjectHelper.checkNotNull("parameter:parameters", parameters);
		this.parameters = parameters;
	}

	/**
	 * Adds a new boolean value parameter to the parameters that will be sent
	 * when the request is made.
	 * 
	 * @param name
	 * @param booleanValue
	 */
	public void addParameter(final String name, final boolean booleanValue) {
		this.addParameter(name, Boolean.toString(booleanValue));
	}

	public void addParameter(final String name, final byte byteValue) {
		this.addParameter(name, Byte.toString(byteValue));
	}

	public void addParameter(final String name, final short shortValue) {
		this.addParameter(name, Short.toString(shortValue));
	}

	public void addParameter(final String name, final int intValue) {
		this.addParameter(name, Integer.toString(intValue));
	}

	public void addParameter(final String name, final long longValue) {
		this.addParameter(name, Long.toString(longValue));
	}

	public void addParameter(final String name, final float floatValue) {
		this.addParameter(name, Float.toString(floatValue));
	}

	public void addParameter(final String name, final double doubleValue) {
		this.addParameter(name, Double.toString(doubleValue));
	}

	public void addParameter(final String name, final char charValue) {
		this.addParameter(name, Character.toString(charValue));
	}

	public void addParameter(final String name, final String value) {
		StringHelper.checkNotEmpty("parameter:name", name);
		StringHelper.checkNotNull("parameter:value", value);

		final StringBuffer parameters = this.getParameters();

		if (parameters.length() > 0) {
			parameters.append('&');
		}

		parameters.append(name);
		parameters.append('=');
		parameters.append(URL.encode(value));
	}
}
