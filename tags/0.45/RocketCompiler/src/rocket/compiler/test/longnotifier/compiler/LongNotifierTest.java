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
 */package rocket.compiler.test.longnotifier.compiler;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import rocket.compiler.LongNotifier;
import rocket.compiler.test.longnotifier.client.LongNotifierGwtTestCase;
import rocket.util.client.Utilities;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.jjs.ast.JProgram;
public class LongNotifierTest extends LongNotifier {

	public boolean work(final JProgram jprogram, final TreeLogger logger) {
		final boolean returnValue = super.work(jprogram, logger);
		
		if( this.types.size() > 0 ){
			throw new AssertionError("The following types should have been noted as having long references, types: " + this.types );
		}
		if( this.methods.size() > 0 ){
			throw new AssertionError("The following methods should have been noted as having long references, methods: " + this.methods );
		}
		if( this.fields.size() > 0 ){
			throw new AssertionError("The following fields should have been noted as having long references, fields: " + this.fields );
		}
		if( this.locals.size() > 0 ){
			throw new AssertionError("The following long parameters/local/literals should have been noted, locals: " + this.locals );
		}
		
		return returnValue;
	}
	
	private Set types = asSet( LongNotifierGwtTestCase.class.getName() ); 
	
	protected void outputType(final Type type,final TreeLogger logger ){
		super.outputType(type, logger);
		
		this.types.remove( type.getName() );
	}
	
	private Set methods = asSet( "methodThatReturnsLong,methodWithLongParameter,testMethodWithLongLocalVariable");
	
	protected void outputMethod(final Method method, final TreeLogger logger ){
		super.outputMethod(method, logger);
		
		this.methods.remove( method.getName() );
	}
	
	private Set fields = this.asSet( "longField");
	
	protected void outputField(final Field field, final TreeLogger logger ){
		super.outputField(field, logger);
		
		this.fields.remove( field.getName() );
	}
	
	private Set locals = this.asSet("longParameter,longLocalVariable,assignedLongLiteral,12345678L");
	
	protected void outputLocals(final List locals, final TreeLogger logger ){
		super.outputLocals(locals, logger);
		
		this.locals.removeAll( locals );
	}
	
	protected Set asSet(final String commaSeparated) {
		return new TreeSet(Arrays.asList(Utilities.split(commaSeparated, ",", true)));
	}
}
