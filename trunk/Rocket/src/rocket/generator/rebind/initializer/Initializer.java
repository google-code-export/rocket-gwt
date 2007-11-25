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
package rocket.generator.rebind.initializer;

import rocket.generator.rebind.ClassComponent;
import rocket.generator.rebind.CodeGenerator;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.comments.HasComments;
import rocket.generator.rebind.type.Type;

/**
 * Represents an Initializer be it an instance or static initializer belonging
 * to a NewType.
 * 
 * @author Miroslav Pokorny
 */
public interface Initializer extends ClassComponent, CodeGenerator, HasComments {

	boolean isStatic();

	void setStatic(boolean staticc);

	CodeBlock getBody();

	void setBody(CodeBlock body);

	Type getEnclosingType();

	void setEnclosingType(Type enclosingType);
}
