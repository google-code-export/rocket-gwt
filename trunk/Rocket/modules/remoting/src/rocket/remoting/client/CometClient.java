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

import rocket.dom.client.Dom;
import rocket.remoting.client.support.comet.CometSupport;
import rocket.style.client.Css;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.util.client.Checker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * There should only ever be one instance of this class which is used to receive
 * streamed objects from a server.
 * 
 * <h6>Gotchas</h6>
 * <ul>
 * <li> When compiling/translated to javascript the Rocket.jar must be included
 * in the classpath before any google classes so that the custom ProxyGenerator
 * is used instead of the regular unmodified class. </li>
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class CometClient {

	/**
	 * Singleton that holds the comet support class.
	 */
	static CometSupport support = (CometSupport) GWT.create(CometSupport.class);

	protected CometClient() {
		super();
	}

	CometSupport getSupport() {
		return support;
	}

	/**
	 * Invoking this method opens the channel betweeen the client and the
	 * server. The server will periodicly continue to send objects to the client
	 * as they are streamed to the server.
	 */
	public void start() {
		final Element frame = this.createFrame();
		this.setFrame(frame);

		this.getSupport().start(this, frame);

		// the reason for the query string is to avoid caching problems...the
		// src attribute is set before the frame is attached this also
		// avoids the nasty clicking noises in ie.
		DOM.setElementProperty(frame, "src", this.getServiceEntryPoint() + "?serializationEngine=Gwt&" + System.currentTimeMillis());

		final Element body = RootPanel.getBodyElement();
		DOM.appendChild(body, frame);
	}

	/**
	 * Stops an existing comet session by closing the connection between the
	 * client and the server.
	 */
	public void stop() {
		if (this.hasFrame()) {
			final Element frame = this.getFrame();
			Dom.removeFromParent(frame);
			this.clearFrame();
			this.getSupport().stop(this, frame);
		}
	}

	/**
	 * This method is called by whenever a iframe finishes loading its document.
	 * This may be caused by the server dropping its connect or failing the
	 * locate a CometServer.
	 * 
	 * @param cometClient
	 */
	static void onDisconnect(final CometClient cometClient) {
		Checker.notNull("parameter:cometClient", cometClient);

		cometClient.disconnect();
	}

	protected void disconnect() {
		// checks if the iframe has its connected flag set.. if not report
		// connection failure...
		if (false == DOM.getElementPropertyBoolean(this.getFrame(), "__connected")) {
			this.onUnableToConnect();
		} else {
			this.restart();
		}
	}

	protected void onUnableToConnect() {
		this.getCallback().onFailure(new CometException("Unable to connect to \"" + this.getServiceEntryPoint() + "\"."));
	}

	/**
	 * Restarts or recreates the connection between this client and the server.
	 */
	protected void restart() {
		this.stop();
		this.start();
	}

	/**
	 * This function is invoked from the hidden frame and takes care of
	 * eventually dispatching the object to the registered callback.
	 * 
	 * @param cometClient
	 * @param serializedForm
	 */
	static void dispatch(final CometClient cometClient, final String serializedForm) {
		cometClient.dispatch(serializedForm);
	}

	/**
	 * This method is overridden to deserialize and then dispatch the object to
	 * the registered payload
	 * 
	 * @param serializedForm
	 *            The serialized form of the incoming object.
	 */
	abstract public void dispatch(final String serializedForm);

	/**
	 * This method is invoked whenever a command received from the server is
	 * unknown, by default an exception is thrown.
	 * 
	 * @param command
	 */
	protected void onUnknownCommand(final int command) {
		throw new CometException("Unknown command recieved from server, command: " + command);
	}

	/**
	 * A reference to the hidden iframe which is used to make a connection which
	 * is kept open for a long time. The server will periodically write objects
	 * within a script tag to the client(iframe).
	 */
	private Element frame;

	protected Element getFrame() {
		Checker.notNull("field:frame", frame);
		return this.frame;
	}

	protected boolean hasFrame() {
		return null != this.frame;
	}

	protected void setFrame(final Element frame) {
		Checker.notNull("parameter:frame", frame);
		this.frame = frame;
	}

	protected void clearFrame() {
		this.frame = null;
	}

	protected Element createFrame() {
		final Element frame = DOM.createIFrame();
		final InlineStyle inlineStyle = InlineStyle.getInlineStyle(frame);
		inlineStyle.setInteger(Css.WIDTH, 0, CssUnit.PX);
		inlineStyle.setInteger(Css.HEIGHT, 0, CssUnit.PX);
		inlineStyle.setInteger(Css.BORDER, 0, CssUnit.PX);
		inlineStyle.setInteger( Css.PADDING, 0, CssUnit.PX);
		inlineStyle.setInteger(Css.MARGIN, 0, CssUnit.PX);
		return frame;
	}

	/**
	 * The url of the server side portion of this component.
	 */
	private String serviceEntryPoint;

	public String getServiceEntryPoint() {
		Checker.notEmpty("field:serviceEntryPoint", serviceEntryPoint);
		return this.serviceEntryPoint;
	}

	public void setServiceEntryPoint(final String serviceEntryPoint) {
		Checker.notEmpty("parameter:serviceEntryPoint", serviceEntryPoint);
		this.serviceEntryPoint = serviceEntryPoint;
	}

	/**
	 * This callback receives and handles all comet related events.
	 */
	private CometCallback callback;

	public CometCallback getCallback() {
		Checker.notNull("field:callback", callback);
		return this.callback;
	}

	public void setCallback(final CometCallback callback) {
		Checker.notNull("parameter:callback", callback);
		this.callback = callback;
	}

	public String toString() {
		return super.toString() + ", serviceEntryPoint: \"" + this.serviceEntryPoint + "\", callback: " + this.callback;
	}
}