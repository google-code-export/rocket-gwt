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
package rocket.logging.test.serverlogger.server;

import java.net.URL;

import javax.servlet.ServletException;

import org.apache.log4j.PropertyConfigurator;

import rocket.logging.server.Log4jLoggingService;

/**
 * This test server initializers log4j from a properties file in the same
 * package. It also provides an adapter between the rocket Logger and log4j
 * Logger interfaces.
 * 
 * @author admin
 * 
 */
public class TestLoggingServerService extends Log4jLoggingService {

	public void init() throws ServletException {
		final String resource = "/rocket/logging/test/serverlogger/server/log4j.properties";
		final URL configFileResource = this.getClass().getResource(resource);
		PropertyConfigurator.configure(configFileResource);
	}
}
