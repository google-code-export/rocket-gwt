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
package rocket.beans.test.generator.beanreference.productofnestedfactorybean;

import rocket.beans.client.FactoryBean;
import rocket.beans.test.generator.beanreference.Bean;
import rocket.util.client.Checker;

public class HasProductOfNestedFactoryBean{
	
	private Bean bean;
	
	public Bean getBean(){
		return this.bean;
	}

	public void setBean( final Bean bean){
		this.bean = bean;
	}
}