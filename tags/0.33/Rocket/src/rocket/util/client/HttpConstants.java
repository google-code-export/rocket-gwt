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
package rocket.util.client;

public class HttpConstants {

    public final static String HTTP = "http://";

    public final static String HTTPS = "https://";

    public final static String GET = "GET";

    public final static String POST = "POST";

    public final static int PORT_NOT_SET = -1;

    public final static int UNSECURED_PORT = 80;

    public final static int SSL_PORT = 443;

    public final static char HOST_PORT_SEPARATOR = ':';

    public final static char HOST_OR_PORT_PATH_SEPARATOR = '/';

    public final static char PATH_SEPARATOR = '/';

    public final static char QUERY_STRING = '?';

    public final static char ANCHOR = '#';

    public final static char QUERY_PARAMETER_SEPARATOR = '&';

    public final static char QUERY_PARAMETER_NAME_VALUE_SEPARATOR = '=';

    public final static String QUERY_PARAMETER_SEPARATOR_STRING = "" + QUERY_PARAMETER_SEPARATOR;

    public final static String HEADER_NAME_VALUE_SEPARATOR = ": ";

    public final static String CONTENT_TYPE_HEADER = "Content-type";

    public final static String REFERER_HEADER = "Referer";

    public final static String HOST_HEADER = "Host";

    public final static String LOCATION_HEADER = "Location";

    public final static String CHARACTER_ENCODING = "Character-encoding";

    public final static String HTML_MIME_TYPE = "text/html";

    protected HttpConstants() {
    }
}
