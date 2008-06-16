/**
 * 
 */
package rocket.generator.rebind.type.generics;

import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * Instances represent a raw type
 * @author Miroslav Pokorny
 */
class RawGenericType extends GenericType implements CodeBlock{
	
	public RawGenericType( final Type type ){
		super();
		
		this.setType(type);
	}
	
	@Override
	public Type getRawType(){
		return this.getType();
	}
	
	/**
	 * The raw type
	 */
	private Type type;
	
	public Type getType(){
		Checker.notNull("field:type", type );
		return this.type;
	}
	
	protected void setType( final Type type ){
		Checker.notNull("field:type", type );
		this.type = type;
	}
	
	public boolean isEmpty(){
		return false;
	}
	
	public void write( final SourceWriter writer ){
		writer.print( this.getType().getName() );
	}
	
	public String toString(){
		return super.toString() + ", rawType: \"" + this.getType().getName() + "\".";
	}
}
