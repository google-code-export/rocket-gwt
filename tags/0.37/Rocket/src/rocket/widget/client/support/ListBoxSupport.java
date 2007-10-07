/*
 * Copyright Google
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
package rocket.widget.client.support;

import com.google.gwt.user.client.Element;

/**
 * Pretty much a copy of the private inner type ListBox.Impl.
 * 
 * TODO When upgrading GWT version replace all methods below with the inner
 * class com.google.gwt.user.client.ui.Listbox.SafariImpl
 */
public class ListBoxSupport {
	native public void clear(Element select) /*-{
	 select.options.length = 0;
	 }-*/;

	native public int getItemCount(Element select) /*-{
	 return select.options.length;
	 }-*/;

	native public String getItemText(Element select, int index) /*-{
	 return select.options[index].text;
	 }-*/;

	native public String getItemValue(Element select, int index) /*-{
	 return select.options[index].value;
	 }-*/;

	native public boolean isItemSelected(Element select, int index) /*-{
	 return select.options[index].selected;
	 }-*/;

	native public void removeItem(Element select, int index) /*-{
	 select.options[index] = null;
	 }-*/;

	native public void setItemSelected(Element select, int index, boolean selected) /*-{
	 select.options[index].selected = selected;
	 }-*/;

	native public void setValue(Element select, int index, String value) /*-{
	 select.options[index].value = value;
	 }-*/;
}
