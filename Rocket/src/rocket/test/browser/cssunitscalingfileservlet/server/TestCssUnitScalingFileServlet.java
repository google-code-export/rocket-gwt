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
package rocket.test.browser.cssunitscalingfileservlet.server;

import java.io.InputStream;

import rocket.server.browser.CssUnitScalingFileServlet;

/**
 * @author Miroslav Pokorny (mP)
 */
public class TestCssUnitScalingFileServlet extends CssUnitScalingFileServlet {

    protected InputStream locateFile( final String path ){
    	return this.getClass().getResourceAsStream( path );
    }

}
