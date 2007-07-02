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
package rocket.beans.rebind;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import rocket.beans.client.BeanFactoryImpl;
import rocket.beans.rebind.bean.BeanDefinition;
import rocket.util.client.ObjectHelper;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

/**
 * This class is responsible for generating the actual BeanFactory
 * implementation java code.
 * 
 * @author Miroslav Pokorny
 */
public class BeanFactoryImplGenerator extends HasBeanFactoryGeneratorContext{

	/**
	 * Invoking this method generates the BeanFactory implementation.
	 * 
	 * @return the name of the generated class.
	 */
	public String generate() {
		final BeanFactoryGeneratorContext context = this.getBeanFactoryGeneratorContext();

		final String generatedClassName = context.getGeneratedClassname(this.getType().getQualifiedSourceName());
		final String packageName = context.getPackageName(generatedClassName);
		final String simpleClassName = context.getSimpleClassName(generatedClassName);
		final PrintWriter printWriter = context.tryCreateTypePrintWriter(packageName, simpleClassName);
		if (printWriter != null) {

			final ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(packageName, simpleClassName);
			composerFactory.setSuperclass(BeanFactoryImpl.class.getName());

			final SourceWriter sourceWriter = context.createSourceWriter(composerFactory, printWriter);
			this.writeBuildBeanFactoriesMethod(sourceWriter);
			this.writeCreateBeanFactoryMethods(sourceWriter);

			context.commitWriter(sourceWriter);
		}

		return generatedClassName;
	}

	/**
	 * This method may be used to generate an individual factory method to
	 * create a singleton or prototype BeanFactory.
	 * 
	 * @param writer
	 */
	protected void writeCreateBeanFactoryMethods(final SourceWriter writer) {
		final Iterator beanDefinitions = this.getBeanFactoryGeneratorContext().getBeanDefinitions().values().iterator();
		while (beanDefinitions.hasNext()) {
			final BeanDefinition beanDefinition = (BeanDefinition) beanDefinitions.next();
			beanDefinition.write(writer);
		}
	}

	/**
	 * Generates a method called populateBeanFactories which create BeanFactory
	 * instances for each bean definition encountered within the xml file.
	 * 
	 * @param writer
	 */
	protected void writeBuildBeanFactoriesMethod(final SourceWriter writer) {
		final String mapTypeName = Map.class.getName();

		// write method declaration..
		writer.println("protected " + mapTypeName + " buildFactoryBeans(){");
		writer.indent();

		// declare beans map instance...
		final String beans = "beans";
		writer.println("final " + mapTypeName + " " + beans + " = new " + HashMap.class.getName() + "();");

		final Iterator beanDefinitions = this.getBeanFactoryGeneratorContext().getBeanDefinitions().values().iterator();
		while (beanDefinitions.hasNext()) {
			final BeanDefinition beanDefinition = (BeanDefinition) beanDefinitions.next();

			final String id = beanDefinition.getId();
			final String factoryMethodName = beanDefinition.getBeanFactoryMethodName();

			writer.println(beans + ".put( \"" + id + "\", " + factoryMethodName + "() );");
		}

		// return beans
		writer.println("return " + beans + ";");

		// close method declaration.
		writer.outdent();
		writer.println("}");
	}
	

	/**
	 * The JClassType that corresponds to the BeanFactory being generated.
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
