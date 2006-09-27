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

import rocket.client.util.StringHelper;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.WindowCloseListener;

/**
 * This listener performs two tasks. If the user tries to navigate a way a confirmation is poppped up confirming they wish to leave. This
 * should never happen if the application is started without the browser address bar etc. The other event listener merely says goodbye!
 *
 * @author Miroslav Pokorny (mP)
 */
public class LeavingApplicationListener implements WindowCloseListener {

    public final static LeavingApplicationListener instance = new LeavingApplicationListener();

    public LeavingApplicationListener() {
    }

    public String onWindowClosing() {
        return this.getQuitMessage();
    }

    public void onWindowClosed() {
        Window.alert(this.getGoodbyeMessage());
    }

    private boolean dontSayGoodbye;

    public boolean isDontSayGoodbye() {
        return this.dontSayGoodbye;
    }

    public void setDontSayGoodbye(final boolean dontSayGoodbye) {
        this.dontSayGoodbye = dontSayGoodbye;
    }

    private String goodbyeMessage;

    public String getGoodbyeMessage() {
        StringHelper.checkNotEmpty("field:goodbyeMessage", goodbyeMessage);
        return goodbyeMessage;
    }

    public void setGoodbyeMessage(final String goodbyeMessage) {
        StringHelper.checkNotEmpty("field:goodbyeMessage", goodbyeMessage);
        this.goodbyeMessage = goodbyeMessage;
    }

    private String quitMessage;

    public String getQuitMessage() {
        StringHelper.checkNotEmpty("field:quitMessage", quitMessage);
        return quitMessage;
    }

    public void setQuitMessage(final String quitMessage) {
        StringHelper.checkNotEmpty("field:quitMessage", quitMessage);
        this.quitMessage = quitMessage;
    }

    public String toString() {
        return super.toString() + ", goodbyeMessage[" + goodbyeMessage + "], quitMessage[" + quitMessage + "]";
    }
}