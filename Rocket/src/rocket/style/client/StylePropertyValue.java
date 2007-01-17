package rocket.style.client;

import rocket.util.client.Colour;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

/**
 * Each instance represents the value of a Style regardless of source eg, rule, inline or computed.
 * @author Miroslav Pokorny (mP)
 */
public class StylePropertyValue {

    /**
     * The value of the style in string form.
     */
    private String string;

    public String getString() {
        StringHelper.checkNotEmpty( "field:string", string);
        return this.string;
    }

    public void setString(final String string) {
        StringHelper.checkNotEmpty( "parameter:string", string);
        this.string = string;
    }

    /**
     * Extracts the number portion of the string by dropping the units.
     *
     * @return
     */
    protected String extractNumber(final String value) {
        StringHelper.checkNotEmpty("parameter:value", value);

        final int length = value.length();
        String number = value;

        while (true) {
            if (length < 2) {
                break;
            }
            final char last = value.charAt(length - 1);
            if (Character.isDigit(last)) {
                break;
            }
            final char secondLast = value.charAt(length - 2);
            if (Character.isDigit(secondLast)) {
                break;
            }

            // value definitely ends in a unit drop the unit portion...
            number = number.substring(0, length - 2);
            break;
        }

        return number;
    }


    public double getDouble(final CssUnit unit) {
        final String value = this.getString();
        return StyleHelper.convertValue(value, unit);
    }

    public void setDouble(final double doubleValue, final CssUnit unit) {
        ObjectHelper.checkNotNull("parameter:unit", unit);

        // drop any trailing decimal 0's.
        final String stringForm = Math.round(doubleValue) == doubleValue ? String.valueOf((int) doubleValue) : String
                .valueOf(doubleValue);

        this.setString(stringForm + unit.getValue());
    }

    public int getInteger(final CssUnit unit) {
        final String value = this.getString();
        return Math.round( StyleHelper.convertValue( value, unit));
    }

    public void setInteger(final int intValue, final CssUnit unit) {
        ObjectHelper.checkNotNull("parameter:unit", unit);

        this.setString(String.valueOf(intValue) + unit.getValue());
    }

    /**
     * Reads a property that contains a rgb value as a Colour object.
     *
     * @return The colour value.
     */
    public Colour getColour() {
        return Colour.parse(this.getString());
    }

    public void setColour(final Colour colour) {
        ObjectHelper.checkNotNull( "parameter:colour", colour);
        this.setString(colour.toCssColour());
    }

    public CssUnit getUnit() {
        return StyleHelper.getUnit(this.getString());
    }

    public String getUrl(){
        return StyleHelper.getUrl( this.getString() );
    }
    public void setUrl( final String url ){
        StringHelper.checkNotEmpty( "parameter:url", url );
        this.setString( "url('" + url + "')");
    }

    public int hashCode(){
        return this.getString().hashCode();
    }

    public boolean equals( final Object otherObject ){
        return otherObject instanceof StylePropertyValue ? this.equals( (StylePropertyValue) otherObject ) : false;
    }
    public boolean equals( final StylePropertyValue otherStylePropertyValue ){
        return this.getString().equals( otherStylePropertyValue.getString() );
    }

    public String toString(){
        return super.toString() + ", string[" + string + "]";
    }
}
