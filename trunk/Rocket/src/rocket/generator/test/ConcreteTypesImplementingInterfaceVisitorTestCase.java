package rocket.generator.test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.java.JavaClassTypeAdapter;
import rocket.generator.rebind.java.JavaGeneratorContext;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.visitor.ConcreteTypesImplementingInterfaceVisitor;

public class ConcreteTypesImplementingInterfaceVisitorTestCase extends TestCase {

	public void testFind() {
		final GeneratorContext context = this.createGeneratorContext();
		
		final TestConcreteTypesImplementingInterfaceFinder finder = new TestConcreteTypesImplementingInterfaceFinder();
		finder.start( context.getType( Interface.class.getName() ));
		
		final Set types = finder.getTypes();
		assertNotNull( "types", types );
		
		assertEquals( 2, types.size() );
		assertTrue( "" + SubClassThatImplementsInterface.class, types.contains( context.getType( SubClassThatImplementsInterface.class.getName() )));
		assertTrue( "" + SubSubClassThatImplementsInterface.class, types.contains( context.getType( SubSubClassThatImplementsInterface.class.getName() )));
	}
	
	class TestConcreteTypesImplementingInterfaceFinder extends ConcreteTypesImplementingInterfaceVisitor{
		protected boolean visit(final Type type){
			this.types.add( type); 
			return false;
		}

		protected boolean skipAbstractTypes(){
			return true;
		}		
		
		Set types = new HashSet();
		
		Set getTypes(){
			return this.types;
		}
	}
	
	GeneratorContext createGeneratorContext() {
		return new JavaGeneratorContext() {
			
			protected Type createClassType(final String name) {
				TestJavaClassTypeAdapter adapter = null;

				try {
					final Class javaClass = Class.forName(name);
					Class adapterClass = null;
					try {
						adapterClass = Class.forName(name
								+ "JavaClassTypeAdapter");
						adapter = (TestJavaClassTypeAdapter) adapterClass
								.newInstance();
					} catch (final Exception useDefault) {
						//adapter = new TestJavaClassTypeAdapter();
						throw new RuntimeException( name );
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

	static class TestJavaClassTypeAdapter extends JavaClassTypeAdapter {
		public void setJavaClass(final Class javaClass) {
			super.setJavaClass(javaClass);
		}

		protected Set createSubTypes(){
			throw new UnsupportedOperationException( this.getName() + ".createSubTypes() , adapter: " + this.getClass() ); 
		}
	}

	static interface Interface {
	}

	static class InterfaceJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set getSubTypes() {
			final Set subTypes = new HashSet();
			subTypes.add(this.getType(SubInterface.class.getName()));
			subTypes.add(this.getType(ConcreteClass.class.getName()));
			subTypes.add(this.getType(AnotherConcreteClass.class.getName()));
			return Collections.unmodifiableSet(subTypes);
		}
	}

	static interface SubInterface extends Interface{
	}

	static class SubInterfaceJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set getSubTypes() {
			return Collections.EMPTY_SET;
		}
	}

	static class ConcreteClass {
	}

	static class ConcreteClassJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set getSubTypes() {
			final Set subTypes = new HashSet();
			subTypes.add(this.getType(SubClassThatImplementsInterface.class.getName()));
			return Collections.unmodifiableSet(subTypes);
		}
	}

	static class SubClass extends ConcreteClass {
	}

	static class SubClassJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set getSubTypes() {
			final Set subTypes = new HashSet();
			subTypes.add(this.getType(SubSubClassThatImplementsInterface.class.getName()));
			return Collections.unmodifiableSet(subTypes);
		}
	}

	static class SubClassThatImplementsInterface extends ConcreteClass implements Interface{
	}

	static class SubClassThatImplementsInterfaceJavaClassTypeAdapter extends
			TestJavaClassTypeAdapter {
		public Set getSubTypes() {
			final Set subTypes = new HashSet();
			subTypes.add(this.getGeneratorContext().getType(SubSubClassThatImplementsInterface.class.getName()));
			return Collections.unmodifiableSet(subTypes);
		}
	}

	static class SubSubClassThatImplementsInterface extends
			SubClassThatImplementsInterface {
	}

	static class SubSubClassThatImplementsInterfaceJavaClassTypeAdapter extends
			TestJavaClassTypeAdapter {
		public Set getSubTypes() {
			return Collections.EMPTY_SET;
		}
	}

	static class AnotherConcreteClass {
	}

	static class AnotherConcreteClassJavaClassTypeAdapter extends TestJavaClassTypeAdapter {
		public Set getSubTypes() {
			return Collections.EMPTY_SET;
		}
	}
}
