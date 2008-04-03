package rocket.remoting.test.comet.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rocket.remoting.server.CometServerServlet;
import rocket.remoting.test.comet.client.TestCometPayload;

/**
 * A simple implementation of the server side portion of a Comet subsystem.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class TestCometServerServlet extends CometServerServlet {

	final static int TIMEOUT_SLEEP = 60 * 1000;
	final static int MAXIMUM_BYTES_WRITTEN = 1 * 1024;
	final static int CONNECTION_TIMEOUT = 5 * 1024;
	
	/**
	 * Hardcodes the connectionTimeout and maximumBytesWritten messages.
	 */
	public void init() {
		this.setConnectionTimeout( CONNECTION_TIMEOUT );
		this.setMaximumBytesWritten( MAXIMUM_BYTES_WRITTEN );
	}

	/**
	 * Adds a guard to send a 500 and drop the connection if the query to {@link CometServerActionServiceImpl#isFailNextConnection()} returns true.
	 * Otherwise the request continues so that the comet server can do its thing.
	 */
	public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
		if( CometServerActionServiceImpl.isFailNextConnection() ){
			response.sendError( 500 );
		}else {
			super.doGet(request, response);
		}
	}
	
	protected void poller(final ServletOutputStream output) throws IOException, ServletException {
		super.poller(output);

		this.log("Server is closing connection.");
	}

	/**
	 * This method pushes a test payload after blocking between 1 and 5 seconds.
	 */
	protected void poll() {
		this.log("Server being polled ");

		while( true ){
			if( CometServerActionServiceImpl.isFailNextPoll() ){
				this.fail();
				break;
			}
			
			if( CometServerActionServiceImpl.isTimeoutNextPoll() ){
				this.timeout();
				break;
			}
			
			// check if the session should terminate ?
			if (CometServerActionServiceImpl.isTerminated()) {
				this.terminate();
				break;
			}	
			
			// otherwise just push an object...
			try {
				final long millis = 1000 + new java.util.Random().nextInt(4000);
				this.log("Server thread is sleeping for " + millis);
				Thread.sleep(millis);
			} catch (final InterruptedException ignored) {
			}

			final TestCometPayload payload = new TestCometPayload();
			payload.setTimestamp(System.currentTimeMillis());
			this.push(payload);
			break;
		}
	}

	protected void fail() {
		this.log("Server is failing during poll request." );
		throw new RuntimeException( "Exception thrown on server.");
	}

	
	protected void push(final Object object) {
		this.log("Server is pushing " + object);
		super.push(object);
	}

	protected void push(final Throwable throwable) {
		this.log("Server is pushing " + throwable);
		super.push(throwable);
	}

	/**
	 * Sub classes may call this method to send a terminate message to the client.
	 */
	protected void terminate() {
		this.log("Server terminating comet session");
		super.terminate();
	}
	
	protected void timeout(){
		this.log("Server will sleep for a long time to simulate a server timeout");
		try{
			Thread.sleep( TIMEOUT_SLEEP );
		} catch ( final InterruptedException ignored ){
			
		}
		this.log( "Server has awaken from sleep, client should have dropped connection and attempted to reconnect.");
	}

	protected void flush(final ServletOutputStream servletOutputStream) throws IOException {
		this.log("Server is flushing buffers.");

		super.flush(servletOutputStream);
	}

	protected void onByteWriteLimitExceeded(final int byteWriteCount) {
		this.log("Server has written too many bytes written to client, will drop connection, " + byteWriteCount + "/"
				+ this.getMaximumBytesWritten());
	}

	protected void onConnectionOpenTooLong(final long milliseconds) {
		this.log("Server has detected connection has been open too long will drop connection, " + milliseconds + "/"
				+ this.getConnectionTimeout());
	}

	public void log(final String message) {
		super.log(message);
		System.out.println(message);
	}
}
