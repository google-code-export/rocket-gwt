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

import rocket.generator.rebind.RebindHelper;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;

/**
 * A specialised GeneratorContext that helps TypeSerializerGenerator to generate
 * Serializer types as required.
 * 
 * @author Miroslav Pokorny
 */
public class JsonSerializerGeneratorContext extends rocket.generator.rebind.GeneratorContext {

	/**
	 * Returns the fully qualified name of a method that can be used to
	 * deserialize the given field.
	 * 
	 * @param field
	 * @return
	 */
	public String getQualifiedTransformerMethodName(final JField field) {
		String method = null;

		while (true) {
			final JType type = field.getType();

			if (type == this.getJavaUtilList()) {
				final JClassType elementListType = this.getListElementType(field);

				if (elementListType == this.getJavaLangBoolean()) {
					method = "this.asBooleanList";
					break;
				}
				if (elementListType == this.getJavaLangByte()) {
					method = "this.asByteList";
					break;
				}
				if (elementListType == this.getJavaLangShort()) {
					method = "this.asShortList";
					break;
				}
				if (elementListType == this.getJavaLangInteger()) {
					method = "this.asIntegerList";
					break;
				}
				if (elementListType == this.getJavaLangLong()) {
					method = "this.asLongList";
					break;
				}
				if (elementListType == this.getJavaLangFloat()) {
					method = "this.asFloatList";
					break;
				}
				if (elementListType == this.getJavaLangDouble()) {
					method = "this.asDoubleList";
					break;
				}
				if (elementListType == this.getJavaLangCharacter()) {
					method = "this.asCharacterList";
					break;
				}
				if (elementListType == this.getJavaLangString()) {
					method = "this.asStringList";
					break;
				}

				// calculate the transformer for type.
				final JClassType classType = (JClassType) elementListType;

				// generate a serializer for $classType
				this.generateReferencedTypeSerializer(classType);

				final String transformerClassName = this.getSerializerClassnameForType(classType);

				final StringBuffer asList = new StringBuffer();
				asList.append('(');
				asList.append(type.getQualifiedSourceName());
				asList.append(')');
				asList.append(transformerClassName);
				asList.append(".singleton.asList");

				method = asList.toString();
				break;
			}

			if (type == this.getJavaUtilSet()) {
				final JClassType elementSetType = this.getSetElementType(field);

				if (elementSetType == this.getJavaLangBoolean()) {
					method = "this.asBooleanSet";
					break;
				}
				if (elementSetType == this.getJavaLangByte()) {
					method = "this.asByteSet";
					break;
				}
				if (elementSetType == this.getJavaLangShort()) {
					method = "this.asShortSet";
					break;
				}
				if (elementSetType == this.getJavaLangInteger()) {
					method = "this.asIntegerSet";
					break;
				}
				if (elementSetType == this.getJavaLangLong()) {
					method = "this.asLongSet";
					break;
				}
				if (elementSetType == this.getJavaLangFloat()) {
					method = "this.asFloatSet";
					break;
				}
				if (elementSetType == this.getJavaLangDouble()) {
					method = "this.asDoubleSet";
					break;
				}
				if (elementSetType == this.getJavaLangCharacter()) {
					method = "this.asCharacterSet";
					break;
				}
				if (elementSetType == this.getJavaLangString()) {
					method = "this.asStringSet";
					break;
				}

				// calculate the transformer for type.
				final JClassType classType = (JClassType) elementSetType;

				// generate a serializer for $classType
				this.generateReferencedTypeSerializer(classType);

				final String transformerClassName = this.getSerializerClassnameForType(classType);

				final StringBuffer asSet = new StringBuffer();
				asSet.append('(');
				asSet.append(type.getQualifiedSourceName());
				asSet.append(')');
				asSet.append(transformerClassName);
				asSet.append(".singleton.asSet");

				method = asSet.toString();
				break;
			}

			if (type == this.getJavaUtilMap()) {
				final JClassType elementMapType = this.getMapValueType(field);

				if (elementMapType == this.getJavaLangBoolean()) {
					method = "this.asBooleanMap";
					break;
				}
				if (elementMapType == this.getJavaLangByte()) {
					method = "this.asByteMap";
					break;
				}
				if (elementMapType == this.getJavaLangShort()) {
					method = "this.asShortMap";
					break;
				}
				if (elementMapType == this.getJavaLangInteger()) {
					method = "this.asIntegerMap";
					break;
				}
				if (elementMapType == this.getJavaLangLong()) {
					method = "this.asLongMap";
					break;
				}
				if (elementMapType == this.getJavaLangFloat()) {
					method = "this.asFloatMap";
					break;
				}
				if (elementMapType == this.getJavaLangDouble()) {
					method = "this.asDoubleMap";
					break;
				}
				if (elementMapType == this.getJavaLangCharacter()) {
					method = "this.asCharacterMap";
					break;
				}
				if (elementMapType == this.getJavaLangString()) {
					method = "this.asStringMap";
					break;
				}

				// calculate the transformer for type.
				final JClassType classType = (JClassType) elementMapType;

				// generate a serializer for $classType
				this.generateReferencedTypeSerializer(classType);

				final String transformerClassName = this.getSerializerClassnameForType(classType);

				final StringBuffer asMap = new StringBuffer();
				asMap.append('(');
				asMap.append(type.getQualifiedSourceName());
				asMap.append(')');
				asMap.append(transformerClassName);
				asMap.append(".singleton.asMap");

				method = asMap.toString();
				break;
			}

			if (type == JPrimitiveType.BOOLEAN) {
				method = "this.asBoolean";
				break;
			}
			if (type == JPrimitiveType.BYTE) {
				method = "this.asByte";
				break;
			}
			if (type == JPrimitiveType.SHORT) {
				method = "this.asShort";
				break;
			}
			if (type == JPrimitiveType.INT) {
				method = "this.asInt";
				break;
			}
			if (type == JPrimitiveType.LONG) {
				method = "this.asLong";
				break;
			}
			if (type == JPrimitiveType.FLOAT) {
				method = "this.asFloat";
				break;
			}
			if (type == JPrimitiveType.DOUBLE) {
				method = "this.asDouble";
				break;
			}
			if (type == JPrimitiveType.CHAR) {
				method = "this.asChar";
				break;
			}
			if (type == this.getJavaLangString()) {
				method = "this.asString";
				break;
			}

			// calculate the transformer for type.
			final JClassType classType = (JClassType) type;

			// generate a serializer for $classType
			this.generateReferencedTypeSerializer(classType);

			final String transformerClassName = this.getSerializerClassnameForType(classType);

			final StringBuffer asObject = new StringBuffer();
			asObject.append('(');
			asObject.append(type.getQualifiedSourceName());
			asObject.append(')');
			asObject.append(transformerClassName);
			asObject.append(".singleton.asObject");

			method = asObject.toString();
			break;
		}

		return method;
	}

