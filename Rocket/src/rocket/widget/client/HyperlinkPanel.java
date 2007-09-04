package rocket.widget.client;

import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.MouseListener;

/**
 * A HyperlinkPanel is a panel which uses a ANCHOR as its root element which
 * will wrap any added children widgets. Each child widget has its element
 * surrounded by a SPAN.
 * 
 * Child widgets will not receive any events. The primary reason to allow the
 * addition of child widgets is to provide a means to construct the content of
 * the hyperlink. This typically means that only Images, Labels and HTML widgets
 * are added.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class HyperlinkPanel extends Panel {

	public HyperlinkPanel() {
		super();

		this.setStyleName(WidgetConstants.HYPERLINK_PANEL_STYLE);
	}

	/**
	 * Factory method which creates the parent Anchor element for this entire
	 * panel
	 * 
	 * @return
	 */
	protected Element createPanelElement() {
		return DOM.createAnchor();
	}

	protected void afterCreatePanelElement() {
		this.createClickListeners();
	}

	protected int getSunkEventsBitMask() {
		return Event.ONCLICK | Event.FOCUSEVENTS | Event.MOUSEEVENTS;
	}

	/**
	 * Returns the element which will house each of the new widget's elements.
	 * 
	 * @return
	 */
	public Element getParentElement() {
		return this.getElement();
	}

	protected Element insert0(final Element element, final int indexBefore) {
		ObjectHelper.checkNotNull("parameter:element", element);

		final Element child = this.createElement();
		DOM.insertChild(this.getParentElement(), child, indexBefore);
		DOM.appendChild(child, element);

		DOM.setEventListener(element, null);
		return child;
	}

	protected Element createElement() {
		return DOM.createSpan();
	}

	protected void remove0(final Element element, final int index) {
		ObjectHelper.checkNotNull("parameter:element", element);

		final Element child = DOM.getChild(this.getParentElement(), index);
		DOM.removeChild(this.getElement(), child);
	}

	// LISTENERS
	// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

	public void addClickListener(final ClickListener clickListener) {
		super.addClickListener(clickListener);
	}

	public void removeClickListener(final ClickListener clickListener) {
		super.removeClickListener(clickListener);
	}

	public void addFocusListener(final FocusListener focusListener) {
		super.addFocusListener(focusListener);
	}

	public void removeFocusListener(final FocusListener focusListener) {
		super.removeFocusListener(focusListener);
	}

	public void addMouseListener(final MouseListener mouseListener) {
		super.addMouseListener(mouseListener);
	}

	public void removeMouseListener(final MouseListener mouseListener) {
		super.removeMouseListener(mouseListener);
	}
}
