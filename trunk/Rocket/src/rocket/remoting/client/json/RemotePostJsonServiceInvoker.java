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

import com.google.gwt.http.client.RequestBuilder;

/**
 * A convenient base class for any invoker that uses the POST method to send
 * data to the server.
 * 
 * @author Miroslav Pokorny
 */
abstract public class RemotePostJsonServiceInvoker extends RemoteJsonServiceInvoker {

	final protected String buildUrl() {
		return this.getUrl();
	}

	final protected String getRequestData() {
		return this.getParameters().toString();
	}

	final protected RequestBuilder.Method getRequestMethod() {
		return RequestBuilder.POST;
	}

	protected void setHeaders(final RequestBuilder request) {
		request.setHeader(Constants.CONTENT_TYPE_HEADER, Constants.POST_CONTENT_TYPE);
		request.setHeader(Constants.CONTENT_LENGTH_HEADER, "" + this.getRequestData().length());
	}
}
