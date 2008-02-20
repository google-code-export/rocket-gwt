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
package rocket.generator.rebind.gwt;

import java.util.List;

import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.field.AbstractField;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;
import rocket.util.client.SystemHelper;

import com.google.gwt.core.ext.typeinfo.JField;

/**
 * Acts as an adapter between a JField and a Field
 * 
 * @author Miroslav Pokorny
 */
public class JFieldFieldAdapter extends AbstractField {

	public String getName() {
		return this.getJField().getName();
	}

	public String getJsniNotation() {
		final StringBuilder jsni = new StringBuilder();
		jsni.append('@');
		jsni.append(this.getEnclosingType().getName());
		jsni.append("::");
		jsni.append(this.getName());
		return jsni.toString();
	}

	public Visibility getVisibility() {
		if (false == this.hasVisibility()) {
			this.setVisibility(this.createVisibility());
		}
		return super.getVisibility();
	}

	protected Visibility createVisibility() {
		Visibility visibility = null;
		while (true) {
			final JField field = this.getJField();
			if (field.isPrivate()) {
				visibility = Visibility.PRIVATE;
				break;
			}
			if (field.isDefaultAccess()) {
				visibility = Visibility.PACKAGE_PRIVATE;
				break;
			}
			if (field.isProtected()) {
				visibility = Visibility.PROTECTED;
				break;
			}
			if (field.isPublic()) {
				visibility = Visibility.PUBLIC;
				break;
			}
			SystemHelper.fail("Unknown visibility for field " + field);
		}
		return visibility;
	}

	public Type getType() {
		final String fieldTypeName = this.getJField().getType().getQualifiedSourceName();
		return this.findType(fieldTypeName);
	}

	public boolean isFinal() {
		return this.getJField().isFinal();
	}

	public boolean isStatic() {
		return this.getJField().isStatic();
	}

	public boolean isTransient() {
		return this.getJField().isTransient();
	}

	public List getMetadataValues(String name) {
		return this.getAnnotationValues(this.getJField(), name);
	}

	/**
	 * The jfield which provides all type field info.
	 */
	private JField jField;

	protected JField getJField() {
		ObjectHelper.checkNotNull("field:jField", jField);
		return jField;
	}

	public void setJField(final JField jField) {
		ObjectHelper.checkNotNull("parameter:jField", jField);
		this.jField = jField;
	}

	public String toString() {
		return "Field: " + this.jField + ", enclosingType: " + this.jField.getEnclosingType();
	}
}
