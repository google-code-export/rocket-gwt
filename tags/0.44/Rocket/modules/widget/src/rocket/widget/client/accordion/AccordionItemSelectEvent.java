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
package rocket.widget.client.accordion;

/**
 * This event is fired after completing a new accordion selection
 * 
 * @author Miroslav Pokorny
 */
public class AccordionItemSelectEvent {

	/**
	 * The previously selected Accordion
	 */
	private AccordionItem previouslySelected;

	public AccordionItem getPreviouslySelected() {
		return previouslySelected;
	}

	void setPreviouslySelected(final AccordionItem previouslySelected) {
		this.previouslySelected = previouslySelected;
	}

	/**
	 * The new currently selected AccordionItem
	 */
	private AccordionItem newSelection;

	public AccordionItem getNewSelection() {
		return newSelection;
	}

	void setNewSelection(final AccordionItem newSelection) {
		this.newSelection = newSelection;
	}
}
