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
package rocket.beans.test.beans.client.factorybeanbeanreference;

import rocket.beans.client.SingletonFactoryBean;
/**
 * A simple factory bean that produces rocket.beans.test.beans.client.beanreferencewithfactorybean.Bean's
 * @author Miroslav Pokorny
 * @factoryBean-objectType rocket.beans.test.beans.client.factorybeanbeanreference.FactoryBeanProducedBean
 */
public class FactoryBeanImpl extends SingletonFactoryBean {

	protected Object createInstance() throws Exception {
		return new FactoryBeanProducedBean();
	}

}
