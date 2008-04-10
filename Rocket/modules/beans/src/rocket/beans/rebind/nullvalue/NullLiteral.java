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
package rocket.beans.rebind.nullvalue;

import rocket.beans.rebind.value.AbstractValue;
import rocket.beans.rebind.value.Value;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * A StringValue holds a string literal which may be converted to any of the primitive or String types. 
 * 
 * @author Miroslav Pokorny
 */
public class NullLiteral extends AbstractValue implements Value {

	/**
	 * Null can be set on any property type except for the primitives.
	 */
	public boolean isCompatibleWith(final Type type) {
		Checker.notNull("parameter:type", type);
		
		return false == type.isPrimitive();
	}


	public void write(final SourceWriter writer) {
		Checker.notNull( "parameter:writer", writer );
	
		writer.print( "" + null );
	}
	
	public String toString(){
		return "null";
	}
}
