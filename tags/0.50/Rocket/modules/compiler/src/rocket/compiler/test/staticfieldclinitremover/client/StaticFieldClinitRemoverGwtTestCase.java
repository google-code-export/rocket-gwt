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
package rocket.compiler.test.staticfieldclinitremover.client;

import com.google.gwt.junit.client.GWTTestCase;

public class StaticFieldClinitRemoverGwtTestCase extends GWTTestCase {

	public String getModuleName() {
		return "rocket.compiler.test.staticfieldclinitremover.StaticFieldClinitRemover";
	}

	/**
	 * This method contains a reference to a class with a static field without a static initializer.
	 * GWTTestCase.assertXXX methods are not used to ensure other clinits are not introduced.
	 */
	public void testHasNoStaticInitializer(){
		final boolean value = HasNoStaticInitializer.staticField0;
		if( value ){
			throw new AssertionError("The HasNoStaticInitializer.staticField0 should be false.");
		}
	}

	static class HasNoStaticInitializer{
		static boolean staticField0;
	}

	/**
	 * This test contains two field references to the same static field. The second reference doesnt require a clinit because the first guarantees that the clinit was called. 
	 */
	public void testSecondFieldAccessNotRequiredDueToPreceedingFieldAccess(){
		final boolean value0 = HasStaticInitializer1.staticField1;
		if( !value0 ){
			throw new AssertionError("The HasStaticInitializer.staticField1 should be true after being set by a static initializer.");
		}
		final boolean value1 = HasStaticInitializer1.staticField1;
		if( !value1 ){
			throw new AssertionError("The HasStaticInitializer.staticField1 should be true after being set by a static initializer.");
		};
	}

	static class HasStaticInitializer1{
		static boolean staticField1 = returnsTrue();
	}

	/**
	 * This method contains a reference to a static field that requires a clinit.
	 * The second field reference (value1) doesnt need a clinit guard.
	 */
	public void testSecondFieldAccessStillRequiresClinit(){
		if( returnsTrue() ){
			final boolean value0 = HasStaticInitializer2.staticField2;
			if( !value0 ){
				throw new AssertionError("The HasStaticInitializer2.staticField2 should be true after being set by a static initializer.");
			}
		}
		final boolean value1 = HasStaticInitializer2.staticField2;
		if( !value1 ){
			throw new AssertionError("The HasStaticInitializer2.staticField2 should be true after being set by a static initializer.");
		};
	}

	static class HasStaticInitializer2{
		static boolean staticField2 = returnsTrue();
	}

	/**
	 * This test involves preparing a method where a static field doesnt require a clinit because of a preceeding method call to the same class.  
	 */
	public void testFieldAccessPreceededByMethodCall(){
		final int expected = 1;
		final int value = HasStaticInitializer3.echo( expected );

		if( expected != value ){
			throw new AssertionError( "The expected and actual values dont match , expected: " + expected + ", actual: " + value );
		}

		final boolean value0 = HasStaticInitializer3.staticField3;
		if( !value0 ){
			throw new AssertionError("The HasStaticInitializer3.staticField3 should be true after being set by a static initializer.");
		}	
	}

	static class HasStaticInitializer3{
		static boolean staticField3 = returnsTrue();

		static int echo( final int value ){
			methodInlineStopper();
			return value;
		}
	}

	/**
	 * This test involves preparing a method where a static field doesnt require a clinit because of a preceeding method call to the same class.  
	 */
	public void testFieldAccessClinitStillPreceededByMethodCallIsOutOfScope(){
		if( returnsTrue() ){
			final int expected = 1;
			final int value = HasStaticInitializer4.echo( expected );

			if( expected != value ){
				throw new AssertionError( "The expected and actual values dont match , expected: " + expected + ", actual: " + value );
			}
		}

		final boolean value0 = HasStaticInitializer4.staticField4;
		if( !value0 ){
			throw new AssertionError("The HasStaticInitializer4.staticField4 should be true after being set by a static initializer.");
		}	
	}
	
	static class HasStaticInitializer4{
		static boolean staticField4 = returnsTrue();

		static int echo( final int value ){
			methodInlineStopper();
			return value;
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
