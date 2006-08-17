/*
 * Copyright 2006 NSW Police Government Australia
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
package rocket.server.browser;

/**
 * Repository of constants used throughout the browser package.
 * @author Miroslav Pokorny (mP)
 */
public class BrowserConstants {

    static String SCALING_FACTOR_PARAMETER_NAME = "scaling-factor-parameter-name";
    static String CSS_FILENAME_PARAMETER_NAME = "filename-parameter-name";

    static String PIXEL_UNIT = "px";
    static String IMPORT_DIRECTIVE = "@import";
    static String SCALABLE_CSS_PROPERTIES = "scalable-css-properties";

	static String USER_AGENT = "User-agent";

	static String FIREFOX_1_X = "Firefox/1.";

	static String INTERNET_EXPLORER_6 = "MSIE 6.0";

	static String INTERNET_EXPLORER_5 = "MSIE 5.0";

	final static char[] START_SCRIPT = "script".toCharArray();

	final static char[] END_SCRIPT = "/script".toCharArray();

}