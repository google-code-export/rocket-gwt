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
package rocket.cookie.client;

import java.util.Date;

/**
 * A collection of constants directly related to this package.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class CookieConstants {
	public final static char SEPARATOR = ';';

	public final static String SEPARATOR_STRING = "" + SEPARATOR;

	public final static char NAME_VALUE_SEPARATOR = '=';

	public final static String COMMENT = "; comment=";

	public final static String DOMAIN = "; domain=";

	public final static String EXPIRES = "; expires=";

	public final static String PATH = "; path=";

	public final static String SECURE = "; secure";

	public final static String VERSION = "; version=";

	public final static String REMOVE_SUFFIX = "=; expires=" + new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000).toGMTString();
}
