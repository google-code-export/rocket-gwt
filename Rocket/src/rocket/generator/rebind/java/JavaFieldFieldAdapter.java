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
package rocket.generator.rebind.java;

import java.lang.reflect.Modifier;
import java.util.List;

import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.field.AbstractField;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * Provides an adapter between a java field and a Field.
 * 
 * @author Miroslav Pokorny
 */
public class JavaFieldFieldAdapter extends AbstractField {

	public Visibility getVisibility() {
		if (false == this.hasVisibility()) {
			this.setVisibility(this.createVisibility());
		}
		return super.getVisibility();
	}

	protected Visibility createVisibility() {
		return JavaAdapterHelper.getVisibility(this.getJavaField().getModifiers());
	}

	public String getJsniNotation() {
		throw new UnsupportedOperationException();
	}

	public String getName() {
		return this.getJavaField().getName();
	}

	public Type getType() {
		return this.getType(this.getJavaField().getType().getName());
	}

	public boolean isFinal() {
		return Modifier.isFinal(this.getJavaField().getModifiers());
	}

	public boolean isStatic() {
		return Modifier.isStatic(this.getJavaField().getModifiers());
	}

	public boolean isTransient() {
		return Modifier.isTransient(this.getJavaField().getModifiers());
	}

	public List getMetadataValues(String name) {
		return null;
	}

	private java.lang.reflect.Field javaField;

	public java.lang.reflect.Field getJavaField() {
		ObjectHelper.checkNotNull("field:javaField", javaField);
		return this.javaField;
	}

	public void setJavaField(final java.lang.reflect.Field javaField) {
		ObjectHelper.checkNotNull("parameter:javaField", javaField);
		this.javaField = javaField;
	}

	public String toString() {
		return "Field: " + this.javaField;
	}
}
