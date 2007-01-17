package rocket.style.client;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import rocket.util.client.ObjectHelper;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Represents a list view of all the rules belonging to a single stylesheet.
 * @author Miroslav Pokorny (mP)
 */
class RuleList extends AbstractList {

    public RuleList(){
        super();

        this.setWrappers( this.createWrappers() );
    }

    /**
     * A copy of the parent stylesheet that this list belongs too.
     */
    private StyleSheet styleSheet;

    protected StyleSheet getStyleSheet(){
        ObjectHelper.checkNotNull( "field:styleSheet", styleSheet );
        return this.styleSheet;
    }
    protected void setStyleSheet(final StyleSheet styleSheet ){
        ObjectHelper.checkNotNull( "parameter:styleSheet", styleSheet );
        this.styleSheet = styleSheet;
    }

    /**
     * Helper which retrieves the native rules collection that this instance is presenting as a List
     * @return
     */
    protected JavaScriptObject getRulesCollection(){
        final JavaScriptObject styleSheet = this.getStyleSheet().getStyleSheet();
        if( false == this.isAlreadyNormalized() ){
            StyleHelper.normalize( styleSheet );
            this.setAlreadyNormalized(true);
        }

        final JavaScriptObject rulesCollection = StyleHelper.getRules(styleSheet );
        return rulesCollection;
    }

    /**
     * This flag keeps track of whether or not the rules have been normalized.
     */
    private boolean alreadyNormalized;

    protected boolean isAlreadyNormalized(){
        return this.alreadyNormalized;
    }
    protected void setAlreadyNormalized( final boolean alreadyNormalized){
        this.alreadyNormalized = alreadyNormalized;
    }

    public int size() {
        return ObjectHelper.getPropertyCount(this.getRulesCollection() );
    }

    /**
     * A cache of all the Rule wrappers belonging to this List.
     */
    private List wrappers;

    protected List getWrappers(){
        ObjectHelper.checkNotNull( "field:wrappers", this.wrappers );
        return this.wrappers;
    }
    protected void setWrappers( final List wrappers ){
        ObjectHelper.checkNotNull( "parameter:wrappers", wrappers );
        this.wrappers = wrappers;
    }

    protected List createWrappers(){
        return new ArrayList();
    }

    public Object get(final int index) {        
        return this.getRule( index );
    }

    protected Rule getRule( final int index ){
        this.checkIndex( index );

        final List wrappers = this.getWrappers();            
        Rule rule = (Rule) wrappers.get( index );
        if( null == rule ){
            rule = new Rule();
            rule.setIndex( index );
            rule.setRuleList( this );

            wrappers.set( index, rule );
        }
        return rule;
    }

    public int indexOf( final Object object ){
        int index = -1;
        if( object instanceof Rule ){
            final Rule rule = (Rule)object;
            if( rule.hasRuleList() && rule.getRuleList() == this ){
                index = rule.getIndex();
            }
        }
        return index;
    }

    public boolean add(final Object newRule ) {
        final Rule rule = (Rule) newRule;
        this.checkNotAlreadyAttached( rule );      

        // prepare to adopt the rule...
        final int index = this.size();
        
        this.addRule( rule );
        
        // adopt...
        rule.setIndex( index );
        rule.setRuleList( this );

        // save the wrapper...
        this.checkIndex( index );
        final List wrappers = this.getWrappers();
        wrappers.set(index, rule );
        return true;
    }

    protected void addRule(final Rule newRule) {
        final Rule rule = (Rule)newRule;
        this.checkNotAlreadyAttached( rule );
        
        final JavaScriptObject styleSheet = this.getStyleSheet().getStyleSheet();
        final String selector = rule.getSelector();
        final String style = "";

        StyleHelper.addRule(styleSheet, selector, style);
    }

    public void add(final int index, final Object newRule) {
        this.checkIndex( index );
        final Rule rule = (Rule)newRule;
        this.checkNotAlreadyAttached( rule );
        
        this.insertRule( index, rule);

        // adopt the rule...
        rule.setIndex( index );
        rule.setRuleList( this );

        // save the wrapper...
        final int size = this.size();
        final List wrappers = this.getWrappers();

        // insert(save) the rule wrapper...
        wrappers.add( index, rule );

        // update the index of the wrappers after $index.
        for( int i = index + 1; i < size; i++ ){
            final Rule previousRule = (Rule)wrappers.get( i );
            if( null == previousRule ){
                continue;
            }
            previousRule.setIndex( i );
        }
    }
    protected void insertRule(final int index, final Rule rule) {
        this.checkNotAlreadyAttached( rule );
        
        final JavaScriptObject styleSheet = this.getStyleSheet().getStyleSheet();
        final String selector = rule.getSelector();
        final RuleStyle ruleStyle = (RuleStyle) rule.getStyle();
        final String style = ruleStyle.getCssText();

        StyleHelper.insertRule(styleSheet, index, selector, style);
    }

    public Object set(final int index, final Object object) {
        this.checkIndex( index );
        
        final Rule rule = (Rule)object;
        this.checkNotAlreadyAttached( rule );
        
        return null;
    }
    
    public Object remove(final int index) {
        this.checkIndex( index );

        final Rule rule = this.getRule( index );
        this.removeRule( rule );

        // fix up the indexes of the rules after $index.
        final List wrappers = this.getWrappers();
        final int wrapperSize = wrappers.size();
        for( int i = index; i < wrapperSize; i++ ){
            final Rule otherRule = (Rule)wrappers.get(i );
            if( null == otherRule){
                continue;
            }
            otherRule.setIndex( i );
        }
        return rule;
    }


    protected void removeRule(final Rule rule) {
        ObjectHelper.checkNotNull("parameter:rule", rule );

        if( rule.hasRuleList() ){
            final JavaScriptObject styleSheet = this.getStyleSheet().getStyleSheet();
            final int index = rule.getIndex();

            StyleHelper.removeRule(styleSheet, index);
            rule.clearRule();
        }
    }

    /**
     * Performs two tasks, verifies that the index is valid (it throws an exception if its not) and
     * also expands the wrappers list to have enough elements to match the passed index.
     * @param index
     */
    protected void checkIndex( final int index ){
        if( index < 0 || index > this.size() ){
            throw new IndexOutOfBoundsException();
        }
        final List wrappers = this.getWrappers();

        final int append = index - wrappers.size() + 1;
        for( int i = 0; i < append; i++ ){
            wrappers.add( null );
        }
    }

    /**
     * Checks that the rule parameter is not null and that it is not already attached to a RuleList
     * @param rule
     */
    protected void checkNotAlreadyAttached( final Rule rule ){
        ObjectHelper.checkNotNull( "parameter:rule", rule );
        if( rule.hasRuleList() ){
            throw new IllegalArgumentException("The parameter:rule has already been added to a List.");
        }
    }
}
