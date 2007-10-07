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
package rocket.remoting.client.support;

import rocket.remoting.client.CometClient;

import com.google.gwt.user.client.Element;

/**
 * This CometSupport contains a number of customisations to handle Safari
 * quirks.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class SafariCometSuport extends CometSupport {
	/**
	 * Because safari does not support an onload event or equivalent a timer is
	 * used to poll the status of the iframe.
	 */
	native protected void registerDisconnectHandler(final CometClient cometClient, final Element iframe)/*-{
	 var callback = @rocket.remoting.client.CometClient::onDisconnect(Lrocket/remoting/client/CometClient;Z);
	 var poller = setInterval( function(){
	 var iframeDocument = iframe.document || iframe.contentDocument;
	 var currentReadyState = iframeDocument.readyState;
	 if( currentReadyState == "loaded" || currentReadyState == "complete" ){
	 clearInterval( poller );
	 delete iframe.__cometSafariOnLoadPoller;
	 
	 // if a title tag is not or doesnt contain the magic text count it as a connection failure
	 var connectionFailed = true;
	 var title = window.document.getElementsByTagName( "TITLE" );
	 if( title && title.length == 1 ){
	 connectionFailed = title[ 0 ].innerHTML().indexOf( "RocketCometServer" ) == -1;
	 }
	 callback( cometClient, connectionFailed );                
	 }
	 iframe.__cometSafariOnLoadPoller = poller;
	 };     
	 }-*/;

	/**
	 * Standards compliant browsers dont need to anything extra to stop a comet
	 * session other than to destroy and deattach the hidden frame.
	 * 
	 * @param owner
	 * @param frame
	 */
	public void stop(CometClient owner, Element frame) {
		this.stop0(frame);
	}

	/**
	 * This javascript checks and kills the associated onloadPoller function
	 * which polls the ready state of the iframe. This is necessary as Safari
	 * does not appear to support any of the listener methods used by other
	 * implementations.
	 * 
	 * @param iframe
	 */
	native protected void stop0(final Element iframe) /*-{
	 if( iframe.__cometSafariOnLoadPoller ){
	 clearInterval( iframe.__cometSafariOnLoadPoller );
	 delete iframe.__cometSafariOnLoadPoller;
	 }
	 }-*/;
}
