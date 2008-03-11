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
package rocket.compiler.test.nullandzerotestsimplifier.client;

import com.google.gwt.junit.client.GWTTestCase;

public class NullAndZeroTestsSimplifierGwtTestCase extends GWTTestCase {

	public String getModuleName() {
		return "rocket.compiler.test.nullandzerotestsimplifier.NullAndZeroTestsSimplifier";
	}
	
	public void testZeroEqualsZero(){
		int i = 1;
		final int zeroEqualsZero = echo( 0 );
		if( zeroEqualsZero == 0 ){
			i = 2;
		}
		assertEquals( "0 == 0", 2, i );
	} 
	
	public void testZeroNotEqualsZero(){
		int i = 1;
		final int zeroNotEqualsZero = echo( 0 );
		if( zeroNotEqualsZero != 0 ){
			i = 2;
		}
		assertEquals( "0 != 0", 1, i );
	}
	
	public void testOneEqualsZero(){
		int i = 1;
		final int oneEqualsZero = echo( 1 );
		if( oneEqualsZero == 0 ){
			i = 2;
		}
		assertEquals( "0 == 0", 1, i );
	} 
	
	public void testOneNotEqualsZero(){
		int i = 1;
		final int oneNotEqualsZero = echo( 1 );
		if( oneNotEqualsZero != 0 ){
			i = 2;
		}
		assertEquals( "0 != 0", 2, i );
	}
	
	int echo( final int i ){
		dummy();
		return i;
	}
	
	public void testNullAgainstNull(){
		int i = 1;
		final X nullEqualsNull = echo( null );
		if( nullEqualsNull == null ){
			i = 2;
		}
		
		assertNull( nullEqualsNull );
		assertEquals( "null == null", 2, i );
	}
	
	public void testNullAgainstNotNull(){
		int i = 1;
		final X nullNotEqualsNull = echo( null );
		if( nullNotEqualsNull != null ){
			i = 2;
		}
		
		assertNull( nullNotEqualsNull );
		assertEquals( "null != null", 1, i );
	}
	
	public void testNotNullAgainstNull(){
		int i = 1;
		final X notNullEqualsNull = echo( new X() );
		if( notNullEqualsNull == null ){
			i = 2;
		}
		
		assertNotNull( notNullEqualsNull );
		assertEquals( "not null == null", 1, i );
	}
	
	public void testNotNullAgainstNotNull(){
		int i = 1;
		final X notNullEqualsNotNull = echo( new X() );
		if( notNullEqualsNotNull != null ){
			i = 2;
		}
		
		assertNotNull( notNullEqualsNotNull );
		assertEquals( "not null != null", 2, i );
	}
	
	X echo( X x ){
		dummy();
		return x;
	}
	
	static class X{
		
	}

	/**
	 * By calling this method other methods avoid being completely removed by
	 * various optimisers.
	 */
	static void dummy() {
		for (int i = 0; i < 1; i++) {
			new Object(); // stops inlining...
		}
	}
}
