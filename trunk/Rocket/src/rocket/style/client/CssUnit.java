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
public class CssUnit {
	public final static CssUnit NONE = new CssUnit("", Float.NaN);

	public final static CssUnit PERCENTAGE = new CssUnit("%", Float.NaN);

	public final static CssUnit PX = new CssUnit("px", 1);

	public final static CssUnit EM = new CssUnit("em", Float.NaN);

	public final static CssUnit EX = new CssUnit("ex", Float.NaN);

	public final static CssUnit IN = new CssUnit("in", Css.IN_TO_PX);

	public final static CssUnit CM = new CssUnit("cm", Css.CM_TO_PX);

	public final static CssUnit MM = new CssUnit("mm", Css.MM_TO_PX);

	public final static CssUnit PT = new CssUnit("pt", Css.PT_TO_PX);

	public final static CssUnit PC = new CssUnit("pc", Css.PC_TO_PX);

	/**
	 * Takes a css position value and returns the enum.
	 * 
	 * @param propertyValue
	 * @return The CssUnit within the propertyValue
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

	protected CssUnit(final String suffix, final float pixels) {
		super();

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

	protected boolean equals(final String string) {
		return this.getSuffix().equalsIgnoreCase(string);
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
			throw new UnsupportedOperationException("Unable to convert to/from this unit suffix\"" + this.suffix + "\".");
		}
		return pixels;
	}

	void setPixels(final float pixels) {
		this.pixels = pixels;
	}

	public String toString() {
		return this.suffix;
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

	/**
	 * Attempts to translate a length with units into another unit.
	 * 
	 * Relative units such as em/ex and percentage will fail and result in a
	 * {@link java.lang.UnsupportedOperationException} being thrown.
	 * 
	 * @param value
	 * @param targetUnit
	 * @return The converted value.
	 */
	static public float convertValue(final String value, final CssUnit targetUnit) {
		Checker.notEmpty("parameter:value", value);
		Checker.notNull("parameter:targetUnit", targetUnit);
	
		float length = 0;
		while (true) {
			if (value.equals("0") || value.equals("auto")) {
				break;
			}
	
			final CssUnit unit = CssUnit.getUnit(value);
			final String numberString = value.substring(0, value.length() - unit.getSuffix().length());
	
			// convert value into a number
			length = Float.parseFloat(numberString);
	
			// if the unit and target unit are the same do nothing...
			if (unit == targetUnit) {
				break;
			}
	
			length = unit.toPixels(length);
			length = targetUnit.fromPixels(length);
			break;
		}
	
		return length;
	}
}
