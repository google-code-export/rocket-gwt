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
package rocket.remoting.rebind.json;

import rocket.generator.rebind.GeneratorContext;
import rocket.remoting.client.json.RemoteJsonService;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * A specialised context that relates to the generation of clients for remote
 * json services.
 * 
 * @author Miroslav Pokorny
 */
public class RemoteJsonServiceGeneratorContext extends GeneratorContext {

	protected String getGeneratedClassNameSuffix() {
		return Constants.CLIENT_SUFFIX;
	}

	public JClassType getRemoteJsonService() {
		return (JClassType) this.getType(RemoteJsonService.class.getName());
	}

	public JClassType getAsyncCallbackType() {
		return (JClassType) this.getType(AsyncCallback.class.getName());
	}
}
