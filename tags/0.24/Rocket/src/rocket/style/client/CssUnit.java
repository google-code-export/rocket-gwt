package rocket.style.client;

/**
 * This enum represents each of the possible CssUnits that may be applied to any style property value.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class CssUnit {
    public final static CssUnit NONE = new CssUnit("", Float.NaN);

    public final static CssUnit PERCENTAGE = new CssUnit("%", Float.NaN);

    public final static CssUnit PX = new CssUnit("px", 1);

    public final static CssUnit EM = new CssUnit("em", Float.NaN );

    public final static CssUnit EX = new CssUnit("ex", Float.NaN );

    public final static CssUnit IN = new CssUnit("in", StyleConstants.IN_TO_PX );

    public final static CssUnit CM = new CssUnit("cm", StyleConstants.CM_TO_PX );

    public final static CssUnit MM = new CssUnit("mm", StyleConstants.MM_TO_PX );

    public final static CssUnit PT = new CssUnit("pt", StyleConstants.PT_TO_PX );

    public final static CssUnit PC = new CssUnit("pc", StyleConstants.PC_TO_PX );

    /**
     * Takes a css position value and returns the enum.
     * 
     * @param propertyValue
     * @return
     */
    static public CssUnit toCssUnit(final String propertyValue) {
        CssUnit unit = null;

        while (true) {
            if (NONE.equals(propertyValue)) {
                unit = NONE;
                break;
            }
            if (PERCENTAGE.equals(propertyValue)) {
                unit = PERCENTAGE;
                break;
            }
            if (PX.equals(propertyValue)) {
                unit = PX;
                break;
            }
            if (EM.equals(propertyValue)) {
                unit = EM;
                break;
            }
            if (EX.equals(propertyValue)) {
                unit = EX;
                break;
            }
            if (IN.equals(propertyValue)) {
                unit = IN;
                break;
            }
            if (CM.equals(propertyValue)) {
                unit = CM;
                break;
            }
            if (MM.equals(propertyValue)) {
                unit = MM;
                break;
            }
            if (PT.equals(propertyValue)) {
                unit = PT;
                break;
            }
            if (PC.equals(propertyValue)) {
                unit = PC;
                break;
            }
            unit = NONE;
            break;
        }

        return unit;
    }

    protected CssUnit(final String value, final float pixels ) {
        super();

        this.setValue(value);
        this.setPixels( pixels );
    }

    /**
     * Takes a length value assumed to be of this unit and converts the value into pixels.
     * @param length
     * @return
     */
    public float toPixels( final float length ){
        return length * this.getPixels();
    }
    /**
     * Takes a pixel length value and converts it into a value of this unit.
     * @param pixelLength
     * @return
     */
    public float fromPixels( final float pixelLength ){
        return pixelLength / this.getPixels();
    }
    
    protected boolean equals(final String string) {
        return this.getValue().equalsIgnoreCase(string);
    }
    /**
     * The string abbreviation for this unit
     */
    private String value;

    public String getValue() {
        return this.value;
    }

    protected void setValue(final String value) {
        this.value = value;
    }

    /**
     * This scaling factor may be used to convert any lengths of this value into pixels
     */
    private float pixels;
    
    float getPixels(){
        if( Float.isNaN( this.pixels )){
            throw new UnsupportedOperationException( "Unable to convert to/from this unit.");
        }
        return pixels;
    }
    void setPixels( final float pixels ){
        this.pixels = pixels;
    }
    
    public String toString() {
        return this.value;
    }
}
