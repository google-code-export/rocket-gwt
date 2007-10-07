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
package rocket.testing.rebind;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.CollectionTemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedCodeBlockException;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

import com.google.gwt.user.rebind.SourceWriter;

/**
 * An abstraction for the build-candidates.txt template
 * 
 * @author Miroslav Pokorny
 */
public class BuildCandidatesTemplatedFile extends TemplatedCodeBlock {

	public BuildCandidatesTemplatedFile() {
		super();
		setNative(false);
		this.setTestMethods(this.createTestMethods());
	}

	protected InputStream getInputStream() {
		final String filename = Constants.BUILD_CANDIDATES_TEMPLATE;
		final InputStream inputStream = this.getClass().getResourceAsStream(filename);
		if (null == inputStream) {
			throw new TemplatedCodeBlockException("Unable to find template file [" + filename + "]");
		}
		return inputStream;
	}

	/**
	 * This list accumulates all the test methods that make up the test.
	 */
	private List testMethods;

	protected List getTestMethods() {
		ObjectHelper.checkNotNull("field:testMethods", testMethods);
		return this.testMethods;
	}

	protected void setTestMethods(final List testMethods) {
		ObjectHelper.checkNotNull("parameter:testMethods", testMethods);
		this.testMethods = testMethods;
	}

	protected List createTestMethods() {
		return new ArrayList();
	}

	public void addTestMethod(final Method method) {
		ObjectHelper.checkNotNull("parameter:method", method);

		this.getTestMethods().add(method);
	}

	protected CodeBlock getAddTestsCodeBlock() {
		final AddTestTemplatedFile template = new AddTestTemplatedFile();

		return new CollectionTemplatedCodeBlock() {

			public InputStream getInputStream() {
				return template.getInputStream();
			}

			protected Object getValue0(final String name) {
				return template.getValue0(name);
			}

			protected Collection getCollection() {
				return BuildCandidatesTemplatedFile.this.getTestMethods();
			}

			protected void prepareToWrite(Object element) {
				final Method testMethod = (Method) element;

				template.setMethod(testMethod);
			}

			protected void writeBetweenElements(SourceWriter writer) {
				writer.println("");
			}
		};
	}

	/**
	 * A reference to the test being run.
	 */
	private Type testRunner;

	protected Type getTestRunner() {
		ObjectHelper.checkNotNull("field:testRunner", testRunner);
		return this.testRunner;
	}

	public void setTestRunner(final Type testRunner) {
		ObjectHelper.checkNotNull("parameter:testRunner", testRunner);
		this.testRunner = testRunner;
	}

	protected Object getValue0(final String name) {
		Object value = null;
		if (Constants.BUILD_CANDIDATES_ADD_TESTS.equals(name)) {
			value = this.getAddTestsCodeBlock();
		}
		return value;
	}

	protected void throwValueNotFoundException(final String name) {
		throw new TemplatedCodeBlockException("Value for placeholder [" + name + "] not found, template file ["
				+ Constants.BUILD_CANDIDATES_TEMPLATE + "]");
	}
}
