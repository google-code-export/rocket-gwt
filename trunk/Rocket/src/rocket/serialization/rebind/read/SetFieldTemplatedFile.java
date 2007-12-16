package rocket.serialization.rebind.read;

import java.io.InputStream;

import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.field.Field;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

public class SetFieldTemplatedFile extends TemplatedCodeBlock {

	public boolean isNative() {
		return true;
	}

	public void setNative(final boolean nativee) {
		throw new UnsupportedOperationException();
	}

	protected InputStream getInputStream() {
		final String fileName = Constants.SET_FIELD_TEMPLATE;

		final InputStream inputStream = this.getClass().getResourceAsStream(fileName);
		ObjectHelper.checkNotNull(fileName, inputStream);
		return inputStream;
	}

	protected Object getValue0(final String name) {
		Object value = null;

		while (true) {
			if (Constants.SET_FIELD_FIELD.equals(name)) {
				value = this.getField();
				break;
			}
			if (Constants.SET_FIELD_FIELD_TYPE.equals(name)) {
				value = this.getFieldType();
			}
			break;
		}

		return value;
	}

	/**
	 * The field being updated
	 */
	private Field field;

	protected Field getField() {
		ObjectHelper.checkNotNull("field:field", field);
		return this.field;
	}

	public void setField(final Field field) {
		ObjectHelper.checkNotNull("parameter:field", field);
		this.field = field;
	}

	protected Type getFieldType() {
		return this.getField().getType();
	}
}
