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
package rocket.remoting.test.comet.client;

import rocket.remoting.client.CometCallback;
import rocket.remoting.client.CometClient;
import rocket.remoting.client.GwtSerializationCometClient;
import rocket.style.client.Css;
import rocket.style.client.CssUnit;
import rocket.style.client.InlineStyle;
import rocket.util.client.StackTrace;
import rocket.widget.client.Label;
import rocket.widget.client.UnorderedListPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class CometTest implements EntryPoint {
	/**
	 * This is the same url as the one used to map the test servlet in the
	 * accompanying *.gwt.xml file.
	 */
	static final String COMET_SERVER_URL = "./server";	

	static final String INVALID_COMET_SERVER_URL = "./invalid";

	static final String SERVER_TERMINATE_COMET_SESSION_URL = "./terminate";
	
	static final long TOO_MUCH_LAG = 1000;

	static final int MAX_LOG_MESSAGES = 20;
	
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				caught.printStackTrace();
				Window.alert(StackTrace.asString(caught));
			}
		});
		
		final Logger logger = new Logger();
		
		final CometClient cometClient = this.createComet( logger );

		final Button start = new Button("Start");
		start.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				cometClient.setServiceEntryPoint(COMET_SERVER_URL);
				cometClient.start();
			}
		});

		final Button startWithBadUrl = new Button("Start w/ bad Url");
		startWithBadUrl.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				cometClient.setServiceEntryPoint(INVALID_COMET_SERVER_URL);
				cometClient.start();
			}
		});

		final Button clientStopper = new Button("Client Terminate");
		clientStopper.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				cometClient.stop();
			}
		});
		
		
		final Button serverStopper = new Button("Server Terminate");
		serverStopper.addClickListener(new ClickListener() {
			public void onClick(final Widget sender) {
				final TerminateServerSessionServiceAsync terminator = (TerminateServerSessionServiceAsync)GWT.create( TerminateServerSessionService.class );
				((ServiceDefTarget)terminator).setServiceEntryPoint( SERVER_TERMINATE_COMET_SESSION_URL );
				terminator.terminate( new AsyncCallback(){
					public void onSuccess( final Object ignored ){
						logger.log("Client has completed request to server to terminate push terminate message.");
					}
					public void onFailure( final Throwable cause ){
						logger.log( "Client failed to send request to terminate comet session, message: " + cause.getMessage() );
						cause.printStackTrace();
					}
				});
			}
		});
		
		final RootPanel rootPanel = RootPanel.get();
		rootPanel.add( start );
		rootPanel.add( startWithBadUrl );
		rootPanel.add( clientStopper );
		rootPanel.add( serverStopper );
		rootPanel.add( logger );
	}

	/**
	 * Factory which creates a CometClient which logs each and every message to the given Logger sink.  
	 * @return
	 */
	protected CometClient createComet( final Logger logger ) {
		final TestGwtSerializationCometClient cometClient = (TestGwtSerializationCometClient) GWT.create(TestGwtSerializationCometClient.class);
		cometClient.setCallback( new CometCallback(){
			public void onPayload( final Object object ){
				logger.log( "Client received \"" + object + "\"...");
				
				final TestCometPayload payload = (TestCometPayload) object;
				final long date = payload.getTimestamp() % 999999;
				final long now = System.currentTimeMillis() % 999999;  
				final long lag = Math.abs( now - date );
				
				logger.log( "Client latency " + lag + " milliseconds, now: " + now + ", payload timestamp: " + date);
				
				if( lag > TOO_MUCH_LAG ){
					throw new AssertionError("Too much lag between push and object being received, lag: " + lag );
				}
			}
			public void onTerminate(){
				logger.log( "Client has had comet session terminated upon server request.");
			}
			
			public void onFailure( final Throwable cause ){
				logger.log( "Client comet session failure, cause: " + cause.getMessage() );
				
				cause.printStackTrace();
			}			
		});
		cometClient.setLogger( logger );
		return cometClient;
	}
	
	/**
	 * This test class sets the width/height of the hidden iframe so its
	 * contents are visible. All other behaviour remains unchanged.
	 * @comet-payloadType rocket.remoting.test.comet.client.TestCometPayload
	 */
	abstract static public class TestGwtSerializationCometClient extends GwtSerializationCometClient {
		
		protected Element createFrame() {
			final Element frame = super.createFrame();

			InlineStyle.setInteger(frame, Css.WIDTH, 100, CssUnit.PX);
			InlineStyle.setInteger(frame, Css.HEIGHT, 100, CssUnit.PX);
			InlineStyle.setInteger(frame, Css.BORDER, 1, CssUnit.PX);

			return frame;
		}

		public void start() {
			this.log("Client is starting new session...");
			super.start();
		}

		public void stop() {
			this.log("Client is stopping existing session...");
			super.stop();
		}
			
		public void dispatch(final String serializedForm){
			this.log("Client about to deserialize \"" + serializedForm + "\".");
			super.dispatch(serializedForm);
		}

		public void restart() {
			this.log("Client is restarting new connection to server...");
			super.restart();
		}
		 
		void log( final String message ){
			this.getLogger().log(message);
		}
		
		Logger logger;
		
		protected Logger getLogger(){
			return this.logger;
		}
		public void setLogger( final Logger logger ){
			this.logger = logger;
		}
	}

	/**
	 * A simple logger which uses an UnorderedListPanel to record log messages.
	 */
	
	static class Logger extends UnorderedListPanel{
		public void log( String message ){
			final int size = this.getWidgetCount();
			if( size == MAX_LOG_MESSAGES ){
				this.remove( 0 );
			}
			this.add( new Label( message ));
			
			System.out.println( message );
		}
	}
}