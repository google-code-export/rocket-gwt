package rocket.remoting.server.comet;

import rocket.remoting.client.CometConstants;

/**
 * Instances represent a single exception being pushed from the server to the
 * client.
 */
public class ExceptionPayload implements Message {

	public ExceptionPayload(final Throwable throwable) {
		this.setThrowable(throwable);
	}

	public int getCommand() {
		return CometConstants.EXCEPTION_PAYLOAD;
	}

	public Object getObject() {
		return this.getThrowable();
	}

	private Throwable throwable;

	Throwable getThrowable() {
		return this.throwable;
	}

	void setThrowable(final Throwable throwable) {
		this.throwable = throwable;
	}
}
