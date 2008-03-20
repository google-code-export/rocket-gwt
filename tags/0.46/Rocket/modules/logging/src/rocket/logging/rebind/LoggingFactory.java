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
package rocket.logging.rebind;

import java.util.Map;

public class LoggingFactory {

	public Logger getLogger(final String category) {
		Logger logger = null;

		String finding = category;
		final Map mappings = this.getMappings();
		while (true) {
			logger = (Logger) mappings.get(finding);
			if (null != logger) {
				break;
			}

			final int dot = finding.lastIndexOf('.');
			if (-1 == dot) {
				break;
			}
			finding = finding.substring(0, dot - 1);
		}

		return logger;
	}

	private Map mappings;

	protected Map getMappings() {
		return this.mappings;
	}

	protected void setMappings(final Map mappings) {
		this.mappings = mappings;
	}
}
