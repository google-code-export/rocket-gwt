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

import rocket.style.client.StyleConstants;
import rocket.util.client.ObjectHelper;

import com.google.gwt.core.client.JavaScriptObject;

public class SafariComputedStyleSupport extends SafariStyleSupport {

	public String get(final JavaScriptObject element, final String name) {
		String value = null;
		while (true) {	
			if (StyleConstants.FONT_WEIGHT.equals( name)) {
				value = "" + this.getComputedFontWeight( ObjectHelper.castToElement( element));
				break;
			}
			value = super.get(element, name);
			break;
		}

		return value;
	}
	
	protected String getUserSelect( final JavaScriptObject element ){
		return this.getUserSelectProperty(element);
	}
	
	protected String getString( final JavaScriptObject element, final String name ){
		return this.getComputed(element, name);
	}
	

	protected void setUserSelect( final JavaScriptObject source, final String value ){
		throw new UnsupportedOperationException("setUserSelect");
	}
	
	protected void setString( JavaScriptObject element, final String name, final String value ){
		throw new UnsupportedOperationException( "setString");
	}
	
	protected void remove0( JavaScriptObject element, final String name ){
		throw new UnsupportedOperationException("remove0");
	}
	
	public String[] getPropertyNames( final JavaScriptObject element ){
		return this.getPropertyNamesFromCssText(element);
	}
}
