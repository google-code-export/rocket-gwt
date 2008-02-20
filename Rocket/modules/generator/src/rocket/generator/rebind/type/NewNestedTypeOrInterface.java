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

import java.util.Iterator;

import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.SourceWriter;
import rocket.util.client.ObjectHelper;

/**
 * Convenient base class for the nested type and nested interface classes.
 * 
 * @author Miroslav Pokorny
 */
abstract class NewNestedTypeOrInterface extends NewConcreteNestedTypeOrInterfaceType {
	
	protected NewNestedTypeOrInterface(){
		super();		
	}
	
	public String getName(){
		return this.getEnclosingType().getName() + '.' + this.getNestedName();
	}
	
	public boolean hasName(){
		boolean hasName = false;
		try{
			this.getName();
			hasName = true;
		} catch ( final Exception ignored ){
			// ignore
		}
		return hasName;
	}
	
	public void setName( final String name ){
		throw new UnsupportedOperationException( "Set name using setNestedName()...");
	}
	
	/**
	 * The nested name of this type. 
	 * 
	 * The fully qualified class name is never recorded and must be fetched using {@link #getName()}
	 */
	private String nestedName;

	public String getNestedName(){
		GeneratorHelper.checkNestedJavaTypeName("field:nestedName", nestedName);
		return nestedName;		
	}

	public void setNestedName( final String nestedName ){
		GeneratorHelper.checkNestedJavaTypeName("parameter:nestedName", nestedName);
		this.nestedName = nestedName;
	}
	
	/**
	 * The outter class containing this nested class.
	 */
	private Type enclosingType;

	public Type getEnclosingType() {
		ObjectHelper.checkNotNull("field:enclosingType", enclosingType);
		return this.enclosingType;
	}
	
	protected boolean hasEnclosingType(){
		return null != this.enclosingType;
	}

	public void setEnclosingType(Type enclosingType) {
		ObjectHelper.checkNotNull("parameter:enclosingType", enclosingType);
		this.enclosingType = enclosingType;
	}

	/**
	 * When true indicates that this class is static
	 */
	private boolean staticc;

	public boolean isStatic() {
		return staticc;
	}

	public void setStatic(final boolean staticc) {
		this.staticc = staticc;
	}
	
	public void write(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		final GeneratorContext context = this.getGeneratorContext();
		context.branch();
		this.log();
		
		this.writeComments( writer );
		
		this.writeDeclaration(writer);
		writer.indent();
		this.writeInitializers(writer);
		this.writeConstructors(writer);
		this.writeFields(writer);
		this.writeMethods(writer);
		this.writeNestedTypes(writer);
		writer.outdent();

		writer.println("} // " + this.getName());
		
		context.unbranch();
	}
	
	protected void writeComments( final SourceWriter writer ){		
		GeneratorHelper.writeComments( this.getComments(), this.getMetaData(), writer);
	}

	protected void writeDeclaration(final SourceWriter writer) {
		ObjectHelper.checkNotNull("parameter:writer", writer);

		if (this.isStatic()) {
			writer.print("static ");
		}
		if (this.isAbstract()) {
			writer.print("abstract ");
		}
		if (this.isFinal()) {
			writer.print("final ");
		}

		writer.print(this.getVisibility().getJavaName());
		writer.print(" ");
		writer.print(this.isInterface() ? "interface" : "class");
		writer.print(" ");
		writer.print(this.getNestedName());

		if (this.hasSuperType()) {
			final Type superType = this.getSuperType();
			if (false == superType.equals(superType.getGeneratorContext().getObject())) {
				writer.print(" extends ");
				writer.print(superType.getName());
			}
		}

		int i = 0;
		final Iterator interfaces = this.getInterfaces().iterator();
		while (interfaces.hasNext()) {
			if (0 == i) {
				writer.print(" implements ");
			}
			i++;
			final Type interfacee = (Type) interfaces.next();
			writer.print(interfacee.getName());

			if (interfaces.hasNext()) {
				writer.print(",");
			}
		}
		writer.println("{");
	}

	protected void log() {
		this.getGeneratorContext().debug( this.toString() );
	}	
}
