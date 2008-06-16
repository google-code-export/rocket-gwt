/**
 * 
 */
package rocket.generator.rebind.type.generics;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.constructor.Constructor;
import rocket.generator.rebind.field.Field;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.packagee.Package;
import rocket.generator.rebind.type.AbstractType;
import rocket.generator.rebind.type.Type;

/**
 * A base class for any generic type. Unlike real types generic types cannot be compared for equality.
 * @author Miroslav Pokorny
 */
abstract public class GenericType extends AbstractType implements Type, CodeBlock {

	/* (non-Javadoc)
	 * @see rocket.generator.rebind.type.AbstractType#createConstructors()
	 */
	@Override
	protected Set<Constructor> createConstructors() {
		// // TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see rocket.generator.rebind.type.AbstractType#createFields()
	 */
	@Override
	protected Set<Field> createFields() {
		// // TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see rocket.generator.rebind.type.AbstractType#createInterfaces()
	 */
	@Override
	protected Set<Type> createInterfaces() {
		// // TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see rocket.generator.rebind.type.AbstractType#createMethods()
	 */
	@Override
	protected Set<Method> createMethods() {
		// // TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see rocket.generator.rebind.type.AbstractType#createNestedTypes()
	 */
	@Override
	protected Set<Type> createNestedTypes() {
		// // TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see rocket.generator.rebind.type.AbstractType#createSubTypes()
	 */
	@Override
	protected Set<Type> createSubTypes() {
		// // TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see rocket.generator.rebind.type.Type#getComponentType()
	 */
	public Type getComponentType() {
		// // TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see rocket.generator.rebind.type.Type#getJsniNotation()
	 */
	public String getJsniNotation() {
		// // TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see rocket.generator.rebind.type.Type#getName()
	 */
	public String getName() {
		// // TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see rocket.generator.rebind.type.Type#getPackage()
	 */
	public Package getPackage() {
		// // TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see rocket.generator.rebind.type.Type#getSimpleName()
	 */
	public String getSimpleName() {
		// // TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see rocket.generator.rebind.type.Type#getSuperType()
	 */
	public Type getSuperType() {
		// // TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see rocket.generator.rebind.type.Type#getVisibility()
	 */
	public Visibility getVisibility() {
		// // TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see rocket.generator.rebind.type.Type#getWrapper()
	 */
	public Type getWrapper() {
		// // TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see rocket.generator.rebind.type.Type#isAbstract()
	 */
	public boolean isAbstract() {
		 // TODO Auto-generated method stub
		return false;
	}

	public boolean isArray() {
		return true;
	}

	/* (non-Javadoc)
	 * @see rocket.generator.rebind.type.Type#isAssignableFrom(rocket.generator.rebind.type.Type)
	 */
	public boolean isAssignableFrom(Type type) {
		 // TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see rocket.generator.rebind.type.Type#isAssignableTo(rocket.generator.rebind.type.Type)
	 */
	public boolean isAssignableTo(Type type) {
		 // TODO Auto-generated method stub
		return false;
	}
	public boolean isFinal() {
		return false;
	}

	/* (non-Javadoc)
	 * @see rocket.generator.rebind.type.Type#isInterface()
	 */
	public boolean isInterface() {
		return false;
	}

	public boolean isPrimitive() {
		return false;
	}

	public boolean isEmpty(){
		return false;
	}
	
	/* (non-Javadoc)
	 * @see rocket.generator.rebind.metadata.HasMetadata#getMetadataValues(java.lang.String)
	 */
	public List<String> getMetadataValues(final String name) {
		return Collections.<String>emptyList();
	}
}
