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
package rocket.beans.rebind.registerfactorybeans;

import java.io.InputStream;

import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.type.NewNestedType;
import rocket.util.client.Checker;

/**
 * An abstraction for the invoker add template
 * 
 * @author Miroslav Pokorny
 */
public class RegisterBeanTemplatedFile extends TemplatedFileCodeBlock {

	public RegisterBeanTemplatedFile() {
		super();
	}
	
	private String beanId;

	protected String getBeanId() {
		Checker.notEmpty("beanId:beanId", beanId);
		return this.beanId;
	}

	public void setBeanId(final String beanId) {
		Checker.notEmpty("parameter:beanId", beanId);
		this.beanId = beanId;
	}

	private NewNestedType factoryBean;

	protected NewNestedType getFactoryBean() {
		Checker.notNull("field:factoryBean", factoryBean);
		return this.factoryBean;
	}

	public void setFactoryBean(final NewNestedType factoryBean) {
		Checker.notNull("factoryBean:factoryBean", factoryBean);
		this.factoryBean = factoryBean;
	}

	protected String getResourceName() {
		return Constants.REGISTER_FACTORY_BEAN_TEMPLATE;
	}
	
	public InputStream getInputStream(){
		return super.getInputStream();
	}

	protected Object getValue0(final String name) {
		Object value = null;
		while (true) {
			if (Constants.REGISTER_FACTORY_BEAN_BEAN_ID.equals(name)) {
				value = new StringLiteral(this.getBeanId());
				break;
			}
			if (Constants.REGISTER_FACTORY_BEAN_FACTORY_BEAN.equals(name)) {
				value = this.getFactoryBean();
				break;
			}
			break;
		}
		return value;
	}
}
