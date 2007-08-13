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
package rocket.generator.rebind.field;

import rocket.generator.rebind.CodeGenerator;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.type.Type;

/**
 * This interface defines a NewField which is basically a mutable Field.
 * 
 * @author Miroslav Pokorny
 */
public interface NewField extends Field, CodeGenerator {

	void setEnclosingType(Type enclosingType);

	void setVisibility(Visibility visibility);

	void setFinal(boolean finall);

	void setStatic(boolean staticc);

	void setTransient(boolean transientt);

	void setType(Type type);

	void setName(String name);

	CodeBlock getValue();

	void setValue(CodeBlock value);
}
