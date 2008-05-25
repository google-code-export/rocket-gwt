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
package rocket.generator.rebind.method;

import rocket.generator.rebind.CodeGenerator;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.comments.HasComments;
import rocket.generator.rebind.metadata.HasMetadata;
import rocket.generator.rebind.methodparameter.NewMethodParameter;
import rocket.generator.rebind.type.Type;

/**
 * Represents a new method that will be added to a new class being built.
 * 
 * Whilst Method is basically a read only view of a Method, NewMethod contains
 * the mutable methods for all method properties
 * 
 * @author Miroslav Pokorny
 */
public interface NewMethod extends Method, CodeGenerator, HasComments, HasMetadata {

	void setEnclosingType(Type enclosingType);

	void setAbstract(boolean abstractt);

	void setFinal(boolean finall);

	void setStatic(boolean staticc);

	void setNative(boolean nativee);

	void setVisibility(Visibility visibility);

	void setName(String name);

	NewMethodParameter newParameter();

	void addParameter(NewMethodParameter parameter);

	void addThrownTypes(Type thrownTypes);

	void setReturnType(Type returnType);

	CodeBlock getBody();

	void setBody(CodeBlock body);
	
	void addMetaData( String key, String value );
}
