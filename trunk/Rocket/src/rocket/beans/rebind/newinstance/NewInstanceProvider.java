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
package rocket.beans.rebind.newinstance;

import rocket.beans.rebind.HasBeanFactoryGeneratorContext;
import rocket.beans.rebind.bean.Bean;
import rocket.generator.rebind.CodeGenerator;
import rocket.util.client.ObjectHelper;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * A new instance provider is responsible for generating code that will create a new bean instance.
 * @author Miroslav Pokorny
 */
abstract public class NewInstanceProvider extends HasBeanFactoryGeneratorContext implements CodeGenerator{
	
	public void write( final SourceWriter writer ){
		writer.println("protected Object createInstance(){");
		writer.indent();

		write0(writer);

		writer.outdent();
		writer.println("}");		
	}
	
	abstract void write0( SourceWriter writer );
	
	/**
	 * The bean which the new instance provider will create a new instance of via generated code.
	 */
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
