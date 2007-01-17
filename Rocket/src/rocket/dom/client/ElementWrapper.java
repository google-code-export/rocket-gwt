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
package rocket.dom.client;

import rocket.util.client.ObjectWrapper;

import com.google.gwt.user.client.Element;

/**
 * Represents a handle to a single DOM element with a number of common attributes.
 * 
 * @author Miroslav Pokorny (mP)
 */
public interface ElementWrapper extends ObjectWrapper {

    String getId();

    boolean hasId();

    void setId(String id);

    String getName();

    boolean hasName();

    void setName(String name);

    String getTitle();

    boolean hasTitle();

    void setTitle(String title);

    Element getElement();

    void setElement(Element element);
}
