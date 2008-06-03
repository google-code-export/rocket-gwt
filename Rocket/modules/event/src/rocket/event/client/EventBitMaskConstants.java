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

public class EventBitMaskConstants {

	public final static int BLUR = com.google.gwt.user.client.Event.ONBLUR;

	public final static int FOCUS = com.google.gwt.user.client.Event.ONFOCUS;

	public final static int FOCUS_EVENTS = BLUR | FOCUS;

	public final static int KEY_DOWN = com.google.gwt.user.client.Event.ONKEYDOWN;

	public final static int KEY_PRESS = com.google.gwt.user.client.Event.ONKEYPRESS;

	public final static int KEY_UP = com.google.gwt.user.client.Event.ONKEYUP;

	public final static int KEY_EVENTS = KEY_DOWN | KEY_PRESS | KEY_UP;

	public final static int IMAGE_LOAD_FAILED = com.google.gwt.user.client.Event.ONERROR;

	public final static int IMAGE_LOAD_SUCCESSFUL = com.google.gwt.user.client.Event.ONLOAD;

	public final static int IMAGE_EVENTS = IMAGE_LOAD_FAILED | IMAGE_LOAD_SUCCESSFUL;

	public final static int MOUSE_CLICK = com.google.gwt.user.client.Event.ONCLICK;

	public final static int MOUSE_DOUBLE_CLICK = com.google.gwt.user.client.Event.ONDBLCLICK;

	public final static int MOUSE_DOWN = com.google.gwt.user.client.Event.ONMOUSEDOWN;

	public final static int MOUSE_MOVE = com.google.gwt.user.client.Event.ONMOUSEMOVE;

	public final static int MOUSE_OUT = com.google.gwt.user.client.Event.ONMOUSEOUT;

	public final static int MOUSE_OVER = com.google.gwt.user.client.Event.ONMOUSEOVER;

	public final static int MOUSE_UP = com.google.gwt.user.client.Event.ONMOUSEUP;

	public final static int MOUSE_WHEEL = com.google.gwt.user.client.Event.ONMOUSEWHEEL;

	public final static int MOUSE_EVENTS = MOUSE_CLICK | MOUSE_DOUBLE_CLICK | MOUSE_DOWN | MOUSE_OUT | MOUSE_OVER | MOUSE_OVER
			| MOUSE_UP | MOUSE_WHEEL;

	public final static int CHANGE = com.google.gwt.user.client.Event.ONCHANGE;

	public final static int SCROLL = com.google.gwt.user.client.Event.ONSCROLL;

	public final static int UNDEFINED = com.google.gwt.user.client.Event.UNDEFINED;

}
