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

import java.util.List;

import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.Literal;
import rocket.generator.rebind.comments.HasComments;
import rocket.generator.rebind.metadata.MetaData;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

/**
 * A holder for a new field.
 * 
 * @author Miroslav Pokorny
 */
public class NewFieldImpl extends AbstractField implements NewField, HasComments {

	public NewFieldImpl() {
		super();

		this.setComments( "" );
		this.setMetaData( this.createMetaData() );
	}

	public void setVisibility(final Visibility visibility) {
		super.setVisibility(visibility);
	}

	/**
	 * When true indicates that this field is final.
	 */
	private boolean finall;

	public boolean isFinal() {
		return finall;
	}

	public void setFinal(final boolean finall) {
		this.finall = finall;
	}

	/**
	 * WHen true indicates that this field is static.
	 */
	private boolean staticc;

	public boolean isStatic() {
		return staticc;
	}

	public void setStatic(final boolean staticc) {
		this.staticc = staticc;
	}

	/**
	 * A flag that indicates this field is transient
	 */
	private boolean transientt;

	public boolean isTransient() {
		return transientt;
	}

	public void setTransient(final boolean transientt) {
		this.transientt = transientt;
	}

	/**
	 * The field type
	 */
	private Type type;

	public Type getType() {
		ObjectHelper.checkNotNull("field:type", type);
		return type;
	}

	protected boolean hasType(){
		return null != this.type;
	}
	
	public void setType(final Type type) {
		ObjectHelper.checkNotNull("parameter:type", type);
		this.type = type;
	}

	/**
	 * The name of this field.
	 */
	private String name;

	public String getName() {
		GeneratorHelper.checkJavaFieldName("field:name", name);
		return name;
	}

	public void setName(final String name) {
		GeneratorHelper.checkJavaFieldName("parameter:name", name);
		this.name = name;
	}

	public String getJsniNotation() {
		return '@' + this.getEnclosingType().getName() + "::" + this.getName();
	}

	/**
	 * A code block which may or may not contain a literal, method etc that sets
	 * a value upon this field.
	 */
	private CodeBlock value;

	public CodeBlock getValue() {
		ObjectHelper.checkNotNull("field:value", value);
		return this.value;
	}

	public void setValue(final CodeBlock value) {
		ObjectHelper.checkNotNull("parameter:value", value);
		this.value = value;
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
	
	public void write(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		this.writeLogger();

		this.writeComments( writer );
		this.writeDeclaration(writer);
		this.writeValue(writer);
	}

	protected void writeLogger() {
		this.getGeneratorContext().debug("Writing field " + this.getType().getName() + ": " + this.getName());
	}

	protected void writeComments( final SourceWriter writer ){		
		GeneratorHelper.writeComments( this.getComments(), this.getMetaData(), writer);
	}
	
	protected void writeDeclaration(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		if (this.isFinal()) {
			writer.print("final ");
		}
		if (this.isStatic()) {
			writer.print("static ");
		}
		if (this.isTransient()) {
			writer.print("transient ");
		}
		writer.print(this.getVisibility().getJavaName());
		writer.print(" ");
		writer.print(this.getType().getName());
		writer.print(" ");
		writer.print(this.getName());
	}

	protected void writeValue(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		final CodeBlock codeBlock = this.getValue();
		if (codeBlock.isEmpty()) {
			writer.println(";");
		} else {
			writer.print("=");
			codeBlock.write(writer);
			
			// terminate any literal with a semi colon.
			if( codeBlock instanceof Literal ){
				writer.println( ";");
			}
		}
	}

	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("NewFieldImpl ");

		if ( this.hasType() ) {
			builder.append(this.getType().getName());
			builder.append(' ');
		}
		builder.append(this.getName());
		
		if( this.hasEnclosingType() ){
			builder.append( ", enclosingType: ");
			builder.append( this.getEnclosingType() );
		}
		
		return builder.toString();
	}	
}
