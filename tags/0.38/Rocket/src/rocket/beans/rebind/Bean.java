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
package rocket.beans.rebind;

import java.util.ArrayList;
import java.util.List;

import rocket.beans.rebind.aop.Advice;
import rocket.generator.rebind.type.NewNestedType;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

/**
 * Holds a number of properties related to constructing a new type
 * 
 * @author Miroslav Pokorny
 */
public class Bean{

	public Bean() {
		super();

		this.setAdvices(this.createAdvisors());
	}

	private String id;

	public String getId() {
		StringHelper.checkNotEmpty("field:id", id);
		return this.id;
	}

	public void setId(final String id) {
		StringHelper.checkNotEmpty("parameter:id", id);
		this.id = id;
	}

	/**
	 * The factory type being created
	 */
	private NewNestedType factoryBean;

	public NewNestedType getFactoryBean() {
		ObjectHelper.checkNotNull("field:factoryBean", factoryBean);
		return this.factoryBean;
	}

	public void setFactoryBean(final NewNestedType factoryBean) {
		ObjectHelper.checkNotNull("factoryBean:factoryBean", factoryBean);
		this.factoryBean = factoryBean;
	}

	/**
	 * The type of the bean
	 */
	private Type type;

	public Type getType() {
		ObjectHelper.checkNotNull("field:type", type);
		return this.type;
	}

	public void setType(final Type type) {
		ObjectHelper.checkNotNull("type:type", type);
		this.type = type;
	}

	/**
	 * A list of advices that apply to this bean.
	 */
	private List advices;

	public List getAdvices() {
		ObjectHelper.checkNotNull("field:advices", advices);
		return this.advices;
	}

	protected void setAdvices(final List advices) {
		ObjectHelper.checkNotNull("parameter:advices", advices);
		this.advices = advices;
	}

	protected List createAdvisors() {
		return new ArrayList();
	}

	public void addAdvice(final Advice advice) {
		ObjectHelper.checkNotNull("parameter:advice", advice);

		this.getAdvices().add(advice);
	}

	/**
	 * Contains the generated proxy for this bean
	 */
	private NewNestedType proxy;

	public NewNestedType getProxy() {
		ObjectHelper.checkNotNull("field:proxy", proxy);
		return this.proxy;
	}

	public boolean hasProxy() {
		return null != proxy;
	}

	public void setProxy(final NewNestedType proxy) {
		ObjectHelper.checkNotNull("proxy:proxy", proxy);
		this.proxy = proxy;
	}

	/**
	 * If a proxy has been generated a proxy factory bean will also exist
	 */
	private NewNestedType proxyFactoryBean;

	public NewNestedType getProxyFactoryBean() {
		ObjectHelper.checkNotNull("field:proxyFactoryBean", proxyFactoryBean);
		return this.proxyFactoryBean;
	}

	public void setProxyFactoryBean(final NewNestedType proxyFactoryBean) {
		ObjectHelper.checkNotNull("proxyFactoryBean:proxyFactoryBean", proxyFactoryBean);
		this.proxyFactoryBean = proxyFactoryBean;
	}

	public String toString() {
		return "bean id[" + this.id + "] type[" + this.type + "]";
	}
}
