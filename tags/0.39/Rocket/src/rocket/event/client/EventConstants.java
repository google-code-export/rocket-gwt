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
package rocket.event.client;

import com.google.gwt.user.client.ui.KeyboardListener;

/**
 * A collection of key code constants.
 * 
 * Refer to {@linkplain http://www.quirksmode.org/js/keys.html } for details.
 * @author n9834386
 */
public class EventConstants {
	public final static int BACKSPACE = KeyboardListener.KEY_BACKSPACE;

	public final static int CURSOR_LEFT = 37;
	public final static int CURSOR_UP = 38;
	public final static int CURSOR_RIGHT = 39;
	public final static int CURSOR_DOWN = 40;
	
	public final static int DELETE = KeyboardListener.KEY_DELETE;
	
	public final static int END = KeyboardListener.KEY_END;
	
	public final static int ENTER = KeyboardListener.KEY_ENTER;
	public final static int ESCAPE = KeyboardListener.KEY_ESCAPE;
	
	public final static int HOME = KeyboardListener.KEY_HOME;
	
	public final static int INSERT = 45;
	
	public final static int PAGE_DOWN = KeyboardListener.KEY_PAGEDOWN;
	public final static int PAGE_UP = KeyboardListener.KEY_PAGEUP;

	public final static int TAB = KeyboardListener.KEY_TAB;
	
	public final static int FUNCTION_F1 = 112;
	public final static int FUNCTION_F12 = 123;
}
