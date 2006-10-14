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
package rocket.client.widget;

import rocket.client.browser.BrowserHelper;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

/**
 * This dialogBox exists primarily to fix several bugs within the GWT DialogBox class.
 * 
 * GWT forgot to set the style for DialogBoxes within the IE6 DialogBox class. ANother bug involves the browser jumping to include the DIV
 * that is the DialogBox. After the DIV is absolutely positioned the browser remains scrolled down showing an ugly black box where the div
 * used to be. The show() method uses a timer to wait a second and then scrolls the window back to its original position. This should be
 * long enough to allow the browser to settle after its jump.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class DialogBox extends com.google.gwt.user.client.ui.DialogBox {
    /**
     * GWT forgot to set the style for IE6 DialogBoxes.
     */
    public DialogBox() {
        if (BrowserHelper.isInternetExplorer6()) {
            this.addStyleName("gwt-DialogBox");
        }
    }

    public void show() {
        final int x = BrowserHelper.getScrollX();
        final int y = BrowserHelper.getScrollY();

        super.show();

        DeferredCommand.add(new Command() {
            public void execute() {
                BrowserHelper.scrollTo(x, y);
            }
        });
    }
}
