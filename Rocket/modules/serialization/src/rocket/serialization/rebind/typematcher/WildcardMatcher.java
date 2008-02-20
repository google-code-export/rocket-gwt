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

import rocket.generator.rebind.type.Type;

public class WildcardMatcher implements TypeMatcher{
	
	public WildcardMatcher(){
		super();
	}
	
	public WildcardMatcher( final String pattern ){
		this();
		
		this.setPattern(pattern);
	}
	
	public boolean matches( Type type ){
		return type.getName().startsWith(this.getPattern() );
	}
	
	private String pattern;
	
	protected String getPattern(){
		return this.pattern;
	}
	
	public void setPattern( final String pattern ){
		final int wildcard = pattern.indexOf( '*'); 
		if( wildcard != pattern.length() - 1 ){
			throw new IllegalArgumentException("The pattern \"" + pattern + "\" is missing a wildcard or doesnt end with a wildcard '*'.");
		}
		
		this.pattern = pattern.substring( 0, pattern.length() - 1 );
	}
	
	public String toString(){
		return super.toString() + ", pattern\"" + pattern + "*]";
	}
}
