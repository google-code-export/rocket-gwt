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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import rocket.generator.rebind.Generator;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.codeblock.ManyCodeBlocks;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.constructor.Constructor;
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
	 * @param newTypeName
	 */
	protected NewConcreteType assembleNewType(final Type type, final String newTypeName) {
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
		this.overrideSetFieldAndFieldSetterMethods(deserializer, type);
		this.overridingAsObjectMethod(deserializer, type);
		this.addDeserializerSingletonField(deserializer, type);

		return deserializer;
	}

	/**
	 * Adds as public static field called singleton to the deserializer class.
	 * The field is also initialized to a new deserializer.
	 * 
	 * <pre>
	 * public final static Deserializer singleton = new Deserializer();
	 * </pre>
	 * 
	 * @param deserializer
	 * @param type
	 */
	protected void addDeserializerSingletonField(final NewConcreteType deserializer, final Type type) {
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
		singleton.setName("singleton");
		singleton.setStatic(true);
		singleton.setTransient(false);
		singleton.setType(deserializer);
		singleton.setValue(value);
		singleton.setVisibility(Visibility.PUBLIC);
	}

	/**
	 * Creates both the setFields and field setters for each field.
	 * 
	 * @param deserializer
	 *            The deserializer itself.
	 * @param type
	 *            The type the deserializer is being generated for
	 */
	protected void overrideSetFieldAndFieldSetterMethods(final NewConcreteType deserializer, final Type type) {
		ObjectHelper.checkNotNull("parameter:deserializer", deserializer);
		ObjectHelper.checkNotNull("parameter:type", type);

		final GeneratorContext context = this.getGeneratorContext();
		context.debug("overriding setFields and creating field setters for type [" + deserializer.getName() + "]");

		final NewMethod setFields = deserializer.newMethod();
		setFields.setAbstract(false);
		setFields.setFinal(false);
		setFields.setName("setFields");
		setFields.setNative(false);
		setFields.setReturnType(context.getVoid());
		setFields.setStatic(false);
		setFields.setVisibility(Visibility.PROTECTED);

		final NewMethodParameter setFieldsInstanceParameter = setFields.newParameter();
		setFieldsInstanceParameter.setFinal(true);
		setFieldsInstanceParameter.setName("instance");
		setFieldsInstanceParameter.setType(type);

		final NewMethodParameter setFieldsJsonObjectParameter = setFields.newParameter();
		setFieldsJsonObjectParameter.setFinal(true);
		setFieldsJsonObjectParameter.setName("jsonObject");
		final Type jsonObjectType = this.getJsonObject();
		setFieldsJsonObjectParameter.setType(jsonObjectType);

		final ManyCodeBlocks setFieldsBody = new ManyCodeBlocks();
		setFields.setBody(setFieldsBody);

		final Type listType = this.getJavaUtilList();
		final Type setType = this.getJavaUtilSet();
		final Type mapType = this.getJavaUtilMap();
		final Type stringType = context.getString();
		final Type booleanWrapperType = context.getBoolean().getWrapper();
		final Type byteWrapperType = context.getByte().getWrapper();
		final Type shortWrapperType = context.getShort().getWrapper();
		final Type integerWrapperType = context.getInt().getWrapper();
		final Type longWrapperType = context.getLong().getWrapper();
		final Type floatWrapperType = context.getFloat().getWrapper();
		final Type doubleWrapperType = context.getDouble().getWrapper();
		final Type characterWrapperType = context.getChar().getWrapper();

		final Iterator fields = type.getFields().iterator();
		while (fields.hasNext()) {
			final FieldSetterTemplatedFile setterMethodBody = new FieldSetterTemplatedFile();

			final Field field = (Field) fields.next();
			setterMethodBody.setField(field);

			// create the setter method itself.
			final NewMethod fieldSetter = deserializer.newMethod();
			fieldSetter.setAbstract(false);
			fieldSetter.setFinal(false);
			fieldSetter.setName(GeneratorHelper.buildSetterName(field.getName()));
			fieldSetter.setNative(true);
			fieldSetter.setReturnType(context.getVoid());
			fieldSetter.setStatic(false);
			fieldSetter.setVisibility(Visibility.PRIVATE);
			fieldSetter.setBody(setterMethodBody);

			// add its instance parameter
			final NewMethodParameter fieldSetterInstanceParameter = fieldSetter.newParameter();
			fieldSetterInstanceParameter.setFinal(true);
			fieldSetterInstanceParameter.setName("instance");
			fieldSetterInstanceParameter.setType(type);
			setterMethodBody.setInstance(fieldSetterInstanceParameter);

			// add the value parameter
			final NewMethodParameter setterValueParameter = fieldSetter.newParameter();
			setterValueParameter.setFinal(true);
			setterValueParameter.setName("value");
			setterValueParameter.setType(field.getType());
			setterMethodBody.setValue(setterValueParameter);

			final Type fieldType = field.getType();

			Type collectionType = null;
			String suffix = null;
			boolean primitiveStringType = false;

			if (fieldType.isPrimitive() || fieldType.equals(stringType)) {
				primitiveStringType = true;
				suffix = "";
			}

			if (fieldType.equals(listType)) {
				collectionType = this.getListElementType(field);
				suffix = "List";
			}
			if (fieldType.equals(setType)) {
				collectionType = this.getSetElementType(field);
				suffix = "Set";
			}
			if (fieldType.equals(mapType)) {
				collectionType = this.getMapValueType(field);
				suffix = "Map";
			}

			final List jsonValueParameterList = Arrays.asList(new Type[] { this.getJsonValue() });

			// deserializing a primitive, string or a collection of them.
			if (primitiveStringType
					|| null != collectionType
					&& (collectionType.isPrimitive() || collectionType.equals(booleanWrapperType) || collectionType.equals(byteWrapperType)
							|| collectionType.equals(shortWrapperType) || collectionType.equals(integerWrapperType)
							|| collectionType.equals(longWrapperType) || collectionType.equals(floatWrapperType)
							|| collectionType.equals(doubleWrapperType) || collectionType.equals(characterWrapperType) || collectionType
							.equals(stringType))) {

				final InvokePrimitiveOrStringFieldSetterTemplatedFile invokeFieldSetter = new InvokePrimitiveOrStringFieldSetterTemplatedFile();
				setFieldsBody.add(invokeFieldSetter);

				invokeFieldSetter.setFieldSetter(fieldSetter);
				invokeFieldSetter.setInstance(setFieldsInstanceParameter);
				invokeFieldSetter.setJsonObject(setFieldsJsonObjectParameter);
				invokeFieldSetter.setJavascriptPropertyName(this.getJavascriptPropertyName(field));

				final String fieldTypeSimpleName = collectionType == null ? fieldType.getSimpleName() : collectionType.getSimpleName();

				final String asMethodName = "as" + Character.toUpperCase(fieldTypeSimpleName.charAt(0)) + fieldTypeSimpleName.substring(1)
						+ suffix;
				final Method asMethod = deserializer.getMostDerivedMethod(asMethodName, jsonValueParameterList);
				invokeFieldSetter.setAsMethod(asMethod);

				continue;
			}

			final InvokeObjectFieldSetterTemplatedFile invokeFieldSetter = new InvokeObjectFieldSetterTemplatedFile();
			setFieldsBody.add(invokeFieldSetter);

			invokeFieldSetter.setFieldSetter(fieldSetter);
			invokeFieldSetter.setInstance(setFieldsInstanceParameter);
			invokeFieldSetter.setJsonObject(setFieldsJsonObjectParameter);

			invokeFieldSetter.setJavascriptPropertyName(this.getJavascriptPropertyName(field));
			invokeFieldSetter.setFieldType(fieldType);

			final Type objectType = null == collectionType ? fieldType : collectionType;

			final String fieldDeserializerTypeName = this.createNewTypeIfNecessary(objectType.getName());
			final Type fieldTypeDeserializer = context.getType(fieldDeserializerTypeName);
			invokeFieldSetter.setFieldTypeDeserializer(fieldTypeDeserializer);

			final String asMethodName = "as" + (StringHelper.isNullOrEmpty(suffix) ? "Object" : suffix);
			final Method asMethod = deserializer.getMostDerivedMethod(asMethodName, jsonValueParameterList);
			invokeFieldSetter.setAsMethod(asMethod);
		}
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
	 * the given field.
	 * 
	 * @param field
	 * @param annotation
	 * @return
	 */
	protected Type getTypeFromAnnotation(final Field field, final String annotation) {
		ObjectHelper.checkNotNull("parameter:field", field);

		final List values = field.getMetadataValues(annotation);
		final String typeName = (String) values.get(0);
		if (null == typeName) {
			throw new JsonSerializerGeneratorException("Unable to find the [" + annotation + "] annotation for the field " + field);
		}

		return this.getGeneratorContext().getType(typeName);
	}

	/**
	 * Creates the asObject method and adds it to the given deserializer.
	 * 
	 * @param deserializer
	 *            The deserializer itself.
	 * @param type
	 *            The type the deserializer is being generated for
	 */
	protected void overridingAsObjectMethod(final NewConcreteType deserializer, final Type type) {
		ObjectHelper.checkNotNull("parameter:deserializer", deserializer);
		ObjectHelper.checkNotNull("parameter:type", type);

		final GeneratorContext context = this.getGeneratorContext();
		context.debug("overriding asObject() for type [" + type.getName() + "]");

		final Type jsonSerializer = this.getJsonSerializer();

		final Method asObject = jsonSerializer.getMethod("asObject", Arrays.asList(new Type[] { this.getJsonValue() }));
		final NewMethod newAsObject = asObject.copy(deserializer);
		final MethodParameter jsonValue = (MethodParameter) newAsObject.getParameters().get(0);

		final AsObjectTemplatedFile body = new AsObjectTemplatedFile();
		body.setDeserializerType(type);
		body.setJsonValue(jsonValue);

		newAsObject.setBody(body);
	}

	/**
	 * Helper which fetches the javascript property name annotation from the
	 * given field throwing an exception if its not found.
	 * 
	 * @param field
	 * @return
	 */
	protected String getJavascriptPropertyName(final Field field) {
		ObjectHelper.checkNotNull("parameter:field", field);

		final List values = field.getMetadataValues(Constants.JAVASCRIPT_PROPERTY_NAME);
		if (null == values || values.size() != 1) {
			throw new JsonSerializerGeneratorException("Unable to find javascript property name for " + field);
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
	protected NewConcreteType createConcreteType(final String newTypeName, final Type type) {
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

	/**
	 * Helper which returns the method name that takes a JSONValue and returns a
	 * primitive or String
	 * 
	 * @param field
	 * @return
	 */
	protected String buildAsJsonValueToPrimitiveMethodName(final Field field) {
		ObjectHelper.checkNotNull("parameter:field", field);

		final StringBuilder builder = new StringBuilder();

		builder.append("as");

		final String typeName = field.getType().getSimpleName();
		builder.append(Character.toUpperCase(typeName.charAt(0)));

		if (typeName.length() > 1) {
			builder.append(typeName.substring(1));
		}

		return builder.toString();
	}

	protected void verifyTypeHasNoArgumentsConstructor(final Type type) {
		ObjectHelper.checkNotNull("parameter:type", type);

		if ( false == type.hasNoArgumentsConstructor() ) {
			this.throwMissingNoArgumentsConstructorException(type);
		}
	}

	protected void throwMissingNoArgumentsConstructorException(final Type type) {
		throw new JsonSerializerGeneratorException("Serializable classes such as [" + type + "] must have a no arguments constructor.");
	}

	protected GeneratorContext createGeneratorContext() {
		return new GeneratorContext() {
			protected String getGeneratedTypeNameSuffix() {
				return Constants.SERIALIZER_SUFFIX;
			}
		};
	}

	protected Type getJsonSerializer() {
		return this.getGeneratorContext().getType(Constants.JSON_SERIALIZER_TYPE);
	}

	protected Type getJsonValue() {
		return this.getGeneratorContext().getType(Constants.JSON_VALUE_TYPE);
	}

	protected Type getJsonObject() {
		return this.getGeneratorContext().getType(Constants.JSON_OBJECT_TYPE);
	}

	protected Type getJavaUtilList() {
		return this.getGeneratorContext().getType(Constants.JAVA_UTIL_LIST);
	}

	protected Type getJavaUtilSet() {
		return this.getGeneratorContext().getType(Constants.JAVA_UTIL_SET);
	}

	protected Type getJavaUtilMap() {
		return this.getGeneratorContext().getType(Constants.JAVA_UTIL_MAP);
	}
}
