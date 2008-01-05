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

import rocket.generator.rebind.packagee.AbstractPackage;
import rocket.util.client.Checker;

import com.google.gwt.core.ext.typeinfo.JPackage;

/**
 * Provides a adapter between a JPackage and Package
 * 
 * @author Miroslav Pokorny
 */
public class JPackagePackageAdapter extends AbstractPackage {

	public String getName() {
		return this.getJPackage().getName();
	}

	private JPackage jPackage;

	protected JPackage getJPackage() {
		Checker.notNull("field:jPackage", jPackage);
		return this.jPackage;
	}

	public void setJPackage(final JPackage jPackage) {
		Checker.notNull("parameter:jPackage", jPackage);
		this.jPackage = jPackage;
	}

	public String toString() {
		return "Package: " + this.jPackage;
	}
}
