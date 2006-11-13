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
package rocket.client.style.impl;

import rocket.client.style.Rule;
import rocket.client.util.ObjectHelper;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * A specialised form of the RuleListSupport class that has a few changes due to Internet Explorer 6.x implementation.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class RuleListInternetExplorer6Impl extends RuleListImpl {

    public RuleListInternetExplorer6Impl() {
        super();
    }

    native protected JavaScriptObject getObject(final JavaScriptObject styleSheet) /*-{
     return styleSheet.rules ? styleSheet.rules : null;
     }-*/;

    protected void addRule(final Rule rule) {
        ObjectHelper.checkNotNull("parameter:rule", rule);

        final JavaScriptObject styleSheet = this.getStyleSheet().getObject();
        final String selector = rule.getSelector();
        final String style = rule.getStyle().getCssText();
        this.addRule0(styleSheet, selector, style);
    }

    protected native void addRule0(final JavaScriptObject styleSheet, final String selectorText, final String styleText)/*-{        
     var cursor = styleSheet.rules.length;
     var safeStyleText = styleText.length == 0 ? ";" : styleText;
     
     styleSheet.addRule( selectorText, safeStyleText, cursor );         
     }-*/;

    protected void insertRule(final int index, final Rule rule) {
        ObjectHelper.checkNotNull("parameter:rule", rule);

        final JavaScriptObject styleSheet = this.getStyleSheet().getObject();
        final String selector = rule.getSelector();
        final String style = rule.getStyle().getCssText();
        this.insertRule0(styleSheet, index, selector, style);
    }

    protected native void insertRule0(final JavaScriptObject styleSheet, final int index, final String selectorText,
            final String styleText)/*-{
     styleSheet.addRule( selectorText, styleText.length == 0 ? ";" : styleText, cursor );         
     }-*/;

    /**
     * Escapes to javascript to delete the requested rule.
     */
    native protected void removeRule0(final JavaScriptObject styleSheet, final int index) /*-{            
     styleSheet.removeRule( cursor );
     }-*/;

    /**
     * There is no need to normalize rules as IE6 does this automatically.
     */
    protected void normalizeRules() {
    }
}
