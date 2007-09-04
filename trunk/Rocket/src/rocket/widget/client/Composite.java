package rocket.widget.client;

import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.FocusListenerCollection;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerCollection;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.Widget;

/**
 * A base class for any composite that makes it easier to author new composite
 * widgets.
 * 
 * This class includes event listener collections for all the basic events. On
 * top of sinking events sub classes must also make the corresponding
 * addXXXListener and removeXXXListener methods public.
 * 
 * @author Miroslav Pokorny
 */
abstract public class Composite extends com.google.gwt.user.client.ui.Composite {

	protected Composite() {
		super();

		this.beforeCreateWidget();
		this.initWidget(this.createWidget());
		this.afterCreateWidget();
	}

	/**
	 * This method is called prior to {@link #createWidget()}
	 * 
	 */
	protected void beforeCreateWidget() {
	}

	/**
	 * This method is called when necessary to create a new widget.
	 * 
	 * @return The new wrapped widget
	 */
	abstract protected Widget createWidget();

	/**
	 * This method is called once the element for this widget is set. Sinking
	 * events and create listener collections should be done in here.
	 */
	protected void afterCreateWidget() {
	}

	protected void onAttach() {
		super.onAttach();

		this.setSinkEvents();
	}

	/**
	 * Sets event sinking and listener.
	 */
	protected void setSinkEvents() {
		final Element original = this.getElement();
		final Element target = this.getSunkEventsTarget();

		if (false == DOM.compare(original, target)) {
			DOM.sinkEvents(original, 0);
		}
		final int mask = this.getSunkEventsBitMask();
		if (0 != mask) {
			DOM.sinkEvents(target, mask);
			DOM.setEventListener(target, this);
		}
	}

	/**
	 * Sub classes must return a bit mask that indicates which events are being
	 * sunk.
	 * 
	 * @return
	 */
	abstract protected int getSunkEventsBitMask();

	protected Element getSunkEventsTarget() {
		return this.getElement();
	}

	/**
	 * The complement of onAttach. This method removes the event listener for
	 * the sunk event target.
	 */
	protected void onDetach() {
		if (0 != this.getSunkEventsBitMask()) {
			DOM.setEventListener(this.getSunkEventsTarget(), this);
		}
	}

	/**
	 * Dispatches the and fires the appropriate listeners based on the event
	 * type
	 */
	public void onBrowserEvent(final Event event) {
		while (true) {
			final int eventType = DOM.eventGetType(event);
			if (eventType == Event.ONCHANGE) {
				if (this.hasChangeListeners()) {
					this.getChangeListeners().fireChange(this);
				}
				break;
			}
			if (eventType == Event.ONCLICK) {
				if (this.hasClickListeners()) {
					this.getClickListeners().fireClick(this);
				}
				break;
			}
			if ((eventType & Event.FOCUSEVENTS) != 0) {
				if (this.hasFocusListeners()) {
					this.getFocusListeners().fireFocusEvent(this, event);
				}
				break;
			}
			if ((eventType & Event.KEYEVENTS) != 0) {
				if (this.hasKeyboardListeners()) {
					this.getKeyboardListeners().fireKeyboardEvent(this, event);
				}
				break;
			}
			if ((eventType & Event.MOUSEEVENTS) != 0) {
				if (this.hasMouseListeners()) {
					this.getMouseListeners().fireMouseEvent(this, event);
				}
			}
			break;
		}
	}

	/**
	 * A collection of change listeners interested in change events for this
	 * widget.
	 */
	private ChangeListenerCollection changeListeners;

	protected ChangeListenerCollection getChangeListeners() {
		ObjectHelper.checkNotNull("field:changeListeners", changeListeners);
		return this.changeListeners;
	}

	protected boolean hasChangeListeners() {
		return null != this.changeListeners;
	}

	protected void setChangeListeners(final ChangeListenerCollection changeListeners) {
		ObjectHelper.checkNotNull("parameter:changeListeners", changeListeners);
		this.changeListeners = changeListeners;
	}

	protected ChangeListenerCollection createChangeListeners() {
		return new ChangeListenerCollection();
	}

