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
package rocket.remoting.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rocket.remoting.client.FailedWebRequestException;
import rocket.remoting.client.WebRequest;
import rocket.remoting.client.WebRequestService;
import rocket.remoting.client.WebResponse;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * This server side service simply does the WebRequest and returns the
 * WebResponse to the client.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class WebRequestServiceImpl extends RemoteServiceServlet implements WebRequestService {

	public WebResponse doRequest(final WebRequest webRequest) throws FailedWebRequestException {
		try {
			final HttpServletRequest request = this.getThreadLocalRequest();
			final HttpServletResponse response = this.getThreadLocalResponse();

			final WebResponse webResponse = WebHelper.doWebRequest(request, response, webRequest);
			return webResponse;
		} catch (final FailedWebRequestException caught) {
			this.log(caught.getMessage(), caught);
			throw caught;
		}
	}

}
