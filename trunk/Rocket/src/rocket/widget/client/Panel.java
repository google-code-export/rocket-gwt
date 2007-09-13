package rocket.widget.client;

import java.util.Iterator;

import rocket.collection.client.CollectionHelper;
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
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerCollection;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;

/**
 * Convenient base class for any Panel implementation.
 * 
 * @author Miroslav Pokorny (mP)
 */
abstract public class Panel extends com.google.gwt.user.client.ui.Panel implements HasWidgets {

	protected Panel() {
		this.setWidgetCollection(createWidgetCollection());

		this.beforeCreatePanelElement();
		this.setElement(this.createPanelElement());
		this.afterCreatePanelElement();
	}

	protected void beforeCreatePanelElement() {
	}

	/**
	 * Sub classes must create the root panel element.
	 * 
	 * @return The new element
	 */
	abstract protected Element createPanelElement();

	/**
	 * This method provides an opportunity for sub classes to register listener collections etctener collections etc
	 */
	protected void afterCreatePanelElement() {
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
	 * Returns the number of widgets within this panel.
	 * 
	 * @return
	 */
	public int getWidgetCount() {
		return this.getWidgetCollection().size();
	}

	public int indexOf(final Widget widget) {
		return this.getWidgetCollection().indexOf(widget);
	}

	/**
	 * Retrieves a widget within this panel.
	 * 
	 * @param index
	 * @return
	 */
	public Widget get(final int index) {
		return this.getWidgetCollection().get(index);
	}

	/**
	 * Adds a new widget to the end of this panel.
	 */
	public void add(final Widget widget) {
		this.insert(widget, this.getWidgetCount());
	}

	/**
	 * Sub-classes need to insert the given widget into the
	 * 
	 * @param widget
	 * @param indexBefore
	 */
	public void insert(final Widget widget, int indexBefore) {
		this.insert0(widget, indexBefore);
		this.adopt(widget);
		this.getWidgetCollection().insert(widget, indexBefore);
	}

	protected Element insert0(final Widget widget, int indexBefore) {
		ObjectHelper.checkNotNull("parameter:widget", widget);
		return this.insert0(widget.getElement(), indexBefore);
	}

	/**
	 * Sub-classes need to create/find the element which will become the parent
	 * of the Widget's element
	 * 
	 * @param element
	 * @param indexBefore
	 * @return Element the parent element of the new widget.
	 */
	protected abstract Element insert0(Element element, int indexBefore);

	/**
	 * Attempts to remove an existing widget from this panel if it is a child.
	 * 
	 * @return true if the widget was a child and was successfully removed,
	 *         otehrwise returns false.
	 */
	public boolean remove(final Widget widget) {
		ObjectHelper.checkNotNull("parameter:widget", widget);

		boolean removed = false;
		final WidgetCollection widgets = this.getWidgetCollection();
		final int index = widgets.indexOf(widget);
		if (-1 != index) {
			this.remove(index);
			removed = true;
		}

		return removed;
	}

	/**
	 * Removes the widget at the given slot.
	 * 
	 * @param index
	 */
	public void remove(final int index) {
		final WidgetCollection widgets = this.getWidgetCollection();

		final Widget widget = widgets.get(index);
		this.remove0(widget.getElement(), index);// cleanup opportunity
		this.orphan(widget);
		widgets.remove(index);
	}

	protected void remove0(final Widget widget, final int index) {
		ObjectHelper.checkNotNull("parameter:widget", widget);
		this.remove0(widget.getElement(), index);
	}

	/**
	 * Cleanup opportunity for sub-classes to remove other outstanding elements
	 * from the dom.
	 * 
	 * @param element
	 * @param index
	 */
	protected abstract void remove0(Element element, int index);

	/**
	 * Clears or removes all widgets from this panel.
	 */
	public void clear() {
		CollectionHelper.removeAll(this.iterator());
	}

	public Iterator iterator() {
		return this.getWidgetCollection().iterator();
	}

	/**
	 * This collection includes all the widgest that belong to each of the
	 * individual cells.
	 */
	private WidgetCollection widgetCollection;

	protected WidgetCollection getWidgetCollection() {
		ObjectHelper.checkNotNull("field:widgetCollection", widgetCollection);
		return widgetCollection;
	}

	protected void setWidgetCollection(final WidgetCollection widgetCollection) {
		ObjectHelper.checkNotNull("parameter:widgetCollection", widgetCollection);
		this.widgetCollection = widgetCollection;
	}

	protected WidgetCollection createWidgetCollection() {
		return new WidgetCollection(this);
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
