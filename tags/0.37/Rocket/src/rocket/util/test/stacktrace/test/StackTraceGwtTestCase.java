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
package rocket.util.test.stacktrace.test;

import junit.framework.TestCase;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StackTrace;
import rocket.util.client.StringHelper;
import rocket.util.client.ThrowableHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * A series of tests that verify the functionality of the introduced support for
 * StackTraceElements in web mode.
 * 
 * Some of the tests below fail because GwtTestCases appear to compile java to
 * javascript in obsfucated mode meaning that function names are lost. I am
 * unable to fix this :(
 * 
 * @author Miroslav Pokorny (mP)
 */
public class StackTraceGwtTestCase extends GWTTestCase {

	/**
	 * Must refer to a valid module that sources this class.
	 */
	public String getModuleName() {
		return "rocket.util.test.stacktrace.test.StackTraceGwtTestCase";
	}

	/**
	 * This test should fail.
	 */
	public void testExceptionCreation() {
		new Exception();
	}

	public void testExceptionCanBeCreatedThrownAndCaught() {
		try {
			throw new RuntimeException();
		} catch (final RuntimeException expected) {
		}
	}

	public void testGetCallStackFunctions0() {
		final JavaScriptObject stackTrace = ThrowableHelper.getCallStackFunctions();
		assertNotNull("stackTrace", stackTrace);

		final int actual = ObjectHelper.getPropertyCount(stackTrace);
		TestCase.assertTrue("stackTraceElement count: " + actual, actual > 1);
		log("stackTraceElement count: " + actual);
	}

	public void testGetCallStackFunctions1() {
		final JavaScriptObject stackTrace = this.nativeMethod();
		assertNotNull("stackTrace", stackTrace);

		final int actual = ObjectHelper.getPropertyCount(stackTrace);
		log("stackTraceElement count: " + actual);
		TestCase.assertTrue("stackTraceElement count: " + actual, actual > 3);

		final JavaScriptObject topMostStackElement = ObjectHelper.getObject(stackTrace, 0);
		log("topMostStackElement: " + topMostStackElement);
		assertNotNull("topMostStackElement", topMostStackElement);

		final JavaScriptObject secondTopStackElement = ObjectHelper.getObject(stackTrace, 1);
		log("secondTopStackElement: " + secondTopStackElement);
		assertNotNull("secondTopStackElement", secondTopStackElement);
	}

	native private JavaScriptObject nativeMethod()/*-{
	 var a = this.@rocket.util.test.stacktrace.test.StackTraceGwtTestCase::javaMethod()();
	 return a;
	 }-*/;

	protected JavaScriptObject javaMethod() {
		return ThrowableHelper.getCallStackFunctions();
	}

	public void testGetCallStackFunctionNames0() {
		final JavaScriptObject callStackFunctions = ThrowableHelper.getCallStackFunctions();
		assertNotNull("callStackFunctions", callStackFunctions);

		final String[] functionNames = ThrowableHelper.getCallStackFunctionNames(callStackFunctions);
		assertNotNull("functionNames", functionNames);

		assertTrue("functionNames.length: " + functionNames.length, functionNames.length > 3);
	}

	public void testGetCallStackFunctionNames1() {
		final String[] functionNames = this.javaFunctionNames();
		assertNotNull("functionNames", functionNames);

		assertTrue("functionNames.length: " + functionNames.length, functionNames.length > 3);

		// scan thru functionNames for "topMostFunctionName"
		int foundIndex = -1;
		int i = 0;
		while (true) {
			final String functionName = functionNames[i];
			if (-1 != "topMostFunctionName".indexOf(functionName)) {
				foundIndex = i;
				break;
			}
			i++;
		}

		assertTrue("topMostFunctionName", foundIndex != -1);

		final String secondTopMostFunctionName = functionNames[foundIndex + 1];
		assertTrue("secondTopMostFunctionName[" + secondTopMostFunctionName + "]", -1 != secondTopMostFunctionName
				.indexOf("nativeFunctionNames"));
	}

	protected String[] javaFunctionNames() {
		final JavaScriptObject callStackFunctions = ThrowableHelper.getCallStackFunctions();
		assertNotNull("callStackFunctions", callStackFunctions);

		final String[] functionNames = ThrowableHelper.getCallStackFunctionNames(callStackFunctions);
		assertNotNull("functionNames", functionNames);
		return functionNames;
	}

	public void testGetStackTraceAsString() {
		RuntimeException runtimeException = null;
		try {
			threeFramesAwayFromMethodWhichThrowsException();
		} catch (final RuntimeException expected) {
			runtimeException = expected;
		}
		assertTrue(runtimeException instanceof RuntimeException);

		final String stackTrace = StackTrace.asString(runtimeException);
		System.out.println(stackTrace);
		assertTrue(stackTrace.length() > 0);

		final String[] lines = StringHelper.split(stackTrace, "\n", true);
		assertTrue(lines.length > 5);

		final String thisClassName = GWT.getTypeName(this);

		int i = 0;
		final String topLine = lines[i++];
		assertTrue(topLine, -1 != topLine.indexOf(thisClassName));

		final String topMostElement = lines[i++];
		assertTrue(stackTrace + "\n" + topMostElement, -1 != topMostElement.indexOf("at"));
		assertTrue(stackTrace + "\n" + topMostElement, -1 != topMostElement.indexOf("throwRuntimeException"));

		final String firstStackElement = lines[i++];
		assertTrue(stackTrace + "\n" + firstStackElement, -1 != firstStackElement.indexOf("at"));
		assertTrue(stackTrace + "\n" + firstStackElement, -1 != firstStackElement.indexOf("oneFrameAwayFromMethodWhichThrowsException"));

		final String secondStackElement = lines[i++];
		assertTrue(stackTrace + "\n" + secondStackElement, -1 != secondStackElement.indexOf("at"));
		assertTrue(stackTrace + "\n" + secondStackElement, -1 != secondStackElement.indexOf(thisClassName
				+ ".twoFramesAwayFromMethodWhichThrowsException"));

		final String thirdStackElement = lines[i++];
		assertTrue(stackTrace + "\n" + thirdStackElement, -1 != thirdStackElement.indexOf("at"));
		assertTrue(stackTrace + "\n" + thirdStackElement, -1 != thirdStackElement.indexOf(thisClassName
				+ ".threeFramesAwayFromMethodWhichThrowsException"));

		final String fourthStackElement = lines[i++];
		assertTrue(stackTrace + "\n" + fourthStackElement, -1 != fourthStackElement.indexOf("at"));
		assertTrue(stackTrace + "\n" + fourthStackElement, -1 != fourthStackElement.indexOf(thisClassName + ".testGetStackTraceAsString"));
	}

	static void threeFramesAwayFromMethodWhichThrowsException() {
		twoFramesAwayFromMethodWhichThrowsException();
	}

	static void twoFramesAwayFromMethodWhichThrowsException() {
		oneFrameAwayFromMethodWhichThrowsException();
	}

	static void oneFrameAwayFromMethodWhichThrowsException() {
		throwRuntimeException();
	}

	static void throwRuntimeException() {
		final RuntimeException exception = new RuntimeException();
		exception.fillInStackTrace();
		throw exception;
	}

	static void log(final Object message) {
		System.out.println("DEBUG: " + message);
		GWT.log("" + message, null);
	}
}
