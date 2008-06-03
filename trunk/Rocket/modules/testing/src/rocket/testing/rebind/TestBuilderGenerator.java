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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import rocket.generator.rebind.Generator;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.method.NewMethod;
import rocket.generator.rebind.type.NewConcreteType;
import rocket.generator.rebind.type.Type;
import rocket.generator.rebind.visitor.VirtualMethodVisitor;
import rocket.testing.client.TestBuilder;
import rocket.testing.client.TestMethodTestBuilder;
import rocket.util.client.Checker;

/**
 * This generator may be used to create a list containing all the public methods
 * that start with test. It seeks to emulate the process JUnit performs to find
 * tests.
 * 
 * @author Miroslav Pokorny
 */
public class TestBuilderGenerator extends Generator {

	protected NewConcreteType assembleNewType(final Type type, final String newTypeName) {
		final NewConcreteType testBuilder = this.createTestBuilder(newTypeName);
		final Type webPageTestRunner = this.getTestRunner(type);
		this.addBuildCandidates(testBuilder, webPageTestRunner);
		return testBuilder;
	}

	/**
	 * Creates a new concrete type that implements TestBuilder
	 * 
	 * @param newTypeName
	 * @return
	 */
	protected NewConcreteType createTestBuilder(final String newTypeName) {
		final NewConcreteType testBuilder = this.getGeneratorContext().newConcreteType(newTypeName);
		testBuilder.setAbstract(false);
		testBuilder.setFinal(true);
		testBuilder.setSuperType(this.getTestMethodTestBuilder());
		testBuilder.addInterface(this.getTestBuilder());

		return testBuilder;
	}

	protected Type getTestRunner(final Type type) {
		final Method method = type.getMethod(Constants.BUILD_CANDIDATES_METHOD, Collections.EMPTY_LIST);
		final List webPageTestRunners = method.getMetadataValues(Constants.TEST_ANNOTATION);
		if (webPageTestRunners.size() != 1) {
			throwTestAnnotationProblem(type);
		}
		final String webPageTestRunnerName = (String) webPageTestRunners.get(0);
		final Type webPageTestRunner = this.getGeneratorContext().findType(webPageTestRunnerName);
		if (null == webPageTestRunner) {
			throwUnableToFindTest(webPageTestRunnerName);
		}
		this.getGeneratorContext().info("Test type is " + webPageTestRunnerName);
		return webPageTestRunner;
	}

	protected void throwTestAnnotationProblem(final Type type) {
		throw new TestBuilderGeneratorException("Unable to find " + Constants.TEST_ANNOTATION + " annotation on type " + type);
	}

	protected void throwUnableToFindTest(final String test) {
		throw new TestBuilderGeneratorException("Unable to find test \"" + test + "\".");
	}

	/**
	 * Loops thru and discovers all the public test instance methods belonging
	 * to the webPageTestRunner type. For each test method a Test instance is
	 * created
	 * 
	 * @param testBuilder
	 * @param webPageTestRunner
	 */
	protected void addBuildCandidates(final NewConcreteType testBuilder, final Type webPageTestRunner) {
		Checker.notNull("parameter:testBuilder", testBuilder);

		final GeneratorContext context = this.getGeneratorContext();

		final Method method = this.getTestBuilder().findMethod(Constants.BUILD_CANDIDATES_METHOD, Collections.EMPTY_LIST);
		final NewMethod newMethod = method.copy(testBuilder);
		newMethod.setAbstract(false);
		newMethod.setFinal(true);
		newMethod.setNative(false);

		final BuildCandidatesTemplatedFile body = new BuildCandidatesTemplatedFile();
		newMethod.setBody(body);

		final Type voidType = this.getGeneratorContext().getVoid();

		final List methods = new ArrayList();

		// find and add public methods that start with test
		final VirtualMethodVisitor testMethodFinder = new VirtualMethodVisitor() {

			protected boolean visit(final Method method) {

				while (true) {
					if (method.getVisibility() != Visibility.PUBLIC) {
						break;
					}
					if (false == method.getName().startsWith(Constants.TEST_METHOD_NAME_PREFIX)) {
						context.debug("Ignoring method " + method + " because it doesnt start with public.");
						break;
					}

					// test method must return void
					final Type returnType = method.getReturnType();
					if (returnType != voidType) {
						throwTestMethodDoesntReturnVoid(method);
					}

					// test method must have no parameters.
					final List parameters = method.getParameters();
					if (false == parameters.isEmpty()) {
						throwTestMethodHasParameters(method);
					}

					// public void test*() method found...
					methods.add(method);
					break;
				}

				return false;
			}

			protected boolean skipJavaLangObjectMethods() {
				return true;
			}
		};
		testMethodFinder.start(webPageTestRunner);

		Collections.sort(methods, new Comparator() {
			public int compare(final Object method, final Object otherMethod) {
				return TestBuilderGenerator.this.getOrder((Method) method) - TestBuilderGenerator.this.getOrder((Method) otherMethod);
			}
		});

		final Iterator iterator = methods.iterator();
		while (iterator.hasNext()) {
			final Method testMethod = (Method) iterator.next();
			body.addTestMethod(testMethod);
			context.debug("Adding test method " + testMethod);
		}
	}

	protected void throwTestMethodDoesntReturnVoid(final Method method) {
		throw new TestBuilderGeneratorException("The test method should return void and not " + method.getReturnType() + ", method: "
				+ method);
	}

	protected void throwTestMethodHasParameters(final Method method) {
		throw new TestBuilderGeneratorException("The test method should have no parameters, method: " + method);
	}

	protected int getOrder(final Method method) {
		final List orderAnnotation = method.getMetadataValues(Constants.ORDER_ANNOTATION);
		if (orderAnnotation.size() != 1) {
			throwUnableToFindOrderAnnotation(method);
		}
		int order = 0;
		try {
			order = Integer.parseInt((String) orderAnnotation.get(0));
		} catch (final NumberFormatException notANumber) {
			throwUnableToConvertOrderAnnotationToANumber(method);
		}
		return order;
	}

	protected void throwUnableToFindOrderAnnotation(final Method method) {
		throw new TestBuilderGeneratorException("Unable to find order annotation for method, method: " + method);
	}

	protected void throwUnableToConvertOrderAnnotationToANumber(final Method method) {
		throw new TestBuilderGeneratorException("The order annotation for method is not a number, method: " + method
				+ " annotation value: " + method.getMetadataValues(Constants.ORDER_ANNOTATION));
	}

	protected String getGeneratedTypeNameSuffix() {
		return Constants.TEST_BUILDER_SUFFIX;
	}

	/**
	 * Helper which returns the TestBuilder type
	 * 
	 * @return
	 */
	protected Type getTestBuilder() {
		return this.getGeneratorContext().getType(TestBuilder.class.getName());
	}

	protected Type getTestMethodTestBuilder() {
		return this.getGeneratorContext().getType(TestMethodTestBuilder.class.getName());
	}
}
