package rocket.client.style.impl;

import rocket.client.style.Rule;
import rocket.client.util.ObjectHelper;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * A specialised form of the RuleListSupport class that has a few changes due to 
 * Internet Explorer 6.x implementation.
 * @author Miroslav Pokorny (mP)
 */
public class RuleListInternetExplorer6Impl extends RuleListImpl {

    public RuleListInternetExplorer6Impl(){
        super();
    }
    
    native protected JavaScriptObject getObject(final JavaScriptObject styleSheet ) /*-{
        return styleSheet.rules ? styleSheet.rules : null;
    }-*/;
    
    protected void addRule( final Rule rule ){
        ObjectHelper.checkNotNull( "parameter:rule", rule );
        
        final JavaScriptObject styleSheet = this.getStyleSheet().getObject();       
        final String selector = rule.getSelector();
        final String style = rule.getStyle().getCssText();
        this.addRule0( styleSheet, selector, style );
    }
    
    protected native void addRule0(final JavaScriptObject styleSheet, final String selectorText, final String styleText)/*-{        
         var index = styleSheet.rules.length;
         var safeStyleText = styleText.length == 0 ? ";" : styleText;
         
         styleSheet.addRule( selectorText, safeStyleText, index );         
    }-*/;
    
    protected void insertRule( final int index, final Rule rule ){
        ObjectHelper.checkNotNull( "parameter:rule", rule );
        
        final JavaScriptObject styleSheet = this.getStyleSheet().getObject();        
        final String selector = rule.getSelector();
        final String style = rule.getStyle().getCssText();
        this.insertRule0( styleSheet, index, selector, style );
    }
    
    protected native void insertRule0(final JavaScriptObject styleSheet, final int index, final String selectorText, final String styleText)/*-{
        styleSheet.addRule( selectorText, styleText.length == 0 ? ";" : styleText, index );         
        }-*/;     
    
   /**
    * Escapes to javascript to delete the requested rule.
    */
   native protected void removeRule0(final JavaScriptObject styleSheet, final int index) /*-{            
        styleSheet.removeRule( index );
    }-*/;
   
   /**
    * There is no need to normalize rules as IE6 does this automatically.
    */
   protected void normalizeRules(){    
   }
}
