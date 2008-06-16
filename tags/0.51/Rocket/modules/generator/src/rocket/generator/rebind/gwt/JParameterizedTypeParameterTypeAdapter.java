/**
 * 
 */
package rocket.generator.rebind.gwt;

import java.lang.reflect.ParameterizedType;

import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.JWildcardType;

import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.type.generics.ParameterisedType;
import rocket.util.client.Checker;

/**
 * @author mP
 *
 */
public class JParameterizedTypeParameterTypeAdapter extends ParameterisedType {
	public JParameterizedTypeParameterTypeAdapter( final JParameterizedType jParameterizedType, final TypeOracleGeneratorContext context ){
		super();
		
		this.setGeneratorContext(context);
		this.setJParameterizedType( jParameterizedType );
	}
	
	public String getName(){
		return this.getJParameterizedType().getName();// ??? FIXME
	}
	
	@Override
	public Type getRawType() {
		final TypeOracleGeneratorContext context = (TypeOracleGeneratorContext) this.getGeneratorContext();
		final JType erased = this.getJParameterizedType().getErasedType();
		return context.getType( erased );
	}

	private JParameterizedType jParameterizedType;
	
	protected JParameterizedType getJParameterizedType(){
		Checker.notNull("field:jParameterizedType", jParameterizedType );
		return this.jParameterizedType;
	}
	
	protected void setJParameterizedType( final JParameterizedType jParameterizedType ){
		Checker.notNull("parameter:jParameterizedType", jParameterizedType );
		this.jParameterizedType = jParameterizedType;
	}
	
	@Override
	public String toString(){
		return super.toString() + ", jParameterizedType: " + this.jParameterizedType;
	}

}
