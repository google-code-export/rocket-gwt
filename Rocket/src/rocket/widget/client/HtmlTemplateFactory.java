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

import com.google.gwt.user.client.ui.CheckBox;

/**
 * Base interface for all generated HtmlTemplates. Sub interfaces must define
 * one or more methods. Each method must satisfy the following rules.
 * <ul>
 * <li>Return type must be a sub-class of Widget</li>
 * <li>An id annotation must be present for each getter</li>
 * </ul>
 * 
 * The parent panel of all widgets retrieved from this factory is the RootPanel
 * singleton.
 * 
 * The label portion of the CheckBox, RadioButton widget is not accessible thus
 * attempts to invoke any related method will fail.
 * <ul>
 * <li>{@link CheckBox#getText }</li>
 * <li>{@link CheckBox#setText }</li>
 * <li>{@link CheckBox#getHTML }</li>
 * <li>{@link CheckBox#setHTML }</li>
 * <li>{@link RadioButton#getText }</li>
 * <li>{@link RadioButton#setText }</li>
 * <li>{@link RadioButton#getHTML }</li>
 * <li>{@link RadioButton#setHTML }</li>
 * </ul>
 * 
 * @author Miroslav Pokorny
 */
public interface HtmlTemplateFactory {

}
