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
package rocket.compiler.test.staticmethodclinitremover.client;

import com.google.gwt.junit.client.GWTTestCase;

public class StaticMethodClinitRemoverGwtTestCase extends GWTTestCase {

	public String getModuleName() {
		return "rocket.compiler.test.staticmethodclinitremover.StaticMethodClinitRemover";
	}
	/**
	 * This method invokes a class that has a single static method which because its method is called from outside must have the static initializer call as the first
	 * statement of the method.
	 */
	public void testStaticMethodThatMustHaveAClinit(){
		final int in = 1;
		final int out = Test1.mustHaveClinit0( in );
		assertEquals( 1 * 2, out );
	}
	
	static class Test1{
		/**
		 * This flag will be set if the static initializer has been run.
		 */
		static boolean initialized = returnsTrue();
		
		static public int mustHaveClinit0( final int value ){
			checkStaticInitializerHasBeenExecuted();
			return 2 * value;
		}
		static void checkStaticInitializerHasBeenExecuted(){
			if( false == initialized ){
				throw new AssertionError( "It appears the static initializer for this class was not run because it should have set the CallsPrivateStaticMethod.initialized field.");
			}
		}
	}
	
	
	/**
	 * Calls a class that has a field initialized ( set to true ) by a static initializer.
	 * The first method which is called by this method should have a static initializer method guard whilst the second shouldnt.
	 */
	public void testPrivateStaticMethodWithRemovableClinit(){
		final int in = 1;
		final int out = Test2.mustHaveClinit1( in );
		assertEquals( 1 * 2, out );
	}
	
	static class Test2{
		
		/**
		 * This flag will be set if the static initializer has been run.
		 */
		static boolean initialized = returnsTrue();
		
		static public int mustHaveClinit1( final int value ){
			checkStaticInitializerHasBeenExecuted();
			return mustNotHaveClinit0( value );
		}
		/**
		 * The StaticInitializerRemover should remove the clint inserted as the first statement of this method.
		 * @param value
		 */
		static private int mustNotHaveClinit0( final int value ){
			checkStaticInitializerHasBeenExecuted();
			return value * 2;
		}
		
		static void checkStaticInitializerHasBeenExecuted(){
			if( false == initialized ){
				throw new AssertionError( "It appears the static initializer for this class was not run because it should have set the CallsPrivateStaticMethod.initialized field.");
			}
		}
	}

	/**
	 * Calls a class with 2 public methods. The second public static method has no external call sites but is called by the other static method. The second method can therefore have its
	 * clint called removed.
	 */
	public void testPublicMethodWithNoExternalCallSitesWithRemovableClinit(){
		final int in = 1;
		final int out = Test3.mustHaveClinit2( in );
		assertEquals( 2 * 1, out );
	}
	
	static class Test3 {
		static boolean initialized = returnsTrue();
		
		static public int mustHaveClinit2( final int value ){
			checkStaticInitializerHasBeenExecuted();
			return mustNotHaveClinit1( value );
		}
		
		static public int mustNotHaveClinit1( final int value ){
			checkStaticInitializerHasBeenExecuted();
			methodInlineStopper();
			return value * 2;
		}
		
		static void checkStaticInitializerHasBeenExecuted(){
			if( false == initialized ){
				throw new AssertionError( "It appears the static initializer for this class was not run because it should have set the CallsPrivateStaticMethod.initialized field.");
			}
		}
	}
	
	/**
	 * This method should be called by other methods to ensure they themselves are not inlined.
	 */
	public static void methodInlineStopper(){
		for( int i = 0; i < 1; i++ ){
			new Object();
		}
	}
	
	/**
	 * This non inlinable static method always returns true.
	 * @return
	 */
	static boolean returnsTrue(){
		methodInlineStopper();
		return true;
	}
}
