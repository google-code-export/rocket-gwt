/**
 * 
 */
package rocket.generator.rebind.type.generics;

import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * @author mP
 */
abstract public class WildcardType extends GenericType implements CodeBlock{
	
	public WildcardType(){
		super();
	}
	
	abstract public Type getRawType();
	
	public boolean isEmpty(){
		return false;
	}
	
	abstract public void write( SourceWriter writer );
}
