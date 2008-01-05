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

import rocket.util.client.JavaScript;

import com.google.gwt.core.client.JavaScriptObject;

public class FireFoxInlineStyleSupport extends FireFoxStyleSupport {
	
	protected String getUserSelect( final JavaScriptObject element ){
		return this.getUserSelectProperty(element);
	}
	
	protected String getString( final JavaScriptObject element, final String name ){
		return getProperty( element, name );
	}

	protected void setUserSelect( final JavaScriptObject element, final String value ){
		this.setString( element, this.getUserSelectPropertyName(), value);
	}
	
	protected void setString( final JavaScriptObject element, final String name, final String value ){
		this.setProperty( element, name, value);
	}
	
	protected void remove0( final JavaScriptObject element, final String name ){
		this.removeProperty( element, name );
	}
	
	public String[] getPropertyNames( final JavaScriptObject element ){
		return JavaScript.getPropertyNames( JavaScript.getObject( element, "style" ));
	}
}
