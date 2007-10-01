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
package rocket.widget.client.tabpanel;

/**
 * This event is fired after completing a new tab selection
 * @author Miroslav Pokorny
 */
public class TabSelectEvent {
	
	/**
	 * The previously selected tab
	 */
	private TabItem previouslySelected;
	
	public TabItem getPreviouslySelected(){
		return previouslySelected;
	}
	void setPreviouslySelected( final TabItem previouslySelected ){
		this.previouslySelected = previouslySelected;
	}
	
	/**
	 * The new tab that is selected
	 */
	private TabItem currentSelection;
	
	public TabItem getCurrentSelection(){
		return currentSelection;
	}
	void setCurrentSelection( final TabItem currentSelection ){
		this.currentSelection = currentSelection;
	}
}
