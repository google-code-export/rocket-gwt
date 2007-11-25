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
package rocket.generator.test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.field.Field;
import rocket.generator.rebind.java.JavaClassTypeAdapter;
import rocket.generator.rebind.java.JavaGeneratorContext;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.visitor.ReachableTypesVisitor;
import rocket.util.client.PrimitiveHelper;

/**
 * A variety of tests that create a simple limited class heirarchy and runs a series of tests against those.
 * @author Miroslav Pokorny
 */
public class ReachableTypesVisitorTestCase extends TestCase {

	final static String OBJECT = Object.class.getName();

	public void testAgainstTypeWithNoReachableTypesExceptSelf() {
		final GeneratorContext context = this.createGeneratorContext();

		final ReachableTypesVisitor visitor = createReachableTypesVisitor(); 		
		visitor.start(context.getType( FINAL_CLASS ));

		final Set types = visitor.getConcreteTypes();
		assertNotNull("types", types);

		assertEquals("" + types, 2, types.size());
		assertTrue("" + types, types.contains(context.getType( FINAL_CLASS )));
		assertTrue("" + types, types.contains(context.getType( OBJECT )));
	}

	static final String FINAL_CLASS = FinalClass.class.getName();

	static class FinalClass{
		
	}
	static class FinalClassJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set createSubTypes() {
			return Collections.EMPTY_SET;
		}
	}
	
	public void testConcreteClassWithASubClass(){
		final GeneratorContext context = this.createGeneratorContext();

		final ReachableTypesVisitor visitor = createReachableTypesVisitor(); 		
		visitor.start(context.getType( CONCRETE_CLASS ));

		final Set types = visitor.getConcreteTypes();
		assertNotNull("types", types);

		assertEquals("" + types, 3, types.size());
		assertTrue("" + types, types.contains(context.getType( CONCRETE_SUB_CLASS )));
		assertTrue("" + types, types.contains(context.getType( CONCRETE_CLASS )));
		assertTrue("" + types, types.contains(context.getType( OBJECT )));
	}
	
	final static String CONCRETE_CLASS = ConcreteClass.class.getName();
	final static String CONCRETE_SUB_CLASS = ConcreteClassSubClass.class.getName();

	static class ConcreteClass{
		
	}
	static class ConcreteClassJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set createSubTypes() {
			return new HashSet( Collections.nCopies( 1,  this.getType( CONCRETE_SUB_CLASS)));
		}
	}
	
	static class ConcreteClassSubClass{
		
	}
	static class ConcreteClassSubClassJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set createSubTypes() {
			return Collections.EMPTY_SET;
		}
	}

	public void testInterfaceWithSeveralImplementingConcreteClasses(){
		final GeneratorContext context = this.createGeneratorContext();

		final ReachableTypesVisitor visitor = createReachableTypesVisitor(); 		
		visitor.start(context.getType( CONCRETE_CLASS_THAT_IMPLEMENTS_INTERFACE ));

		final Set types = visitor.getConcreteTypes();
		assertNotNull("types", types);

		assertEquals("" + types, 3, types.size());
		assertTrue("" + types, types.contains(context.getType( CONCRETE_SUB_CLASS_THAT_IMPLEMENTS_INTERFACE )));
		assertTrue("" + types, types.contains(context.getType( CONCRETE_CLASS_THAT_IMPLEMENTS_INTERFACE )));
		assertTrue("" + types, types.contains(context.getType( OBJECT )));
	}
	
	public void testClassWithInterfaceField(){
		final GeneratorContext context = this.createGeneratorContext();

		final ReachableTypesVisitor visitor = createReachableTypesVisitor(); 		
		visitor.start(context.getType( CONCRETE_CLASS_THAT_IMPLEMENTS_INTERFACE ));

		final Set types = visitor.getConcreteTypes();
		assertNotNull("types", types);

		assertEquals("" + types, 3, types.size());
		assertTrue("" + types, types.contains(context.getType( CONCRETE_SUB_CLASS_THAT_IMPLEMENTS_INTERFACE )));
		assertTrue("" + types, types.contains(context.getType( CONCRETE_CLASS_THAT_IMPLEMENTS_INTERFACE )));
		assertTrue("" + types, types.contains(context.getType( OBJECT )));
	}
	
	final static String CLASS_WITH_INTERFACE_FIELD = Interface.class.getName();
	
	static class ClassWithInterfaceField{
		public Interface interfaceField;
	}
	static class ClassWithInterfaceFieldJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set createSubTypes() {
			return Collections.EMPTY_SET; // no sub classes.
		}
	}

	public void testClassThatHasSubClassWithInterfaceField(){
		final GeneratorContext context = this.createGeneratorContext();

		final ReachableTypesVisitor visitor = createReachableTypesVisitor(); 		
		visitor.start(context.getType( CLASS_WITH_SUB_CLASS_WITH_INTERFACE_FIELD ));

		final Set types = visitor.getConcreteTypes();
		assertNotNull("types", types);

		assertEquals("" + types, 5, types.size());
		assertTrue("" + types, types.contains(context.getType( CLASS_WITH_SUB_CLASS_WITH_INTERFACE_FIELD )));
		assertTrue("" + types, types.contains(context.getType( SUB_CLASS_WITH_INTERFACE_FIELD )));
		assertTrue("" + types, types.contains(context.getType( CONCRETE_SUB_CLASS_THAT_IMPLEMENTS_INTERFACE )));
		assertTrue("" + types, types.contains(context.getType( CONCRETE_CLASS_THAT_IMPLEMENTS_INTERFACE )));
		assertTrue("" + types, types.contains(context.getType( OBJECT )));
	}

	public void testClassWithFinalClassArray(){
		final GeneratorContext context = this.createGeneratorContext();

		final ReachableTypesVisitor visitor = createReachableTypesVisitor(); 		
		visitor.start(context.getType( CLASS_WITH_FINAL_CLASS_ARRAY ));

		final Set types = visitor.getConcreteTypes();
		assertNotNull("types", types);

		assertEquals("" + types, 4, types.size());
		assertTrue("" + types, types.contains(context.getType( CLASS_WITH_FINAL_CLASS_ARRAY )));
		assertTrue("" + types, types.contains(context.getType( FINAL_CLASS_ARRAY  )));
		assertTrue("" + types, types.contains(context.getType( FINAL_CLASS )));
		assertTrue("" + types, types.contains(context.getType( OBJECT )));
		
	}
	
	final static String CLASS_WITH_SUB_CLASS_WITH_INTERFACE_FIELD = ClassThatHasSubClassWithInterfaceField.class.getName();
	final static String SUB_CLASS_WITH_INTERFACE_FIELD = SubClassWithInterfaceField.class.getName();
	
	static class ClassThatHasSubClassWithInterfaceField{
	}
	static class ClassThatHasSubClassWithInterfaceFieldJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set createSubTypes() {
			return new HashSet( Collections.nCopies( 1, this.getType( SUB_CLASS_WITH_INTERFACE_FIELD )));
		}
	}
	static class SubClassWithInterfaceField{
		Interface interfaceField;
	}
	static class SubClassWithInterfaceFieldJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set createSubTypes() {
			return Collections.EMPTY_SET;
		}
	}
	
	final static String INTERFACE = Interface.class.getName();
	final static String CONCRETE_CLASS_THAT_IMPLEMENTS_INTERFACE = ConcreteClassThatImplementsInterface.class.getName();
	final static String CONCRETE_SUB_CLASS_THAT_IMPLEMENTS_INTERFACE = ConcreteClassSubClassThatImplementsInterface.class.getName();

	static class ObjectJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set createSubTypes() {
			final Set subTypes = new HashSet();
			subTypes.add( getType( INTERFACE ));
			subTypes.add( getType( CONCRETE_CLASS_THAT_IMPLEMENTS_INTERFACE ));
			subTypes.add( getType( CLASS_WITH_SUB_CLASS_WITH_INTERFACE_FIELD ));
			subTypes.add( getType( CONCRETE_CLASS ));
			subTypes.add( getType( FINAL_CLASS ));
			return subTypes;
		}
	}
	
	static interface Interface{
		
	}
	static class InterfaceJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set createSubTypes() {
			return Collections.EMPTY_SET;
		}
	}	
	
	static class ConcreteClassThatImplementsInterface implements Interface{
		
	}
	static class ConcreteClassThatImplementsInterfaceJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set createSubTypes() {
			return new HashSet( Collections.nCopies( 1,  this.getType( CONCRETE_SUB_CLASS_THAT_IMPLEMENTS_INTERFACE)));
		}
	}
	
	static class ConcreteClassSubClassThatImplementsInterface extends ConcreteClassThatImplementsInterface{
		
	}
	static class ConcreteClassSubClassThatImplementsInterfaceJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set createSubTypes() {
			return Collections.EMPTY_SET;
		}
	}

	
	
	final static String CLASS_WITH_FINAL_CLASS_ARRAY = ClassWithFinalClassArray.class.getName();
	
	static class ClassWithFinalClassArray{
		FinalClass[] simpleClassArray;
	}
	static class ClassWithFinalClassArrayJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set createSubTypes() {
			return Collections.EMPTY_SET;
		}
	}	

	final static String FINAL_CLASS_ARRAY = "[L" + FinalClass.class.getName() + ";";  
	
	static class FinalClassArrayJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set createSubTypes() {
			return Collections.EMPTY_SET;
		}
	}		
	
	/**
	 * Factory method that creates a ReachableTypesVisitor visitor with some additional checks.
	 * @return
	 */
	ReachableTypesVisitor createReachableTypesVisitor(){
		return new ReachableTypesVisitor() {		
			
			
			protected boolean skipArray( final Type array ){
				return false;
			}
			
			protected boolean skipType( final Type type ){
				PrimitiveHelper.checkFalse( "The type: " + type + " has already been visited...", this.alreadyVisitedTypes.contains( type ));
				this.alreadyVisitedTypes.add( type );
				return false;
			}
			final Set alreadyVisitedTypes = new HashSet();
			
			protected boolean skipSuperType( final Type superType ){
				return false;
			}
			
			protected boolean skipSubType( final Type subType ){
				return false;
			}
			protected boolean skipField( final Field field ){			
				PrimitiveHelper.checkFalse( "The field: " + field + " has already been visited...", this.alreadyVisitedFields.contains( field ));
				this.alreadyVisitedFields.add( field );
				return false;
			}
			final Set alreadyVisitedFields = new HashSet();			
		};
	}
	
	/**
	 * Creates a GeneratorContext that uses a combination of the jre and various classes and some adapters that fill in ability to get any
	 * sub types for a given type.
	 * @return
	 */
	GeneratorContext createGeneratorContext() {
		return new JavaGeneratorContext() {

			protected Type createClassType(final String name) {
				TestJavaClassTypeAdapter adapter = null;
								
				Class javaClass = null; 
				while( true ){
					try{
						javaClass = Class.forName(name);
					} catch ( Exception classNotFound ){
						throw new RuntimeException("Unable to find type \"" + name + "\".");
					}
					if( OBJECT.equals( name )){
						adapter = new ObjectJavaClassTypeAdapter();
						break;
					}
					if( FINAL_CLASS_ARRAY.equals( name )){
						adapter = new FinalClassArrayJavaClassTypeAdapter();
						break;
					}

					final String adapterName = name + "JavaClassTypeAdapter";
					try {
						final Class adapterClass = Class.forName( adapterName );
						adapter = (TestJavaClassTypeAdapter) adapterClass.newInstance();
					} catch (final Exception complainIfNotObject ) {
						if( false == OBJECT.equals( name )){
							throw new RuntimeException( "Unable to find \"" + adapterName + "\" for the type \"" + name + "\".", complainIfNotObject );							
						}
						adapter = new ObjectJavaClassTypeAdapter();
					}
					break;
				}
				adapter.setGeneratorContext(this);
				adapter.setJavaClass(javaClass);
				return adapter;
			}
		};
	}
	
	static abstract class TestJavaClassTypeAdapter extends JavaClassTypeAdapter {	
		
		public void setJavaClass(final Class javaClass) {
			super.setJavaClass(javaClass);
		}

		abstract protected Set createSubTypes();
	}
}
