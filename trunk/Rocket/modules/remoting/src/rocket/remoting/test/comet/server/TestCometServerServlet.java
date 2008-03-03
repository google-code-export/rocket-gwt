package rocket.remoting.test.comet.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import rocket.remoting.server.CometServerServlet;
import rocket.remoting.test.comet.client.TestCometPayload;

/**
 * A simple implementation of the server side portion of a Comet subsystem.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class TestCometServerServlet extends CometServerServlet {

	/**
	 * Hardcodes the connectionTimeout and maximumBytesWritten messages.
	 */
	public void init() {
		this.setConnectionTimeout(5 * 1000);
		this.setMaximumBytesWritten(1 * 1024);
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

		// check if the session should terminate ?
		if (TerminateCometSessionService.isTerminated()) {
			this.terminate();
		} else {

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
		}
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
