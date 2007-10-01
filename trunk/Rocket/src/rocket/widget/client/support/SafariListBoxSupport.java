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
 * TODO When upgrading GWT version replace all methods below with the inner class com.google.gwt.user.client.ui.Listbox.SafariImpl
 */
public class SafariListBoxSupport extends ListBoxSupport{
	 public native void clear(Element select) /*-{
     select.innerText = '';
   }-*/;

   public native int getItemCount(Element select) /*-{
     return select.children.length;
   }-*/;

   public native String getItemText(Element select, int index) /*-{
     return select.children[index].text;
   }-*/;

   public native String getItemValue(Element select, int index) /*-{
     return select.children[index].value;
   }-*/;

   public native boolean isItemSelected(Element select, int index) /*-{
     return select.children[index].selected;
   }-*/;

   public native void removeItem(Element select, int index) /*-{
     select.removeChild(select.children[index]);
   }-*/;

   public native void setItemSelected(Element select, int index,
                                      boolean selected) /*-{
     select.children[index].selected = selected;
   }-*/;

   public native void setValue(Element select, int index, String value) /*-{
     select.children[index].value = value;
   }-*/;
}
