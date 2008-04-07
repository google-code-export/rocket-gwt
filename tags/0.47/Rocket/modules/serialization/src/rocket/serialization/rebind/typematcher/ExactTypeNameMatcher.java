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
package rocket.serialization.rebind.typematcher;

import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.type.Type;

public class ExactTypeNameMatcher implements TypeMatcher{

	public ExactTypeNameMatcher(){
		super();
	}
	
	public ExactTypeNameMatcher( final String name ){
		this();
		
		this.setName(name);
	}
	
	public boolean matches( Type type ){
		return this.getName().equals( type.getName() );
	}
	
	private String name;
	
	private String getName(){
		GeneratorHelper.checkJavaTypeName( "field:name", name );
		return this.name;
	}
	
	public void setName( final String name ){
		GeneratorHelper.checkJavaTypeName( "parameter:name", name );
		this.name = name;
	}
	
	public String toString(){
		return super.toString() + ", name\"" + name + "\".";
	}
}
