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
package rocket.generator.test.templatedfilecodeblock.rebind;

import rocket.generator.rebind.Generator;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.NewType;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

abstract public class AbstractTemplatedFileCodeBlockGenerator extends Generator {

	protected NewConcreteType assembleNewType(final Type superType, final String newTypeName) {
		final GeneratorContext context = this.getGeneratorContext();
		this.setType(superType);

		final NewConcreteType newType = context.newConcreteType(newTypeName);
		newType.setSuperType(superType);
		newType.setVisibility(Visibility.PUBLIC);
		this.setNewType(newType);

		this.addNewConstructor();
		this.addNewField();
		this.addNewMethod();

		return newType;
	}

	protected void addNewConstructor() {
	}

	protected void addNewField() {
	}

	protected void addNewMethod() {
		final GeneratorContext context = this.getGeneratorContext();
		final NewType type = this.getNewType();

		final NewMethod newMethod = type.newMethod();
		newMethod.setAbstract(false);

		final Template body = new Template();
		body.setNative(this.isNewMethodNative());
		body.setResourceName(this.getResourceNameFromGeneratorPackage(this.getTemplateFilename()));
		newMethod.setBody(body);

		newMethod.setFinal(false);
		newMethod.setName(this.getNewMethodName());
		newMethod.setNative(this.isNewMethodNative());
		newMethod.setReturnType(context.getType(this.getNewMethodReturnType()));
		newMethod.setStatic(false);
		newMethod.setVisibility(Visibility.PUBLIC);

		this.postNewMethodCreate(newMethod);

		this.visitTemplate(body);
	}

	protected void postNewMethodCreate(final NewMethod method) {
	}

	abstract protected String getTemplateFilename();

	abstract protected String getNewMethodName();

	abstract protected boolean isNewMethodNative();

	abstract protected void visitTemplate(final Template template);

	abstract protected String getNewMethodReturnType();

	/**
	 * The type passed to GWT.create()
	 */
	private Type type;

	protected Type getType() {
		return this.type;
	}

	protected void setType(final Type superType) {
		this.type = superType;
	}

	/**
	 * The new type being created
	 */
	private NewConcreteType newType;

	protected NewConcreteType getNewType() {
		Checker.notNull("field:newType", newType);
		return this.newType;
	}

	protected void setNewType(final NewConcreteType newType) {
		Checker.notNull("parameter:newType", newType);
		this.newType = newType;
	}

	protected String getGeneratedTypeNameSuffix() {
		return "1";
	}
}
