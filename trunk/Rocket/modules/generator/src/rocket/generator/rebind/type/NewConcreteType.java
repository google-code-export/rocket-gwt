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
package rocket.generator.rebind.type;

import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.constructor.NewConstructor;

/**
 * A NewConcreteType represents a Type being created
 * 
 * @author Miroslav Pokorny
 */
public interface NewConcreteType extends NewType {

	void setName(String name);

	NewConstructor newConstructor();

	void addConstructor(NewConstructor constructor);

	void setAbstract(boolean abstractt);

	void setFinal(boolean finall);

	void setVisibility(Visibility visibility);

	void write();
}
