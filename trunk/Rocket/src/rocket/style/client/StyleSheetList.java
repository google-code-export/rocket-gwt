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

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import rocket.util.client.ObjectHelper;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Represents a read only list view of the stylesheets belonging to this
 * document.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class StyleSheetList extends AbstractList {

	StyleSheetList() {
		super();

		this.setStyleSheets(this.createStyleSheets());
	}

	public int size() {
		return ObjectHelper.getPropertyCount(this.getStyleSheetCollection());
	}

	public Object get(final int index) {
		final List cache = this.getStyleSheets();
		Object styleSheet = null;
		if (index < cache.size()) {
			styleSheet = cache.get(index);
		}
		if (null == styleSheet) {
			// takes a lazy approach to creating StyleSheetList instances.
			styleSheet = this.createStyleSheet(index);

			// expand the cache with null elements if necessary...
			final int counter = cache.size() - index + 1;
			for (int i = 0; i < counter; i++) {
				cache.add(null);
			}
			cache.set(index, styleSheet);

			// increase the index of styleSheets that were moved up one slot...
			final int size = cache.size();
			for (int i = index + 1; i < size; i++) {
				final StyleSheet previousStyleSheet = (StyleSheet) cache.get(i);
				if (null == previousStyleSheet) {
					continue;
				}
				previousStyleSheet.setIndex(i);
			}

		}
		return styleSheet;
	}

	protected StyleSheet createStyleSheet(final int index) {
		final StyleSheet styleSheet = new StyleSheet();
		styleSheet.setIndex(index);
		styleSheet.setStyleSheetList(this);
		return styleSheet;
	}

	/**
	 * A cache of stylesheet objects created for each stylesheet.
	 */
	private List styleSheets;

	protected List getStyleSheets() {
		ObjectHelper.checkNotNull("field:styleSheets", styleSheets);
		return this.styleSheets;
	}

	protected void setStyleSheets(final List styleSheets) {
		ObjectHelper.checkNotNull("parameter:styleSheets", styleSheets);
		this.styleSheets = styleSheets;
	}

	protected List createStyleSheets() {
		return new ArrayList();
	}

	/**
	 * Helper which retrieves a child stylesheet object.
	 * 
	 * @param index
	 * @return
	 */
	JavaScriptObject getNativeStyleSheet(final int index) {
		return ObjectHelper.getObject(this.getStyleSheetCollection(), index);
	}

	protected JavaScriptObject getStyleSheetCollection() {
		return StyleSheet.getStyleSheetCollection();
	}
}
