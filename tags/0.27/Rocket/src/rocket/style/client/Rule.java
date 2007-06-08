package rocket.style.client;

import java.util.Map;

import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Each instance of this class represents a single Rule belonging to a StyleSheet
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Rule {

    /**
     * A cached copy of the selector belonging to this rule.
     */
    private String selector = "";

    public String getSelector() {
        String selector = this.selector;
        if (this.hasRuleList()) {
            final JavaScriptObject rule = this.getRule();
            selector = ObjectHelper.getString(rule, StyleConstants.SELECTOR_TEXT_PROPERTY_NAME);
        }
        return selector;
    }

    public void setSelector(final String selector) {
        StringHelper.checkNotEmpty("parameter:selector", selector);

        if (this.hasRuleList()) {
            // remove this rule from its parent and reinsert it at the same spot.
            final RuleList ruleList = this.getRuleList();
            final JavaScriptObject styleSheet = ruleList.getStyleSheet().getStyleSheet();
            final int index = this.getIndex();

            final RuleStyle style = (RuleStyle) this.getStyle();
            final JavaScriptObject nativeStyle = style.getStyle();
            final String styleText = ObjectHelper.getString(nativeStyle, StyleConstants.CSS_STYLE_TEXT_PROPERTY_NAME);

            // remove and reinser the rule...
            StyleHelper.removeRule(styleSheet, index);
            StyleHelper.insertRule(styleSheet, index, selector, styleText);
        }
        this.selector = selector;
    }

    /**
     * A cached copy of the map view of the style object belonging to this Rule
     */
    private Map style;

    public Map getStyle() {
        if (false == this.hasStyle()) {
            this.setStyle(this.createStyle());
        }
        return this.style;
    }

    protected boolean hasStyle() {
        return null != this.style;
    }

    protected void setStyle(final Map style) {
        ObjectHelper.checkNotNull("parameter:style", style);
        this.style = style;
    }

    protected Map createStyle() {
        final RuleStyle style = new RuleStyle();
        style.setRule(this);
        return style;
    }

    /**
     * Helper that gets the native rule object.
     * 
     * @return
     */
    protected JavaScriptObject getRule() {
        return ObjectHelper.getObject(this.getRuleList().getRulesCollection(), this.getIndex());
    }

    /**
     * A copy of the parent ruleList that this rule belongs too.
     */
    private RuleList ruleList;

    protected RuleList getRuleList() {
        ObjectHelper.checkNotNull("field:ruleList", ruleList);
        return this.ruleList;
    }

    protected boolean hasRuleList() {
        return null != ruleList;
    }

    protected void setRuleList(final RuleList ruleList) {
        ObjectHelper.checkNotNull("parameter:ruleList", ruleList);
        this.ruleList = ruleList;
    }

    protected void clearRule() {
        this.ruleList = null;
    }

    /**
     * The index of the native StyleSheet within the StyleSheet collection.
     */
    private int index;

    protected int getIndex() {
        return index;
    }

    protected void setIndex(final int index) {
        this.index = index;
    }

}
