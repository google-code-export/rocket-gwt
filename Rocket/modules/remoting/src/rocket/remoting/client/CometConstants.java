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

public class CometConstants {
	public static final int TERMINATE_COMET_SESSION = 0;
	public static final int OBJECT_PAYLOAD = TERMINATE_COMET_SESSION + 1;
	public static final int EXCEPTION_PAYLOAD = OBJECT_PAYLOAD + 1;
}
