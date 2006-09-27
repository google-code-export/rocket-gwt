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
package rocket.test.widget.autocompletetextbox.client;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import rocket.client.util.StringHelper;
import rocket.client.widget.AutoCompleteTextBox;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.KeyboardListenerAdapter;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class AutoCompleteTextBoxTest implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        try {
            final RootPanel rootPanel = RootPanel.get();

            final List matchCandidates = Arrays.asList(new String[] { "Red square", "Red star", "Red apple",
                    "Red baron", "Red", "New York", "New York state", "New Zealand", "New Jersey", "New Mexico", "New South Wales", "New South Wales government",
                    "New England", "Zebra", "Zebra crossing", "ZebraFlexTable" });

            final StringBuffer buf = new StringBuffer();
            buf.append("<br/>");
            boolean addSeparator = false;
            final Iterator iterator = matchCandidates.iterator();
            while (iterator.hasNext()) {
                if (addSeparator) {
                    buf.append(", ");
                }
                addSeparator = true;
                buf.append(iterator.next());
            }
            buf.append("<hr/>");

            rootPanel.add(new HTML(buf.toString()));

            final AutoCompleteTextBox autoCompleteTextBox = new AutoCompleteTextBox();
            autoCompleteTextBox.setWidth( "200px");
            rootPanel.add(autoCompleteTextBox);
            autoCompleteTextBox.setFocus(true);
            autoCompleteTextBox.addKeyboardListener(new KeyboardListenerAdapter() {
                public void onKeyUp(final Widget sender, final char keyCode, final int modifiers) {
                    final String text = autoCompleteTextBox.getText();

                    autoCompleteTextBox.clear();
                    if (text.length() > 0) {
                        final Iterator iterator = matchCandidates.iterator();
                        while (iterator.hasNext()) {
                            final String test = (String) iterator.next();
                            if (StringHelper.startsWithIgnoringCase(test, text)) {
                                autoCompleteTextBox.add(test);
                            }
                        }
                    }
                }
            });
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
