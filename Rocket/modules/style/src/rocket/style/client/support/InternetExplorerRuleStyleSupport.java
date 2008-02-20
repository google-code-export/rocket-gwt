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
package rocket.style.client.support;

import rocket.util.client.ObjectHelper;

import com.google.gwt.core.client.JavaScriptObject;

public class InternetExplorerRuleStyleSupport extends StyleSupport{

	protected String getString(final JavaScriptObject rule, String name){
		return this.getProperty( rule, name);
	}
	
	protected String getCssText( final JavaScriptObject rule ){
		return this.buildCssText( rule );
	}
	
	protected String getUserSelect( final JavaScriptObject rule ){
		throw new UnsupportedOperationException("getUserSelect");
	}
	
	protected String getUserSelectPropertyName(){
		throw new UnsupportedOperationException("getUserSelectPropertyName");
	}
	
	protected void setUserSelect( final JavaScriptObject rule, final String value ){
		throw new UnsupportedOperationException("setUserSelect");
	}
	
	protected void setString( final JavaScriptObject rule, final String name, final String value) {
		this.setProperty( rule, name, value);
	}
	
	protected void remove0( final JavaScriptObject rule, final String name ){
		this.setProperty( rule, name, "");
	}
	
	public String[] getPropertyNames( final JavaScriptObject element ){
		return ObjectHelper.getPropertyNames( ObjectHelper.getObject( element, "style" ));
	}
}
