package rocket.test.collection;

import java.util.Iterator;
import java.util.List;

import rocket.client.collection.CyclicList;
import junit.framework.TestCase;

public class CyclicListTestCase extends TestCase {
	public void testAdd0WithoutOverflowing(){
		final int capacity = 3;
		final List list = new CyclicList(capacity);
		
		for( int i = 0; i < capacity; i++ ){
			list.add( "element-" + i );
		}	
	}
	public void testAdd1WhichOverflows(){
		final int capacity = 3;
		final List list = new CyclicList(capacity);
		
		for( int i = 0; i < capacity + 1; i++ ){
			list.add( "element-" + i );
		}	
	}
	
	public void testSize0WithoutOverflowing(){
		final List list = new CyclicList(4);
		final Object first = "first";
		final Object second = "second";
		final Object third = "third";
		
		int i = 0;
		assertEquals( i++, list.size() );
		
		list.add( first );
		assertEquals( i++, list.size() );
		
		list.add( second );
		assertEquals( i++, list.size() );
		
		list.add( third );
		assertEquals( i++, list.size() );
	}

	public void testSize1WithOverflowing(){
		final List list = new CyclicList(2);
		final Object first = "first";
		final Object second = "second";
		final Object third = "third";
		
		int i = 0;
		assertEquals( i++, list.size() );
		
		list.add( first );
		assertEquals( i++, list.size() );
		
		list.add( second );
		assertEquals( i, list.size() );
		
		list.add( third );
		assertEquals( i, list.size() );
	}

	public void testIsEmpty(){
		final List list = new CyclicList(2);
		final Object first = "first";
		final Object second = "second";
		final Object third = "third";
		
		assertTrue( list.isEmpty() );
		
		list.add( first );
		assertFalse( list.isEmpty() );
		
		list.add( second );
		assertFalse( list.isEmpty() );
		
		list.add( third );
		assertFalse( list.isEmpty() );
	}
	
	
	public void testGet0WhereAddDoesntOverflow(){
		final List list = new CyclicList(4);
		final Object first = "first";
		final Object second = "second";
		final Object third = "third";
		
		list.add( first );
		list.add( second );
		list.add( third );
		
		int i = 0;
		assertSame( first, list.get( i++ ));
		assertSame( second, list.get( i++ ));
		assertSame( third, list.get( i++ ));
	}
	
	public void testGet1WhereAddsDidOverflow(){
		final List list = new CyclicList(2);
		final Object first = "first";
		final Object second = "second";
		final Object third = "third";
		
		list.add( first );
		list.add( second );
		list.add( third );
		
		int i = 0;
		assertSame( second, list.get( i++ ));
		assertSame( third, list.get( i++ ));
	}
	
	public void testGet2WhereAddsDidOverflow(){
		final List list = new CyclicList(3);
		final Object first = "first";
		final Object second = "second";
		final Object third = "third";
		final Object fourth = "fourth";
		
		list.add( first );
		list.add( second );
		list.add( third );
		list.add( fourth );
		
		int i = 0;
		assertSame( second, list.get( i++ ));
		assertSame( third, list.get( i++ ));
		assertSame( fourth, list.get( i++ ));
	}
	

	public void testGet3WhereAddsDidOverflow(){
		final List list = new CyclicList(3);
		final Object first = "first";
		final Object second = "second";
		final Object third = "third";
		final Object fourth = "fourth";
		final Object fifth = "fifth";
		
		list.add( first );
		list.add( second );
		list.add( third );
		list.add( fourth );
		list.add( fifth );
		
		int i = 0;
		assertSame( third, list.get( i++ ));
		assertSame( fourth, list.get( i++ ));
		assertSame( fifth, list.get( i++ ));
	}
	
	public void testAddUsingIndex0ThatActuallyAppends(){
		final List list = new CyclicList( 4 );
		list.add( 0, "first");
		list.add( 1, "second");
		list.add( 2, "third");
	}
	
	public void testAddUsingIndex1WithIndexGreaterThanSizeThrowsException(){
		final List list = new CyclicList( 3 );
		try{
			list.add( 2, "shouldFail");
			fail( "An exception should have been thrown when attempting to add(insert) with an index after the current size");
		}catch ( final Exception expected ){
			
		}
	}
	
	public void testAddUsingIndex2WhichActuallyInserts(){
		final List list = new CyclicList( 3 );
		
		final Object first = "first";
		final Object second = "second";
		final Object third = "third";
		
		list.add( 0, third);
		list.add( 0, second);
		list.add( 0, first);
		
		assertEquals( 3, list.size() );
		int i = 0;
		assertSame( first, list.get( i++ ));
		assertSame( second, list.get( i++ ));
		assertSame( third, list.get( i++ ));
	}

	public void testAddUsingIndex3WhichActuallyInsertsAfterCyclingListAndThrowsException(){
		final List list = new CyclicList( 3 );
		
		final Object first = "first";
		final Object second = "second";
		final Object third = "third";
		
		list.add( "willBeLost");
		list.add( 0, third);
		list.add( 0, second);
		
		try{
		list.add( 0, first);
		fail( "An exception should have been thrown when attempting to add(insert) to a full CyclicList");
		} catch ( Exception caught ){
			
		}		
	}
	
