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
package rocket.beans.rebind.jsonandrpc;

import rocket.beans.client.RemoteRpcOrJsonServiceFactoryBean;
import rocket.beans.rebind.config.Constants;
import rocket.beans.rebind.property.Property;
import rocket.beans.rebind.values.StringValue;
import rocket.browser.client.BrowserHelper;

/**
 * A special type of Bean that defines a {@link RemoteJsonService}
 * 
 * @author Miroslav Pokorny
 */
public class RemoteJsonServiceBean extends RemoteRpcOrJsonServiceBean {
	protected String getAddressPropertyName() {
		return Constants.REMOTE_JSON_SERVICE_ADDRESS;
	}

	protected String getFactoryBeanSuperClass() {
		return RemoteRpcOrJsonServiceFactoryBean.class.getName();
	}

	protected String getAddress() {
		final Property property = (Property) this.getProperties().get(this.getAddressPropertyName());
		final StringValue address = (StringValue) property.getValue();
		return address.generateValue();
	}
}
