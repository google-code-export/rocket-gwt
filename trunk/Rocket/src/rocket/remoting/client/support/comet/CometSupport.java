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
import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.Element;

/**
 * A implementation details that applies to standard compliant browsers.
 * 
 * @author Miroslav Pokorny (mP)
 * 
 */
public class CometSupport {
	/**
	 * Standards complient browsers register a onload event which is then
	 * delegated to the CometClient instance which handles deserialization and
	 * calls registered callbacks.
	 * 
	 * @param cometClient
	 * @param frame
	 */
	public void start(CometClient cometClient, Element frame) {
		ObjectHelper.checkNotNull("parameter:cometClient", cometClient);
		this.registerConnectHandler(cometClient, frame);
		this.registerDisconnectHandler(cometClient, frame);
		this.registerObjectRecievedDispatcher(cometClient, frame);
	}

	/**
	 * Registers a function on the main window which will set the __connected
	 * flag on the given iframe to true when executed.
	 * 
	 * @param cometClient
	 * @param frame
	 */
	native protected void registerConnectHandler(final CometClient cometClient, final Element frame)/*-{
	 $wnd.__cometOnConnect = function(){
	 frame.__connected=true;
	 };
	 }-*/;

	native protected void registerDisconnectHandler(final CometClient cometClient, final Element iframe)/*-{
	 var callback = @rocket.remoting.client.CometClient::onDisconnect(Lrocket/remoting/client/CometClient;);
	 iframe.onload=function(){
	 callback( cometClient );
	 }
	 }-*/;

	native protected void registerObjectRecievedDispatcher(final CometClient cometClient, final Element frame)/*-{
	 var callback = @rocket.remoting.client.CometClient::dispatch(Lrocket/remoting/client/CometClient;Ljava/lang/String;)
	 var dispatch = function( serializedForm ){
	 callback( cometClient, serializedForm );     	
	 }
	 $wnd.__cometDispatch = dispatch;
	 }-*/;

	/**
	 * Standards compliant browsers dont need to anything extra to stop a comet
	 * session other than to destroy and deattach the hidden frame.
	 * 
	 * @param cometClient
	 * @param frame
	 */
	public void stop(CometClient cometClient, Element frame) {
	}
}