	public void testContains(){
		final List list = new CyclicList(4);
		final Object first = "first";
		final Object second = "second";
		final Object third = "third";
		
		list.add( first );
		list.add( second );
		list.add( third );
		
		int i = 0;
		assertFalse( list.contains( "fourth" ));
		assertTrue( list.contains( first ));
		assertTrue( list.contains( second ));
		assertTrue( list.contains( third ));
	}
	
	public void testSet0WhereAddDoesntOverflow(){
		final List list = new CyclicList(4);
		final Object first = "first";
		final Object second = "second";
		final Object third = "third";
		
		list.add( first );
		list.add( second );
		list.add( third );
		
		final Object newFirst = "newFirst";
		final Object newSecond = "newSecond";
		final Object newThird = "newThird";
		
		int j = 0;
		list.set( j++, newFirst );
		list.set( j++, newSecond );
		list.set( j++, newThird );
		
		int i = 0;
		assertSame( newFirst, list.get( i++ ));
		assertSame( newSecond, list.get( i++ ));
		assertSame( newThird, list.get( i++ ));
	}
	
	public void testSet1WhereOverflowsThrowsException(){
		final List list = new CyclicList(2);

		try{
		list.set( 0, "overflowCulprit");
		fail( "Setting the cyclic with an index greater than its capacity should throw an exception, list: " + list );
		} catch ( final Exception expected ){
			
		}
	}

	public void testRemoveByObject0DoesNothingAndReturnsFalseWhenAttemptingToRemoveNonContainedElement(){
		final List list = new CyclicList(4);
		final Object first = "first";
		final Object second = "second";
		final Object third = "third";
		
		list.add( first );
		list.add( second );
		list.add( third );
		
		assertFalse( list.remove( "fourth"));	
		assertFalse( list.remove( "fifth"));
	}
	
	public void testRemoveByObject1WhichActuallyRemovedPreviousAddedElement(){
		final List list = new CyclicList(4);
		final Object first = "first";
		final Object second = "second";
		final Object third = "third";
		
		list.add( first );
		list.add( second );
		list.add( third );
		
		assertTrue( list.remove( "first"));
		assertEquals( 2, list.size() );
		
		int i = 0;
		assertSame( second, list.get( i++ ));
		assertSame( third, list.get( i++ ));
	}
	public void testRemoveByIndex0(){
		final List list = new CyclicList(4);
		final Object first = "first";
		final Object second = "second";
		final Object third = "third";
		
		list.add( first );
		list.add( second );
		list.add( third );
		
		assertSame( second, list.remove( 1 ));
		assertSame( first, list.remove( 0  ));
		assertSame( third, list.remove( 0 ));
	}
	
	public void testIndexOf(){
		final List list = new CyclicList(4);
		final Object first = "first";
		final Object second = "second";
		
		list.add( first );
		list.add( second );
		list.add( second );
		
		assertEquals( -1, list.indexOf( "zebra" ));
		assertEquals( 0, list.indexOf( first ));
		assertEquals( 1, list.indexOf( second ));
	}
	
	public void testLastIndexOf(){
		final List list = new CyclicList(4);
		final Object first = "first";
		final Object second = "second";
		
		list.add( first );
		list.add( second );
		list.add( second );
		
		assertEquals( -1, list.lastIndexOf( "zebra" ));
		assertEquals( 0, list.lastIndexOf( first ));
		assertEquals( 2, list.lastIndexOf( second ));
	}
	
	public void testIterator0VisitsAllElementsPreceedingEachNextWithAHasNextTest(){
		final List list = new CyclicList(3);
		final Object first = "first";
		final Object second = "second";
		final Object third = "third";
		final Object fourth = "fourth";
		
		list.add( first );
		list.add( second );
		list.add( third );
		list.add( fourth );
		
		final Iterator iterator = list.iterator();
	
		assertTrue( iterator.hasNext() );
		assertSame( second, iterator.next() );
		
		assertTrue( iterator.hasNext() );
		assertSame( third, iterator.next() );
	
		assertTrue( iterator.hasNext() );
		assertSame( fourth, iterator.next() );
		
		assertFalse( iterator.hasNext() );
	}
	
	public void testIterator1VisitsAllElementsWithoutHasNextPreTest(){
		final List list = new CyclicList(3);
		final Object first = "first";
		final Object second = "second";
		final Object third = "third";
		final Object fourth = "fourth";
		
		list.add( first );
		list.add( second );
		list.add( third );
		list.add( fourth );
		
		final Iterator iterator = list.iterator();
		assertSame( second, iterator.next() );
		assertSame( third, iterator.next() );
		assertSame( fourth, iterator.next() );
		
		assertFalse( iterator.hasNext() );
	}
	public void testIterator0VisitsAllElementsAndRemovesEachElement(){
		final List list = new CyclicList(3);
		final Object first = "first";
		final Object second = "second";
		final Object third = "third";
		final Object fourth = "fourth";
		
		list.add( first );
		list.add( second );
		list.add( third );
		list.add( fourth );
		
		assertEquals( 3, list.size() );
		
		final Iterator iterator = list.iterator();
		assertSame( second, iterator.next() );
		iterator.remove();
		assertEquals( 2, list.size() );
		
		assertSame( third, iterator.next() );
		iterator.remove();
		assertEquals( 1, list.size() );
		
		
		assertSame( fourth, iterator.next() );
		iterator.remove();
		
		assertEquals( 0, list.size() );
		assertFalse( iterator.hasNext() );
	}

}
