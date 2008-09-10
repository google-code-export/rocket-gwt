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
package rocket.widget.client;

import rocket.util.client.Utilities;

/**
 * Base class for any ImageFactory implementation generated by the accompanying
 * ImageFactoryGenerator.
 * 
 * @author Miroslav Pokorny
 */
abstract public class ImageFactoryImpl implements ImageFactory {

	protected ImageFactoryImpl() {
		super();

		this.preload();
	}

	/**
	 * Preloads all the non lazy server images.
	 */
	protected void preload() {
		final String urls = this.getPreloadUrls();
		final String[] urlsArray = Utilities.split(urls, ",", true);
		for (int i = 0; i < urlsArray.length; i++) {
			final String url = urlsArray[i];
			Image.prefetch(url);
		}
	}

	/**
	 * This method is overridden typically by the ImageFactoryGenerator to
	 * include a comma separated list of urls to preload.
	 */
	abstract protected String getPreloadUrls();

	protected Image createImageFromServerUrl(final String url, final int width, final int height) {
		final Image image = new Image(url);
		return image;
	}

	protected Image createImageFromDataUrl(final String url) {
		return new Image(url);
	}
}
