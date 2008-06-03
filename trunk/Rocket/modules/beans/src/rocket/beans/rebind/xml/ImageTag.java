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
package rocket.beans.rebind.xml;

/**
 * Provides a bean like view of an image element
 * 
 * @author Miroslav Pokorny
 */
class ImageTag extends XmlDocumentComponent {

	public String getFile() {
		return this.getAttribute(Constants.IMAGE_FILE_ATTRIBUTE);
	}

	public boolean isLocal() {
		return Constants.IMAGE_LOCAL.equals(this.getLocation());
	}

	public boolean isServer() {
		return Constants.IMAGE_SERVER.equals(this.getLocation());
	}

	protected String getLocation() {
		return this.getAttribute(Constants.IMAGE_LOCATION_ATTRIBUTE);
	}

	protected String getServerRequest() {
		return this.getAttribute(Constants.IMAGE_SERVER_REQUEST_ATTRIBUTE);
	}

	public boolean isLazy() {
		return Constants.IMAGE_LAZY.equals(this.getServerRequest());
	}
}
