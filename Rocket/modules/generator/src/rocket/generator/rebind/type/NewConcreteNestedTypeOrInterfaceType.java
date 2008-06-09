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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.constructor.NewConstructor;
import rocket.generator.rebind.constructor.NewConstructorImpl;
import rocket.generator.rebind.initializer.Initializer;
import rocket.generator.rebind.initializer.InitializerImpl;
import rocket.util.client.Checker;

/**
 * Common base class containing common functionality for the concrete and nested
 * types.
 * 
 * @author Miroslav Pokorny
 */
abstract class NewConcreteNestedTypeOrInterfaceType extends NewTypeImpl implements NewType {

	public NewConcreteNestedTypeOrInterfaceType() {
		super();

		this.setInitializers(this.createInitializers());
		this.setComments("");
		this.setMetaData(this.createMetaData());
	}

	public Initializer newInitializer() {
		final InitializerImpl initializer = new InitializerImpl();
		initializer.setEnclosingType(this);
		initializer.setGeneratorContext(this.getGeneratorContext());
		this.addInitializer(initializer);
		return initializer;
	}

	private Visibility visibility;

	public Visibility getVisibility() {
		Checker.notNull("field:visibility", visibility);
		return this.visibility;
	}

	public void setVisibility(final Visibility visibility) {
		Checker.notNull("field:visibility", visibility);
		this.visibility = visibility;
	}

	/**
	 * When true indicates that this method is abstract
	 */
	private boolean abstractt;

	public boolean isAbstract() {
		return abstractt;
	}

	public void setAbstract(final boolean abstractt) {
		this.abstractt = abstractt;
	}

	/**
	 * When true indicates that this method is final
	 */
	private boolean finall;

	public boolean isFinal() {
		return finall;
	}

	public void setFinal(final boolean finall) {
		this.finall = finall;
	}

	public String getJsniNotation() {
		return 'L' + this.getName().replace('.', '/') + ';';
	}

	protected void addInterface0(final Type interfacee) {
		this.getInterfaces().add(interfacee);
	}

	/**
	 * A set which contains all the initializers that have been built and added
	 * to this type.
	 */
	private Set<Initializer> initializers;

	protected Set<Initializer> getInitializers() {
		Checker.notNull("field:initializers", initializers);
		return this.initializers;
	}

	protected void setInitializers(final Set<Initializer> initializers) {
		Checker.notNull("parameter:initializers", initializers);
		this.initializers = initializers;
	}

	public void addInitializer(final Initializer initializer) {
		Checker.notNull("parameter:initializer", initializer);

		this.getInitializers().add(initializer);
	}

	protected Set<Initializer> createInitializers() {
		return new HashSet<Initializer>();
	}

	public NewConstructor newConstructor() {
		final NewConstructorImpl constructor = new NewConstructorImpl();
		constructor.setGeneratorContext(this.getGeneratorContext());

		this.addConstructor(constructor);

		return constructor;
	}

	public void addConstructor(final NewConstructor constructor) {
		Checker.notNull("parameter:constructor", constructor);
		this.getConstructors().add(constructor);
		constructor.setEnclosingType(this);
	}

	public boolean hasNoArgumentsConstructor() {
		return this.getConstructors().isEmpty() ? true : null != this.findConstructor(Collections.<Type>emptyList());
	}

	protected void writeInitializers(final SourceWriter writer) {
		Checker.notNull("parameter:writer", writer);

		final Set<Initializer> initializers = this.getInitializers();
		if (false == initializers.isEmpty()) {

			final GeneratorContext context = this.getGeneratorContext();
			context.branch();

			final String message = "Initializers";
			context.debug(message);

			writer.beginJavaDocComment();
			writer.print(message);
			writer.endJavaDocComment();

			writer.println();
			GeneratorHelper.writeClassComponents(initializers, writer, false, true);
			writer.println();

			context.unbranch();
		}
	}
}
