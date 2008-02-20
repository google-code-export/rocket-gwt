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

import rocket.generator.rebind.packagee.AbstractPackage;
import rocket.generator.rebind.packagee.Package;
import rocket.util.client.ObjectHelper;

public class JavaPackagePackageAdapter extends AbstractPackage implements Package {

	public String getName() {
		return this.getJavaPackage().getName();
	}

	private java.lang.Package javaPackage;

	public java.lang.Package getJavaPackage() {
		ObjectHelper.checkNotNull("field:javaPackage", javaPackage);
		return this.javaPackage;
	}

	public void setJavaPackage(final java.lang.Package javaPackage) {
		ObjectHelper.checkNotNull("parameter:javaPackage", javaPackage);
		this.javaPackage = javaPackage;
	}

	public String toString() {
		return "Package: " + this.javaPackage;
	}
}
