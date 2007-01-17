package rocket.style.client;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.core.client.JavaScriptObject;
/**
 * A common base class for any map view of a set of styles.
 * @author Miroslav Pokorny (mP)
 */
abstract public class Style extends AbstractMap{


    public String getCssText(){
        return ObjectHelper.getString( this.getStyle(), StyleConstants.CSS_STYLE_TEXT_PROPERTY_NAME );
    }
    
    public void setCssText( final String cssText ){
        ObjectHelper.setString( this.getStyle(), StyleConstants.CSS_STYLE_TEXT_PROPERTY_NAME, cssText );        
    }
    
    abstract protected JavaScriptObject getStyle();
    
    public abstract int size();

    public Object get( final Object key ){
        return this.getStylePropertyValue((String) key );
    }

    /**
     * Factory method which creates a new StylePropertyValue and populates it with a string value if the inline style property is available.
     * @param propertyName
     * @return
     */
    protected StylePropertyValue getStylePropertyValue( final String propertyName ){
        StylePropertyValue value = null;
        String stringValue =this.getValue( propertyName );
        if( false == StringHelper.isNullOrEmpty( stringValue )){
            value = new StylePropertyValue();
            value.setString( stringValue );
        }
        return value;
    }

    abstract String getValue( String propertyName );

    public boolean containsKey( final Object key ){
        return null != this.get( key );
    }

    public Object put( final Object key, final Object value ){
        return this.putStyle((String) key, (StylePropertyValue)value);
    }

    protected StylePropertyValue putStyle( final String propertyName, final StylePropertyValue newValue ){
        final StylePropertyValue replaced = this.getStylePropertyValue(propertyName);

        final String propertyValue = newValue.getString();
        this.putValue( propertyName, propertyValue);

        return replaced;
    }

    abstract protected void putValue( String propertyName, String propertyValue );

    public Object remove( final Object key ){
        return this.removeStyle( (String) key );
    }

    protected StylePropertyValue removeStyle( final String propertyName ){
        final StylePropertyValue removed = this.getStylePropertyValue(propertyName);
        this.removeValue( propertyName);
        return removed;
    }

    abstract protected void removeValue( final String propertyName );

    public Set entrySet() {
        return new StyleEntrySet();
    }
    /**
     * Implements a Set view of all the inline styles belonging to an Element.
     */
    class StyleEntrySet extends AbstractSet{
        public int size(){
            return Style.this.size();
        }
        public Iterator iterator(){
            return new StyleEntrySetIterator();
        }
    }

    /**
     * This iterator may be used to visit all inline style entries.
     */
    class StyleEntrySetIterator implements Iterator {

        public boolean hasNext(){
            return this.getCursor() < this.getPropertyNames().length;
        }
        public Object next(){
            final int cursor = this.getCursor();
            final String[] propertyNames = this.getPropertyNames();
            if( cursor >= propertyNames.length ){
                throw new NoSuchElementException();
            }
            final String key = propertyNames[ cursor ];
            final Object value = Style.this.get( key );
            this.setCursor( cursor + 1 );
            return new Map.Entry(){
                public Object getKey(){
                    return key;
                }
                public Object getValue(){
                    return value;
                }
                public Object setValue( final Object newValue ){
                    return Style.this.put( key, newValue);
                }
            };
        }
        public void remove(){
            final int cursor = this.getCursor() - 1;
            final String[] propertyNames = this.getPropertyNames();
            final String propertyName = propertyNames[ cursor ];
            if( null == propertyName ){
                throw new IllegalStateException();
            }

            Style.this.remove( propertyName );
            propertyNames[ cursor ] = null;// mark that its already been deleted.
        }

        /**
         * An array containing all the property names for the native object.
         */
        String[] propertyNames;

        String[] getPropertyNames() {
            if (false == this.hasPropertyNames()) {
                final String commaSeparatedList = Style.this.getPropertyNames();
                this.setPropertyNames( StringHelper.split( commaSeparatedList, ",", true));
            }
            return this.propertyNames;
        }

        boolean hasPropertyNames() {
            return null != this.propertyNames;
        }

        void setPropertyNames(final String[] propertyNames) {
            this.propertyNames = propertyNames;
        }

        /**
         * This cursor points to the next visitable element.
         */
        int cursor = 0;

        int getCursor() {
            return this.cursor;
        }

        void setCursor(final int cursor) {
            this.cursor = cursor;
        }
    }
    
    abstract String getPropertyNames();
}
