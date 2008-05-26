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

 import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.compiler.LongNotifier;
import rocket.compiler.TreeLoggers;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.jjs.ast.JProgram;
 public class LongNotifierTest extends LongNotifier {

	 public boolean work(final JProgram jprogram, final TreeLogger logger) {		 
		 if( 0 == pass ){
			 this.checkAgainstExpectedLongReferences(jprogram, logger);
		 }
		 this.pass++;
		 return false;
	 }
	 
	 int pass = 0;
	 
	 void checkAgainstExpectedLongReferences( final JProgram jprogram, final TreeLogger logger ){
		 final List messages = new ArrayList();
		 final TreeLogger checker = TreeLoggers.pipeAndCapture(logger, messages);

		 super.work(jprogram, checker);
		
		 assertContains( messages, "methodThatReturnsLong");
		 assertContains( messages, "methodWithLongParameter");
		 assertContains( messages, "testMethodWithLongLocalVariable");
		 assertContains( messages, "longField");
		 assertContains( messages, "longParameter");
		 assertContains( messages, "longLocalVariable");
		 assertContains( messages, "assignedLongLiteral");
		 assertContains( messages, "12345678L");
	 }
	 
	 void assertContains( final List list, final String text ){
		 boolean found = false;
		 final Iterator i = list.iterator();
		 while( i.hasNext() ){
			 final String string = (String)i.next();

			 if( string.equals( text ) || string.contains( text)){
				 found = true;
				 break;
			 }
		 }

		 if( ! found ){
			 throw new AssertionError( "Unable to find \"" + text + "\" within any of the captured messages: " + list );
		 }
	 }
 }
