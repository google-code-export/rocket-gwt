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
package rocket.generator.rebind;

import java.util.HashSet;
import java.util.Set;

import rocket.util.client.StringHelper;

import com.google.gwt.core.ext.typeinfo.HasMetaData;

/**
 * An assortment of various helper methods that are used during the code
 * generation process.
 * 
 * @author Miroslav Pokorny
 */
public class RebindHelper {

	/**
	 * Convenient method to retrieve an annotation value from the given source.
	 * 
	 * @param source
	 * @param annotationName
	 * @return
	 */
	static public String getAnnotationValue(final HasMetaData source, final String annotationName) {
		String value = null;
		final String[][] values = source.getMetaData(annotationName);
		if (null != values && values.length == 1) {
			value = values[0][0];
		}
		return value;
	}

	/**
	 * This set contains all reserved javascript keywords.
	 */
	static Set javascriptIdentifierBlacklist = createSetFromCommaDelimiteredString(Constants.JAVASCRIPT_RESERVED_KEYWORDS);

	/**
	 * This method exists purely as a way to easily load a set with String
	 * values.
	 * 
	 * @param input
	 * @return
	 */
	static Set createSetFromCommaDelimiteredString(final String input) {
		final Set set = new HashSet();
		final String[] literals = StringHelper.split(input, ",", true);
		for (int i = 0; i < literals.length; i++) {
			set.add(literals[i]);
		}
		return set;
	}

	static public void checkJavascriptIdentifier(final String javascriptName) {
		if (false == isValidJavascriptIdentifier(javascriptName)) {
			throw new IllegalArgumentException("The javascript name[" + javascriptName + "] is not valid.");
		}
	}

	/**
	 * Tests if a given literal is a valid javascript literal.
	 * 
	 * @param name
	 * @return
	 */
	static public boolean isValidJavascriptIdentifier(final String name) {
		boolean valid = false;

		while (true) {
			if (StringHelper.isNullOrEmpty(name)) {
				break;
			}
			if (RebindHelper.javascriptIdentifierBlacklist.contains(name)) {
				break;
			}

			if (false == isJavascriptIdentifierStart(name.charAt(0))) {
				break;
			}

			valid = true;
			final int count = name.length();
			for (int i = 1; i < count; i++) {
				if (false == isJavascriptIdentifierPart(name.charAt(i))) {
					valid = false;
					break;
				}
			}
			break;
		}

		return valid;
	}

	static private boolean isJavascriptIdentifierStart(final char c) {
		return c == '_' || c == '$' || (c >= 'A' & c <= 'Z') || (c >= 'a' & c <= 'z') || c > 'z';
	}

	static private boolean isJavascriptIdentifierPart(final char c) {
		return c == '_' || c == '$' || (c >= 'A' & c <= 'Z') || (c >= 'a' & c <= 'z') || (c >= '0' & c <= '9') || c > 'z';
	}

	/**
	 * Convenience method which calculates the setter name for a given field.
	 * 
	 * @param name
	 * @return
	 */
	static public String getSetterName(final String name) {
		final StringBuffer setterName = new StringBuffer();
		setterName.append("set");
		setterName.append(Character.toUpperCase(name.charAt(0)));
		if (name.length() > 0) {
			setterName.append(name.substring(1));
		}
		return setterName.toString();
	}
}
