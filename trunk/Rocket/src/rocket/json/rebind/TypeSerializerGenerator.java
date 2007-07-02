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
package rocket.json.rebind;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rocket.generator.rebind.RebindHelper;
import rocket.json.client.JsonSerializer;
import rocket.util.client.ObjectHelper;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * This generator is able to generate a new transformer for a specific type. All
 * generated Serializers extend the JsonSerializer class which contains most of
 * the implementation.
 * 
 * @author Miroslav Pokorny
 */
public class TypeSerializerGenerator {

	/**
	 * Returns the name of the generated type.
	 * 
	 * @return
	 */
	public String getGeneratedSerializerClassname() {
		final String className = this.getType().getQualifiedSourceName();
		return this.getJsonSerializerGeneratorContext().getGeneratedClassname(className);
	}

	/**
	 * Tests and generates the transformer if necessary. THe serializable check
	 * is only done for referenced types and not super types as they might not
	 * implement JsonSerializable themselves.
	 */
	public void generate() {
		this.verifyTypeIsSerializable();
		this.verifyTypeHasNoArgumentsConstructor();

		generateWithoutSerializableCheck();
	}

	protected void generateWithoutSerializableCheck() {
		final JClassType type = this.getType();
		final JsonSerializerGeneratorContext context = this.getJsonSerializerGeneratorContext();

		final String serializerClassName = context.getSerializerClassnameForType(type);
		final String packageName = context.getPackageName(serializerClassName);
		final String simpleClassName = context.getSimpleClassName(serializerClassName);
		final PrintWriter printWriter = context.tryCreateTypePrintWriter(packageName, simpleClassName);
		if (printWriter != null) {
			this.write(this.createSourceWriter(type, printWriter));
		}

		this.generateSuperTypeDeserializerIfNecessary();
	}

	/**
	 * Factory field which creates a SourceWriter which will be used to write
	 * the generate code for the json client. The beginning of class is created
	 * ready for methods to be added.
	 * 
	 * @param type
	 * @param printWriter
	 * @return
	 */
	protected SourceWriter createSourceWriter(final JClassType type, final PrintWriter printWriter) {
		final JsonSerializerGeneratorContext context = this.getJsonSerializerGeneratorContext();

		final String classname = context.getSerializerClassnameForType(type);
		final String packageName = context.getPackageName(classname);
		final String simpleClassName = context.getSimpleClassName(classname);

		final ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(packageName, simpleClassName);

		String superTypeName = JsonSerializer.class.getName();
		final JClassType superType = type.getSuperclass();
		if (superType != context.getJavaLangObject()) {
			superTypeName = context.getSerializerClassnameForType(superType);
		}

		composerFactory.setSuperclass(superTypeName);
		return context.createSourceWriter(composerFactory, printWriter);
	}

	/**
	 * Begins the process that actually generates the new transformer
	 * 
	 * @param writer
	 */
	protected void write(final SourceWriter writer) {
		this.writeSingletonField(writer);
		this.writeAsObjectMethod(writer);
		this.writeSetFields(writer);
		this.getJsonSerializerGeneratorContext().commitWriter(writer);
	}

	/**
	 * Verifies that the type that this transformer will be created for is in
	 * fact serializable.
	 */
	protected void verifyTypeIsSerializable() {
		final JClassType type = this.getType();
		if (null == type.isClass()) {
			this
					.throwIsNotSerializableException("Serializable classes must be concrete classes and not interfaces or abstract classes like ["
							+ type.getQualifiedSourceName() + "].");
		}

		final JClassType serializable = this.getJsonSerializerGeneratorContext().getJsonSerializable();
		if (false == type.isAssignableTo(serializable)) {
			this.throwIsNotSerializableException(type);
		}
	}

	/**
	 * Convenience method that throws an exception complaining about the given
	 * type not being serializable.
	 * 
	 * @param type
	 */
	protected void throwIsNotSerializableException(final JClassType type) {
		throwIsNotSerializableException("The type " + type + " is not serializable");
	}

