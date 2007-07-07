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
package rocket.beans.rebind.values;

import rocket.util.client.StringHelper;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;

/**
 * HOlds a string value that is assigned to a property
 * 
 * @author Miroslav Pokorny
 * 
 */
public class StringValueDefinition extends PropertyValueDefinition {

	/**
	 * Generates the literal that contains the value of this property.
	 */
	public String generatePropertyValueCodeBlock() {
		String statement = null;

		while (true) {
			final JType propertyType = this.getType();
			if (propertyType == JPrimitiveType.BOOLEAN) {
				statement = generateBooleanPropertyValueLiteral();
				break;
			}
			if (propertyType == JPrimitiveType.BYTE) {
				statement = generateBytePropertyValueLiteral();
				break;
			}
			if (propertyType == JPrimitiveType.SHORT) {
				statement = generateShortPropertyValueLiteral();
				break;
			}
			if (propertyType == JPrimitiveType.INT) {
				statement = generateIntPropertyValueLiteral();
				break;
			}
			if (propertyType == JPrimitiveType.LONG) {
				statement = generateLongPropertyValueLiteral();
				break;
			}
			if (propertyType == JPrimitiveType.FLOAT) {
				statement = generateFloatPropertyValueLiteral();
				break;
			}
			if (propertyType == JPrimitiveType.DOUBLE) {
				statement = generateDoublePropertyValueLiteral();
				break;
			}
			if (propertyType == JPrimitiveType.CHAR) {
				statement = generateCharPropertyValueLiteral();
				break;
			}
			if (propertyType.getQualifiedSourceName().equals(String.class.getName())) {
				statement = generateStringPropertyValueLiteral();
				break;
			}
			break;
		}

		return statement;
	}

	private String generateBooleanPropertyValueLiteral() {
		return "(boolean)" + this.getValue();
	}

	private String generateBytePropertyValueLiteral() {
		return "(byte)" + this.getValue();
	}

	private String generateShortPropertyValueLiteral() {
		return "(short)" + this.getValue();
	}

	private String generateIntPropertyValueLiteral() {
		return this.getValue();
	}

	private String generateLongPropertyValueLiteral() {
		return this.getValue() + "L";
	}

	private String generateFloatPropertyValueLiteral() {
		String statement;
		statement = "(float)" + this.getValue() + "f";
		return statement;
	}

	private String generateDoublePropertyValueLiteral() {
		String statement;
		statement = this.getValue();
		return statement;
	}

	private String generateCharPropertyValueLiteral() {
		String statement;
		statement = "'" + Generator.escape(this.getValue()) + "'";
		return statement;
	}

	private String generateStringPropertyValueLiteral() {
		return "\"" + Generator.escape(this.getValue()) + "\"";
	}

	/**
	 * If the property is a primitive or String report true
	 * 
	 * @return
	 */
	public boolean isCompatibleWith() {
		boolean compatible = false;

		while (true) {
			final JType propertyType = this.getType();
			if (propertyType.getQualifiedSourceName().equals(String.class.getName())) {
				compatible = true;
				break;
			}
			final String value = this.getValue();
			if (propertyType == JPrimitiveType.BOOLEAN) {
				compatible = isCompatibleWithBoolean(value);
				break;
			}
			if (propertyType == JPrimitiveType.BYTE) {
				compatible = isCompatibleWithByte(compatible, value);
				break;
			}
			if (propertyType == JPrimitiveType.SHORT) {
				compatible = isCompatibleWithShort(compatible, value);
				break;
			}
			if (propertyType == JPrimitiveType.INT) {
				compatible = isCompatibleWithInt(compatible, value);
				break;
			}
			if (propertyType == JPrimitiveType.LONG) {
				compatible = isCompatibleWithLong(compatible, value);
				break;
			}
			if (propertyType == JPrimitiveType.FLOAT) {
				compatible = isCompatibleWithFloat(compatible, value);
				break;
			}
			if (propertyType == JPrimitiveType.DOUBLE) {
				compatible = isCompatibleWithDouble(compatible, value);
				break;
			}
			if (propertyType == JPrimitiveType.CHAR) {
				compatible = isCompatibleWithChar(value);
				break;
			}
			break;
		}

		return compatible;
	}

	private boolean isCompatibleWithBoolean(final String value) {
		return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
	}

	private boolean isCompatibleWithByte(boolean compatible, final String value) {
		try {
			Byte.parseByte(value);
			compatible = true;
		} catch (final NumberFormatException bad) {
		}
		return compatible;
	}

	private boolean isCompatibleWithShort(boolean compatible, final String value) {
		try {
			Short.parseShort(value);
			compatible = true;
		} catch (final NumberFormatException bad) {
		}
		return compatible;
	}

	private boolean isCompatibleWithInt(boolean compatible, final String value) {
		try {
			Integer.parseInt(value);
			compatible = true;
		} catch (final NumberFormatException bad) {
		}
		return compatible;
	}

	private boolean isCompatibleWithLong(boolean compatible, final String value) {
		try {
			Long.parseLong(value);
			compatible = true;
		} catch (final NumberFormatException bad) {
		}
		return compatible;
	}

	private boolean isCompatibleWithFloat(boolean compatible, final String value) {
		try {
			Float.parseFloat(value);
			compatible = true;
		} catch (final NumberFormatException bad) {
		}
		return compatible;
	}

	private boolean isCompatibleWithDouble(boolean compatible, final String value) {
		try {
			Double.parseDouble(value);
			compatible = true;
		} catch (final NumberFormatException bad) {
		}
		return compatible;
	}

	private boolean isCompatibleWithChar(final String value) {
		boolean compatible;
		compatible = value.length() == 1;
		return compatible;
	}

	/**
	 * The string value
	 */
	private String value;

	public String getValue() {
		StringHelper.checkNotEmpty("field:value", value);
		return value;
	}

	public void setValue(final String value) {
		StringHelper.checkNotEmpty("parameter:value", value);
		this.value = value;
	}

}
