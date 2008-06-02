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
package rocket.generator.rebind.constructor;

import rocket.generator.rebind.CodeGenerator;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.comments.HasComments;
import rocket.generator.rebind.constructorparameter.NewConstructorParameter;
import rocket.generator.rebind.metadata.HasMetadata;
import rocket.generator.rebind.type.Type;

/**
 * A NewConstructor is basically a mutable Constructor
 * 
 * @author Miroslav Pokorny
 */
public interface NewConstructor extends Constructor, CodeGenerator, HasMetadata, HasComments {

	void setEnclosingType(Type enclosingType);

	void setVisibility(final Visibility visibility);

	NewConstructorParameter newParameter();

	void addParameter(NewConstructorParameter parameter);

	void addThrownType(Type thrownTypes);

	CodeBlock getBody();

	void setBody(final CodeBlock body);

	void addMetaData(String name, String value);
}
