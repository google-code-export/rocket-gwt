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

import rocket.beans.rebind.BeanFactoryGeneratorContext;
import rocket.beans.rebind.bean.Bean;
import rocket.util.client.StringHelper;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * This class generates code that calls a factory method to provide a new bean instance
 * @author Miroslav Pokorny
 */
public class FactoryMethod extends NewInstanceProvider {

	protected void write0(final SourceWriter writer) {
		this.checkMethod();		
		
		final BeanFactoryGeneratorContext context = this.getBeanFactoryGeneratorContext();
		final String factoryBeanId = this.getId();
		final Bean factoryBean = context.getBean( factoryBeanId );
		final String factoryBeanTypeName = factoryBean.getTypeName();
		
		final String factoryBeanVariable = "factoryBean";		
		
		writer.println( "final " + factoryBeanTypeName + " " + factoryBeanVariable + " = (" + factoryBeanTypeName + ") this.getBeanFactory().getBean( \"" + factoryBeanId + "\");");
		writer.println( "return " + factoryBeanVariable + "." + this.getMethodName() + "();");
	}
	
	protected void checkMethod(){
		final BeanFactoryGeneratorContext context = this.getBeanFactoryGeneratorContext();
		final String id = this.getId();
		final Bean factoryBean = context.getBean( id );
		if (null == factoryBean) {
			throwFactoryMethodNotFoundException0("Unable to find factory bean [" + id + "]");
		}		
		final JClassType type = (JClassType)context.getType( factoryBean.getTypeName() );
		final String methodName = this.getMethodName();
		
		final JMethod method = type.findMethod(methodName, new JType[0]);
		if (null == method) {
			throwFactoryMethodNotFoundException0("Unable to find factory method [" + methodName + "]");
		}
		if (false == method.isPublic()) {
			throwFactoryMethodNotFoundException("must be public");
		}
		if (method.isStatic()) {
			throwFactoryMethodNotFoundException("must not be static");
		}
		if (method.isAbstract()) {
			throwFactoryMethodNotFoundException("must not be abstract");
		}
		if( method.getParameters().length != 0 ){
			throwFactoryMethodNotFoundException("must have no parameters");
		}
	}

	protected void throwFactoryMethodNotFoundException(final String message) {
		throw new FactoryMethodNotFoundException("The factory method [" + this.getMethodName() + "]" + message + " on " + this.getBean().getTypeName());
	}

	protected void throwFactoryMethodNotFoundException0(final String message) {
		throw new FactoryMethodNotFoundException(message + " on " + this.getBean().getTypeName());
	}
	
	/**
	 * The name of an factory method that will create a new bean instance
	 */
	private String methodName;

	protected String getMethodName() {
		StringHelper.checkNotEmpty("field:methodName", methodName);
		return methodName;
	}

	public void setMethodName(final String methodName) {
		StringHelper.checkNotEmpty("parameter:methodName", methodName);
		this.methodName = methodName;
	}
	
	/**
	 * The id of the factory bean
	 */
	private String id;

	public String getId() {
		StringHelper.checkNotEmpty("field:id", id);
		return id;
	}

	public void setId(final String id) {
		StringHelper.checkNotEmpty("parameter:id", id);
		this.id = id;
	}

	public String toString(){
		return super.toString() + ", id[" + id + "] methodName[" + methodName + "]";
	}
}
