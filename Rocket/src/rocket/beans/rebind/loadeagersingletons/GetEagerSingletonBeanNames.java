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
package rocket.beans.rebind.loadeagersingletons;

import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.StringLiteral;
import rocket.util.client.Checker;

/**
 * An abstraction for providing the body of a {@link rocket.beans.client.BeanFactoryImpl#getEagerSingletonBeanNames} 
 * 
 * @author Miroslav Pokorny
 */
public class GetEagerSingletonBeanNames implements CodeBlock {

	public GetEagerSingletonBeanNames() {
		super();
		this.setBuffer(this.createBuffer());
	}
	
	public boolean isNative(){
		return false;
	}
	
	public void setNative( final boolean ignored){
		throw new UnsupportedOperationException();
	}
	
	/**
	 * A buffer that accumulates all eager singleton beans as a comma separated string.
	 */
	private StringBuffer buffer;

	protected StringBuffer getBuffer() {
		Checker.notNull("field:buffer", buffer);
		return this.buffer;
	}

	protected void setBuffer(final StringBuffer buffer) {
		Checker.notNull("parameter:buffer", buffer);
		this.buffer = buffer;
	}

	protected StringBuffer createBuffer() {
		return new StringBuffer();
	}

	public void addBean(final String beanId ) {
		Checker.notEmpty("parameter:beanId", beanId );
		
		final StringBuffer buf = this.getBuffer();
		if( buf.length() > 0 ){
			buf.append(',');
		}
		
		buf.append( beanId );
	}

	public boolean isEmpty(){
		return false;
	}
	
	public void write( final SourceWriter writer ){
		writer.print( "return " );
		
		final StringBuffer buffer = this.getBuffer();
		new StringLiteral( buffer.toString() ).write( writer );
		
		writer.println( ";");
	}
}