	protected void throwIsNotSerializableException(final String message) {
		throw new TypeIsNotSerializableException(message);
	}

	/**
	 * Verifies that the type currently being generated for has a no arguments
	 * constructor.
	 */
	protected void verifyTypeHasNoArgumentsConstructor() {
		final JClassType type = this.getType();
		if (null == type.findConstructor(new JType[0])) {
			this.throwMissingNoArgumentsConstructorException();
		}
	}

	protected void throwMissingNoArgumentsConstructorException() {
		throw new MissingNoArgumentsConstructorException("Serializable classes such as [" + this.getType().getQualifiedSourceName()
				+ "] must have a no arguments constructor.");
	}

	/**
	 * This method generates a static field that contains an instance of a Type
	 * deserializer.
	 * 
	 * @param writer
	 */
	protected void writeSingletonField(final SourceWriter writer) {
		final String generatedClassName = this.getGeneratedSerializerClassname();

		final StringBuffer singleton = new StringBuffer();
		singleton.append("final public static ");
		singleton.append(generatedClassName);
		singleton.append(" singleton = new ");
		singleton.append(generatedClassName);
		singleton.append("();");

		writer.println(singleton.toString());
		writer.println();
	}

	/**
	 * Generates the writeAsObject method which is responsible for instantiating
	 * a new instance and calling the setFields method to read and set all
	 * fields.
	 * 
	 * <pre>
	 * protected Object asObject( final ${Type} instance, final JSONValue jsonValue ){
	 * 		${Type} instance = null;
	 * 		final JSONObject jsonObject = jsonValue.isObject();
	 * 		if( null != jsonObject ){
	 * 			instance = new ${Type};
	 * 			this.setFields( instance, jsonObject );
	 * 		}
	 * 		return instance;
	 * }
	 * </pre>
	 * 
	 * @param writer
	 */
	protected void writeAsObjectMethod(final SourceWriter writer) {
		final JClassType type = this.getType();
		final String typeName = type.getQualifiedSourceName();

		final String jsonValueTypeName = JSONValue.class.getName();

		final StringBuffer asObjectMethodDeclaration = new StringBuffer();
		asObjectMethodDeclaration.append("public ");
		asObjectMethodDeclaration.append(Object.class.getName());
		asObjectMethodDeclaration.append(" asObject( final ");
		asObjectMethodDeclaration.append(jsonValueTypeName);
		asObjectMethodDeclaration.append(" jsonValue ){");
		writer.println(asObjectMethodDeclaration.toString());

		writer.indent();

		// declare instance. ${Type} instance = null;
		final StringBuffer declareInstance = new StringBuffer();
		declareInstance.append(typeName);
		declareInstance.append(" instance = null;");
		writer.println(declareInstance.toString());

		// cast jsonValue to JSONObject using final JSONObject jsonObject =
		// jsonValue.isObject();
		final StringBuffer castToJsonObject = new StringBuffer();
		castToJsonObject.append("final ");
		castToJsonObject.append(JSONObject.class.getName());
		castToJsonObject.append(" jsonObject = jsonValue.isObject();");
		writer.println(castToJsonObject.toString());

		// if( null != jsonObject ){
		writer.println("if ( null != jsonObject ){");
		writer.indent();

		final StringBuffer newInstance = new StringBuffer();
		newInstance.append("instance = new ");
		newInstance.append(typeName);
		newInstance.append("();");
		writer.println(newInstance.toString());

		// call this.setFields( instance, jsonObject );
		writer.println("this.setFields( instance, jsonObject );");

		// end if
		writer.println("}");
		writer.outdent();

		// return instance...
		writer.println("return instance;");

		writer.outdent();
		writer.println("}");// end of method
	}

