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
package rocket.beans.rebind.alias;

import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.StringLiteral;

/**
 * An abstraction for the producing a list of alias to bean mappings.
 * 
 * @author Miroslav Pokorny
 */
public class GetAliasesToBeans implements CodeBlock {

	public GetAliasesToBeans() {
		super();
		
		this.setBuffer( createBuffer() );
	}

	public boolean isEmpty(){
		return false;
	}
	
	public void write(final SourceWriter writer){
		writer.print( "return " );
		
		final StringBuffer buffer = this.getBuffer();
		new StringLiteral( buffer.toString() ).write( writer );
		
		writer.println( ";");
	}
	
	public void register( final String alias, final String bean ){
		final StringBuffer buffer = this.getBuffer();
		if( buffer.length() > 0 ){
			buffer.append(',');
		}
		buffer.append( alias );
		buffer.append( '=');
		buffer.append( bean );		
	}
	
	private StringBuffer buffer;
	
	protected StringBuffer getBuffer(){
		return this.buffer;
	}
	protected void setBuffer( final StringBuffer buffer ){
		this.buffer = buffer;
	}
	protected StringBuffer createBuffer(){
		return new StringBuffer();
	}
}
