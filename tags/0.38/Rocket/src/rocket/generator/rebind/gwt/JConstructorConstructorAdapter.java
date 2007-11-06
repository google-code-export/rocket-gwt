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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.constructor.AbstractConstructor;
import rocket.generator.rebind.constructorparameter.ConstructorParameter;
import rocket.util.client.ObjectHelper;
import rocket.util.client.SystemHelper;

import com.google.gwt.core.ext.typeinfo.JConstructor;
import com.google.gwt.core.ext.typeinfo.JParameter;

/**
 * Provides a Constructor view of a JConstructor
 * 
 * @author Miroslav Pokorny
 */
public class JConstructorConstructorAdapter extends AbstractConstructor {

	public Visibility getVisibility() {
		if (false == this.hasVisibility()) {
			this.setVisibility(this.createVisibility());
		}
		return super.getVisibility();
	}

	protected Visibility createVisibility() {
		Visibility visibility = null;
		while (true) {
			final JConstructor method = this.getJConstructor();
			if (method.isPrivate()) {
				visibility = Visibility.PRIVATE;
				break;
			}
			if (method.isDefaultAccess()) {
				visibility = Visibility.PACKAGE_PRIVATE;
				break;
			}
			if (method.isProtected()) {
				visibility = Visibility.PROTECTED;
				break;
			}
			if (method.isPublic()) {
				visibility = Visibility.PUBLIC;
				break;
			}
			SystemHelper.fail("Unknown visibility for field " + method);
		}
		return visibility;
	}

	protected List createParameters() {
		final List list = new ArrayList();

		final JParameter[] parameters = this.getJConstructor().getParameters();
		for (int i = 0; i < parameters.length; i++) {
			list.add(this.createParameter(parameters[i]));
		}

		return list;
	}

	/**
	 * Factory parameter which creates a
	 * {@link JParameterConstructorParameterAdapter} from a {@link JParameter}.
	 * 
	 * @param parameter
	 *            The source JParameter
	 * @return
	 */
	protected ConstructorParameter createParameter(final JParameter parameter) {
		final JParameterConstructorParameterAdapter adapter = new JParameterConstructorParameterAdapter();
		adapter.setGeneratorContext(this.getGeneratorContext());
		adapter.setEnclosingConstructor(this);
		adapter.setJParameter(parameter);
		return adapter;
	}

	protected Set createThrownTypes() {
		return TypeOracleAdaptersHelper.asSetOfTypes(this.getGeneratorContext(), this.getJConstructor().getThrows());
	}

	private JConstructor jConstructor;

	protected JConstructor getJConstructor() {
		ObjectHelper.checkNotNull("field:jConstructor", jConstructor);
		return jConstructor;
	}

	public void setJConstructor(final JConstructor jConstructor) {
		ObjectHelper.checkNotNull("parameter:jConstructor", jConstructor);
		this.jConstructor = jConstructor;
	}

	public String toString() {
		return "Constructor: " + this.jConstructor;
	}
}
