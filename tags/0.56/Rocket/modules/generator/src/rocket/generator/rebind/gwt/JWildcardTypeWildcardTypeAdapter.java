/**
 * 
 */
package rocket.generator.rebind.gwt;

import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.JWildcardType;

import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.type.generics.WildcardType;
import rocket.util.client.Checker;

/**
 * @author mP
 *
 */
public class JWildcardTypeWildcardTypeAdapter extends WildcardType {

	public JWildcardTypeWildcardTypeAdapter(){
		super();
	}
	
	public JWildcardTypeWildcardTypeAdapter( final JWildcardType jWildcardType, final TypeOracleGeneratorContext generatorContext ){
		this();
		
		this.setJWildcardType(jWildcardType);
		this.setGeneratorContext(generatorContext);
	}

	@Override
	public Type getRawType() {
		final TypeOracleGeneratorContext context = (TypeOracleGeneratorContext) this.getGeneratorContext();
		final JType erased = this.getJWildcardType().getErasedType();
		return context.getType( erased );
	}

	@Override
	public void write(final SourceWriter writer) {
		// TODO Auto-generated method stub

	}

	private JWildcardType jWildcardType;
	
	protected JWildcardType getJWildcardType(){
		Checker.notNull("field:jWildcardType", jWildcardType );
		return this.jWildcardType;
	}
	
	protected void setJWildcardType( final JWildcardType jWildcardType ){
		Checker.notNull("parameter:jWildcardType", jWildcardType );
		this.jWildcardType = jWildcardType;
	}
	
	@Override
	public String toString(){
		return super.toString() + ", jWildcardType: " + this.jWildcardType;
	}
}
