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
package rocket.remoting.client.json;

/**
 * A collection of constants for this package.
 * 
 * @author Miroslav Pokorny
 */
class Constants {
	final static int HTTP_RESPONSE_OK = 200;

	final static String CONTENT_TYPE_HEADER = "Content-Type";

	final static String GET_CONTENT_TYPE = "text/plain; charset=utf-8";

	final static String POST_CONTENT_TYPE = "application/x-www-form-urlencoded";

	final static String CONTENT_LENGTH_HEADER = "Content-Length";
}