	/**
	 * This method visits all fields belonging to this class inserting
	 * statements to set the field using the accompanying setter after fetching
	 * the property.
	 * 
	 * The method it generates looks something like this. After this method is
	 * completed all setters are then generated.
	 * 
	 * <pre>
	 * protected void setFields(final ${Type} instance, final JSONObject jsonObject) {
	 * 
	 * 	// for each primitive/String field of this type.
	 * 	this.setFieldName(instance, jsonObject.get(&quot;javascriptPropertyName&quot;));
	 * 
	 * 	// if has a supertype that is serializable call that.
	 * 	super.setFields(instance, jsonObject);
	 * }
	 * </pre>
	 * 
	 * @param writer
	 */
	protected void writeSetFields(final SourceWriter writer) {
		final JClassType type = this.getType();

		final StringBuffer methodDeclaration = new StringBuffer();
		methodDeclaration.append("protected void setFields( final ");
		methodDeclaration.append(type.getQualifiedSourceName());
		methodDeclaration.append(" instance, final ");
		methodDeclaration.append(JSONObject.class.getName());
		methodDeclaration.append(" jsonObject ){");
		writer.println(methodDeclaration.toString());
		writer.indent();

		final List setters = new ArrayList();
		final JsonSerializerGeneratorContext context = this.getJsonSerializerGeneratorContext();

		// visit all the fields belonging to $current
		final JField[] fields = type.getFields();
		for (int i = 0; i < fields.length; i++) {
			final JField field = fields[i];

			// skip static and transient fields...
			if (field.isStatic() || field.isTransient()) {
				continue;
			}
			if (field.isFinal()) {
				this.throwIsNotSerializableException("because final fields cannot be set.");
			}
			final JType fieldType = field.getType();
			if (fieldType.isArray() != null) {
				this.throwIsNotSerializableException("because array types are not supported. Change field to a List.");
			}

			// remember to write a setter after we finish generating this
			// method.
			setters.add(field);

			// insert the call to the setter after fetching of the property.
			final StringBuffer invokeFieldSetterAfterReadingValue = new StringBuffer();
			invokeFieldSetterAfterReadingValue.append("this.");
			invokeFieldSetterAfterReadingValue.append(RebindHelper.getSetterName(field.getName()));
			invokeFieldSetterAfterReadingValue.append("( instance, ");

			// insert method call to deserializer / value extractor...
			final String deserializeMethod = context.getQualifiedTransformerMethodName(field);
			invokeFieldSetterAfterReadingValue.append(deserializeMethod);
			invokeFieldSetterAfterReadingValue.append("(");

			// TODO need to check that $javascriptPropertyName is not used more than once.
			final String javascriptPropertyName = this.getJavascriptPropertyName(field);

			invokeFieldSetterAfterReadingValue.append("jsonObject.get( \"");
			invokeFieldSetterAfterReadingValue.append(javascriptPropertyName);
			invokeFieldSetterAfterReadingValue.append("\")"); // jsonObject.get(
			// "" )

			invokeFieldSetterAfterReadingValue.append(")"); // close

			invokeFieldSetterAfterReadingValue.append(");");
			writer.println(invokeFieldSetterAfterReadingValue.toString());
		}

		// if a deserializer exists to for this deserialize insert a call to its
		// setFields method
		final JType superType = type.getSuperclass();
		if (superType != context.getJavaLangObject()) {
			writer.println("super.setFields( instance, jsonObject );");
		}

		// close the method
		writer.outdent();
		writer.println("}");
		writer.println();

		// write the field setters...
		final Iterator setterIterator = setters.iterator();
		while (setterIterator.hasNext()) {
			writeFieldSetter((JField)setterIterator.next(), writer);
		}
	}

