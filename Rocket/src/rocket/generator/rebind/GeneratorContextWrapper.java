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
 */package rocket.generator.rebind;

import java.io.PrintWriter;
import java.util.Set;

import rocket.generator.rebind.packagee.Package;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.NewInterfaceType;
import rocket.generator.rebind.type.Type;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;

/**
 * Ready to go wrapper for any context that wishes to wrap another.
 * @author Miroslav Pokorny
 */
public class GeneratorContextWrapper implements GeneratorContext {

	public void addType(final Type type) {
		this.getWrapped().addType( type );
	}

	public Type getBoolean() {
		return this.getWrapped().getBoolean();
	}
	public Type getByte() {
		return this.getWrapped().getByte();
	}
	public Type getShort() {
		return this.getWrapped().getShort();
	}
	public Type getInt() {
		return this.getWrapped().getInt();
	}
	public Type getLong() {
		return this.getWrapped().getLong();
	}
	public Type getFloat() {
		return this.getWrapped().getFloat();
	}
	public Type getDouble() {
		return this.getWrapped().getDouble();
	}
	public Type getChar() {
		return this.getWrapped().getChar();
	}
	public Type getVoid() {
		return this.getWrapped().getVoid();
	}
	public Type getObject() {
		return this.getWrapped().getObject();
	}
	public Type getString() {
		return this.getWrapped().getString();
	}

	public Type findType(final String name) {
		return this.getWrapped().findType( name );
	}

	public Type getType(final String name) {
		return this.getWrapped().getType( name );
	}
	public Set getNewTypes() {		
		return this.getWrapped().getNewTypes();
	}
	
	public NewConcreteType newConcreteType() {
		return this.getWrapped().newConcreteType();
	}

	public NewInterfaceType newInterfaceType() {
		return this.getWrapped().newInterfaceType();
	}

	public String getGeneratedTypeName(final String name) {
		return this.getWrapped().getGeneratedTypeName(name);
	}

	public String getGeneratedTypeName(final String name, final String suffix) {
		return this.getWrapped().getGeneratedTypeName(name, suffix);
	}

	public Generator getGenerator() {
		return this.getWrapped().getGenerator();
	}

	public String getPackageName(final String fullyQualifiedClassName) {	
		return this.getWrapped().getPackageName(fullyQualifiedClassName);
	}
	public String getSimpleClassName(final String fullyQualifiedClassName) {
		return this.getWrapped().getSimpleClassName(fullyQualifiedClassName);
	}
	
	public PrintWriter tryCreateTypePrintWriter(String typeName) {
		return this.getWrapped().tryCreateTypePrintWriter(typeName);
	}

	public SourceWriter createSourceWriter(ClassSourceFileComposerFactory composerFactory, PrintWriter printWriter) {
		return this.getWrapped().createSourceWriter(composerFactory, printWriter);
	}

	public void trace(final String message) {
		this.getWrapped().trace(message);
	}
	public void trace(final String message, final Throwable throwable) {
		this.getWrapped().trace(message, throwable);
	}

	public void debug(final String message) {
		this.getWrapped().debug(message);
	}
	public void debug(final String message, final Throwable throwable) {
		this.getWrapped().debug(message, throwable);
	}

	public void info(final String message) {
		this.getWrapped().info(message);
	}
	public void info(final String message, final Throwable throwable) {
		this.getWrapped().info(message, throwable);
	}
	
	public void error(final String message) {
		this.getWrapped().error(message);
	}
	public void error(final String message, final Throwable throwable) {
		this.getWrapped().error(message, throwable);
	}

	public void warn(final String message) {
		this.getWrapped().warn(message);
	}
	public void warn(final String message, final Throwable throwable) {
		this.getWrapped().warn(message, throwable);
	}
	
	public void branch(final String message) {
		this.getWrapped().branch(message);
	}
	/**
	 * The generator context being wrapped
	 */
	private GeneratorContext wrapped;
	
	protected GeneratorContext getWrapped(){
		return this.wrapped;
	}
	
	public void setWrapped( final GeneratorContext wrapped ){
		this.wrapped = wrapped; 
	}

}
