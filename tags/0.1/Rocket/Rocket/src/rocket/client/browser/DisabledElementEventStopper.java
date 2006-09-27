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
package rocket.client.browser;

import rocket.client.util.ObjectHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;

/**
 * This listener listens to all global events and cancels any events that are directed towards an element that is disabled or has a disabled
 * ancestor.
 *
 * Unfortunately it seems that the disabled attribute only works on form elements, and it would be nice to have it working for all elements.
 * This class solves that problem.
 *
 * @author Miroslav Pokorny (mP)
 */
public class DisabledElementEventStopper implements EventPreview {
    public boolean onEventPreview(final Event event) {
        ObjectHelper.checkNotNull("parameter:event", event);

        boolean dontCancel = true;
        Element element = DOM.eventGetTarget(event);

        // keep looping until the root of the dom is reached or a disabled
        // attribute set to true is found
        while (true) {
            if (null == element) {
                break;
            }

            final String disabled = DOM.getAttribute(element, "disabled");
            if ("true".equals(disabled)) {
                dontCancel = false;
                break;
            }

            element = DOM.getParent(element);
        }

        // most of the time will be true(dont cancel) will be false when a
        // disabled ancestor is found.
        return dontCancel;
    }
}