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
package rocket.widget.test.collapsablepanel.client;

import rocket.widget.client.CollapsablePanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class CollapsablePanelTest implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            public void onUncaughtException(final Throwable caught) {
                caught.printStackTrace();
                Window.alert("Caught:" + caught + "\nmessage[" + caught.getMessage() + "]");
            }
        });

        final RootPanel rootPanel = RootPanel.get();

        final Button addRedTextStyle = new Button("add redText style");
        rootPanel.add(addRedTextStyle);
        final Button setRedTextStyle = new Button("set redText style");
        rootPanel.add(setRedTextStyle);
        final Button removeRedTextStyle = new Button("remove redText style");
        rootPanel.add(removeRedTextStyle);
        final Button removeCardStyle = new Button("remove card style");
        rootPanel.add(removeCardStyle);
        rootPanel.add(new HTML("<br/>"));

        final CollapsablePanel card = new CollapsablePanel();
        card.setTitle("***Title***");

        card.add(card.createMinimize());
        card.add(card.createMaximize());
        card.add(card.createClose());

        final FlowPanel content = new FlowPanel();
        content.add(new HTML(
                "<span style='background-color: #ffeedd'> The quick\nbrown fox jumped over\nthe lazy dog!</span>"));

        final Button button = new Button("click me!");
        button.addClickListener(new ClickListener() {
            public void onClick(final Widget ignore) {
                Window.alert("clicked!");
            }
        });
        content.add(button);
        content.add(new HTML(
                "<span style='background-color: #ddeeff'> The quick\nbrown fox jumped over\nthe lazy dog!</span>"));

        card.setContent(content);
        rootPanel.add(card);

        final Button showContent = new Button("ShowContent");
        showContent.addClickListener(new ClickListener() {
            public void onClick(final Widget ignore) {
                card.showContent();
            };
        });

        final Button hideContent = new Button("HideContent");
        hideContent.addClickListener(new ClickListener() {
            public void onClick(final Widget ignore) {
                card.hideContent();
            };
        });

        addRedTextStyle.addClickListener(new ClickListener() {
            public void onClick(final Widget ignore) {
                card.addStyleName("redText");
            }
        });
        setRedTextStyle.addClickListener(new ClickListener() {
            public void onClick(final Widget ignore) {
                card.setStyleName("redText");
            }
        });
        removeRedTextStyle.addClickListener(new ClickListener() {
            public void onClick(final Widget ignore) {
                card.removeStyleName("redText");
            }
        });
        removeCardStyle.addClickListener(new ClickListener() {
            public void onClick(final Widget ignore) {
                card.removeStyleName("card");
            }
        });
    }
}
