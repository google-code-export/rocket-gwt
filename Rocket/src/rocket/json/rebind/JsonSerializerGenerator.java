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

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.json.client.JSONObject;
import rocket.generator.rebind.Generator;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.codeblock.CodeBlockList;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.field.Field;
import rocket.generator.rebind.field.NewField;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.methodparameter.NewMethodParameter;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

/**
 * Generates a JsonSerializer for the given type if one does not already exists.
 * 
 * @author Miroslav Pokorny
 */
public class JsonSerializerGenerator extends Generator {

	/**
	 * Builds a new Deserializer type but first checks if the given type also
	 * has a super type that needs a deserializer.
	 * 
	 * @param type
	 *            The type passed to GWT.create()
	 * @param newTypeName
	 *            The name of the new type being generated
	 */
	protected NewConcreteType assembleNewType(final Type type,
			final String newTypeName) {
		ObjectHelper.checkNotNull("parameter:type", type);
		GeneratorHelper.checkJavaTypeName("parameter:newTypeName", newTypeName);

		NewConcreteType deserializer = null;

		this.verifyTypeHasNoArgumentsConstructor(type);

		// check if super type deserializer exists for type...
		final GeneratorContext context = this.getGeneratorContext();
		final Type superType = type.getSuperType();
		final Type objectType = context.getObject();

		// test if type has a superType and potentially create a serializer for
		// that.
		if (false == superType.equals(objectType)) {
			this.createNewTypeIfNecessary(superType.getName());
		}

		// now that all super type deserializers exist create the new type.
		deserializer = this.createConcreteType(newTypeName, type);
		this.overrideReadFieldsMethods(deserializer, type);
		this.overrideReadObjectMethod(deserializer, type);

		this.overrideWriteFieldsMethods(deserializer, type);
		this.overrideWriteJsonMethod(deserializer, type);

		this.addDeserializerSingletonField(deserializer, type);

		return deserializer;
	}

	/**
	 * Creates both the readFields and list readers for each list.
	 * 
	 * @param deserializer
	 *            The deserializer itself.
	 * @param type
	 *            The type the deserializer is being generated for
	 */
	protected void overrideReadFieldsMethods(
			final NewConcreteType deserializer, final Type type) {
		ObjectHelper.checkNotNull("parameter:deserializer", deserializer);
		ObjectHelper.checkNotNull("parameter:type", type);

		final GeneratorContext context = this.getGeneratorContext();
		context.debug("override " + Constants.READ_FIELDS_METHOD
				+ " and creating list setters for type ["
				+ deserializer.getName() + "]");

		final NewMethod readFields = deserializer.newMethod();
		readFields.setAbstract(false);
		readFields.setFinal(false);
		readFields.setName(Constants.READ_FIELDS_METHOD);
		readFields.setNative(false);
		readFields.setReturnType(context.getVoid());
		readFields.setStatic(false);
		readFields.setVisibility(Visibility.PROTECTED);

		final NewMethodParameter readFieldsJsonObjectParameter = readFields
				.newParameter();
		readFieldsJsonObjectParameter.setFinal(true);
		readFieldsJsonObjectParameter.setName("jsonObject");
		final Type jsonObjectType = this.getJsonObject();
		readFieldsJsonObjectParameter.setType(jsonObjectType);

		final NewMethodParameter readFieldsInstanceParameter = readFields
				.newParameter();
		readFieldsInstanceParameter.setFinal(true);
		readFieldsInstanceParameter.setName("instance");
		readFieldsInstanceParameter.setType(type);

		final CodeBlockList body = new CodeBlockList();
		readFields.setBody(body);

		final Type stringType = context.getString();

		final Iterator fields = type.getFields().iterator();
		while (fields.hasNext()) {
			final Field field = (Field) fields.next();
			if (field.isStatic() || field.isTransient()) {
				continue;
			}
			if (field.isFinal()) {
				throwFinalFieldsCannotBeDeserialized(field);
			}

			final SetFieldTemplatedFile writeMethodBody = new SetFieldTemplatedFile();
			writeMethodBody.setField(field);

			// create the setter method itself.
			final NewMethod setter = deserializer.newMethod();
			setter.setAbstract(false);
			setter.setFinal(false);
			setter.setName(GeneratorHelper.buildSetterName(field.getName()));
			setter.setNative(true);
			setter.setReturnType(context.getVoid());
			setter.setStatic(false);
			setter.setVisibility(Visibility.PRIVATE);
			setter.setBody(writeMethodBody);

			// add its instance parameter
			final NewMethodParameter setterInstanceParameter = setter
					.newParameter();
			setterInstanceParameter.setFinal(true);
			setterInstanceParameter.setName("instance");
			setterInstanceParameter.setType(type);
			writeMethodBody.setInstance(setterInstanceParameter);

			// add the value parameter
			final NewMethodParameter setterValueParameter = setter
					.newParameter();
			setterValueParameter.setFinal(true);
			setterValueParameter.setName("value");
			setterValueParameter.setType(field.getType());
			writeMethodBody.setValue(setterValueParameter);

			final Type fieldType = field.getType();

			// simple type ?
			if (fieldType.isPrimitive() || fieldType.equals(stringType)) {
				final SetSimpleTemplatedFile template = new SetSimpleTemplatedFile();

				template.setFieldSetter(setter);
				template.setInstance(readFieldsInstanceParameter);
				template.setJsonObject(readFieldsJsonObjectParameter);
				template.setJavascriptPropertyName(this.getJavascriptPropertyName(field));
				template.setSerializer(this.getSerializer(field));

				body.add(template);
				continue;
			}

			final SetComplexTemplatedFile template = new SetComplexTemplatedFile();
			template.setFieldSetter(setter);
			template.setInstance(readFieldsInstanceParameter);
			template.setJsonObject(readFieldsJsonObjectParameter);
			template.setJavascriptPropertyName(this.getJavascriptPropertyName(field));
			template.setFieldType(fieldType);
			
			final Type serializer = this.getSerializer( field );
			template.setSerializer( serializer );
			
			final String readMethodName = this.selectReadMethod(fieldType);
			final Method readMethod = serializer.getMostDerivedMethod( readMethodName , Arrays.asList( new Type[]{ this.getJsonValue() }));
			template.setReadMethod(readMethod);

			body.add(template);
		}
	}

