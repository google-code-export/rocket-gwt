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
package rocket.remoting.server;

class Constants {
	static final String COMET_SERVER_RESPONSE_CONTENT_TYPE = "text/html; charset=utf-8";

	final static String MAXIMUM_BYTES_WRITTEN_INIT_PARAMETER = "maximum-bytes-written";

	final static String CONNECTION_TIME_OUT_INIT_PARAMETER = "connection-timeout";

	final static String DOCUMENT_START_HTML = "<html>\n<head>\n<script>\nwindow.parent.__cometOnConnect();\n</script>\n";

	final static String DOCUMENT_END_HTML = "</head>\n</html>\n";
}
