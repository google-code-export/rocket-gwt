package rocket.widget.client;

import rocket.dom.client.Dom;
import rocket.event.client.EventBitMaskConstants;
import rocket.event.client.FocusEventListener;
import rocket.event.client.MouseEventListener;
import rocket.util.client.Checker;
import rocket.util.client.JavaScript;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * A HyperlinkPanel is a panel which uses a ANCHOR as its root element which
 * will wrap any added children widgets. Each child widget has its element
 * surrounded by a SPAN.
 * 
 * Child widgets (widgets that are added) will not receive any events. The
 * primary reason to allow the addition of child widgets is to provide a means
 * to construct the content of the hyperlink. This typically means that only
 * Images, Labels and HTML widgets are added.
 * 
 * This does however also mean that widgets that are added and then removed from
 * this panel are forever
 * 
 * @author Miroslav Pokorny (mP)
 */
public class HyperlinkPanel extends SimplePanel {

	public HyperlinkPanel() {
		super();
	}

	public HyperlinkPanel(final Element anchorElement) {
		super(anchorElement);
	}

	@Override
	protected void checkElement(final Element element) {
		Dom.checkTagName("parameter:element", element, WidgetConstants.HYPERLINK_TAG);
	}

	/**
	 * Factory method which creates the parent Anchor element for this entire
	 * panel
	 * 
	 * @return The new anchor
	 */
	@Override
	protected Element createPanelElement() {
		return DOM.createAnchor();
	}

	@Override
	protected String getInitialStyleName() {
		return WidgetConstants.HYPERLINK_PANEL_STYLE;
	}

	@Override
	protected int getSunkEventsBitMask() {
		return EventBitMaskConstants.MOUSE_CLICK | EventBitMaskConstants.FOCUS_EVENTS | EventBitMaskConstants.MOUSE_EVENTS;
	}

	/**
	 * Returns the element which will house each of the new widget's elements.
	 * 
	 * @return The parent
	 */
	public Element getParentElement() {
		return this.getElement();
	}

	@Override
	protected void insert0(final Element element, final int indexBefore) {
		Checker.notNull("parameter:element", element);

		final Element child = this.createElement();
		DOM.insertChild(this.getParentElement(), child, indexBefore);
		DOM.appendChild(child, element);

		// save the event sunk bit mask so this value can be restored during a
		// remove
		final int sunk = DOM.getEventsSunk(element);
		JavaScript.setInteger(element, WidgetConstants.HYPERLINK_PANEL_PREVIOUS_SUNK_EVENTS_BIT_MASK, sunk);
		DOM.sinkEvents(element, 0);
	}

	protected Element createElement() {
		return DOM.createSpan();
	}

	@Override
	protected void remove0(final Element element, final int index) {
		Checker.notNull("parameter:element", element);

		final Element child = DOM.getChild(this.getParentElement(), index);
		Dom.removeFromParent(child);

		// restore the bits that were blanked out during an insert()
		final int sunk = JavaScript.getInteger(element, WidgetConstants.HYPERLINK_PANEL_PREVIOUS_SUNK_EVENTS_BIT_MASK);
		DOM.sinkEvents(child, sunk);
	}

	// LISTENERS
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	public void addFocusEventListener(final FocusEventListener focusEventListener) {
		this.getEventListenerDispatcher().addFocusEventListener(focusEventListener);
	}

	public void removeFocusEventListener(final FocusEventListener focusEventListener) {
		this.getEventListenerDispatcher().removeFocusEventListener(focusEventListener);
	}

	public void addMouseEventListener(final MouseEventListener mouseEventListener) {
		this.getEventListenerDispatcher().addMouseEventListener(mouseEventListener);
	}

	public void removeMouseEventListener(final MouseEventListener mouseEventListener) {
		this.getEventListenerDispatcher().removeMouseEventListener(mouseEventListener);
	}
}
