package rocket.remoting.client;

import rocket.util.client.ObjectHelper;
import rocket.util.client.PrimitiveHelper;

import com.google.gwt.user.client.Timer;

/**
 * The Throttler class is used to throttle or control requests typically to the
 * server, so that multiple requests are consumed and only a single *DELAYED*
 * request is actually made.
 * 
 * This is typically useful when doing server requests for search as you fill in
 * the textbox.
 * 
 * @author n9834386
 */
public abstract class Throttler {

	protected Throttler() {
	}

	/**
	 * Submits a request for an action to be executed later. Invoking this
	 * method more than once within a period of time results in the action.
	 * Prior to calling this method the period must be set via
	 * {@link #setPeriod}. {@link #doAction} being executed only once.
	 */
	public void doActionLater() {
		if (false == hasTimer()) {
			this.createTimer();
		}
	}

	/**
	 * Sub-classes must override this method and perform the delayed action
	 * here.
	 */
	protected abstract void doAction();

	//

	/**
	 * The doAction method will only be called once every period in
	 * milliseconds, even if more than one request is made. Subsequent requests
	 * are ignored.
	 */
	private int period;

	public int getPeriod() {
		PrimitiveHelper.checkGreaterThan("field:period", period, 0);
		return period;
	}

	public void setPeriod(final int period) {
		PrimitiveHelper.checkGreaterThan("parameter:period", period, 0);
		this.period = period;
	}

	/**
	 * This is the timer which is used to execute the delayed request
	 */
	private Timer timer;

	protected Timer getTimer() {
		ObjectHelper.checkNotNull("field:timer", timer);
		return timer;
	}

	protected boolean hasTimer() {
		return null != timer;
	}

	protected void setTimer(final Timer timer) {
		ObjectHelper.checkNotNull("parameter:timer", timer);
		this.timer = timer;
	}

	protected void clearTimer() {
		timer.cancel();
		this.timer = null;
	}

	protected void createTimer() {
		final Throttler that = this;

		final Timer timer = new Timer() {
			public void run() {
				that.timerExecuted();
			}
		};
		this.setTimer(timer);
		timer.schedule(this.getPeriod());
	}

	/**
	 * Clears the timer and executes the action
	 */
	protected void timerExecuted() {
		this.clearTimer();
		this.doAction();
	}

	public String toString() {
		return super.toString() + ", timer: " + timer;
	}
}
