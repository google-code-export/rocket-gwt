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
package rocket.generator.rebind.codeblock;

import java.util.Collection;
import java.util.Iterator;

import rocket.generator.rebind.SourceWriter;
import rocket.util.client.Checker;

/**
 * Iterates over a collection building a response when requested to write
 * itself.
 * 
 * A number of methods remain to be implemented by subclassing allowing fine
 * control over what actually gets outputted. A {@link #getIndex()} is also
 * available if one needs to know the position of the element being visited
 * relative to the entire collection.
 * 
 * The {@link #getInputStream()} is now invoked for each and every element.
 * 
 * The code fragment below shows how one typically integrates a
 * TemplatedFileCodeBlock with a CollectionTemplatedCodeBlock. For each every
 * element of the collection the TemplatedFileCodeBlock has its placeholders
 * updated and inserted into the output.
 * 
 * <pre>
 * 
 * protected CodeBlock buildXXXCodeBlock( final Collection collection ){
 * 	final ATemplatedFileCodeBlock template = ATemplatedFileCodeBlock();
 * 	return new CollectionTemplatedCodeBlock(){
 * 
 * 		public Collection getCollection(){
 * 			return collection;
 * 		}
 * 
 * 		public InputStream getInputStream(){
 * 			return forEachElement.getInputStream();
 * 		}
 * 
 * 		public Object getValue0( final String name ){
 * 			return template.getValue0( name );
 * 		}
 * 		protected void prepareToWrite( Object element ){
 * 			// update placeholders on template here...
 * 		}
 * 
 * 		// this method may be used to write a comma, new line etc between each element. 
 * 		protected void writeBetweenElements( SourceWriter writer ){
 * 			writer.println();
 * 		}
 * } 
 * 
 * 
 * </pre>
 * 
 * @author Miroslav Pokorny
 */
abstract public class CollectionTemplatedCodeBlock extends TemplatedCodeBlock {

	/**
	 * Returns the collection whose elements will be iterated over
	 * 
	 * @return
	 */
	abstract protected Collection getCollection();

	public boolean isEmpty() {
		return this.getCollection().isEmpty();
	}

	public void write(final SourceWriter writer) {
		writeCollection(writer);
	}

	protected void writeCollection(final SourceWriter writer) {
		Checker.notNull("parameter:writer", writer);

		final Iterator iterator = this.getCollection().iterator();
		while (iterator.hasNext()) {
			final Object element = iterator.next();

			this.prepareToWrite(element);
			this.write0(writer);

			if (false == iterator.hasNext()) {
				break;
			}

			this.writeBetweenElements(writer);
			this.setIndex(this.getIndex() + 1);
		}
	}

	/**
	 * This method is invoked for each element. Typically during this step sub
	 * classes are expected to populate this template ready for writing.
	 * 
	 * @param element
	 */
	abstract protected void prepareToWrite(Object element);

	/**
	 * This method is invoked after an element has been written providing that
	 * another exists...
	 * 
	 * @param writer
	 *            The writer
	 */
	abstract protected void writeBetweenElements(SourceWriter writer);

	private int index;

	protected int getIndex() {
		return this.index;
	}

	protected void setIndex(final int index) {
		this.index = index;
	}
}
