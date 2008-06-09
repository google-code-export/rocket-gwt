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

import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.codeblock.CodeBlock;
import rocket.generator.rebind.codeblock.CollectionTemplatedCodeBlock;
import rocket.generator.rebind.codeblock.TemplatedFileCodeBlock;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * An abstraction for the build-candidates.txt template
 * 
 * @author Miroslav Pokorny
 */
public class BuildCandidatesTemplatedFile extends TemplatedFileCodeBlock {

	public BuildCandidatesTemplatedFile() {
		super();

		this.setTestMethods(this.createTestMethods());
	}

	@Override
	public void setNative(final boolean ignored) {
		throw new UnsupportedOperationException();
	}

	/**
	 * This list accumulates all the test methods that make up the test.
	 */
	private List<Method> testMethods;

	protected List<Method> getTestMethods() {
		Checker.notNull("field:testMethods", testMethods);
		return this.testMethods;
	}

	protected void setTestMethods(final List<Method> testMethods) {
		Checker.notNull("parameter:testMethods", testMethods);
		this.testMethods = testMethods;
	}

	protected List<Method> createTestMethods() {
		return new ArrayList<Method>();
	}

	public void addTestMethod(final Method method) {
		Checker.notNull("parameter:method", method);

		this.getTestMethods().add(method);
	}

	protected CodeBlock getAddTestsCodeBlock() {
		final AddTestTemplatedFile template = new AddTestTemplatedFile();

		return new CollectionTemplatedCodeBlock<Method>() {

			@Override
			public InputStream getInputStream() {
				return template.getInputStream();
			}

			@Override
			protected Object getValue0(final String name) {
				return template.getValue0(name);
			}

			@Override
			protected Collection<Method> getCollection() {
				return BuildCandidatesTemplatedFile.this.getTestMethods();
			}

			@Override
			protected void prepareToWrite( final Method method ) {
				template.setMethod(method);
			}

			@Override
			protected void writeBetweenElements(final SourceWriter writer) {
				writer.println("");
			}
		};
	}

	/**
	 * A reference to the test being run.
	 */
	private Type testRunner;

	protected Type getTestRunner() {
		Checker.notNull("field:testRunner", testRunner);
		return this.testRunner;
	}

	public void setTestRunner(final Type testRunner) {
		Checker.notNull("parameter:testRunner", testRunner);
		this.testRunner = testRunner;
	}

	@Override
	protected String getResourceName() {
		return Constants.BUILD_CANDIDATES_TEMPLATE;
	}

	@Override
	protected Object getValue0(final String name) {
		Object value = null;
		if (Constants.BUILD_CANDIDATES_ADD_TESTS.equals(name)) {
			value = this.getAddTestsCodeBlock();
		}
		return value;
	}
}
