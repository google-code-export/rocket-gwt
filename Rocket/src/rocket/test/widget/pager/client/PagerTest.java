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
package rocket.test.widget.pager.client;

import rocket.client.widget.Pager;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PagerTest implements EntryPoint {

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

        this.createPager(1, 20, 1, 7);
        this.createPager(1, 20, 10, 7);
        this.createPager(1, 20, 19, 7);
    }

    protected void createPager(final int first, final int last, final int currentPage, final int inBetweenCount) {
        final RootPanel rootPanel = RootPanel.get();

        final Label label = new Label("" + currentPage);

        final Pager pager = new Pager();
        pager.setFirstPage(first);
        pager.setLastPage(last);
        pager.setCurrentPage(currentPage);
        pager.setPagesInBetweenCount(inBetweenCount);

        pager.addChangeListener(new ChangeListener() {
            public void onChange(final Widget source) {
                label.setText("Current Page: " + pager.getValue());
            }
        });

        rootPanel.add(label);
        rootPanel.add(pager);
        pager.redraw();
    }

}
