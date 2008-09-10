package rocket.remoting.client;

import rocket.util.client.Checker;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

/**
 * A simple container that shows and hides its widget whenever at least one rpc
 * is underway and hides the widget when they all complete.
 * 
 * The widget may be an Image with an animated spinner gif
 * {@linkplain http://www.ajaxload.info/} to perhaps add to a tab like Firefox
 * or add a "Loading" text inthe top right hand corner of the browser client
 * area like gmail.
 * 
 * In order to avoid a leak the callback returned by {@link #prepare} must be
 * passed to a rpc service method.
 * 
 * @author Miroslav Pokorny
 */
public class OustandingRpcTracker {
	/**
	 * The widget that is show when rpc requests are outstanding or hidden when
	 * none are active.
	 */
	private Widget widget;

	public Widget getWidget() {
		Checker.notNull("field:widget", widget);
		return this.widget;
	}

	public void setWidget(final Widget widget) {
		Checker.notNull("parameter:widget", widget);
		this.widget = widget;

		this.setOutstandingCount(this.getOutstandingCount());
	}

	/**
	 * This counter keeps track of the number of outstanding rpc requests.
	 */
	private int outstandingCount;

	protected int getOutstandingCount() {
		Checker.greaterThanOrEqual("field:outstandingCount", 0, outstandingCount);
		return this.outstandingCount;
	}

	protected void setOutstandingCount(final int outstandingCount) {
		Checker.greaterThanOrEqual("parameter:outstandingCount", 0, outstandingCount);
		this.outstandingCount = outstandingCount;

		this.getWidget().setVisible(outstandingCount > 0);
	}

	/**
	 * This method must be invoked to decorate the intended callback with the
	 * result given to the invoked rpc proxy method.
	 * 
	 * @param callback
	 * @return
	 */
	public AsyncCallback prepare(final AsyncCallback callback) {
		this.setOutstandingCount(this.getOutstandingCount() + 1);

		return new AsyncCallback() {
			public void onSuccess(final Object result) {
				OustandingRpcTracker.this.handleOnSuccess(result);
				callback.onSuccess(result);
			}

			public void onFailure(final Throwable cause) {
				OustandingRpcTracker.this.handleOnFailure(cause);
				callback.onFailure(cause);
			}
		};
	}

	protected void handleOnSuccess(final Object result) {
		this.setOutstandingCount(this.getOutstandingCount() - 1);
	}

	protected void handleOnFailure(final Throwable cause) {
		this.setOutstandingCount(this.getOutstandingCount() - 1);
	}
}
