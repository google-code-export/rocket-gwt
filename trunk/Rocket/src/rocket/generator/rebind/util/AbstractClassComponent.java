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

import java.util.ArrayList;
import java.util.List;

import rocket.generator.rebind.ClassComponent;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

import com.google.gwt.core.ext.typeinfo.HasMetaData;

/**
 * A convenient base for any compilation component including types,
 * constructors, methods, fields
 * 
 * @author Miroslav Pokorny
 */
public class AbstractClassComponent implements ClassComponent {

	final protected Type findType(final String name) {
		return this.getGeneratorContext().findType(name);
	}

	final protected Type getType(final String name) {
		return this.getGeneratorContext().getType(name);
	}

	private GeneratorContext generatorContext;

	public GeneratorContext getGeneratorContext() {
		ObjectHelper.checkNotNull("field:generatorContext", generatorContext);
		return this.generatorContext;
	}

	public void setGeneratorContext(final GeneratorContext generatorContext) {
		ObjectHelper.checkNotNull("parameter:generatorContext", generatorContext);
		this.generatorContext = generatorContext;
	}

	/**
	 * Returns a read only list view for a list of annotated values from the
	 * given source.
	 * 
	 * @param source
	 * @param annotationName
	 * @return
	 */
	protected List getAnnotationValues(final HasMetaData source, final String annotationName) {
		final String[][] values = source.getMetaData(annotationName);

		final List list = new ArrayList();
		if (null != values) {
			for (int i = 0; i < values.length; i++) {
				list.add(values[i][0]);
			}
		}

		return list;
	}
}
