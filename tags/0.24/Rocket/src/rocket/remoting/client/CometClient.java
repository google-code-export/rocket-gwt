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

import rocket.dom.client.DomHelper;
import rocket.remoting.client.support.CometSupport;
import rocket.style.client.StyleConstants;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.impl.ClientSerializationStreamReader;
import com.google.gwt.user.client.rpc.impl.Serializer;

/**
 * There should only ever be one instance of this class which is used to receive streamed objects from a server.
 * 
 * <h6>Gotchas</h6>
 * <ul>
 * <li> The only requirement is that the {@link #createProxy() } method is implemented to request for the runtime to create a Proxy for a
 * service that declares a return type that covers objects returned by the server side component. </li>
 * <li> When compiling/translated to javascript the Rocket.jar must be included in the classpath before any google classes so that the
 * custom ProxyGenerator is used instead of the regular class. </li>
 * 
 * @author Miroslav Pokorny (mP)
 */
public abstract class CometClient {

    public CometClient() {
        super();

        this.createSupport();
    }

    /**
     * The browser aware support that takes care of browser difference nasties.
     */
    private CometSupport support;

    protected CometSupport getSupport() {
        ObjectHelper.checkNotNull("field:support", this.support);
        return support;
    }

    protected void setSupport(final CometSupport support) {
        ObjectHelper.checkNotNull("parameter:support", support);
        this.support = support;
    }

    protected void createSupport() {
        this.setSupport((CometSupport) GWT.create(CometSupport.class));
    }

    /**
     * Stops or closes the connection between the client and the server.
     * 
     */
    public void stop() {
        if (this.hasFrame()) {
            final Element frame = this.getFrame();
            DOM.removeChild(DOM.getParent(frame), frame);
            this.clearFrame();
            this.getSupport().stop(this, frame);
        }
    }

    /**
     * Invoking this method opens the channel betweeen the client and the server. The server will periodicly continue to send objects to the
     * client.
     */
    public void start() {
        this.createFrame();
        final Element frame = this.getFrame();
        this.getSupport().start(this, frame);

        // the reason for the query string is to avoid caching problems...the src attribute is set before the frame is attached this also
        // avoids the nasty clicking noises in ie.
        DOM.setAttribute(frame, "src", this.getUrl() + '?' + System.currentTimeMillis());

        final Element body = DomHelper.getBody();
        DOM.appendChild(body, frame);
    }

    /**
     * This method is called by whenever a iframe finishes loading its document. This may be caused by the server dropping its connect or
     * failing the locate a CometServer.
     * 
     * @param thisInstance
     */
    public static void onDisconnect(final CometClient thisInstance) {
        ObjectHelper.checkNotNull("parameter:thisInstance", thisInstance);

        // checks if the iframe has its connected flag set.. if not report connection failure...
        if (false == DOM.getBooleanAttribute(thisInstance.getFrame(), "__connected")) {
            thisInstance.getCallback().onFailure(
                    new CometServerConnectionFailureException("Unable to connect to [" + thisInstance.getUrl() + "]"));
        } else {
            thisInstance.restart();
        }
    }

    /**
     * Restarts or recreates the connection between this client and the server.
     */
    protected void restart() {
        CometClient.this.stop();
        CometClient.this.start();
    }

    /**
     * This function is invoked from the hidden frame and takes care of eventually dispatching the object to the registered callback.
     * 
     * @param c
     * @param serializedForm
     * @throws SerializationException
     */
    public static void dispatch(final CometClient c, final String serializedForm) throws SerializationException {
        c.dispatch(serializedForm);
    }

    public void dispatch(final String serializedForm) throws SerializationException {
        final String serializedForm0 = StringHelper.htmlDecode(serializedForm);

        final boolean failed = serializedForm0.startsWith("{EX}");
        final Object object = deserialize(serializedForm0.substring(4));

        final AsyncCallback callback = this.getCallback();
        if (failed) {
            callback.onFailure((Throwable) object);
        } else {
            callback.onSuccess(object);
        }
    }

    /**
     * Deserializes the Object and its graph which are encoded within the given String.
     * 
     * @param serializedForm
     * @return
     * @throws SerializationException
     */
    protected Object deserialize(final String serializedForm) throws SerializationException {
        StringHelper.checkNotEmpty("parameter:serializedForm", serializedForm);

        final Object proxy = this.createProxy();
        final HasSerializer serializerHost = (HasSerializer) proxy;
        final Serializer serializer = serializerHost.getSerializer();

        final ClientSerializationStreamReader deserializer = new ClientSerializationStreamReader(serializer);
        deserializer.prepareToRead(serializedForm);
        return deserializer.readObject();
    }

    /**
     * Sub-classes must override this method to create the ServiceProxy using defered binding.
     * 
     * <pre>
     *   return GWT.create( INSERT SERVICE CLASS.class );
     * </pre>
     * 
     * @return
     */
    protected abstract Object createProxy();

    /**
     * A reference to the hidden iframe which is used to make a connection which is kept open for a long time. The server will periodically
     * write objects within a script tag to the client(iframe).
     */
    private Element frame;

    protected Element getFrame() {
        ObjectHelper.checkNotNull("field:frame", frame);
        return this.frame;
    }

    protected boolean hasFrame() {
        return null != this.frame;
    }

    protected void setFrame(final Element frame) {
        ObjectHelper.checkNotNull("parameter:frame", frame);
        this.frame = frame;
    }

    protected void clearFrame() {
        this.frame = null;
    }

    protected void createFrame() {
        final Element frame = DOM.createIFrame();
        DOM.setStyleAttribute(frame, StyleConstants.WIDTH, "0px");
        DOM.setStyleAttribute(frame, StyleConstants.HEIGHT, "0px");
        DOM.setStyleAttribute(frame, StyleConstants.BORDER, "0px");
        DOM.setStyleAttribute(frame, StyleConstants.PADDING, "0px");
        DOM.setStyleAttribute(frame, StyleConstants.MARGIN, "0px");
        this.setFrame(frame);
    }

    /**
     * The url of the server side portion of this component.
     */
    private String url;

    public String getUrl() {
        StringHelper.checkNotEmpty("field:url", url);
        return this.url;
    }

    public void setUrl(final String url) {
        StringHelper.checkNotEmpty("parameter:url", url);
        this.url = url;
    }

    /**
     * This callback receives all objects and exceptions recieved from the server.
     */
    private AsyncCallback callback;

    public AsyncCallback getCallback() {
        ObjectHelper.checkNotNull("field:callback", callback);
        return this.callback;
    }

    public void setCallback(final AsyncCallback callback) {
        ObjectHelper.checkNotNull("parameter:callback", callback);
        this.callback = callback;
    }
}