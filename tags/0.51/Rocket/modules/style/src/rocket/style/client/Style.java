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

import rocket.util.client.Colour;
import rocket.util.client.Tester;

import com.google.gwt.core.client.JavaScriptObject;


/**
 * A private base class for the various style flavours inline, computed and rule.
 * 
 * @author Miroslav Pokorny (mP)
 */
abstract class Style extends JavaScriptObject{

	protected Style(){
		super();
	}
	
	static Colour getColour0( final String string ){
		Colour colour = null;
		
		if (false == Tester.isNullOrEmpty(string)) {
			colour = Colour.parse(string);
		}
		
		return colour;
	}
		
	static int getInteger0( final String string, final CssUnit unit, final int defaultValue ){
		int value = defaultValue;
		
		if (false == Tester.isNullOrEmpty(string)) {
			value = (int) unit.convert(string);
		}
		
		return value;
	}
		
	static double getDouble0( final String string, final CssUnit unit, final double defaultValue ){
		double value = defaultValue;
		
		if (false == Tester.isNullOrEmpty(string)) {
			value = unit.convert(string);
		}
		
		return value;
	}
		
	static String getUrl0( final String string ){
		String url = string;
		if (false == Tester.isNullOrEmpty(url)) {
				int first = "url(".length();
				int last = url.length() - 1 - 1;
				if (url.charAt(first) == '\'') {
					first++;
				}
				if (url.charAt(first) == '"') {
					first++;
				}
				if (url.charAt(last) == '\'') {
					last--;
				}
				if (url.charAt(last) == '"') {
					last--;
				}
				url = url.substring(first, last + 1);
		}
		return url;		
	}
	
	static String buildUrl( final String url ){
		return "url(\"" + url + "\")";
	}
}
