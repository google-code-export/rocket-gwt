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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * This new instance creates a new instance using deferred binding {@link GWT.create}
 * @author Miroslav Pokorny
 */
public class DeferredBindingNewInstance extends NewInstanceProvider {
	
	protected void write0(final SourceWriter writer) {
		final StringBuilder statement = new StringBuilder();
		statement.append("return ");
		statement.append(GWT.class.getName());
		statement.append(".create(");
		statement.append(this.getBean().getTypeName() );
		statement.append(".class );");
		writer.println(statement.toString());
	}
}