	protected void throwFinalFieldsCannotBeDeserialized(final Field field) {
		throw new JsonSerializerGeneratorException(
				"Final instance fields cannot be deserialized, list: " + field);
	}

	protected String selectReadMethod( final Type type ){
		String methodName = null;
		while( true ){
			if( type.equals( this.getList() )){
				methodName = Constants.SET_COMPLEX_READ_LIST_METHOD;
				break;
			}
			if( type.equals( this.getSet() )){
				methodName = Constants.SET_COMPLEX_READ_SET_METHOD;
				break;
			}
			if( type.equals( this.getMap() )){
				methodName = Constants.SET_COMPLEX_READ_MAP_METHOD;
				break;
			}
			methodName = Constants.SET_COMPLEX_READ_OBJECT_METHOD;
			break;
		}
		return methodName;
	}
	/**
	 * Creates the readObject method and adds it to the given deserializer.
	 * 
	 * @param deserializer
	 *            The deserializer itself.
	 * @param type
	 *            The type the deserializer is being generated for
	 */
	protected void overrideReadObjectMethod(final NewConcreteType deserializer,
			final Type type) {
		ObjectHelper.checkNotNull("parameter:deserializer", deserializer);
		ObjectHelper.checkNotNull("parameter:type", type);

		final GeneratorContext context = this.getGeneratorContext();
		context.debug("override " + Constants.READ_COMPLEX_METHOD_NAME
				+ "() for type [" + type.getName() + "]");

		final Type jsonSerializer = this.getJsonSerializer();

		final Method readObject = jsonSerializer.getMethod(
				Constants.READ_COMPLEX_METHOD_NAME, Arrays
						.asList(new Type[] { this.getJsonValue() }));
		final NewMethod newReadObject = readObject.copy(deserializer);
		final MethodParameter jsonValue = (MethodParameter) newReadObject
				.getParameters().get(0);

		final ReadComplexTemplatedFile body = new ReadComplexTemplatedFile();
		body.setDeserializerType(type);
		body.setJsonValue(jsonValue);

		newReadObject.setBody(body);
	}

