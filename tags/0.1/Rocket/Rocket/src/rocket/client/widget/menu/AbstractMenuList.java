/*
 * Copyright 2006 NSW Police Government Australia
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rocket.client.widget.menu;

import java.util.Iterator;

import rocket.client.util.ObjectHelper;
import rocket.client.widget.WidgetHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractMenuList extends Composite implements MenuList {

    protected AbstractMenuList() {
        this.createEventPreviewer();
    }

    public void open() {
        this.setVisible(true);
        DOM.addEventPreview(this.getEventPreviewer());
    }

    /**
     * An EventPreview is used to detect if a click occurs outside any menu/menuList. If a click does occur all menu/menuLists are closed.
     */
    private EventPreview eventPreviewer;

    protected EventPreview getEventPreviewer() {
        ObjectHelper.checkNotNull("field:eventPreviewer", this.eventPreviewer);
        return this.eventPreviewer;
    }

    protected void setEventPreviewer(final EventPreview eventPreviewer) {
        ObjectHelper.checkNotNull("parameter:eventPreviewer", eventPreviewer);
        this.eventPreviewer = eventPreviewer;
    }

    protected void createEventPreviewer() {
        final AbstractMenuList that = this;
        final EventPreview eventPreviewer = new EventPreview() {
            public boolean onEventPreview(final Event event) {
                boolean dontCancel = true;
                if (DOM.eventGetType(event) == Event.ONCLICK) {
                    boolean outside = true;
                    final Element targetElement = DOM.eventGetTarget(event);
                    MenuList list = that;

                    while (true) {
                        if (DOM.isOrHasChild(list.getElement(), targetElement)) {
                            outside = false;
                            break;
                        }
                        if (false == list.hasParentMenuList()) {
                            break;
                        }
                        list = list.getParentMenuList();
                    } // while

                    if (outside) {
                        final AbstractMenu menu = that.getMenu();
                        menu.close();
                        menu.getMenuListeners().fireMenuCancelled( menu );
                        dontCancel = false;
                    } // if outside
                } // if click
                return dontCancel;
            }
        };
        this.setEventPreviewer(eventPreviewer);
    }

    // COMPOSITE + EVENT LISTENER::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    protected void onAttach() {
        super.onAttach();
        this.sinkEvents(Event.ONCLICK | Event.ONMOUSEOVER | Event.ONMOUSEOUT );
        DOM.setEventListener(this.getElement(), this);
    }

    protected void onDetatch() {
        super.onDetach();

        this.close();
    }

    public void onBrowserEvent(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        while (true) {
            // if a widget wasnt found or is not a MenuWidget ignore.
            final Widget widget = WidgetHelper.findWidget(DOM.eventGetTarget(event), this.iterator());
            if (null == widget || false == (widget instanceof MenuWidget)) {
                break;
            }

            // if the menuWidget is disabled ignore the event.)
            final MenuWidget menuWidget = (MenuWidget) widget;
            if (menuWidget.isDisabled()) {
                break;
            }

            final int type = DOM.eventGetType(event);
            if (type == Event.ONCLICK) {
                this.attemptOpen(widget);
                break;
            }
            if (type == Event.ONMOUSEOVER) {
                this.closeAll();
                menuWidget.select();

                // if autoOpen is enabled and widget is a SubMenuItem open!.
                if (widget instanceof SubMenuItem && this.getMenu().isAutoOpen()) {
                    this.attemptOpen(widget);
                }
                break;
            }
            if (type == Event.ONMOUSEOUT) {
                if (menuWidget instanceof MenuItem) {
                    menuWidget.unselect();
                }
                break;
            }
            break;
        }

    }

    protected void attemptOpen(final Widget widget) {
        while (true) {
            // close all other widgets...
            if (widget instanceof SubMenuItem) {
                this.closeAll();
            }
            // ask listeners if item can be opened.
            final AbstractMenu menu = this.getMenu();
            final MenuListenerCollection listeners = menu.getMenuListeners();
            if (false == listeners.fireBeforeMenuOpened(this)) {
                break;
            }

            // now open the widget.
            final MenuWidget menuWidget = (MenuWidget) widget;
            menuWidget.open();

            // tell all listeners that widget was opened.
            listeners.fireMenuOpened(widget);

            // if its a menuItem close everything.
            if (widget instanceof MenuItem) {
                menu.close();
            }
            break;
        }
    }

    // MENU LIST :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    public void add(final Widget widget) {
        this.insert(widget, this.getCount());
    }
    
    protected void afterInsert(final Widget widget) {
        ObjectHelper.checkNotNull("parameter:widget", widget);

        final MenuWidget menuWidget = (MenuWidget) widget;
        menuWidget.setParentMenuList(this);
    }

    protected void afterRemove(final Widget widget) {
        this.closeIfMenuWidget(widget);
    }

    public void close() {
        this.closeAll();

        DOM.removeEventPreview(this.getEventPreviewer());
    }

    protected void closeAll() {
        final Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            this.closeIfMenuWidget((Widget) iterator.next());
        }
    }

    protected void closeIfMenuWidget(final Widget widget) {
        if (widget instanceof MenuWidget) {
            final MenuWidget menuWidget = (MenuWidget) widget;
            menuWidget.close();
        }
    }    
    
    /**
     * If this is the topmost menuList this property will be set otherwise children will need to check their parent until the
     * top is reached.
     */
    private AbstractMenu menu;

    public AbstractMenu getMenu() {
    	AbstractMenu menu = this.menu;
    	
    	// if this widget doesnt have a menu property set check its parent...
    	if( false == this.hasMenu() ){
    		menu = this.getParentMenuList().getMenu();
    	}
    	
        ObjectHelper.checkNotNull("menu", menu);
        return menu;
    }
    
    protected boolean hasMenu(){
    	return null != this.menu;
    }

    public void setMenu(final AbstractMenu menu) {
        ObjectHelper.checkNotNull("parameter:menu", menu);
        this.menu = menu;
    }

    /**
     * All menuLists will have a parent except if they have been added to a menu.
     */
    private MenuList parentMenuList;

    public MenuList getParentMenuList() {
        ObjectHelper.checkNotNull("field:parentMenuList", parentMenuList);
        return this.parentMenuList;
    }

    public boolean hasParentMenuList() {
        return null != this.parentMenuList;
    }

    public void setParentMenuList(final MenuList parentMenuList) {
        ObjectHelper.checkNotNull("parameter:parentMenuList", parentMenuList);
        this.parentMenuList = parentMenuList;
    }
}
