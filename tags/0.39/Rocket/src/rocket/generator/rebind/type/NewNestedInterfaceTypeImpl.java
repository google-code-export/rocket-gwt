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
package rocket.generator.rebind.type;

import rocket.generator.rebind.SourceWriter;

/**
 * Represents a inner class being built.
 * 
 * @author Miroslav Pokorny
 */
public class NewNestedInterfaceTypeImpl extends NewNestedTypeOrInterface implements NewNestedInterfaceType {

	public NewNestedInterfaceTypeImpl() {
		super();
	}

	public boolean isInterface() {
		return true;
	}

	public void setInterface(final boolean interfacee) {
		throw new UnsupportedOperationException("setInterface");
	}
	
	/**
	 * Interfaces cant have constructors so theres nothing to write skip the constructor header etc...
	 */
	protected void writeConstructors(final SourceWriter writer) {		
	}
}