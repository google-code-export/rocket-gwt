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
import java.util.List;
import java.util.Set;

import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.constructor.NewConstructor;
import rocket.generator.rebind.constructor.NewConstructorImpl;
import rocket.generator.rebind.initializer.Initializer;
import rocket.generator.rebind.initializer.InitializerImpl;
import rocket.generator.rebind.metadata.MetaData;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

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
		this.setComments( "" );
		this.setMetaData( this.createMetaData() );
	}
	
	public Initializer newInitializer(){
		final InitializerImpl initializer = new InitializerImpl();
		initializer.setEnclosingType( this );
		initializer.setGeneratorContext( this.getGeneratorContext() );
		this.addInitializer(initializer);
		return initializer;
	}

	private Visibility visibility;

	public Visibility getVisibility() {
		ObjectHelper.checkNotNull("field:visibility", visibility);
		return this.visibility;
	}

	public void setVisibility(final Visibility visibility) {
		ObjectHelper.checkNotNull("field:visibility", visibility);
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
	private Set initializers;

	protected Set getInitializers() {
		ObjectHelper.checkNotNull("field:initializers", initializers);
		return this.initializers;
	}

	protected void setInitializers(final Set initializers) {
		ObjectHelper.checkNotNull("parameter:initializers", initializers);
		this.initializers = initializers;
	}

	public void addInitializer(final Initializer initializer) {
		ObjectHelper.checkNotNull("parameter:initializer", initializer);

		this.getInitializers().add(initializer);
	}

	protected Set createInitializers() {
		return new HashSet();
	}

	public NewConstructor newConstructor() {
		final NewConstructorImpl constructor = new NewConstructorImpl();
		constructor.setGeneratorContext(this.getGeneratorContext());

		this.addConstructor(constructor);

		return constructor;
	}

	public void addConstructor(final NewConstructor constructor) {
		ObjectHelper.checkNotNull("parameter:constructor", constructor);
		this.getConstructors().add(constructor);
		constructor.setEnclosingType(this);
	}

	public boolean hasNoArgumentsConstructor() {
		return this.getConstructors().isEmpty() ? true : null != this.findConstructor(Collections.EMPTY_LIST);
	}
	
	protected void writeInitializers(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		final Set initializers = this.getInitializers();

		writer.beginJavaDocComment();
		writer.print("Initializers (" + initializers.size() + ")");
		writer.endJavaDocComment();

		writer.println();
		GeneratorHelper.writeClassComponents(initializers, writer, false, true);
		writer.println();
	}
	
	/**
	 * Any text which will appear within javadoc comments for this field.
	 */
	private String comments;
	
	public String getComments(){
		StringHelper.checkNotNull( "field:comments", comments );
		return comments;
	}
	
	public void setComments( final String comments ){
		StringHelper.checkNotNull( "parameter:comments", comments );
		this.comments = comments;
	}
	
	public void addMetaData( final String name, final String value ){
		this.getMetaData().add( name, value);
	}
	
	public List getMetadataValues( final String name ){
		return this.getMetaData().getMetadataValues(name);
	}
	
	/**
	 * A container which holds any meta data that is added to a new field instance. 
	 */
	private MetaData metaData;
	
	protected MetaData getMetaData(){
		ObjectHelper.checkNotNull("field:metaData", metaData );
		return this.metaData;
	}
	
	protected void setMetaData( final MetaData metaData ){
		ObjectHelper.checkNotNull("parameter:metaData", metaData );
		this.metaData = metaData;
	}
	
	protected MetaData createMetaData(){
		return new MetaData();
	}	
}
