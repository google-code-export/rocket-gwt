/*
 * Copyright 2006 NSW Police Government Australia
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
package rocket.server.rpc;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import rocket.client.rpc.Headers;
import rocket.client.rpc.RequestParameters;
import rocket.client.util.StringHelper;
import rocket.server.util.ObjectHelper;

/**
 * This request supports simulating posting of data from the client using rpc eventually making a regular POSt to a local web app resource.
 *
 * @author Miroslav Pokorny (mP)
 */
public class PostHttpServletRequest extends AbstractHttpServletRequest implements HttpServletRequest {

	public PostHttpServletRequest(final HttpServletRequest request, final String url, final Headers headers,
			final RequestParameters parameters) {
		super(request, url, headers);

		this.setRequestParameters(parameters);
	}

	public PostHttpServletRequest(final HttpServletRequest request, final String url, final Headers headers, final byte[] data) {
		super(request, url, headers);

		this.setData(data);
	}

	/**
	 * The bytes that are the post data.
	 */
	private byte[] data;

	public byte[] getData() {
		if (this.hasRequestParameters()) {
			throw new IllegalStateException("Parameters have been read post data may not be read.");
		}

		if (false == this.hasData()) {
			throw new IllegalStateException("This request was created without any post data");
		}
		return this.data;
	}

	protected boolean hasData() {
		return this.data != null;
	}

	protected void setData(final byte[] data) {
		StringHelper.checkNotNull("parameter:data", data);
		this.data = data;
	}

	public String getMethod() {
		return "POST";
	}

	public int getContentLength() {
		return this.getData().length;
	}

	/**
	 * An inputStream being used to read the post data from this request. There can only ever be either an inputStream or reader active
	 * never both.
	 */
	private ServletInputStream inputStream;

	public ServletInputStream getInputStream() throws IOException {
		if (false == this.hasInputStream()) {
			this.createInputStream();
		}

		ObjectHelper.checkPropertySet("inputStream", this, this.hasInputStream());
		return this.inputStream;
	}

	protected boolean hasInputStream() {
		return null != this.inputStream;
	}

	protected void setInputStream(final ServletInputStream inputStream) {
		ObjectHelper.checkNotNull("parameter:inputStream", inputStream);
		ObjectHelper.checkPropertySet("inputStream", this, this.hasInputStream());
		ObjectHelper.checkPropertySet("reader", this, this.hasReader());

		this.inputStream = inputStream;
	}

	protected void createInputStream() {
		ObjectHelper.checkNotNull("parameter:inputStream", inputStream);
		ObjectHelper.checkPropertySet("inputStream", this, this.hasInputStream());
		ObjectHelper.checkPropertySet("reader", this, this.hasReader());

		this.setInputStream(new ByteArrayServletInputStream(this.getData()));
	}

	/**
	 * An BufferedReader being used to read the post data from this request. There can only ever be either an inputStream or reader
	 * active never both.
	 */
	private BufferedReader reader;

	public BufferedReader getReader() throws IOException {
		if (false == this.hasReader()) {
			this.createReader();
		}

		ObjectHelper.checkPropertySet("reader", this, this.hasReader());
		return this.reader;
	}

	protected boolean hasReader() {
		return null != this.reader;
	}

	protected void setReader(final BufferedReader reader) {
		ObjectHelper.checkNotNull("parameter:reader", reader);
		ObjectHelper.checkPropertySet("reader", this, this.hasReader());
		ObjectHelper.checkPropertySet("inputStream", this, this.hasInputStream());

		this.reader = reader;
	}

	protected void createReader() {
		ObjectHelper.checkPropertySet("reader", this, this.hasReader());
		ObjectHelper.checkPropertySet("inputStream", this, this.hasInputStream());
		this.reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(this.getData())));
		;
	}

	protected RequestParameters getRequestParameters() {
		if (false == this.hasRequestParameters()) {
			this.createRequestParameters();
		}

		return super.getRequestParameters();
	}

	protected void createRequestParameters() {
		final String postData = new String(this.getData());

		final RequestParameters parameters = new RequestParameters();
		parameters.buildFromQueryString( postData );
		this.setRequestParameters(parameters);
	}

	public String toString() {
		return super.toString() + ", data: " + data + ", inputStream: " + inputStream + ", reader: " + reader;
	}
}
