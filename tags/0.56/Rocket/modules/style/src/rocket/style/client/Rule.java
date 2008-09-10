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
package rocket.style.client;

import rocket.util.client.JavaScript;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Each instance of this class represents a single Rule belonging to a StyleSheet
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Rule extends JavaScriptObject{

	protected Rule(){
		super();
	}
	
	final public String getSelector(){
		return JavaScript.getString(this, Constants.SELECTOR_TEXT_PROPERTY_NAME);
	}	
	
	final public RuleStyle getRuleStyle(){
		return RuleStyle.getRule( this );
	}
}
