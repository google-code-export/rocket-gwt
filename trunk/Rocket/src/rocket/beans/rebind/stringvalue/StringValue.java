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
package rocket.beans.rebind.stringvalue;

import rocket.beans.rebind.value.Value;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.codeblock.BooleanLiteral;
import rocket.generator.rebind.codeblock.ByteLiteral;
import rocket.generator.rebind.codeblock.CharLiteral;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.DoubleLiteral;
import rocket.generator.rebind.codeblock.FloatLiteral;
import rocket.generator.rebind.codeblock.IntLiteral;
import rocket.generator.rebind.codeblock.LongLiteral;
import rocket.generator.rebind.codeblock.ShortLiteral;
import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import rocket.generator.rebind.SourceWriter;

/**
 * A StringValue holds a string literal which may be converted to an used to set
 * a String or primitive property or constructor argument.
 * 
 * @author Miroslav Pokorny
 */
public class StringValue extends Value {

	public boolean isCompatibleWith(final Type type) {
		ObjectHelper.checkNotNull("parameter:type", type);

		boolean compatible = false;

		while (true) {
			final GeneratorContext context = this.getGeneratorContext();
			if (type == context.getString()) {
				compatible = isCompatibleWithString();
				break;
			}

			if (type == context.getBoolean()) {
				compatible = isCompatibleWithBoolean();
				break;
			}
			if (type == context.getByte()) {
				compatible = isCompatibleWithByte();
				break;
			}
			if (type == context.getShort()) {
				compatible = isCompatibleWithShort();
				break;
			}
			if (type == context.getInt()) {
				compatible = isCompatibleWithInt();
				break;
			}
			if (type == context.getLong()) {
				compatible = isCompatibleWithLong();
				break;
			}
			if (type == context.getFloat()) {
				compatible = isCompatibleWithFloat();
				break;
			}
			if (type == context.getDouble()) {
				compatible = isCompatibleWithDouble();
				break;
			}
			if (type == context.getChar()) {
				compatible = isCompatibleWithChar();
				break;
			}
			break;
		}

		return compatible;
	}

	protected boolean isCompatibleWithBoolean() {
		final String value = this.getValue();
		return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
	}

	protected boolean isCompatibleWithByte() {
		boolean compatible = false;
		try {
			this.getByteValue();
			compatible = true;
		} catch (final NumberFormatException bad) {
		}
		return compatible;
	}

	protected boolean isCompatibleWithShort() {
		boolean compatible = false;
		try {
			this.getShortValue();
			compatible = true;
		} catch (final NumberFormatException bad) {
		}
		return compatible;
	}

	protected boolean isCompatibleWithInt() {
		boolean compatible = false;

		try {
			this.getIntValue();
			compatible = true;
		} catch (final NumberFormatException bad) {
		}
		return compatible;
	}

	protected boolean isCompatibleWithLong() {
		boolean compatible = false;
		try {
			this.getLongValue();
			compatible = true;
		} catch (final NumberFormatException bad) {
		}
		return compatible;
	}

	protected boolean isCompatibleWithFloat() {
		boolean compatible = false;
		try {
			this.getFloatValue();
			compatible = true;
		} catch (final NumberFormatException bad) {
		}
		return compatible;
	}

	protected boolean isCompatibleWithDouble() {
		boolean compatible = false;
		try {
			this.getDoubleValue();
			compatible = true;
		} catch (final NumberFormatException bad) {
		}
		return compatible;
	}

	protected boolean isCompatibleWithChar() {
		return value.length() == 1;
	}

	protected boolean isCompatibleWithString() {
		return true;
	}

	/**
	 * The string value
	 */
	private String value;

	public String getValue() {
		StringHelper.checkNotNull("field:value", value);
		return value;
	}

	protected boolean getBooleanValue() {
		final String value = this.getValue();
		return "true".equals(value);
	}

	protected byte getByteValue() {
		return Byte.parseByte(this.getValue());
	}

	protected short getShortValue() {
		return Short.parseShort(this.getValue());
	}

	protected int getIntValue() {
		return Integer.parseInt(this.getValue());
	}

	protected long getLongValue() {
		return Long.parseLong(this.getValue());
	}

	protected float getFloatValue() {
		return Float.parseFloat(this.getValue());
	}

	protected double getDoubleValue() {
		return Double.parseDouble(this.getValue());
	}

	protected char getCharValue() {
		return this.getValue().charAt(0);
	}

	public void setValue(final String value) {
		StringHelper.checkNotNull("parameter:value", value);
		this.value = value;
	}

	public void write(final SourceWriter writer) {
		CodeBlock literal = null;

		while (true) {
			final Type type = this.getType();
			final GeneratorContext context = this.getGeneratorContext();
			if (type == context.getBoolean()) {
				literal = new BooleanLiteral(this.getBooleanValue());
				break;
			}
			if (type == context.getByte()) {
				literal = new ByteLiteral(this.getByteValue());
				break;
			}
			if (type == context.getShort()) {
				literal = new ShortLiteral(this.getShortValue());
				break;
			}
			if (type == context.getInt()) {
				literal = new IntLiteral(this.getIntValue());
				break;
			}
			if (type == context.getLong()) {
				literal = new LongLiteral(this.getLongValue());
				break;
			}
			if (type == context.getFloat()) {
				literal = new FloatLiteral(this.getFloatValue());
				break;
			}
			if (type == context.getDouble()) {
				literal = new DoubleLiteral(this.getDoubleValue());
				break;
			}
			if (type == context.getChar()) {
				literal = new CharLiteral(this.getCharValue());
				break;
			}
			literal = new StringLiteral(this.getValue());
			break;
		}

		literal.write(writer);
	}

	public String toString() {
		return super.toString() + ", value[" + value + "]";
	}
}
