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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import rocket.generator.rebind.GeneratorConstants;
import rocket.generator.rebind.GeneratorContextImpl;
import rocket.generator.rebind.GeneratorException;
import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.packagee.Package;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.NewConcreteTypeImpl;
import rocket.generator.rebind.type.NewInterfaceType;
import rocket.generator.rebind.type.NewInterfaceTypeImpl;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JArrayType;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JPackage;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;

/**
 * This GeneratorContext sources all its type info from the GWT TypeOracle.
 * 
 * @author Miroslav Pokorny
 */
public class TypeOracleGeneratorContext extends GeneratorContextImpl {

	/**
	 * Factory method which creates a new concrete type.
	 * 
	 * @param name
	 *            The name of the new concrete type
	 * @return The new concrete type.
	 */
	public NewConcreteType newConcreteType(final String name) {
		NewConcreteTypeImpl type = null;

		final PrintWriter printWriter = this.tryCreateTypePrintWriter(name);
		if (null != printWriter) {
			type = new NewConcreteTypeImpl();
			type.setGeneratorContext(this);
			type.setName(name);
			type.setPrintWriter(printWriter);
			type.setSuperType(this.getObject());
			type.setVisibility(Visibility.PUBLIC);

			this.addNewType(type);
		}

		return type;
	}

	/**
	 * Factory method which creates a new interface type.
	 * 
	 * @param name
	 *            The name of the new interface
	 * @return The new interface type.
	 */
	public NewInterfaceType newInterfaceType(final String name) {
		NewInterfaceTypeImpl type = null;

		final PrintWriter printWriter = this.tryCreateTypePrintWriter(name);
		if (null != printWriter) {
			type = new NewInterfaceTypeImpl();
			type.setGeneratorContext(this);
			type.setName(name);
			type.setPrintWriter(printWriter);
			type.setSuperType(this.getObject());
			type.setVisibility(Visibility.PUBLIC);

			this.addNewType(type);
		}

		return type;
	}

	/**
	 * Tests if a class has already been generated. If the class does not exist
	 * a PrintWriter is returned which may eventually be used to create the new
	 * class.
	 * 
	 * @param typeName
	 * @return Null if the class does not exist otherwise returns a PrintWriter
	 */
	public PrintWriter tryCreateTypePrintWriter(final String typeName) {
		GeneratorHelper.checkJavaTypeName("parameter:typeName", typeName);

		final String packageName = this.getPackageName(typeName);
		final String simpleClassName = this.getSimpleClassName(typeName);
		return this.getGeneratorContext().tryCreate(this.getTreeLogger(), packageName, simpleClassName);
	}

