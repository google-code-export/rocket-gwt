/*
 * Copyright 2006 NSW Police Government Australia
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
package rocket.client.dom;


import rocket.client.util.StringHelper;

/**
* A bean like view of a stylesheet dom object.
* @author Miroslav Pokorny (mP)
*/
public class StyleSheet extends ElementWrapper {

	public StyleSheet() {
	}

	public boolean isExternalFile(){
		boolean external = false;
		while( true ){
			if( ! this.hasTitle() ){
				break;
			}

			if( StringHelper.isNullOrEmpty( this.getTitle())){
				break;
			}

			external = true;
			break;
		}

		return external;
	}

	public String getHref() {
		return (String) this.getProperty(DomConstants.HREF_ATTRIBUTE);
	}

	public boolean hasHref() {
		return this.hasProperty(DomConstants.HREF_ATTRIBUTE);
	}

	public void setHref(final String href) {
		this.setProperty(DomConstants.HREF_ATTRIBUTE, href);
	}
	public String getType() {
		return (String) this.getProperty(DomConstants.TYPE_ATTRIBUTE);
	}

	public boolean hasType() {
		return this.hasProperty(DomConstants.TYPE_ATTRIBUTE);
	}

	public void setType(final String type) {
		this.setProperty(DomConstants.TYPE_ATTRIBUTE, type);
	}
	public boolean isDisabled() {
		return this.hasProperty(DomConstants.DISABLED_ATTRIBUTE) ? this
				.getBooleanProperty(DomConstants.DISABLED_ATTRIBUTE)
				: false;
	}

	public void setDisabled(final boolean disabled) {
		this.setProperty(DomConstants.DISABLED_ATTRIBUTE, disabled);
	}
}