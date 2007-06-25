package rocket.widget.client;

import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.ClickListenerCollection;

/**
 * A HyperlinkPanel is a panel which uses a ANCHOR as its root element which will wrap any added children widgets. Each child widget has its
 * element surrounded by a SPAN.
 * 
 * Child widgets will not receive any events. The primary reason to allow the addition of child widgets is to provide a means to construct
 * the content of the hyperlink. This typically means that only Images, Labels and HTML widgets are added.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class HyperlinkPanel extends AbstractPanel {

    public HyperlinkPanel() {
        super();

        this.setElement(this.createPanelElement());
        this.setStyleName(WidgetConstants.HYPERLINK_PANEL_STYLE);
        this.createClickListeners();
    }

    public void onAttach() {
        super.onAttach();

        this.unsinkEvents(-1);
        this.sinkEvents(Event.ONCLICK);
        DOM.setEventListener(this.getParentElement(), this);
    }

    public void onBrowserEvent(final Event event) {
        final int eventType = DOM.eventGetType(event);
        if (eventType == Event.ONCLICK) {
            this.getClickListeners().fireClick(this);
        }
    }

    /**
     * Factory method which creates the parent Anchor element for this entire panel
     * 
     * @return
     */
    protected Element createPanelElement() {
        return DOM.createAnchor();
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

    public void addClickListener(final ClickListener listener) {
        this.getClickListeners().add(listener);
    }

    public void removeClickListener(final ClickListener listener) {
        this.getClickListeners().remove(listener);
    }

    /**
     * A collection of click listeners that will be notified when this hyperlink is clicked.
     */
    private ClickListenerCollection clickListeners;

    public ClickListenerCollection getClickListeners() {
        ObjectHelper.checkNotNull("field:clickListeners", clickListeners);
        return clickListeners;
    }

    public void setClickListeners(final ClickListenerCollection clickListeners) {
        ObjectHelper.checkNotNull("parameter:clickListeners", clickListeners);
        this.clickListeners = clickListeners;
    }

    protected void createClickListeners() {
        this.setClickListeners(new ClickListenerCollection());
    }

}
