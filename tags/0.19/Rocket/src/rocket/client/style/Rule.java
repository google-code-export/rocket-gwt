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
package rocket.client.style;

import rocket.client.dom.DomObjectListElement;
import rocket.client.style.impl.RuleListImpl;
import rocket.client.util.ObjectHelper;
import rocket.client.util.StringHelper;

/**
 * Each instance of the Rule class represents a handle to a rule belonging to a styleSheet.
 * 
 * Methods are available to set/get the selector and style.
 * @author Miroslav Pokorny (mP)
 *
 */
public class Rule extends DomObjectListElement {
    public Rule() {
        super();
        
        this.setStyle( this.createStyle() );
    }

    // SELECTOR ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * This property caches the selectorText value of this rule until it is added to a RuleList which is turn belongs too a StyleSheet
     */
    private String selector;

    public String getSelector() {
        String selector = this.selector;

        if (this.hasParent() && this.hasIndex() ) {
            selector = ObjectHelper.getString( this.getObject(), StyleConstants.SELECTOR_TEXT_PROPERTY_NAME);

            selector = StringHelper.nullToEmpty( selector );
        }

        StyleHelper.checkSelector("field:selector", selector);
        return selector;
    }

    public void setSelector(final String selector) {
        StyleHelper.checkSelector("parameter:selector", selector);

        this.selector = selector;

        // if the actual Rule object is present recreate Rule...
        if (this.hasParent() & this.hasIndex() ) { 
            final RuleListImpl rules = (RuleListImpl) this.getParent();
            
            // get this rules index...
            final int index = this.getIndex(); 
            
            final Style style = this.getStyle();
            final String styleText = style.getCssText(); 
            
            // remove this rule from its parent...
            rules.remove( this );
            
            // insert it back in its original position...
            this.setIndex( index );
            style.setCssText( styleText );
            rules.add( index, this );
        }
    }

    // STYLE ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * The style accompanying this Rule
     */
    private Style style;

    public Style getStyle() {
        ObjectHelper.checkNotNull("field:style", style);
        return this.style;
    }

    protected boolean hasStyle() {
        return null != this.style;
    }

    protected void setStyle(final Style style) {
        ObjectHelper.checkNotNull("parameter:style", style);
        this.style = style;
    }

    /**
     * Factory method which creates the style accompanying this Rule.
     */
    protected Style createStyle() {
        final Style style = new Style();
        style.setRule(this);
        return style;
    }

    /**
     * Reads the style text property of this rule.
     * 
     * @return Returns null if this Rule does not belong to a RuleList.
     */
    public String getStyleText() {
        return this.hasParent() ? 
                ObjectHelper.getString( this.getObject(), StyleConstants.STYLE_TEXT_PROPERTY_NAME) : "";
    }

    public void destroy() {
        if (this.hasStyle()) {
            this.getStyle().destroy();
        }
     
        super.destroy();
    }
}