	protected void addChangeListener(final ChangeListener changeListener) {
		this.getChangeListeners().add(changeListener);
	}

	protected void removeChangeListener(final ChangeListener changeListener) {
		this.getChangeListeners().remove(changeListener);
	}

	/**
	 * A collection of click listeners interested in click events for this
	 * widget.
	 */
	private ClickListenerCollection clickListeners;

	protected ClickListenerCollection getClickListeners() {
		ObjectHelper.checkNotNull("field:clickListeners", clickListeners);
		return this.clickListeners;
	}

	protected boolean hasClickListeners() {
		return null != this.clickListeners;
	}

	protected void setClickListeners(final ClickListenerCollection clickListeners) {
		ObjectHelper.checkNotNull("parameter:clickListeners", clickListeners);
		this.clickListeners = clickListeners;
	}

	protected ClickListenerCollection createClickListeners() {
		return new ClickListenerCollection();
	}

	protected void addClickListener(final ClickListener clickListener) {
		this.getClickListeners().add(clickListener);
	}

	protected void removeClickListener(final ClickListener clickListener) {
		this.getClickListeners().remove(clickListener);
	}

	/**
	 * A collection of focus listeners interested in focus events for this
	 * widget.
	 */
	private FocusListenerCollection focusListeners;

	protected FocusListenerCollection getFocusListeners() {
		ObjectHelper.checkNotNull("field:focusListeners", focusListeners);
		return this.focusListeners;
	}

	protected boolean hasFocusListeners() {
		return null != this.focusListeners;
	}

	protected void setFocusListeners(final FocusListenerCollection focusListeners) {
		ObjectHelper.checkNotNull("parameter:focusListeners", focusListeners);
		this.focusListeners = focusListeners;
	}

	protected FocusListenerCollection createFocusListeners() {
		return new FocusListenerCollection();
	}

	protected void addFocusListener(final FocusListener focusListener) {
		this.getFocusListeners().add(focusListener);
	}

	protected void removeFocusListener(final FocusListener focusListener) {
		this.getFocusListeners().remove(focusListener);
	}

	/**
	 * A collection of key listeners interested in key events for this widget.
	 */
	private KeyboardListenerCollection keyboardListeners;

	protected KeyboardListenerCollection getKeyboardListeners() {
		ObjectHelper.checkNotNull("field:keyboardListeners", keyboardListeners);
		return this.keyboardListeners;
	}

	protected boolean hasKeyboardListeners() {
		return null != this.keyboardListeners;
	}

	protected void setKeyboardListeners(final KeyboardListenerCollection keyboardListeners) {
		ObjectHelper.checkNotNull("parameter:keyboardListeners", keyboardListeners);
		this.keyboardListeners = keyboardListeners;
	}

	protected KeyboardListenerCollection createKeyboardListeners() {
		return new KeyboardListenerCollection();
	}

	protected void addKeyboardListener(final KeyboardListener keyboardListener) {
		this.getKeyboardListeners().add(keyboardListener);
	}

	protected void removeKeyboardListener(final KeyboardListener keyboardListener) {
		this.getKeyboardListeners().remove(keyboardListener);
	}

	/**
	 * A collection of mouse listeners interested in mouse events for this
	 * widget.
	 */
	private MouseListenerCollection mouseListeners;

	protected MouseListenerCollection getMouseListeners() {
		ObjectHelper.checkNotNull("field:mouseListeners", mouseListeners);
		return this.mouseListeners;
	}

	protected boolean hasMouseListeners() {
		return null != this.mouseListeners;
	}

	protected void setMouseListeners(final MouseListenerCollection mouseListeners) {
		ObjectHelper.checkNotNull("parameter:mouseListeners", mouseListeners);
		this.mouseListeners = mouseListeners;
	}

	protected MouseListenerCollection createMouseListeners() {
		return new MouseListenerCollection();
	}

	protected void addMouseListener(final MouseListener mouseListener) {
		this.getMouseListeners().add(mouseListener);
	}

	protected void removeMouseListener(final MouseListener mouseListener) {
		this.getMouseListeners().remove(mouseListener);
	}
}
