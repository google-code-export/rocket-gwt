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
package rocket.beans.rebind.values;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.core.ext.typeinfo.JType;

import rocket.beans.client.ListBuilder;

/**
 * A container that collects values for a list.
 * 
 * @author Miroslav Pokorny
 */
public class ListValue extends CollectionValue {

	protected Collection createCollection() {
		return new ArrayList();
	}

	/**
	 * If the property is a not List report false
	 * 
	 * @return
	 */
	public boolean isCompatibleWith() {
		return this.getType().getQualifiedSourceName().equals(List.class.getName());
	}

	protected String getBuilderTypeName() {
		return ListBuilder.class.getName();
	}

	protected String getCollectionGetterName() {
		return "getList";
	}
}
