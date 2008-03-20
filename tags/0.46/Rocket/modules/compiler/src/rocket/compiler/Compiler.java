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
package rocket.compiler;

import java.util.Iterator;

import rocket.util.client.Checker;

import com.google.gwt.dev.jjs.ast.JMethod;
import com.google.gwt.dev.jjs.ast.JNode;
import com.google.gwt.dev.jjs.ast.JType;
import com.google.gwt.dev.js.ast.JsNode;

/**
 * Convenient base class with some common functionality
 * @author Miroslav Pokorny
 */
public class Compiler {
	/**
	 * Extracts the source equivalent of the given node replacing newlines and carriage returns with their escaped slash equivalent.
	 * @param node
	 * @return
	 */
	static public String getSource(final JNode node) {
		Checker.notNull("parameter:node", node );
		
		return node.toSource().replaceAll( "\n", "\\\\n" ).replaceAll( "\r", "\\\\r" );
	}
	
	final static String CONSTRUCTOR_SUFFIX = " -Constructor";
	final static String STATIC_INITIALIZER_METHOD_NAME = "$clinit";
	final static String STATIC_INITIALIZER_SUFFIX = " -Static initializer";
	final static String INITIALIZER_METHOD_NAME = "$init";
	final static String INITIALIZER_SUFFIX = " -Initializer";
	
	/**
	 * Returns a string containing the method name followed by any parameter types.
	 * @param method
	 * @return
	 */
	static public String getMethodName( final JMethod method ){
		Checker.notNull("parameter:method", method );
		
		final String methodName = method.getName();
		
		final StringBuffer buf = new StringBuffer();
		buf.append( methodName );
		buf.append( '(');
		
		final Iterator p = method.getOriginalParamTypes().iterator();
		while( p.hasNext() ){
			final JType parameter = (JType)p.next();
			buf.append( parameter.getName() );
			
			if( p.hasNext() ){
				buf.append( ",");
			}
		}
		
		buf.append( ')');
		
		if( methodName.equals( STATIC_INITIALIZER_METHOD_NAME)){
			buf.append( STATIC_INITIALIZER_SUFFIX );
		}
		if( methodName.equals( INITIALIZER_METHOD_NAME)){
			buf.append( INITIALIZER_SUFFIX );
		}
		if( method.isConstructor() ){
			buf.append( CONSTRUCTOR_SUFFIX );
		}
		
		return buf.toString();
	}
	
	/**
	 * Extracts the source equivalent of the given node replacing newlines and carriage returns with their escaped slash equivalent.
	 * @param node
	 * @return
	 */
	static public String getSource(final JsNode node) {
		Checker.notNull("parameter:node", node );
		
		return node.toSource().replaceAll( "\n", "\\\\n" ).replaceAll( "\r", "\\\\r" );
	}
	
	/**
	 * Tests if a particular optimiser is enabled by checking if a system property with the same name has a value of enabled.
	 * @param className
	 * @return
	 */
	static public boolean isEnabled(final String className) {
		boolean enabled = false;
		
		while( true ){			
			final String individual = System.getProperty( className );
			if( "enabled".equals( individual )){
				enabled = true;
				break;
			}
			if( "disabled".equals( individual )){
				enabled = false;
				break;
			}
			
			// global enable/disable...
			final String global = System.getProperty( "rocket.compiler");
			if( "enabled".equals( global )){
				enabled = true;
				break;
			}
			if( "disabled".equals( global )){
				enabled = false;
				break;
			}
		
			// defaults to disabled...
			break;
		}
		return enabled;
	}
}
