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
package rocket.generator.rebind.util;

import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * Adds visibility and enclosing type properties to AbstractClassComponent.
 * These two properties are shared by Constructors, Methods and Fields
 * 
 * @author Miroslav Pokorny
 */
abstract public class AbstractConstructorMethodOrField extends AbstractClassComponent {
	/**
	 * The visibility of this field
	 */
	private Visibility visibility;

	public Visibility getVisibility() {
		ObjectHelper.checkNotNull("field:visibility", visibility);
		return this.visibility;
	}

	protected boolean hasVisibility() {
		return this.visibility != null;
	}

	protected void setVisibility(final Visibility visibility) {
		ObjectHelper.checkNotNull("parameter:visibility", visibility);
		this.visibility = visibility;
	}

	/**
	 * The type that this constructor, method or field belongs too.
	 */
	private Type enclosingType;

	public Type getEnclosingType() {
		ObjectHelper.checkNotNull("field:enclosingType", enclosingType);
		return enclosingType;
	}

	protected boolean hasEnclosingType() {
		return null != this.enclosingType;
	}

	public void setEnclosingType(final Type enclosingType) {
		ObjectHelper.checkNotNull("parameter:enclosingType", enclosingType);
		this.enclosingType = enclosingType;
	}
}
