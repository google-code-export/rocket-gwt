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
package rocket.compiler.test.longnotifier.client;

import com.google.gwt.junit.client.GWTTestCase;

public class LongNotifierGwtTestCase extends GWTTestCase {

	public String getModuleName() {
		return "rocket.compiler.test.longnotifier.LongNotifier";
	}

	public void testMethodThatReturnsLong(){
		this.methodThatReturnsLong();
	}		
	
	long methodThatReturnsLong(){		
		return 0;
	}
	
	public void testInvokesMethodWithLongParameter(){
		final long longValue = 1;
		this.methodWithLongParameter( longValue );
	}
	
	void methodWithLongParameter( final long longParameter ){
		this.dummy();
	}
	
	public void testMethodWithLongLocalVariable(){
		final long longLocalVariable = 1L;
		dummy();
	}
	
	public void testMethodWithLongLiteral(){
		final long assignedLongLiteral = 12345678L;
	}
	
	long longField;
	
	public void testAccessLongField(){
		this.longField = 1;
	}
	
	void dummy(){
		for( int i = 0; i < 1; i++ ){
			new Object();
		}
	}
}
