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
package rocket.remoting.client;

import rocket.json.client.JsonSerializable;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * A maker interface that marks a interface as being a JsonRpcService.
 * 
 * Many of the steps involved in creating a JsonRpcService are similar to those
 * required to create a GWT JsonRpcService.
 * <ul>
 * <li>A service interface which must extends {@link JsonRpcService}.</li>
 * <li>The return type of the service interface and all types referenced must
 * implement {@link JsonSerializable}</li>
 * <li>An async interface following similar rules to that of
 * {@link JsonRpcService}</li>
 * <li>All async methods must return void</li>
 * <li>All methods from the service interface must also appear in the async
 * interface, same name, same arguments plus a trailing {@link AsyncCallback}
 * parameter</li>
 * <li>The service address must be set after casting to
 * {@link ServiceDefTarget} and calling {@link ServiceDefTarget#setServiceEntry}
 * </ul>
 * 
 * @author Miroslav Pokorny
 */
public interface JsonRpcService {

}
