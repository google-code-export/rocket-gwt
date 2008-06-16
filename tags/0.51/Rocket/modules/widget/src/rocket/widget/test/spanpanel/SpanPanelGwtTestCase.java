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
package rocket.widget.test.spanpanel;

import rocket.widget.client.Panel;
import rocket.widget.client.SpanPanel;
import rocket.widget.test.divpanel.DivPanelGwtTestCase;


public class SpanPanelGwtTestCase extends DivPanelGwtTestCase {

	@Override
	public String getModuleName() {
		return "rocket.widget.test.spanpanel.SpanPanelGwtTestCase";
	}
	
	@Override
	protected Panel createPanel(){
		return new SpanPanel();
	}
	
	@Override
	protected String outterElementTagName(){
		return "span";
	}
	
	@Override
	protected String innerElementTagName(){
		return "span";
	}
}
