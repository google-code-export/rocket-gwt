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
package rocket.beans.rebind.init;

import rocket.beans.client.BeanFactoryImpl;
import rocket.beans.rebind.HasBeanFactoryGeneratorContext;
import rocket.beans.rebind.bean.Bean;
import rocket.generator.rebind.CodeGenerator;
import rocket.util.client.ObjectHelper;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * A place holder for an init method for a bean
 * @author Miroslav Pokorny
 */
public class InitMethod extends HasBeanFactoryGeneratorContext  implements CodeGenerator{

	/**
	 * Does nothing leaving the default {@link BeanFactoryImpl#satisfyProperties} which tests if the new instance implements
	 * InitializingBean.
	 * @param writer
	 */
	public void write( final SourceWriter writer ){		
	}
	
	private Bean bean;
	
	protected Bean getBean(){
		ObjectHelper.checkNotNull("field:bean", bean );
		return this.bean;
	}

	public void setBean(final Bean bean){
		ObjectHelper.checkNotNull("parameter:bean", bean );
		this.bean = bean;
	}
}
