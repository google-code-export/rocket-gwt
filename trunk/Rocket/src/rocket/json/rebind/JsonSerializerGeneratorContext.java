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
 * A specialised GeneratorContext that helps SerializableType to generate
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
	public String getQualifiedDeserializeMethodName(final JField field) {
		String method = null;

		while (true) {
			final JType type = field.getType();

			final String thisReference = "this";
			final String singleton = "singleton";
			
			if (type == this.getJavaUtilList()) {
				final JClassType elementListType = this.getListElementType(field);

				if (elementListType == this.getJavaLangBoolean()) {
					method = thisReference + ".asBooleanList";
					break;
				}
				if (elementListType == this.getJavaLangByte()) {
					method = thisReference + ".asByteList";
					break;
				}
				if (elementListType == this.getJavaLangShort()) {
					method = thisReference + ".asShortList";
					break;
				}
				if (elementListType == this.getJavaLangInteger()) {
					method = thisReference + ".asIntegerList";
					break;
				}
				if (elementListType == this.getJavaLangLong()) {
					method = thisReference + ".asLongList";
					break;
				}
				if (elementListType == this.getJavaLangFloat()) {
					method = thisReference + ".asFloatList";
					break;
				}
				if (elementListType == this.getJavaLangDouble()) {
					method = thisReference + ".asDoubleList";
					break;
				}
				if (elementListType == this.getJavaLangCharacter()) {
					method = thisReference + ".asCharacterList";
					break;
				}
				if (elementListType == this.getJavaLangString()) {
					method = thisReference + ".asStringList";
					break;
				}

				// calculate the transformer for type.
				final JClassType classType = (JClassType) elementListType;

				// generate a serializer for $classType
				this.generateReferencedTypeSerializer(classType);

				final String serializerClassName = this.getSerializerClassnameForType(classType);

				final StringBuffer asList = new StringBuffer();
				asList.append('(');
				asList.append(type.getQualifiedSourceName());
				asList.append(')');
				asList.append(serializerClassName);
				asList.append("." );
				asList.append( singleton );
				asList.append(".asList");

				method = asList.toString();
				break;
			}

			if (type == this.getJavaUtilSet()) {
				final JClassType elementSetType = this.getSetElementType(field);

				if (elementSetType == this.getJavaLangBoolean()) {
					method = thisReference + ".asBooleanSet";
					break;
				}
				if (elementSetType == this.getJavaLangByte()) {
					method = thisReference + ".asByteSet";
					break;
				}
				if (elementSetType == this.getJavaLangShort()) {
					method = thisReference + ".asShortSet";
					break;
				}
				if (elementSetType == this.getJavaLangInteger()) {
					method = thisReference + ".asIntegerSet";
					break;
				}
				if (elementSetType == this.getJavaLangLong()) {
					method = thisReference + ".asLongSet";
					break;
				}
				if (elementSetType == this.getJavaLangFloat()) {
					method = thisReference + ".asFloatSet";
					break;
				}
				if (elementSetType == this.getJavaLangDouble()) {
					method = thisReference + ".asDoubleSet";
					break;
				}
				if (elementSetType == this.getJavaLangCharacter()) {
					method = thisReference + ".asCharacterSet";
					break;
				}
				if (elementSetType == this.getJavaLangString()) {
					method = thisReference + ".asStringSet";
					break;
				}

				final JClassType classType = (JClassType) elementSetType;

				// generate a serializer for $classType
				this.generateReferencedTypeSerializer(classType);

				final String serializerClassName = this.getSerializerClassnameForType(classType);

				final StringBuffer asSet = new StringBuffer();
				asSet.append('(');
				asSet.append(type.getQualifiedSourceName());
				asSet.append(')');
				asSet.append(serializerClassName);				
				asSet.append("." );
				asSet.append( singleton );
				asSet.append(".asSet");				

				method = asSet.toString();
				break;
			}

			if (type == this.getJavaUtilMap()) {
				final JClassType elementMapType = this.getMapValueType(field);

				if (elementMapType == this.getJavaLangBoolean()) {
					method = thisReference + ".asBooleanMap";
					break;
				}
				if (elementMapType == this.getJavaLangByte()) {
					method = thisReference + ".asByteMap";
					break;
				}
				if (elementMapType == this.getJavaLangShort()) {
					method = thisReference + ".asShortMap";
					break;
				}
				if (elementMapType == this.getJavaLangInteger()) {
					method = thisReference + ".asIntegerMap";
					break;
				}
				if (elementMapType == this.getJavaLangLong()) {
					method = thisReference + ".asLongMap";
					break;
				}
				if (elementMapType == this.getJavaLangFloat()) {
					method = thisReference + ".asFloatMap";
					break;
				}
				if (elementMapType == this.getJavaLangDouble()) {
					method = thisReference + ".asDoubleMap";
					break;
				}
				if (elementMapType == this.getJavaLangCharacter()) {
					method = thisReference + ".asCharacterMap";
					break;
				}
				if (elementMapType == this.getJavaLangString()) {
					method = thisReference + ".asStringMap";
					break;
				}

				// calculate the transformer for type.
				final JClassType classType = (JClassType) elementMapType;

				// generate a serializer for $classType
				this.generateReferencedTypeSerializer(classType);

				final String serializerClassName = this.getSerializerClassnameForType(classType);

				final StringBuffer asMap = new StringBuffer();
				asMap.append('(');
				asMap.append(type.getQualifiedSourceName());
				asMap.append(')');
				asMap.append(serializerClassName);
				asMap.append("." );
				asMap.append( singleton );
				asMap.append(".asMap");			

				method = asMap.toString();
				break;
			}

			if (type == JPrimitiveType.BOOLEAN) {
				method = thisReference + ".asBoolean";
				break;
			}
			if (type == JPrimitiveType.BYTE) {
				method = thisReference + ".asByte";
				break;
			}
			if (type == JPrimitiveType.SHORT) {
				method = thisReference + ".asShort";
				break;
			}
			if (type == JPrimitiveType.INT) {
				method = thisReference + ".asInt";
				break;
			}
			if (type == JPrimitiveType.LONG) {
				method = thisReference + ".asLong";
				break;
			}
			if (type == JPrimitiveType.FLOAT) {
				method = thisReference + ".asFloat";
				break;
			}
			if (type == JPrimitiveType.DOUBLE) {
				method = thisReference + ".asDouble";
				break;
			}
			if (type == JPrimitiveType.CHAR) {
				method = thisReference + ".asChar";
				break;
			}
			if (type == this.getJavaLangString()) {
				method = thisReference + ".asString";
				break;
			}

			final JClassType classType = (JClassType) type;

			// generate a serializer for $classType
			this.generateReferencedTypeSerializer(classType);

			final String serializerClassName = this.getSerializerClassnameForType(classType);

			final StringBuffer asObject = new StringBuffer();
			asObject.append('(');
			asObject.append(type.getQualifiedSourceName());
			asObject.append(')');
			asObject.append(serializerClassName);			
			asObject.append("." );
			asObject.append( singleton );
			asObject.append(".asObject");						

			method = asObject.toString();
			break;
		}

		return method;
	}

	protected void generateReferencedTypeSerializer(final JClassType type) {
		final SerializableType generator = new SerializableType();
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
