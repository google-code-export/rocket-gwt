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
package rocket.remoting.client.support.comet;

import rocket.remoting.client.CometClient;

import com.google.gwt.user.client.Element;

/**
 * The InternetExplorer6 specific Comet support class
 * 
 * @author Miroslav Pokorny (mP)
 */
public class InternetExplorer6CometSupport extends CometSupport {
	/**
	 * Internet Explorer6 doesnt support the onload event for iframes therefore
	 * one must watch out for readystate changes.
	 */
	native protected void registerDisconnectHandler(final CometClient cometClient, final Element iframe)/*-{
	 var callback = @rocket.remoting.client.CometClient::onDisconnect(Lrocket/remoting/client/CometClient;);

	 iframe.onreadystatechange=function(){     
	 if( iframe.parentNode && iframe.readyState=="complete" ){
	 callback( cometClient );
	 }     	 
	 }
	 }-*/;
}
