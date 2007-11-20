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
		visitor.start(context.getType( SIMPLE_CLASS ));

		final Set types = visitor.getConcreteTypes();
		assertNotNull("types", types);

		assertEquals("" + types, 2, types.size());
		assertTrue("" + types, types.contains(context.getType( SIMPLE_CLASS )));
		assertTrue("" + types, types.contains(context.getType( OBJECT )));
	}

	static final String SIMPLE_CLASS = SimpleClass.class.getName();

	static class SimpleClass{
		
	}
	static class SimpleClassJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
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
			subTypes.add( getType( SIMPLE_CLASS ));
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


	ReachableTypesVisitor createReachableTypesVisitor(){
		return new ReachableTypesVisitor() {		
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
	
	GeneratorContext createGeneratorContext() {
		return new JavaGeneratorContext() {

			protected Type createClassType(final String name) {
				TestJavaClassTypeAdapter adapter = null;
								
				try {
					final Class javaClass = Class.forName(name);
					Class adapterClass = null;
					try {
						adapterClass = Class.forName(name + "JavaClassTypeAdapter");
						adapter = (TestJavaClassTypeAdapter) adapterClass.newInstance();
					} catch (final Exception useDefault) {
						if( false == OBJECT.equals( name )){
							throw new RuntimeException(name);							
						}
						adapter = new ObjectJavaClassTypeAdapter();
					}
					adapter.setGeneratorContext(this);
					adapter.setJavaClass(javaClass);
				} catch (final ExceptionInInitializerError caught) {
					throwTypeNotFoundException(name, caught);
				} catch (final ClassNotFoundException caught) {
					throwTypeNotFoundException(name, caught);
				} catch (final LinkageError caught) {
					throwTypeNotFoundException(name, caught);
				}

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
