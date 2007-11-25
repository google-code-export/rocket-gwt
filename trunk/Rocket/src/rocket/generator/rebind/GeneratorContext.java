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
package rocket.generator.rebind;

import java.io.PrintWriter;
import java.util.Set;

import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.NewInterfaceType;
import rocket.generator.rebind.type.Type;

import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;

/**
 * Defines a generator context.
 * 
 * @author Miroslav Pokorny
 */
public interface GeneratorContext {
	
	String getGeneratedTypeName(final String name, final String suffix );
	
	Type findType(final String name);
	Type getType(final String name);	
	Type getBoolean();
	Type getByte();
	Type getShort();
	Type getInt();
	Type getLong();
	Type getFloat();
	Type getDouble();
	Type getChar();
	Type getVoid();
	Type getObject();
	Type getString();
	
	void addType(final Type type);
	Set getNewTypes();
	NewConcreteType newConcreteType();
	NewInterfaceType newInterfaceType();
	PrintWriter tryCreateTypePrintWriter(final String typeName);
	SourceWriter createSourceWriter(final ClassSourceFileComposerFactory composerFactory, final PrintWriter printWriter);
	
	String getPackageName(final String fullyQualifiedClassName);
	String getSimpleClassName(final String fullyQualifiedClassName);

	void trace(final String message);
	void trace(final String message, final Throwable throwable);
	void debug(final String message);
	void debug(final String message, final Throwable throwable);
	void info(final String message);
	void info(final String message, final Throwable throwable);
	void warn(final String message);
	void warn(final String message, final Throwable throwable);
	void error(final String message);
	void error(final String message, final Throwable throwable);
	void branch(final String message);
	
	boolean isDebugEnabled();
	boolean isInfoEnabled();
	boolean isTraceEnabled();
	
	Generator getGenerator();
}
