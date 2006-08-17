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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * This servlet adds an additional means for ServiceImplementations to get access to the HttpServletResponse.
 * THe fix which swaps the setting of the status and output is within the fixed com.google.gwt.user.server.rpc.RemoteServiceServlet.
 *
 * @author Miroslav Pokorny (mP)
 */
public class RemoteServiceServlet extends
		com.google.gwt.user.server.rpc.RemoteServiceServlet {

	  public final void service(final HttpServletRequest request,
	  		final HttpServletResponse response) throws IOException, ServletException{

	  	this.perThreadResponse.set( response );
	  	super.service( request, response );
	  }

	  private final ThreadLocal perThreadResponse = new ThreadLocal();
	  /**
	   * Gets the <code>HttpServletResponse</code> object for the current call. It
	   * is stored thread-locally so that simultaneous invocations can have
	   * different response objects.
	   */
	  protected HttpServletResponse getThreadLocalResponse() {
	    return (HttpServletResponse) perThreadResponse.get();
	  }

	  public void onAfterResponseSerialized( String output ){
	  	this.log( output );
	  }
}
