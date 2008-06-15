/**
 * 
 */
package rocket.generator.rebind.type.generics;

import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.util.client.Checker;

/**
 * @author mP
 *
 */
abstract public class ParameterisedType extends GenericType implements CodeBlock{

	public ParameterisedType(){
		super();
	}
//	
//	public boolean equals( final Object object ){
//		return false;
//	}
	
	@Override
	abstract public String getName();
	
	@Override
	public String getSimpleName(){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getRuntimeName(){
		throw new UnsupportedOperationException();
	}
		
	public void write( final SourceWriter sourceWriter ){
		Checker.notNull("parameter:sourceWriter", sourceWriter );
		
		sourceWriter.print( this.getName() );
	}
}
