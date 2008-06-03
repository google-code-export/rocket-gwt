/*
 * Copyright Miroslav Pokorny
 *
 * Licensed under the Apache License, Version 2.0 (the "License"; you may not
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

import rocket.widget.client.WidgetConstants;

class Constants {
	private final static String CAPTIONS = "captions";

	private final static String CAPTION = "caption";

	private final static String CONTENT = "content";

	private final static String CONTENTS = "contents";

	private final static String SELECTED = "-selected";

	/**
	 * VerticalAccordionPanel constants
	 */
	final static String VERTICAL_ACCORDION_PANEL_STYLE = WidgetConstants.ROCKET + "-verticalAccordionPanel";

	final static String VERTICAL_ACCORDION_PANEL_CAPTION_STYLE = CAPTION;

	final static String VERTICAL_ACCORDION_PANEL_CAPTION_SELECTED_STYLE = VERTICAL_ACCORDION_PANEL_CAPTION_STYLE + SELECTED;

	final static String VERTICAL_ACCORDION_PANEL_CONTENT_STYLE = CONTENT;

	/**
	 * LeftAccordionPanel constants.
	 */
	final static String LEFT_SIDE_ACCORDION_PANEL_STYLE = WidgetConstants.ROCKET + "-leftSideAccordionPanel";

	final static String LEFT_SIDE_ACCORDION_PANEL_CAPTIONS_STYLE = CAPTIONS;

	final static String LEFT_SIDE_ACCORDION_PANEL_CAPTION_STYLE = CAPTION;

	final static String LEFT_SIDE_ACCORDION_PANEL_CAPTION_SELECTED_STYLE = LEFT_SIDE_ACCORDION_PANEL_CAPTION_STYLE + SELECTED;

	final static String LEFT_SIDE_ACCORDION_PANEL_CONTENTS_STYLE = CONTENTS;

	final static String LEFT_SIDE_ACCORDION_PANEL_CONTENT_STYLE = CONTENT;

	final static int LEFT_SIDE_ACCORDION_PANEL_CAPTIONS_PANEL_INDEX = 0;
	final static int LEFT_SIDE_ACCORDION_PANEL_CONTENTS_PANEL_INDEX = LEFT_SIDE_ACCORDION_PANEL_CAPTIONS_PANEL_INDEX + 1;

	/**
	 * RightAccordionPanel constants.
	 */
	final static String RIGHT_SIDE_ACCORDION_PANEL_STYLE = WidgetConstants.ROCKET + "-rightSideAccordionPanel";

	final static String RIGHT_SIDE_ACCORDION_PANEL_CAPTIONS_STYLE = CAPTIONS;

	final static String RIGHT_SIDE_ACCORDION_PANEL_CAPTION_STYLE = CAPTION;

	final static String RIGHT_SIDE_ACCORDION_PANEL_CAPTION_SELECTED_STYLE = RIGHT_SIDE_ACCORDION_PANEL_CAPTION_STYLE + SELECTED;

	final static String RIGHT_SIDE_ACCORDION_PANEL_CONTENTS_STYLE = CONTENTS;

	final static String RIGHT_SIDE_ACCORDION_PANEL_CONTENT_STYLE = CONTENT;

	final static int RIGHT_SIDE_ACCORDION_PANEL_CONTENTS_PANEL_INDEX = 0;
	final static int RIGHT_SIDE_ACCORDION_PANEL_CAPTIONS_PANEL_INDEX = RIGHT_SIDE_ACCORDION_PANEL_CONTENTS_PANEL_INDEX + 1;
}
