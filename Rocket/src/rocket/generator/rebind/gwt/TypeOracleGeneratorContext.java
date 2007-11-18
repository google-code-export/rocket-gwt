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
package rocket.generator.rebind.gwt;

import rocket.generator.rebind.GeneratorConstants;
import rocket.generator.rebind.GeneratorContextImpl;
import rocket.generator.rebind.packagee.Package;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

import com.google.gwt.core.ext.typeinfo.JArrayType;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JPackage;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;

/**
 * This GeneratorContext sources all its type info from the GWT TypeOracle.
 * @author Miroslav Pokorny
 */
public class TypeOracleGeneratorContext extends GeneratorContextImpl {

	public void setGeneratorContext(final com.google.gwt.core.ext.GeneratorContext generatorContext) {
		super.setGeneratorContext(generatorContext);
		this.preloadTypes();
	}
	
	/**
	 * Factory method which creates an adapter for any array type. This method should only ever be called once for each array type after which all references are cached.
	 * @param name
	 * @return A new JArrayTypeTypeAdapter
	 */
	protected Type createArrayType(final String name) {
		final String componentTypeName = name.substring( 0, name.length() - 2 );
		final JClassType componentType = this.getTypeOracle().findType(componentTypeName);
		ObjectHelper.checkNotNull("Unable to find component type [" + componentTypeName + "]", componentType);

		return this.createArrayType(componentType);
	}

	protected Type createArrayType(final JType componentType) {
		ObjectHelper.checkNotNull("parameter:componentType", componentType);

		final JArrayType jArrayType = (JArrayType) this.getTypeOracle().getArrayType(componentType);
		JArrayTypeTypeAdapter adapter = new JArrayTypeTypeAdapter();
		adapter.setGeneratorContext(this);
		adapter.setJArrayType(jArrayType);
		return adapter;
	}

	/**
	 * Factory method which creates an adapter for any type. This method should only ever be called once for each array type after which all references are cached.
	 * @param name
	 * @return a new JClassTypeTypeAdapter
	 */
	protected Type createClassType(final String name) {
		final JClassType jClassType = (JClassType) this.getTypeOracle().findType(name);
		JClassTypeTypeAdapter adapter = null;
		if (null != jClassType) {
			adapter = this.shouldBeSerializable(name) ? new ShouldBeSerializableJClassTypeAdapter() : new JClassTypeTypeAdapter();
			adapter.setGeneratorContext(this);
			adapter.setJClassType(jClassType);
		}
		return adapter;
	}
	
	/**
	 * This method only exists because the concrete collection types are not marked as serializable but really are.
	 * @param name
	 * @return
	 * 
	 * TODO GWT When all jdk concrete collection types are really serializable this hack will no longer be needed.
	 */
	protected boolean shouldBeSerializable( final String name ){
		boolean shouldBeSerializable = false;
		
		final String[] shouldBeSerializableTypeNames = Constants.SHOULD_BE_SERIALIZABLE_TYPENAMES;
		for( int i = 0; i < shouldBeSerializableTypeNames.length; i++ ){
			if( shouldBeSerializableTypeNames[ i ].equals( name )){
				shouldBeSerializable = true;
				break;
			}
		}
		
		return shouldBeSerializable;
	}

	/**
	 * Factory method which creates a package instance the first time a request
	 * is made.
	 * 
	 * @param name
	 * @return
	 */
	protected Package createPackage(final String name) {
		JPackagePackageAdapter packagee = null;
		final JPackage jPackage = this.findJPackage(name);
		if (null != jPackage) {
			packagee = new JPackagePackageAdapter();
			packagee.setJPackage(jPackage);
			packagee.setGeneratorContext(this);

		}
		return packagee;
	}

	protected JPackage findJPackage(final String name) {
		return this.getTypeOracle().findPackage(name);
	}

	protected void preloadTypes() {
		this.addType(this.createBooleanType());
		this.addType(this.createBooleanArrayType());

		this.addType(this.createByteType());
		this.addType(this.createByteArrayType());

		this.addType(this.createShortType());
		this.addType(this.createShortArrayType());

		this.addType(this.createIntType());
		this.addType(this.createIntArrayType());

		this.addType(this.createLongType());
		this.addType(this.createLongArrayType());

		this.addType(this.createFloatType());
		this.addType(this.createFloatArrayType());

		this.addType(this.createDoubleType());
		this.addType(this.createDoubleArrayType());

		this.addType(this.createCharType());
		this.addType(this.createCharArrayType());

		this.addType(this.createVoidType());
	}

	protected Type createBooleanType() {
		final BooleanJPrimitiveTypeTypeAdapter type = new BooleanJPrimitiveTypeTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createBooleanArrayType() {
		return this.createArrayType(JPrimitiveType.BOOLEAN);
	}

	public Type getBoolean() {
		return this.getType(GeneratorConstants.BOOLEAN);
	}

	protected Type createByteType() {
		final ByteJPrimitiveTypeTypeAdapter type = new ByteJPrimitiveTypeTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createByteArrayType() {
		return this.createArrayType(JPrimitiveType.BYTE);
	}

	public Type getByte() {
		return this.getType(GeneratorConstants.BYTE);
	}

	protected Type createShortType() {
		final ShortJPrimitiveTypeTypeAdapter type = new ShortJPrimitiveTypeTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createShortArrayType() {
		return this.createArrayType(JPrimitiveType.SHORT);
	}

	public Type getShort() {
		return this.getType(GeneratorConstants.SHORT);
	}

	protected Type createIntType() {
		final IntJPrimitiveTypeTypeAdapter type = new IntJPrimitiveTypeTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createIntArrayType() {
		return this.createArrayType(JPrimitiveType.INT);
	}

	public Type getInt() {
		return this.getType(GeneratorConstants.INT);
	}

	protected Type createLongType() {
		final LongJPrimitiveTypeTypeAdapter type = new LongJPrimitiveTypeTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createLongArrayType() {
		return this.createArrayType(JPrimitiveType.LONG);
	}

	public Type getLong() {
		return this.getType(GeneratorConstants.LONG);
	}

	protected Type createFloatType() {
		final FloatJPrimitiveTypeTypeAdapter type = new FloatJPrimitiveTypeTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createFloatArrayType() {
		return this.createArrayType(JPrimitiveType.FLOAT);
	}

	public Type getFloat() {
		return this.getType(GeneratorConstants.FLOAT);
	}

	protected Type createDoubleType() {
		final DoubleJPrimitiveTypeTypeAdapter type = new DoubleJPrimitiveTypeTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createDoubleArrayType() {
		return this.createArrayType(JPrimitiveType.DOUBLE);
	}

	public Type getDouble() {
		return this.getType(GeneratorConstants.DOUBLE);
	}

	protected Type createCharType() {
		final CharJPrimitiveTypeTypeAdapter type = new CharJPrimitiveTypeTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}

	protected Type createCharArrayType() {
		return this.createArrayType(JPrimitiveType.CHAR);
	}
	
	protected Type createVoidType() {
		final VoidJPrimitiveTypeTypeAdapter type = new VoidJPrimitiveTypeTypeAdapter();
		type.setGeneratorContext(this);
		return type;
	}
}
