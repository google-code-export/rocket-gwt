package rocket.client.style;

import rocket.client.dom.DomObjectMapValue;
import rocket.client.util.ColourHelper;
import rocket.client.util.ObjectHelper;
import rocket.client.util.PrimitiveHelper;
import rocket.client.util.StringHelper;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * A StylePropertyValue represents a single property value belonging to a style.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class StylePropertyValue extends DomObjectMapValue {
    
    public StylePropertyValue() {
        super();
    }

    /**
     * Returns the entire property value as a string.
     * 
     * @return
     */
    public String getString() {
        return this.getPropertyValue();
    }

    public void setString(final String propertyValue) {
        this.setPropertyValue(propertyValue);
    }

    /**
     * Returns the portion of the property value that contains the numeric value without the unit.
     * 
     * @return
     */
    protected String getNumber() {
        final String string = this.getPropertyValue();
        final int length = string.length();
        int i = 0;

        // skip until a digit is found...
        while (i < length) {
            final char c = string.charAt(i);
            if (Character.isDigit(c)) {
                break;
            }
            i++;
        }

        // skip until a non digit is found ...
        int j = i + 1;
        while (j < length) {
            final char c = string.charAt(j);
            j++;
            if (Character.isDigit(c)) {
                break;
            }
        }
        return string.substring(i, j);
    }

    protected void setNumber(final String number) {
        this.setString(number + ' ' + this.getUnit());
    }

    /**
     * Retrieves the unit portion of a style.
     * 
     * @return
     */
    public String getUnit() {
        String unit = null;
        while (true) {
            final String value = this.getPropertyValue();
            final int valueLength = value.length();
            if (valueLength < 2) {
                unit = "";
                break;
            }
            unit = value.substring(valueLength - 2);
            break;
        }
        return unit;
    }

    public void setUnit(final String unit) {
        StringHelper.checkNotEmpty("parameter:unit", unit);

        this.setString(this.getNumber() + ' ' + unit);
    }

    public double getDouble() {
        return Double.parseDouble(this.getNumber());
    }

    public void setDouble(final double doubleValue) {
        this.setNumber(String.valueOf(doubleValue));
    }

    public int getInteger() {
        return Integer.parseInt(this.getNumber());
    }

    public void setInteger(final int intValue) {
        this.setNumber(String.valueOf(intValue));
    }

    /**
     * Reads a property that contains a rgb value in a format neutral manner.
     * 
     * This is necessary as Internet Explorer returns the value as was found in the style declaration, whilst other browsers such as FireFox
     * always return in the colour value as rgb( rr,gg,bb );
     * 
     * @return An integer containing the colour value.
     */
    public int getColour() {
        int colourValue;

        while (true) {
            final String value = this.getString();

            // test if a #rrggbb or #rgb value...
            if (value.startsWith("#")) {

                // handles #rgb values.
                if (value.length() == 1 + 3) {
                    int red = PrimitiveHelper.characterDigit(value.charAt(1), 16);
                    red = red * 17;
                    int green = PrimitiveHelper.characterDigit(value.charAt(2), 16);
                    green = green * 17;
                    int blue = PrimitiveHelper.characterDigit(value.charAt(3), 16);
                    blue = blue * 17;
                    colourValue = ColourHelper.makeColour(red, green, blue);
                    break;
                }

                // handles #rrggbb values.
                if (value.length() == 1 + 6) {
                    colourValue = Integer.parseInt(value.substring(1, 1 + 6), 16);
                    break;
                }
            }
            // test if rgb(rr,gg,bb)
            if (value.startsWith("rgb(") & value.endsWith(")")) {

                // break up value into rgb(RRR,GGG,BBB) where RRR etc are the red, green and blue triplet.
                final String[] triplets = StringHelper.split(value.substring(4, value.length() - 1), ",", true);
                int red = Integer.parseInt(triplets[0]);
                int green = Integer.parseInt(triplets[1]);
                int blue = Integer.parseInt(triplets[2]);

                colourValue = ColourHelper.makeColour(red, green, blue);
                break;
            }

            colourValue = ColourHelper.getColour(value);
            if (-1 != colourValue) {
                break;
            }

            // unknown colour value/ format etc.
            throw new UnsupportedOperationException("Unable to read rgb value from property value[" + value + "]");
        }

        return colourValue;
    }

    public void setColour(final int colourValue) {
        this.setString("#" + StringHelper.padLeft("" + colourValue, 6, ' '));
    }

    /**
     * Writes a previously cached propertyValue whenever an object is set.
     */
    public void setObject(final JavaScriptObject object) {
        String value = null;
        if (this.hasPropertyValue()) {
            value = this.getPropertyValue();
        }

        super.setObject(object);

        if (null != value) {
            this.setPropertyValue(value);
        }
        this.clearPropertyValue();
    }

    // OBJECT ::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    /**
     * Compares if two StylePropertyValue objects are in fact equal.
     */
    public boolean equals(final Object otherObject) {
        return otherObject instanceof StylePropertyValue ? this.equals((StylePropertyValue) otherObject) : false;
    }

    public boolean equals(final StylePropertyValue otherStylePropertyValue) {
        ObjectHelper.checkNotNull("parameter:otherStylePropertyValue", otherStylePropertyValue);

        return this.getPropertyValue().equals(otherStylePropertyValue.getPropertyValue());
    }
}
