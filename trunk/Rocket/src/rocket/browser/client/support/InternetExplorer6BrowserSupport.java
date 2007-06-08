/*
 * Copyright Miroslav Pokorny
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
package rocket.browser.client.support;

import rocket.browser.client.BrowserConstants;

import com.google.gwt.core.client.GWT;

/**
 * Provides an InternetExplorer 6.x specific BrowserHelperSupport.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class InternetExplorer6BrowserSupport extends BrowserSupport {

    native public int getScrollX()/*-{
     return $doc.documentElement ? $doc.documentElement.scrollLeft: $doc.body.scrollLeft;
     }-*/;

    native public int getScrollY()/*-{
     return $doc.documentElement ? $doc.documentElement.scrollTop: $doc.body.scrollTop;
     }-*/;

    native public int getClientWidth()/*-{
     return $doc.documentElement ? $doc.documentElement.clientWidth: $doc.body.clientWidth;     
     }-*/;

    native public int getClientHeight()/*-{
     return $doc.documentElement ? $doc.documentElement.clientHeight: $doc.body.clientHeight;     
     }-*/;
}
