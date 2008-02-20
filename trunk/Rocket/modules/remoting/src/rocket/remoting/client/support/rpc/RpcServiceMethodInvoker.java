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
package rocket.remoting.client.support.rpc;

import rocket.remoting.client.RpcException;
import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Common base class for both java and json rpc method invokers.
 * @author Miroslav Pokorny
 */
abstract public class RpcServiceMethodInvoker implements RequestCallback{
	
	/**
	 * Copies over the user credentials, timeout and service entry point from the rpc service client
	 * to this particular method invoker.
	 * @param client The source
	 */
	public void prepare( final RpcServiceClient client ){
		ObjectHelper.checkNotNull("parameter:client", client );
		
		this.setUrl( client.getServiceEntryPoint());

		// copy over any authentication details 
		if( client.hasUsername() ){
			this.setUsername( this.getUsername() );
			this.setPassword( this.getPassword() );
		}
		
		// copy timeout value if present...
		if( client.hasTimeout() ){
			this.setTimeout( this.getTimeout() );
		}
	}
	
	/**
	 * Initiates a Http Request to the given url and provides a bridge between
	 * the {@link RequestCallback} and the given {@link AsyncCallback}.
	 */
	public void makeRequest() {
		final RequestBuilder request = this.createRequestBuilder();

		if (this.hasUsername()) {
			request.setUser(this.getUsername());
			request.setPassword(this.getPassword());
		}

		if (this.hasTimeout()) {
			request.setTimeoutMillis(this.getTimeout());
		}

		this.setHeaders(request);

		try {
			request.sendRequest(this.getRequestData(), this);
		} catch (final RequestException requestException) {
			this.onFailedRequest(requestException);
		}
	}

	/**
	 * Factory method which creates a new RequestBuilder.
	 * 
	 * @return
	 */
	abstract protected RequestBuilder createRequestBuilder();

	/**
	 * THis method will be implemented by either
	 * {@link GetJsonRpcServiceMethodInvoker} or
	 * {@link PostJsonServiceMethodInvoker}.
	 * 
	 * @return
	 */
	abstract String getRequestData();

	/**
	 * THis method will be implemented by either
	 * {@link GetJsonRpcServiceMethodInvoker} or
	 * {@link PostJsonServiceMethodInvoker}.
	 * 
	 * @return
	 */
	abstract RequestBuilder.Method getRequestMethod();

	/**
	 * This method will be implemented by either
	 * {@link GetJsonRpcServiceMethodInvoker} or
	 * {@link PostJsonServiceMethodInvoker}.
	 */
	abstract protected void setHeaders(final RequestBuilder request);

	public void onResponseReceived(final Request request, final Response response) {
		final int status = response.getStatusCode();
		if (status == Constants.HTTP_RESPONSE_OK) {
			this.onSuccessfulResponse(request, response);
		} else {
			this.onFailedResponse(request, response);
		}
	}
	
	/**
	 * Handles a successful response (status code =200) reply from the server.
	 * @param request The initial request
	 * @param response The server response
	 */
	abstract void onSuccessfulResponse(final Request request, final Response response);
	
	/**
	 * Dispatches to one of two methods depending on the response code.
	 */
	public void onError(final Request request, final Throwable caught) {
		this.onFailedRequest(caught);
	}

	/**
	 * This method is invoked if a request fails for any reason that is not the
	 * result of a server failure.
	 *
	 * @param throwable A throwable holding what went wrong.
	 */
	protected void onFailedRequest(final Throwable throwable) {
		final Throwable wrapper = new RpcException("Call to server failed: " + throwable.getMessage(), throwable);
		this.getCallback().onFailure(wrapper);
	}

	/**
	 * Creates an exception that expresses the reason why the server invocation
	 * failed, and executes the {@link AsyncCallback#onFailure(Throwable)}
	 *
	 * @param request
	 * @param response
	 */
	protected void onFailedResponse(final Request request, final Response response) {
		this.getCallback().onFailure( new RpcException("Call failed on server, " + response.getStatusText() + "(" + response.getStatusCode() + ")"));
	}
	
	/**
	 * This property will be set by copying the serviceEntryPoint property from
	 * the matching JsonRpcServiceClient.
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
	 * When present a username and password is also attached to the request.
	 */
	private String username;

	protected String getUsername() {
		StringHelper.checkNotEmpty("field:username", username);
		return this.username;
	}

	public boolean hasUsername() {
		return null != username;
	}

	public void setUsername(final String username) {
		StringHelper.checkNotEmpty("parameter:username", username);
		this.username = username;
	}

	/**
	 * To add authentication to any request both the username and password
	 * properties must be set.
	 */
	private String password;

	protected String getPassword() {
		StringHelper.checkNotEmpty("field:password", password);
		return this.password;
	}

	public boolean hasPassword() {
		return null != password;
	}

	public void setPassword(final String password) {
		StringHelper.checkNotEmpty("parameter:password", password);
		this.password = password;
	}

	/**
	 * This property must be set to allow a custom timeout value for this rpc.
	 */
	private int timeout;

	protected int getTimeout() {
		PrimitiveHelper.checkGreaterThan("field:timeout", 0, timeout);
		return timeout;
	}

	public boolean hasTimeout() {
		return this.timeout > 0;
	}

	public void setTimeout(final int timeout) {
		PrimitiveHelper.checkGreaterThan("parameter:timeout", 0, timeout );
		this.timeout = timeout;
	}
	

	/**
	 * The callback that will have either of its two method invoked depending on the result recieved from the server.
	 */
	private AsyncCallback callback;

	AsyncCallback getCallback() {
		ObjectHelper.checkNotNull("field:callback", callback);
		return this.callback;
	}

	public void setCallback(final AsyncCallback callback) {
		ObjectHelper.checkNotNull("parameter:callback", callback);
		this.callback = callback;
	}
}
