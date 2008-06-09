/*
 * Copyright Miroslav Pokorny
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
package rocket.style.client;

import rocket.util.client.Checker;
import rocket.util.client.Tester;

/**
 * This enum represents each of the possible CssUnits that may be applied to any
 * style property suffix.
 * 
 * @author Miroslav Pokorny (mP)
 */
public enum CssUnit {

	NONE( "", Float.NaN ),
	PERCENTAGE( "%", Float.NaN ),
	PX( "px", 1.0f ),
	EM( "em", Float.NaN ),
	EX( "ex", Float.NaN ),
	IN( "in", Css.IN_TO_PX ),
	CM( "cm", Css.CM_TO_PX ),
	MM( "mm", Css.MM_TO_PX ),
	PT( "pt", Css.PT_TO_PX ),
	PC( "pc", Css.PC_TO_PX );
	
	/**
	 * Takes a css position value and returns the enum.
	 * 
	 * @param propertyValue
	 * @return The CssUnit within the propertyValue
	 */
	static public CssUnit toCssUnit(final String propertyValue) {
		CssUnit unit = null;

		while (true) {
			if (propertyValue.endsWith(PX.getSuffix())) {
				unit = PX;
				break;
			}
			if (propertyValue.endsWith(PERCENTAGE.getSuffix())) {
				unit = PERCENTAGE;
				break;
			}
			if (propertyValue.endsWith(EM.getSuffix())) {
				unit = EM;
				break;
			}
			if (propertyValue.endsWith(EX.getSuffix())) {
				unit = EX;
				break;
			}
			if (propertyValue.endsWith(IN.getSuffix())) {
				unit = IN;
				break;
			}
			if (propertyValue.endsWith(CM.getSuffix())) {
				unit = CM;
				break;
			}
			if (propertyValue.endsWith(MM.getSuffix())) {
				unit = MM;
				break;
			}
			if (propertyValue.endsWith(PT.getSuffix())) {
				unit = PT;
				break;
			}
			if (propertyValue.endsWith(PC.getSuffix())) {
				unit = PC;
				break;
			}
			unit = NONE;
			break;
		}

		return unit;
	}


	/**
	 * Extracts the unit portion as a CssUnit instance given a length.
	 * 
	 * @param value
	 *            If value is empty or null null will be returned.
	 * @return The CssUnit within the string value
	 */
	static public CssUnit getUnit(final String value) {
		CssUnit unit = NONE;
		while (true) {
			// defensive test.
			if (Tester.isNullOrEmpty(value)) {
				break;
			}

			if (value.endsWith("%")) {
				unit = PERCENTAGE;
				break;
			}

			final int valueLength = value.length();
			if (valueLength < 3) {
				unit = NONE;
				break;
			}
			// if the third last char is not a number then value isnt
			// number-unit.
			final char thirdLastChar = value.charAt(valueLength - 3);
			if (false == Character.isDigit(thirdLastChar)) {
				unit = NONE;
				break;
			}

			unit = toCssUnit(value.substring(valueLength - 2));
			break;
		}
		return unit;
	}
	
	CssUnit(final String suffix, final float pixels) {
		this.setSuffix(suffix);
		this.setPixels(pixels);
	}

	/**
	 * Takes a length value assumed to be of this unit and converts the value
	 * into pixels.
	 * 
	 * @param length
	 * @return The pixel value
	 */
	public float toPixels(final float length) {
		return length * this.getPixels();
	}

	/**
	 * Takes a pixel length value and converts it into a value of this unit.
	 * 
	 * @param pixelLength
	 * @return The pixel value
	 */
	public float fromPixels(final float pixelLength) {
		return pixelLength / this.getPixels();
	}

	/**
	 * The string abbreviation for this unit
	 */
	private String suffix;

	public String getSuffix() {
		return this.suffix;
	}

	protected void setSuffix(final String suffix) {
		this.suffix = suffix;
	}

	/**
	 * This scaling factor may be used to convert any lengths of this value into
	 * pixels
	 */
	private float pixels;

	float getPixels() {
		if (Float.isNaN(this.pixels)) {
			throw new UnsupportedOperationException("Unable to convert to/from \"" + this.suffix + "\".");
		}
		return pixels;
	}

	void setPixels(final float pixels) {
		this.pixels = pixels;
	}

	public float convert(final String value ) {
		Checker.notEmpty("parameter:value", value);

		float length = 0;
		while (true) {
			if (value.equals("0") || value.equals("auto")) {
				break;
			}

			CssUnit unit = CssUnit.getUnit(value);
		
			String numberString = null;
			if( this == CssUnit.NONE  ){
				numberString = value.endsWith("px") ? value.substring( 0, value.length() - 2 ) : value; 
			} else {
				numberString = value.substring(0, value.length() - unit.getSuffix().length());
			}

			// convert value into a number
			length = Float.parseFloat(numberString);

			// if the unit and target unit are the same do nothing...
			if (this == unit ) {
				break;
			}

			length = unit.toPixels(length);
			length = this.fromPixels(length);
			break;
		}

		return length;
	}
	
	public String toString() {
		return this.suffix;
	}
}
