package rocket.style.client;

import rocket.util.client.ObjectHelper;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * A style that belongs to a rule which in turn belongs to a StyleSheet.
 * 
 * @author Miroslav Pokorny (mP)
 */
class RuleStyle extends Style {

    public int size() {
        return ObjectHelper.getPropertyCount(this.getStyle());
    }

    /**
     * Helper which retrieves the native style object
     * 
     * @return
     */
    protected JavaScriptObject getStyle() {
        return ObjectHelper.getObject(this.getRule().getRule(), "style");
    }

    public String getValue(final String propertyName) {
        return StyleHelper.getRuleStyleProperty(this.getRule().getRule(), propertyName);
    }

    protected void putValue(final String propertyName, final String propertyValue) {
        StyleHelper.setRuleStyleProperty(this.getRule().getRule(), propertyName, propertyValue);
    }

    protected void removeValue(final String propertyName) {
        StyleHelper.removeRuleStyleProperty(this.getRule().getRule(), propertyName);
    }

    protected String getPropertyNames() {
        return this.getPropertyNames(this.getRule().getRule());
    }

    native private String getPropertyNames(final JavaScriptObject rule)/*-{
     var style = rule.style;
     var names = "";
     for( n in style ){
     names = names + n + ",";
     }
     return names;
     }-*/;

    /**
     * A copy of the parent rule that this RuleStyle belongs too.
     */
    private Rule rule;

    protected Rule getRule() {
        ObjectHelper.checkNotNull("field:rule", rule);
        return this.rule;
    }

    protected boolean hasRule() {
        return null != rule;
    }

    protected void setRule(final Rule rule) {
        ObjectHelper.checkNotNull("parameter:rule", rule);
        this.rule = rule;
    }

    protected void clearRule() {
        this.rule = null;
    }
}
