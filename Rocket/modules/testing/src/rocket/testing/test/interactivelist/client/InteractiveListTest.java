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
package rocket.testing.test.interactivelist.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.util.client.Checker;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class InteractiveListTest implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				caught.printStackTrace();
				Window.alert("Caught:" + caught + "\nmessage\"" + caught.getMessage() + "\".");
			}
		});

		final RootPanel rootPanel = RootPanel.get();
		rootPanel.add(new InteractiveList());
	}

	public class InteractiveList extends rocket.testing.client.InteractiveList {

		InteractiveList() {
			this.createList();
		}

		protected String getCollectionTypeName() {
			return "java.util.ArrayList";
		}

		protected int getListSize() {
			return this.getList().size();
		}

		protected boolean getListIsEmpty() {
			return this.getList().isEmpty();
		}

		protected boolean listAdd(final Object element) {
			return this.getList().add(element);
		}

		protected void listInsert(final int index, final Object element) {
			this.getList().add(index, element);
		}

		protected Object listGet(final int index) {
			return this.getList().get(index);
		}

		protected Object listRemove(final int index) {
			return this.getList().remove(index);
		}

		protected Object listSet(final int index, final Object element) {
			return this.getList().set(index, element);
		}

		protected Object createElement() {
			return new HTML("" + System.currentTimeMillis());
		}

		protected Iterator listIterator() {
			return this.getList().iterator();
		}

		protected void checkType(Object element) {
			if (false == (element instanceof HTML)) {
				Checker.fail("Unknown element type. element ");
			}
		}

		/**
		 * Creates a listbox friendly string form for the given element.
		 * 
		 * @param element
		 * @return
		 */
		protected String toString(final Object element) {
			final HTML html = (HTML) element;
			return html.getText();
		}

		/**
		 * Contains the list being interactively controlled.
		 */
		private List list;

		protected List getList() {
			Checker.notNull("field:list", list);
			return this.list;
		}

		protected void setList(final List list) {
			Checker.notNull("parameter:list", list);
			this.list = list;
		}

		protected List createList() {
			final List list = new ArrayList();
			this.setList(list);
			return list;
		}
	}
}