	protected void generateReferencedTypeSerializer(final JClassType type) {
		final TypeSerializerGenerator generator = new TypeSerializerGenerator();
		generator.setJsonSerializerGeneratorContext(this);
		generator.setType(type);
		generator.generate();
	}

	protected JClassType getListElementType(final JField field) {
		return this.getTypeFromAnnotation(field, Constants.LIST_ELEMENT_TYPE);
	}

	protected JClassType getSetElementType(final JField field) {
		return this.getTypeFromAnnotation(field, Constants.SET_ELEMENT_TYPE);
	}

	protected JClassType getMapValueType(final JField field) {
		return this.getTypeFromAnnotation(field, Constants.MAP_VALUE_TYPE);
	}

	/**
	 * Helper which retrieves a JClassType from a field given an annotation.
	 * 
	 * @param field
	 * @param annotation
	 * @return
	 */
	protected JClassType getTypeFromAnnotation(final JField field, final String annotation) {
		final String typeName = RebindHelper.getAnnotationValue(field, annotation);
		if (null == typeName) {
			throw new MissingAnnotationException("Unable to find the [" + annotation + "] annotation for the field " + field.getName()
					+ " which belongs to " + field.getEnclosingType().getQualifiedSourceName());
		}

		return (JClassType) this.getType(typeName);
	}

	protected String getGeneratedClassNameSuffix() {
		return Constants.SERIALIZER_SUFFIX;
	}

	public String getSerializerClassnameForType(final JClassType type) {
		return makeIntoAPublicClass(type) + this.getGeneratedClassNameSuffix();
	}
}
