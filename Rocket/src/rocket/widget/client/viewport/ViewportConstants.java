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
package rocket.widget.client.viewport;

import rocket.style.client.StyleHelper;
import rocket.widget.client.WidgetConstants;

/**
 * A collection of constants used through out this package.
 * 
 * @author Miroslav Pokorny
 */
class ViewportConstants {

	public final static String VIEWPORT_STYLE = StyleHelper.buildCompound(WidgetConstants.ROCKET, "viewport");

	public final static String VIEWPORT_TILE_STYLE = StyleHelper.buildCompound(VIEWPORT_STYLE, "tile");

	public final static String VIEWPORT_OUT_OF_BOUNDS_STYLE = StyleHelper.buildCompound(VIEWPORT_STYLE, "outOfBounds");

	final static String TILE_LEFT_ATTRIBUTE = "__tileLeft";

	final static String TILE_TOP_ATTRIBUTE = "__tileTop";

	final static int X_OFFSET = 16384;

	final static int Y_OFFSET = 16384;
}
