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
package rocket.generator.rebind.type;

import java.text.DateFormat;
import java.util.Date;

import rocket.generator.rebind.GeneratorHelper;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.metadata.MetaData;
import rocket.generator.rebind.util.StringBufferSourceWriter;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;

abstract class NewConcreteOrInterfaceType extends NewConcreteNestedTypeOrInterfaceType{
	
	public NewConcreteOrInterfaceType(){
		super();
	}
	
	/**
	 * NewConcrete and NewInterfaceTypeImpls must be written using the GWT way using a ComposerFactory etc.
	 */
	public void write(final SourceWriter writer) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Adds a java doc comment that includes a variety of statistics about the
	 * class thats about to be generated.
	 * 
	 * @param composerFactory
	 */
	protected void setClassJavaDoc(final ClassSourceFileComposerFactory composerFactory) {
		ObjectHelper.checkNotNull("parameter:composerFactory", composerFactory);

		String comments = this.getComments();
		final String date = DateFormat.getInstance().format(new Date());
		final String generatorName = this.getGeneratorContext().getGenerator().getClass().getName();
		comments = comments + "\n\nGenerated at " + date + " by " + generatorName;

		final MetaData metaData = this.getMetaData();

		final StringBufferSourceWriter writer = new StringBufferSourceWriter();
		while( true ){
			final boolean noComments = StringHelper.isNullOrEmpty(comments);			
			final boolean noAnnotations = metaData.isEmpty();
			
			if( noComments && noAnnotations ){
				break;
			}
			
			// only has annotations...
			if( noComments && false == noAnnotations ){
				metaData.write(writer);
				break;
			}
			//only has comments...
			if( noComments && false == noAnnotations ){
				writer.println( comments );
				break;
			}
			
			// must have both annotations and comments...
				writer.println( comments );
				writer.println();
				metaData.write(writer);
				break;
		}			

		composerFactory.setJavaDocCommentForClass(writer.getBuffer().toString());
	}
	
	/**
	 * The name of the type being generated.
	 */
	private String name;

	public String getName() {
		GeneratorHelper.checkJavaTypeName("field:name", name);
		return name;
	}

	public boolean hasName() {
		return this.name != null;
	}

	public void setName(final String name) {		
		GeneratorHelper.checkJavaTypeName("parameter:name", name);
		this.name = name;
	}
}
