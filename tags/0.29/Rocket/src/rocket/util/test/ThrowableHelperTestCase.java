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
package rocket.util.test;

import junit.framework.TestCase;
import rocket.util.client.ThrowableHelper;
import rocket.util.client.StackTraceHelper;

/**
 * This testcase is designed to only test portions of StackTraceHelper that do not have any dependencies on JSNI.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class ThrowableHelperTestCase extends TestCase {
    public void testBuildStackTraceElementsAnonymousFunction() {
        final String[] functionNames = { "anonymous" };
        final StackTraceElement[] elements = ThrowableHelper.buildStackTraceElements(functionNames);
        assertEquals("element count", 1, elements.length);

        final StackTraceElement element = elements[0];
        assertEquals("className", "anonymous", element.getClassName());
        assertEquals("methodName", "", element.getMethodName());
    }

    public void testBuildStackTraceElementsNonGWTGeneratedFunctionName() {
        final String[] functionNames = { "abc" };
        final StackTraceElement[] elements = ThrowableHelper.buildStackTraceElements(functionNames);
        assertEquals("element count", 1, elements.length);

        final StackTraceElement element = elements[0];
        assertEquals("className", "abc", element.getClassName());
        assertEquals("methodName", "", element.getMethodName());
    }

    public void testBuildStackTraceElementsNonGWTGeneratedFunctionNameWithFakeDoubleUnderscore() {
        final String[] functionNames = { "abc__de" };
        final StackTraceElement[] elements = ThrowableHelper.buildStackTraceElements(functionNames);
        assertEquals("element count", 1, elements.length);

        final StackTraceElement element = elements[0];
        assertEquals("className", "abc__de", element.getClassName());
        assertEquals("methodName", "", element.getMethodName());
    }

    public void testBuildStackTraceElementsInstanceMethodFunction() {
        final String[] functionNames = { "java_lang_Object_toString__" };
        final StackTraceElement[] elements = ThrowableHelper.buildStackTraceElements(functionNames);
        assertEquals("element count", 1, elements.length);

        final StackTraceElement element = elements[0];
        assertEquals("className", "java.lang.Object", element.getClassName());
        assertEquals("methodName", "toString", element.getMethodName());
    }

    public void testBuildStackTraceElementsConstructorFunction() {
        final String[] functionNames = { "com_google_gwt_user_client_impl_DOMImplStandard" };
        final StackTraceElement[] elements = ThrowableHelper.buildStackTraceElements(functionNames);
        assertEquals("element count", 1, elements.length);

        final StackTraceElement element = elements[0];
        assertEquals("className", "com.google.gwt.user.client.impl.DOMImplStandard", element.getClassName());
        assertEquals("methodName", "", element.getMethodName());
    }

    public void testBuildStackTraceElementsUnnamedPackageConstructorFunction() {
        final String[] functionNames = { "MyClass" };
        final StackTraceElement[] elements = ThrowableHelper.buildStackTraceElements(functionNames);
        assertEquals("element count", 1, elements.length);

        final StackTraceElement element = elements[0];
        assertEquals("className", "MyClass", element.getClassName());
        assertEquals("methodName", "", element.getMethodName());
    }

    public void testBuildStackTraceElementsStaticInitializerFunction() {
        final String[] functionNames = { "java_lang_Throwable_$clint__()" };
        final StackTraceElement[] elements = ThrowableHelper.buildStackTraceElements(functionNames);
        assertEquals("element count", 1, elements.length);

        final StackTraceElement element = elements[0];
        assertEquals("className", "java.lang.Throwable", element.getClassName());
        assertEquals("methodName", "<clint>", element.getMethodName());
    }

    public void testBuildStackTraceElementsStaticMethodFunction() {
        final String[] functionNames = { "rocket_browser_client_BrowserHelper_getContextPath__" };
        final StackTraceElement[] elements = ThrowableHelper.buildStackTraceElements(functionNames);
        assertEquals("element count", 1, elements.length);

        final StackTraceElement element = elements[0];
        assertEquals("className", "rocket.browser.client.BrowserHelper", element.getClassName());
        assertEquals("methodName", "getContextPath", element.getMethodName());
    }

    public void testBuildStackTraceElementsStaticMethodWithSingleArgumentsFunction() {
        final String[] functionNames = { "com_google_gwt_core_client_GWT_getTypeName__Ljava_lang_Object_2" };
        final StackTraceElement[] elements = ThrowableHelper.buildStackTraceElements(functionNames);
        assertEquals("element count", 1, elements.length);

        final StackTraceElement element = elements[0];
        assertEquals("className", "com.google.gwt.core.client.GWT", element.getClassName());
        assertEquals("methodName", "getTypeName", element.getMethodName());
        // assertEquals( "arguments", "java.lang.Object", element.getArguments());
    }

    public void testBuildStackTraceElementsStaticMethodWithManyArgumentsFunction() {
        final String[] functionNames = { "com_google_gwt_user_client_DOM_setStyleAttribute__Lcom_google_gwt_user_client_Element_2Ljava_lang_String_2Ljava_lang_String_2" };
        final StackTraceElement[] elements = ThrowableHelper.buildStackTraceElements(functionNames);
        assertEquals("element count", 1, elements.length);

        final StackTraceElement element = elements[0];
        assertEquals("className", "com.google.gwt.user.client.DOM", element.getClassName());
        assertEquals("methodName", "setStyleAttribute", element.getMethodName());
        // assertEquals( "fileName(aka argumentList when in webmode)",
        // "com.google.gwt.user.client.Element,java.lang.String,java.lang.String", element.getFileName());
    }

    public void testBuildStackTraceElementsInstanceMethodWithSingleArgumentsFunction() {
        final String[] functionNames = { "com_google_gwt_core_client_GWT_getTypeName__Ljava_lang_Object_2" };
        final StackTraceElement[] elements = ThrowableHelper.buildStackTraceElements(functionNames);
        assertEquals("element count", 1, elements.length);

        final StackTraceElement element = elements[0];
        assertEquals("className", "com.google.gwt.core.client.GWT", element.getClassName());
        assertEquals("methodName", "getTypeName", element.getMethodName());
        // assertEquals( "fileName(aka argumentList when in webmode)", "java.lang.Object", element.getFileName());
    }

    public void testBuildStackTraceElementsInstanceMethodWithManyArgumentsFunction() {
        final String[] functionNames = { "a_b_C_method__Ljava_lang_String_2ZBCSIJF[[D" };
        final StackTraceElement[] elements = ThrowableHelper.buildStackTraceElements(functionNames);
        assertEquals("element count", 1, elements.length);

        final StackTraceElement element = elements[0];
        assertEquals("className", "a.b.C", element.getClassName());
        assertEquals("methodName", "method", element.getMethodName());
        // assertEquals( "fileName(aka argumentList when in webmode)", "java.lang.String,boolean,byte,char,short,int,long,float,[][]double",
        // element.getFileName());
    }
}
