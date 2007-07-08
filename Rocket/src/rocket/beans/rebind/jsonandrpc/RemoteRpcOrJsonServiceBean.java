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

import rocket.beans.rebind.bean.Bean;
import rocket.beans.rebind.property.Property;
import rocket.beans.rebind.property.PropertyNotFoundException;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Common base class for both {@link RemoteJsonServiceBean } and
 * {@link RemoteRpcServiceBean}
 * 
 * @author Miroslav Pokorny
 */
abstract public class RemoteRpcOrJsonServiceBean extends Bean {

	public void addProperty(final Property property) {
		final String name = property.getName();
		if (false == name.equals(this.getAddressPropertyName())) {
			this.throwUnknownProperty(name);
		}
		this.getProperties().put(name, property);
	}

	abstract String getAddressPropertyName();

	protected void throwUnknownProperty(final String property) {
		throw new PropertyNotFoundException("Property not found [" + property + "]");
	}

	abstract protected String getFactoryBeanSuperClass();


	protected void checkType0( final JClassType type ){
		if (null == type) {
			this.throwBeanTypeNotFoundException();
		}
	}
	
	protected void writeBeanFactorySatisfyProperty0(final SourceWriter writer) {
		final String instance = "instance";
		final String instance0 = instance + '0';

		// cast instance to its type.
		final String type = ServiceDefTarget.class.getName();
		writer.println("final " + type + " " + instance0 + " = (" + type + ") " + instance + ";");

		writer.println(instance0 + ".setServiceEntryPoint(" + this.getAddress() + ");");
	}

	abstract protected String getAddress();
}