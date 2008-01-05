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
package rocket.remoting.client;

import rocket.util.client.Checker;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A value object that contains the result of a request for a web resource done
 * via rpc. In keeping with limitations and uselessness of having a byte array
 * in javascript only text response bodies are supported.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class WebResponse implements IsSerializable {

	/**
	 * The status code returned by the server with the response
	 */
	private int code = -1;

	public int getCode() {
		Checker.greaterThan("field:code", 0, code);
		return this.code;
	}

	public void setCode(final int code) {
		Checker.greaterThan("parameter:code", 0, code );
		this.code = code;
	}

	/**
	 * The stauts message sent by the server
	 */
	private String message;

	public String getMessage() {
		Checker.notEmpty("field:message", message);
		return this.message;
	}

	public void setMessage(final String message) {
		Checker.notEmpty("parameter:message", message);
		this.message = message;
	}

	/**
	 * Any headers that the server replied with.
	 */
	private Headers headers;

	public Headers getHeaders() {
		Checker.notNull("field:headers", headers);
		return this.headers;
	}

	public void setHeaders(final Headers headers) {
		Checker.notNull("parameter:headers", headers);
		this.headers = headers;
	}

	public String getContentType() {
		return this.getHeaders().getValue("Content-type");
	}

	/**
	 * THe output of the web request.
	 */
	private String body;

	public String getBody() {
		Checker.notNull("field:body", body);
		return body;
	}

	public void setBody(final String body) {
		Checker.notNull("parameter:body", body);
		this.body = body;
	}

	public String toString() {
		return super.toString() + ", code: " + code + ", message\"" + message + "\", headers: " + headers + ", body\"" + body + "\".";
	}
}