	/**
	 * Helper which fetches the javascript property name annotation from the
	 * given list throwing an exception if its not found.
	 * 
	 * @param list
	 * @return
	 */
	protected String getJavascriptPropertyName(final Field field) {
		ObjectHelper.checkNotNull("parameter:list", field);

		final List values = field
				.getMetadataValues(Constants.JAVASCRIPT_PROPERTY_NAME_ANNOTATION);
		if (null == values || values.size() != 1) {
			throw new JsonSerializerGeneratorException(
					"Unable to find javascript property name for " + field);
		}
		return (String) values.get(0);
	}

	/**
	 * Factory method which creates a new ConcreteType awaiting methods and
	 * fields.
	 * 
	 * @param newTypeName
	 * @param type
	 * @return
	 */
	protected NewConcreteType createConcreteType(final String newTypeName,
			final Type type) {
		ObjectHelper.checkNotNull("parameter:type", type);

		final GeneratorContext context = this.getGeneratorContext();
		context.debug("creating new type [" + type.getName() + "]");

		final NewConcreteType newType = context.newConcreteType();
		newType.setAbstract(false);
		newType.setFinal(false);
		newType.setName(newTypeName);

		// extend either JsonSerializerType or the generated type of type...
		Type superType = type.getSuperType();
		String superTypeName = Constants.JSON_SERIALIZER_TYPE;
		if (false == superType.equals(context.getObject())) {
			superTypeName = context.getGeneratedTypeName(superType.getName());
		}
		superType = context.getType(superTypeName);
		newType.setSuperType(superType);

		return newType;
	}

	protected void verifyTypeHasNoArgumentsConstructor(final Type type) {
		ObjectHelper.checkNotNull("parameter:type", type);

		if (false == type.hasNoArgumentsConstructor()) {
			this.throwMissingNoArgumentsConstructorException(type);
		}
	}

	protected void throwMissingNoArgumentsConstructorException(final Type type) {
		throw new JsonSerializerGeneratorException(
				"Serializable classes such as [" + type
						+ "] must have a no arguments constructor.");
	}

	/**
	 * This method generates methods that will eventually copy all the fields
	 * for instance of the given type to a JSONObject.
	 * 
	 * @param deserializer
	 * @param type
	 */
	protected void overrideWriteFieldsMethods(
			final NewConcreteType deserializer, final Type type) {
		ObjectHelper.checkNotNull("parameter:deserializer", deserializer);
		ObjectHelper.checkNotNull("parameter:type", type);

		ObjectHelper.checkNotNull("parameter:deserializer", deserializer);
		ObjectHelper.checkNotNull("parameter:type", type);

		final GeneratorContext context = this.getGeneratorContext();
		context.debug("override " + Constants.WRITE_FIELDS_WRITE_METHODS
				+ " and creating list getters for type ["
				+ deserializer.getName() + "]");

		final NewMethod writeFields = deserializer.newMethod();
		writeFields.setAbstract(false);
		writeFields.setFinal(false);
		writeFields.setName(Constants.WRITE_FIELDS_WRITE_METHODS);
		writeFields.setNative(false);
		writeFields.setReturnType(context.getVoid());
		writeFields.setStatic(false);
		writeFields.setVisibility(Visibility.PROTECTED);

		final NewMethodParameter instanceParameter = writeFields.newParameter();
		instanceParameter.setFinal(true);
		instanceParameter.setName("instance");
		instanceParameter.setType(context.getObject());

		final NewMethodParameter jsonObjectParameter = writeFields
				.newParameter();
		jsonObjectParameter.setFinal(true);
		jsonObjectParameter.setName("jsonObject");
		final Type jsonObjectType = this.getJsonObject();
		jsonObjectParameter.setType(jsonObjectType);

		final WriteFieldsTemplatedFile body = new WriteFieldsTemplatedFile();
		body.setInstance(instanceParameter);
		body.setInstanceType(type);
		body.setJsonObject(jsonObjectParameter);
		writeFields.setBody(body);

		// find all fields belonging to type
		final Iterator fields = type.getFields().iterator();
		while (fields.hasNext()) {
			final Field field = (Field) fields.next();
			if (field.isStatic() || field.isTransient()) {
				continue;
			}

			final NewMethod fieldGetter = this.createFieldGetter(deserializer, field);
			final String javascriptPropertyName = this.getJavascriptPropertyName(field);
			final Type serializer = this.getSerializer( field );

			body.addField(javascriptPropertyName, fieldGetter, serializer);
		} // while
	}

