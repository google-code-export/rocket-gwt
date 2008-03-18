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
package rocket.compiler.test.nullandzerotestsimplifier.compiler;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.dev.js.ast.JsProgram;

import rocket.compiler.JavaScriptCompilationWorker;
import rocket.compiler.JavaScriptSourceChecker;

/**
 * This class examines the given source code and makes sure the optimisations were performed.
 * @author Miroslav Pokorny
 */
public class NullAndZeroTestsSimplifierChecker implements JavaScriptSourceChecker {

	public void examine(final String source) {
		// remove all spaces
		System.out.println( "@@@START@@@\n" + source + "\n@@@END@@@");
		final String source0 = source.replaceAll( "[ \t\r\n]", "" );
		
		if( source0.contains( "zeroEqualsZero==0")){
			throw new AssertionError( "The if( zeroEqualsZero == 0 ) should have been optimised to if( !zeroEqualsZero )");
		}
		if( false == source0.contains( "if(!zeroEqualsZero")){
			throw new AssertionError( "The if( zeroEqualsZero == 0 ) should have been optimised to if( !zeroEqualsZero )");
		}
		
		if( source0.contains( "zeroNotEqualsZero!=0")){
			throw new AssertionError( "The if( zeroEqualsZero != 0 ) should have been optimised to if( zeroEqualsZero )");
		}
		if( false == source0.contains( "if(zeroNotEqualsZero")){
			throw new AssertionError( "The if( zeroEqualsZero != 0 ) should have been optimised to if(zeroNotEqualsZero )");
		}
		
		if( source0.contains( "oneEqualsZero==0")){
			throw new AssertionError( "The if( oneEqualsZero == 0 ) should have been optimised to if( !oneEqualsZero )");
		}
		if( false == source0.contains( "if(!oneEqualsZero")){
			throw new AssertionError( "The if( oneEqualsZero == 0 ) should have been optimised to if( !oneEqualsZero )");
		}
		
		if( source0.contains( "oneNotEqualsZero!=0")){
			throw new AssertionError( "The if( oneEqualsZero != 0 ) should have been optimised to if( oneEqualsZero )");
		}
		if( false == source0.contains( "if(oneNotEqualsZero")){
			throw new AssertionError( "The if( oneEqualsZero != 0 ) should have been optimised to if( oneEqualsZero )");
		}
		
		
		if( source0.contains( "nullEqualsNull==null")){
			throw new AssertionError( "The if( nullEqualsNull == null ) should have been optimised to if( !nullEqualsNull )");
		}
		if( false == source0.contains( "if(!nullEqualsNull")){
			throw new AssertionError( "The if( nullEqualsNull == null ) should have been optimised to if( !nullEqualsNull )");
		}
		
		if( source0.contains( "nullNotEqualsNull!=null")){
			throw new AssertionError( "The if( nullEqualsNull != null ) should have been optimised to if( nullEqualsNull )");
		}
		if( false == source0.contains( "if(nullNotEqualsNull")){
			throw new AssertionError( "The if( nullEqualsNull != null ) should have been optimised to if( nullEqualsNull )");
		}
		
		if( source0.contains( "notNullEqualsNull==null")){
			throw new AssertionError( "The if( notNullEqualsNull == null ) should have been optimised to if( !notNullEqualsNull )");
		}
		if( false == source0.contains( "if(!notNullEqualsNull")){
			throw new AssertionError( "The if( notNullEqualsNull == null ) should have been optimised to if( !notNullEqualsNull )");
		}
		
		if( source0.contains( "notNullEqualsNotNull!=null")){
			throw new AssertionError( "The if( notNullEqualsNotNull != null ) should have been optimised to if( notNullEqualsNotNull )");
		}
		if( false == source0.contains( "if(notNullEqualsNotNull")){
			throw new AssertionError( "The if( notNullEqualsNotNull != null ) should have been optimised to if( notNullEqualsNotNull )");
		}
	}

	
}
