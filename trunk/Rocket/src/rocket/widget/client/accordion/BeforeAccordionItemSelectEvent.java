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
 * This event is fired prior to a new accordion item actually being selected
 * @author Miroslav Pokorny
 */
public class BeforeAccordionItemSelectEvent {

	/**
	 * The current item that is selected.
	 */
	private AccordionItem currentSelection;
	
	public AccordionItem getCurrentSelection(){
		return currentSelection;
	}
	void setCurrentSelection( final AccordionItem currentSelection ){
		this.currentSelection = currentSelection;
	}
	
	/**
	 * The AccordionItem about to be newSelection.
	 */
	private AccordionItem newSelection;
	
	public AccordionItem getNewSelection(){
		return newSelection;
	}
	void setNewSelection( final AccordionItem newSelection ){
		this.newSelection = newSelection;
	}
	
	/**
	 * This flag indicates that the Accordion select should be ignored and not happen.
	 */
	private boolean cancelled;
	
	boolean isCancelled(){
		return this.cancelled;
	}
	void setCancelled( final boolean stopSelect ){
		this.cancelled = stopSelect;
	}
	
	/**
	 * Invoking this method stops or ignores the new Accordion selection from happening.
	 */
	public void stop(){
		this.setCancelled( true );
	}
}