	/**
	 * Creates a sourceWriter. All attempts to create a SourceWriter eventually
	 * call this method once they have setup the ClassSourceFileComposerFactory
	 * and have gotten a PrintWriter
	 * 
	 * @param composerFactory
	 * @param printWriter
	 * @return A SourceWriter instance which may be used to emit a new
	 *         standalone type.
	 */
	public SourceWriter createSourceWriter(final ClassSourceFileComposerFactory composerFactory, final PrintWriter printWriter) {
		final com.google.gwt.user.rebind.SourceWriter sourceWriter = composerFactory.createSourceWriter(this.getGeneratorContext(),
				printWriter);

		return new SourceWriter() {
			public void beginJavaDocComment() {
				sourceWriter.beginJavaDocComment();
			}

			public void endJavaDocComment() {
				sourceWriter.endJavaDocComment();
			}

			public void indent() {
				sourceWriter.indent();
			}

			public void outdent() {
				sourceWriter.outdent();
			}

			public void print(final String string) {
				sourceWriter.print(string);
			}

			public void println() {
				sourceWriter.println();
			}

			public void println(final String string) {
				sourceWriter.println(string);
			}

			public void commit() {
				sourceWriter.commit(TypeOracleGeneratorContext.this.getTreeLogger());
			}

			public void rollback() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public void setGeneratorContext(final com.google.gwt.core.ext.GeneratorContext generatorContext) {
		super.setGeneratorContext(generatorContext);
		this.preloadTypes();
	}

	/**
	 * Factory method which creates an adapter for any array type. This method
	 * should only ever be called once for each array type after which all
	 * references are cached.
	 * 
	 * @param name
	 *            The array type name
	 * @return A new JArrayTypeTypeAdapter
	 */
	protected Type createArrayType(final String name) {
		final String componentTypeName = name.substring(0, name.length() - 2);
		final JClassType componentType = this.getTypeOracle().findType(componentTypeName);
		Checker.notNull("Unable to find array component type \"" + componentTypeName + "\".", componentType);

		return this.createArrayType(componentType);
	}

	protected Type createArrayType(final JType componentType) {
		Checker.notNull("parameter:componentType", componentType);

		// FIX RAW HERE

		final JArrayType jArrayType = (JArrayType) this.getTypeOracle().getArrayType(componentType);
		JArrayTypeTypeAdapter adapter = new JArrayTypeTypeAdapter();
		adapter.setGeneratorContext(this);
		adapter.setJArrayType(jArrayType);
		return adapter;
	}

	/**
	 * Factory method which creates an adapter for any type. This method should
	 * only ever be called once for each array type after which all references
	 * are cached.
	 * 
	 * @param name
	 * @return a new JClassTypeTypeAdapter
	 */
	protected Type createClassType(final String name) {
		Checker.notEmpty("parameter:name", name);

		final JClassType jClassType = (JClassType) this.getTypeOracle().findType(name);
		JClassTypeTypeAdapter adapter = null;
		if (null != jClassType) {
			adapter = new JClassTypeTypeAdapter();
			adapter.setGeneratorContext(this);
			adapter.setJClassType(jClassType);
		}
		return adapter;
	}

	/**
	 * Factory method which creates a package instance the first time a request
	 * is made.
	 * 
	 * @param name
	 *            The name of the package
	 * @return The package
	 */
	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	/**
	 * This method maybe used to create a resource or file that will be
	 * available to the public path of an application or compilation.
	 * 
	 * @param filename
	 * @return Null if the resource already existed otherwise returns a stream.
	 *         When closed the stream will be committed.
	 */
	@Override
	public OutputStream createResource(final String filename) {
		try {
			return this.createResource0(filename);
		} catch (final UnableToCompleteException caught) {
			throw new GeneratorException("Something went wrong whilst attempting to retrieve an OutputStream for the resource \""
					+ filename + "\".");
		}
	}

	protected OutputStream createResource0(final String filename) throws UnableToCompleteException {
		OutputStream outputStream = null;
		while (true) {
			final TreeLogger logger = this.getTreeLogger();
			final com.google.gwt.core.ext.GeneratorContext context = this.getGeneratorContext();
			outputStream = context.tryCreateResource(logger, filename);
			if (null == outputStream) {
				break;
			}

			outputStream = new ResourceOutputStream(outputStream);
			break;
		}
		return outputStream;

	}

	/**
	 * Wrapper around a GWT provided OutputStream that commits a resource when
	 * this stream is closed.
	 */
	class ResourceOutputStream extends OutputStream {
		ResourceOutputStream(final OutputStream outputStream) {
			super();

			this.setOutputStream(outputStream);
		}

		OutputStream outputStream;

		OutputStream getOutputStream() {
			Checker.notNull("field:outputStream", outputStream);
			return this.outputStream;
		}

		void setOutputStream(final OutputStream outputStream) {
			Checker.notNull("parameter:outputStream", outputStream);
			this.outputStream = outputStream;
		}

		@Override
		public void write(final byte[] bytes) throws IOException {
			this.getOutputStream().write(bytes);
		}

		@Override
		public void write(final byte[] bytes, final int start, final int length) throws IOException {
			this.getOutputStream().write(bytes, start, length);
		}

		@Override
		public void write(final int byteValue) throws IOException {
			this.getOutputStream().write(byteValue);
		}

		@Override
		public void flush() throws IOException {
			this.getOutputStream().flush();
		}

		@Override
		public void close() throws IOException {
			try {
				final OutputStream outputStream = this.getOutputStream();
				final TreeLogger logger = TypeOracleGeneratorContext.this.getTreeLogger();
				TypeOracleGeneratorContext.this.getGeneratorContext().commitResource(logger, outputStream);
			} catch (final UnableToCompleteException caught) {
				throw new IOException("Something went wrong whilst attempting to commit resource, reason: \"" + caught.getMessage()
						+ "\"");
			}
		}
	}
}
