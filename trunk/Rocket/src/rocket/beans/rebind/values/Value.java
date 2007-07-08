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

import rocket.beans.rebind.BeanFactoryGeneratorContext;
import rocket.beans.rebind.HasBeanFactoryGeneratorContext;
import rocket.util.client.ObjectHelper;

import com.google.gwt.core.ext.typeinfo.JType;

/**
 * Holds a property value for a bean property
 * 
 * @author Miroslav Pokorny
 */
abstract public class Value extends HasBeanFactoryGeneratorContext{

	/**
	 * Tests if this property value entry is compatible with its field type.
	 * 
	 * @return
	 */
	abstract public boolean isCompatibleWith();

	/**
	 * Asks the Value to generate peice of code that can be
	 * used to set a property on a bean.
	 * 
	 * @return
	 */
	abstract public String generateValue();

	private JType type;
	
	protected JType getType(){
		ObjectHelper.checkNotNull("field:type", type );
		return this.type;
	}
	
	public void setType( final JType type ){
		ObjectHelper.checkNotNull("parameter:type", type );
		this.type = type;
	}
}