	/**
	 * Creates a method that uses jsni to retrieve a list from a given instance
	 */
	protected NewMethod createFieldGetter(final NewConcreteType deserializer,
			final Field field) {
		ObjectHelper.checkNotNull("parameter:deserializer", deserializer);
		ObjectHelper.checkNotNull("parameter:list", field);

		final NewMethod fieldGetter = deserializer.newMethod();
		fieldGetter.setAbstract(false);
		fieldGetter.setFinal(false);
		fieldGetter.setName(Constants.GET_FIELD_METHOD_PREFIX
				+ this.capitalize(field.getName()));
		fieldGetter.setNative(true);
		fieldGetter.setReturnType(field.getType());
		fieldGetter.setStatic(false);
		fieldGetter.setVisibility(Visibility.PRIVATE);

		final NewMethodParameter instance = fieldGetter.newParameter();
		instance.setName(Constants.GET_FIELD_INSTANCE);
		instance.setFinal(true);
		instance.setType(field.getEnclosingType());

		final GetFieldTemplatedFile fieldGetterBody = new GetFieldTemplatedFile();
		fieldGetterBody.setField(field);
		fieldGetterBody.setInstance(instance);
		fieldGetter.setBody(fieldGetterBody);

		return fieldGetter;
	}

	/**
	 * Creates the writeJson method and adds it to the given deserializer.
	 * 
	 * @param deserializer
	 *            The deserializer itself.
	 * @param type
	 *            The type the deserializer is being generated for
	 */
	protected void overrideWriteJsonMethod(final NewConcreteType deserializer,
			final Type type) {
		ObjectHelper.checkNotNull("parameter:deserializer", deserializer);
		ObjectHelper.checkNotNull("parameter:type", type);

		final GeneratorContext context = this.getGeneratorContext();
		context.debug("override " + Constants.WRITE_JSON_METHOD_NAME
				+ "() for type [" + type.getName() + "]");

		final Type jsonSerializer = this.getJsonSerializer();

		final Method writeJson = jsonSerializer.getMethod(
				Constants.WRITE_JSON_METHOD_NAME, Arrays
						.asList(new Type[] { context.getObject() }));
		final NewMethod newAsJson = writeJson.copy(deserializer);
		final WriteJsonTemplatedFile body = new WriteJsonTemplatedFile();
		newAsJson.setBody(body);
	}

	/**
	 * Adds as public static list called singleton to the deserializer class.
	 * The list is also initialized to a new deserializer.
	 * 
	 * <pre>
	 * public final static Deserializer serializer = new Deserializer();
	 * </pre>
	 * 
	 * @param deserializer
	 * @param type
	 */
	protected void addDeserializerSingletonField(
			final NewConcreteType deserializer, final Type type) {
		ObjectHelper.checkNotNull("parameter:deserializer", deserializer);
		ObjectHelper.checkNotNull("parameter:type", type);

		final TemplatedCodeBlock value = new TemplatedCodeBlock() {
			protected Object getValue0(String name) {
				Object value = null;
				if (name.equals("deserializer")) {
					value = deserializer;
				}
				return value;
			}

			public InputStream getInputStream() {
				return new StringBufferInputStream("new ${deserializer}();");
			}
		};

		final NewField singleton = deserializer.newField();
		singleton.setFinal(true);
		singleton.setName(Constants.SERIALIZER_SINGLETON);
		singleton.setStatic(true);
		singleton.setTransient(false);
		singleton.setType(deserializer);
		singleton.setValue(value);
		singleton.setVisibility(Visibility.PUBLIC);
	}

	protected GeneratorContext createGeneratorContext() {
		return new GeneratorContext() {
			protected String getGeneratedTypeNameSuffix() {
				return Constants.SERIALIZER_SUFFIX;
			}
		};
	}

	protected Type getJsonSerializer() {
		return this.getGeneratorContext().getType(
				Constants.JSON_SERIALIZER_TYPE);
	}

	protected Type getJsonValue() {
		return this.getGeneratorContext().getType(Constants.JSON_VALUE_TYPE);
	}

	protected Type getJsonObject() {
		return this.getGeneratorContext().getType(Constants.JSON_OBJECT_TYPE);
	}

	protected Type getList() {
		return this.getGeneratorContext().getType(Constants.JAVA_UTIL_LIST);
	}

	protected Type getSet() {
		return this.getGeneratorContext().getType(Constants.JAVA_UTIL_SET);
	}

