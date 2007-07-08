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
package rocket.beans.rebind.newinstance;

import java.util.*;

import rocket.beans.rebind.bean.Bean;
import rocket.beans.rebind.values.Value;
import rocket.util.client.ObjectHelper;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JConstructor;
import com.google.gwt.core.ext.typeinfo.JParameter;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * Generates code that calls the constructor of the bean type passing any parameters if any are found.
 * @author Miroslav Pokorny
 */
public class Constructor extends NewInstanceProvider {
	
	public Constructor (){
		this.setParameters( this.createParameters() );
	}
	protected void write0(final SourceWriter writer) {
		
		this.checkConstructor();
		
		final Bean bean = this.getBean();
		final List parameters = this.getParameters();
		
		// write out generated values...
		final StringBuilder builder = new StringBuilder();
		builder.append( "return new ");
		builder.append( bean.getTypeName()  );
		builder.append( "( ");
		
		final Iterator parametersIterator = parameters.iterator();
		while( parametersIterator.hasNext() ){
			final Value value = (Value) parametersIterator.next();
			builder.append( value.generateValue() );
			
			if( parametersIterator.hasNext() ){
				builder.append( ", ");
			}
		}
		
		builder.append( " );");

		writer.println( builder.toString() );
	}
	
	protected void checkConstructor(){
		final Bean bean = this.getBean();
		final List parameters = this.getParameters();
		
		final JClassType type = (JClassType) this.getBeanFactoryGeneratorContext().getType( bean.getTypeName() );
		boolean found = false;
		final JConstructor[] constructors = type.getConstructors();
		
		for( int i = 0; i < constructors.length; i++ ){
			final JConstructor constructor = constructors[ i ];
			final JParameter[] constructorParameters = constructor.getParameters();
			
			if( constructorParameters.length != parameters.size() ){
				continue;
			}
			
			found = true;
			
			int j = 0;
			final Iterator values = parameters.iterator();
			while( values.hasNext() ){
				final JParameter parameter = constructorParameters[ j ];
				final Value value = (Value) values.next();
				value.setType( parameter.getType() );
				if( false == value.isCompatibleWith()){
					found = false;
					break;
				}
				
				j++;
			}
			
			if( found ){
				break;
			}
		}	
		
		if( ! found ){
			throwConstructorNotFoundException();
		}
	}
	
	protected void throwConstructorNotFoundException(){
		throw new ConstructorNotFoundException( "Unable to find a constructor that matches the types of values declared, beanId: " + this.getBean().getId() );
	}
	
	
	public void addParameter( final Value value ){
		this.getParameters().add( value );
	}
	
	private List parameters;
	
	protected List getParameters(){
		ObjectHelper.checkNotNull("field:parameters", parameters);
		return this.parameters;
	}
	protected void setParameters( final List parameters ){
		ObjectHelper.checkNotNull("parameters:parameters", parameters);
		this.parameters = parameters;
	}
	protected List createParameters(){
		return new ArrayList();
	}
}