	/**
	 * This method adds a field setter for a single property to the class being
	 * generated.
	 * 
	 * <pre>
	 * native private void setFieldName( final ${InstanceType} instance, final ${FieldType} _fieldName ) \*-{
	 * instance.@this.type::fieldName = _fieldName; }-*\;
	 * }-* /
	 * </pre>
	 * 
	 * @param field
	 * @param writer
	 */
	protected void writeFieldSetter(final JField field, final SourceWriter writer) {
		final StringBuffer setterDeclaration = new StringBuffer();
		setterDeclaration.append("final native private void ");

		final String fieldName = field.getName();
		final String setterMethodName = RebindHelper.getSetterName(field.getName());
		setterDeclaration.append(setterMethodName);

		// the instance
		setterDeclaration.append("( final ");
		setterDeclaration.append(field.getEnclosingType().getQualifiedSourceName());
		setterDeclaration.append(" instance, ");

		// the new field value...
		setterDeclaration.append("final ");
		setterDeclaration.append(field.getType().getQualifiedSourceName());
		setterDeclaration.append(" _");
		setterDeclaration.append(fieldName);
		setterDeclaration.append(" ) /*-{");

		writer.println(setterDeclaration.toString());

		// write the actual jsni line of code that sets the field.
		final StringBuffer fieldAssignment = new StringBuffer();
		fieldAssignment.append("instance.@");
		fieldAssignment.append(field.getEnclosingType().getQualifiedSourceName());
		fieldAssignment.append("::");
		fieldAssignment.append(fieldName);
		fieldAssignment.append("=_");
		fieldAssignment.append(fieldName);
		fieldAssignment.append(";");

		writer.indent();
		writer.println(fieldAssignment.toString());
		writer.outdent();

		// write the end of the method
		writer.println("}-*/;");
		writer.println();
	}

	/**
	 * Helper which queries an annotation for the javascriptPropertyName of this
	 * field.
	 * 
	 * @param field
	 * @return
	 */
	protected String getJavascriptPropertyName(final JField field) {
		final String javascriptPropertyName = RebindHelper.getAnnotationValue(field, Constants.JAVASCRIPT_PROPERTY_NAME);
		if (false == RebindHelper.isValidJavascriptIdentifier(javascriptPropertyName)) {
			this.throwMissingAnnotationException("The javascript property name[" + javascriptPropertyName
					+ "] is not a valid javascript property.");
		}
		return javascriptPropertyName;
	}

	protected void throwMissingAnnotationException(final String message) {
		throw new MissingAnnotationException(message);
	}

	/**
	 * Attempts to generate a new transformer for the superclass of the type
	 * being generated.
	 * 
	 */
	protected void generateSuperTypeDeserializerIfNecessary() {
		final JClassType superType = this.getType().getSuperclass();
		final JsonSerializerGeneratorContext context = this.getJsonSerializerGeneratorContext();

		if (superType != context.getJavaLangObject()) {
			final TypeSerializerGenerator generator = new TypeSerializerGenerator();
			generator.setJsonSerializerGeneratorContext(this.getJsonSerializerGeneratorContext());
			generator.setType(superType);
			generator.generateWithoutSerializableCheck();
		}
	}

	/**
	 * A reference to the code generation context.
	 */
	private JsonSerializerGeneratorContext jsonSerializerGeneratorContext;

	protected JsonSerializerGeneratorContext getJsonSerializerGeneratorContext() {
		ObjectHelper.checkNotNull("field:jsonSerializerGeneratorContext", jsonSerializerGeneratorContext);
		return this.jsonSerializerGeneratorContext;
	}

	public void setJsonSerializerGeneratorContext(final JsonSerializerGeneratorContext generatorContext) {
		ObjectHelper.checkNotNull("parameter:jsonSerializerGeneratorContext", generatorContext);
		this.jsonSerializerGeneratorContext = generatorContext;
	}

	/**
	 * The JClassType that corresponds to the Serializer being generated.
	 */
	private JClassType type;

	protected JClassType getType() {
		ObjectHelper.checkNotNull("field:type", type);
		return type;
	}

	public void setType(final JClassType type) {
		ObjectHelper.checkNotNull("parameter:type", type);
		this.type = type;
	}
}