	protected Type getMap() {
		return this.getGeneratorContext().getType(Constants.JAVA_UTIL_MAP);
	}

	protected String capitalize(final String propertyName) {
		return Character.toUpperCase(propertyName.charAt(0))
				+ propertyName.substring(1);
	}

	protected Type getSerializer( final Field field ){
		Type serializerType = null;
		
		while( true ){
			final Type fieldType = field.getType();
		if (fieldType.equals( this.getList())) {
			serializerType = this.getListElementType(field);
			break;
		}
		if (fieldType.equals( this.getSet() )) {
			serializerType = this.getSetElementType(field);
			break;
		}
		if (fieldType.equals( this.getMap() )) {
			serializerType = this.getMapValueType(field);
			break;
		}
		serializerType = fieldType;
		break;
		}
		return this.getSerializer0( serializerType );
	}
	
	protected Type getSerializer0(final Type type) {
		Type serializer = null;

		while (true) {
			final GeneratorContext context = this.getGeneratorContext();
			{
				final Type booleanType = context.getBoolean();
				if (type.equals(booleanType)
						|| type.equals(booleanType.getWrapper())) {
					serializer = context.getType(Constants.BOOLEAN_SERIALIZER);
					break;
				}
			}
			{
				final Type byteType = context.getByte();
				if (type.equals(byteType) || type.equals(byteType.getWrapper())) {
					serializer = context.getType(Constants.BYTE_SERIALIZER);
					break;
				}
			}

			{
				final Type shortType = context.getShort();
				if (type.equals(shortType)
						|| type.equals(shortType.getWrapper())) {
					serializer = context.getType(Constants.SHORT_SERIALIZER);
					break;
				}
			}

			{
				final Type intType = context.getInt();
				if (type.equals(intType) || type.equals(intType.getWrapper())) {
					serializer = context.getType(Constants.INT_SERIALIZER);
					break;
				}
			}

			{
				final Type longType = context.getLong();
				if (type.equals(longType) || type.equals(longType.getWrapper())) {
					serializer = context.getType(Constants.LONG_SERIALIZER);
					break;
				}
			}

			{
				final Type floatType = context.getFloat();
				if (type.equals(floatType)
						|| type.equals(floatType.getWrapper())) {
					serializer = context.getType(Constants.FLOAT_SERIALIZER);
					break;
				}
			}

			{
				final Type doubleType = context.getDouble();
				if (type.equals(doubleType)
						|| type.equals(doubleType.getWrapper())) {
					serializer = context.getType(Constants.DOUBLE_SERIALIZER);
					break;
				}
			}

			{
				final Type charType = context.getChar();
				if (type.equals(charType) || type.equals(charType.getWrapper())) {
					serializer = context.getType(Constants.CHAR_SERIALIZER);
					break;
				}
			}
			if (type.equals(context.getString())) {
				serializer = context.getType(Constants.STRING_SERIALIZER);
				break;
			}

			final String serializerTypeName = this
					.createNewTypeIfNecessary(type.getName());
			serializer = context.getType(serializerTypeName);
			break;
		}
		return serializer;
	}

	protected Type getListElementType(final Field field) {
		return this.getTypeFromAnnotation(field, Constants.LIST_ELEMENT_TYPE);
	}

	protected Type getSetElementType(final Field field) {
		return this.getTypeFromAnnotation(field, Constants.SET_ELEMENT_TYPE);
	}

	protected Type getMapValueType(final Field field) {
		return this.getTypeFromAnnotation(field, Constants.MAP_VALUE_TYPE);
	}

	/**
	 * Retrieves the type after reading the type from an annotation belonging to
	 * the given list.
	 * 
	 * @param list
	 * @param annotation
	 * @return
	 */
	protected Type getTypeFromAnnotation(final Field field,
			final String annotation) {
		ObjectHelper.checkNotNull("parameter:list", field);

		final List values = field.getMetadataValues(annotation);
		if (values.size() != 1) {
			throw new JsonSerializerGeneratorException("Unable to find the ["
					+ annotation + "] annotation for the list " + field);
		}

		final String typeName = (String) values.get(0);
		if (null == typeName) {
			throw new JsonSerializerGeneratorException("Unable to find the ["
					+ annotation + "] annotation for the list " + field);
		}

		return this.getGeneratorContext().getType(typeName);
	}

}
