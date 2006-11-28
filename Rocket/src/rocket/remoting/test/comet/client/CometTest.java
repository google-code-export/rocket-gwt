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
package rocket.remoting.test.comet.client;

import rocket.remoting.client.CometClient;
import rocket.style.client.StyleConstants;
import rocket.util.client.ObjectHelper;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class CometTest implements EntryPoint {
    /**
     * This is the same url as the one used to map the test servlet in the accompanying *.gwt.xml file.
     */
    final String COMET_SERVER_URL = "./cometServer";
    final String INVALID_COMET_SERVER_URL = "./invalid";
    
    public void onModuleLoad() {
        GWT.setUncaughtExceptionHandler( new UncaughtExceptionHandler(){
            public void onUncaughtException(final Throwable caught){
                System.err.println( "caught:" + caught.getMessage() );
                caught.printStackTrace();
            }
        });
        this.createComet();

        final Button start = new Button("Start w/ valid Url");
        start.addClickListener( new ClickListener(){
           public void onClick( final Widget sender ){
               final CometClient client = CometTest.this.getCometClient();
               client.setUrl( COMET_SERVER_URL );
               client.start();
           }
        });
        RootPanel.get().add( start );

        final Button startUsingInvalidUrl = new Button("Start w/ invalid Url(Log should include exceptions)");
        startUsingInvalidUrl.addClickListener( new ClickListener(){
           public void onClick( final Widget sender ){
               final CometClient client = CometTest.this.getCometClient();
               client.setUrl( INVALID_COMET_SERVER_URL );
               client.start();
           }
        });
        RootPanel.get().add( startUsingInvalidUrl );
        
        final Button stop = new Button("Stop");
        stop.addClickListener( new ClickListener(){
           public void onClick( final Widget sender ){
               CometTest.this.getCometClient().stop();
           }
        });
        RootPanel.get().add( stop );

        final Button clearLog = new Button("ClearLog");
        clearLog.addClickListener( new ClickListener(){
           public void onClick( final Widget sender ){
               DOM.setInnerHTML( DOM.getElementById( "log"), "");
           }
        });
        RootPanel.get().add( clearLog );
    }

    private CometClient cometClient;

    CometClient getCometClient(){
        ObjectHelper.checkNotNull("field:cometClient", cometClient );
        return this.cometClient;
    }
    void setCometClient(final CometClient cometClient ){
        ObjectHelper.checkNotNull("parameter:cometClient", cometClient );
        this.cometClient = cometClient;
    }

    protected void createComet(){
        final CometClient client = new TestCometClient();
        client.setCallback( new AsyncCallback(){
            public void onSuccess( final Object result ){
                log( "Callback.onSuccess() - " + GWT.getTypeName( result ) + "=[" + result + "]");
                
                final TestCometPayload payload = (TestCometPayload) result;
                final long now = System.currentTimeMillis();
                final long serverTime = payload.getDate().getTime();
                final boolean deliveredImmediately =  now - serverTime < 1000;
                log( "Callback.onSuccess() - now: " + now + " serverTime: " + serverTime + " now-serverTime: " + ( now - serverTime ) + ", deliveredImmediately: " + deliveredImmediately ); 
            }
           public void onFailure( Throwable throwable ){
               log( "Callback.onFailure() - " + throwable );
           }
        });
        this.setCometClient( client );
    }

    /**
     * This test class sets the width/height of the hidden iframe so its contents are visible.
     * All other behaviour remains unchanged.
     * @author Miroslav Pokorny (mP)
     */
    public class TestCometClient extends CometClient {
        
        protected Object createProxy(){
            return GWT.create( TestCometService.class );
        }
        
        protected void createFrame(){
            super.createFrame();

            final Element frame = this.getFrame();
            DOM.setStyleAttribute( frame, StyleConstants.WIDTH, "100px");
            DOM.setStyleAttribute( frame, StyleConstants.HEIGHT, "100px");
            DOM.setStyleAttribute( frame, StyleConstants.BORDER, "1px");
        }

        public void start(){
            super.start();
            log( "CometClient.start() - Starting comet client session...");
        }
        public void stop(){
            super.stop();
            log( "CometClient.stop() - Stopping comet client session...");
        }
        public void dispatch( final String serializedForm ) throws SerializationException{
            log( "CometClient.dispatch() - serializedForm [" + serializedForm + "]");
            super.dispatch( serializedForm );
        }
        public void restart(){
            log( "CometClient.restart() - restarting new connection to server...");
            super.restart();
        }
    }

    protected void log( final Object object ){
        final Element element = DOM.getElementById("log");
        DOM.setInnerHTML( element, DOM.getInnerHTML( element ) + "<br>" + object );
    }
}
