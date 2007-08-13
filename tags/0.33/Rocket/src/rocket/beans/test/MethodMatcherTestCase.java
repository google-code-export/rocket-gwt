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
package rocket.beans.test;

import java.util.Collections;

import junit.framework.TestCase;
import rocket.beans.rebind.aop.MethodMatcher;
import rocket.beans.rebind.aop.MethodMatcherFactory;
import rocket.generator.rebind.GeneratorContext;
import rocket.generator.rebind.java.JavaGeneratorContext;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.type.Type;

public class MethodMatcherTestCase extends TestCase {

	public void testMatches() {
		final String expression = "abc";
		final MethodMatcher matcher = this.createMethodMatcher(expression);

		final Method method = this.createContext().getType( Test.class.getName() ).getMethod( "abc", Collections.EMPTY_LIST );
		final boolean actual = matcher.matches(method);
		assertTrue(actual);
	}
	
	static class Test{
		public void abc(){			
		}
	}

	public void testWildcardMatches() {
		final String expression = "foo.*";
		final MethodMatcher matcher = this.createMethodMatcher(expression);

		final Method method = this.createContext().getType( Test2.class.getName() ).getMethod( "foo", Collections.EMPTY_LIST );
		assertTrue(expression, matcher.matches( method ));
	}

	public void testWildcardDoesntMatch() {
		final String expression = "foo.*xxx";
		final MethodMatcher matcher = this.createMethodMatcher(expression);

		final Method method = this.createContext().getType( Test2.class.getName() ).getMethod( "foo", Collections.EMPTY_LIST );
		assertFalse(expression, matcher.matches( method ));
	}

	static class Test2{
		public void foo(){			
		}
	}
	
	public void testManyMatchersWithinExpression() {
		final String expression = "foo.*,bar.*";
		final MethodMatcher matcher = this.createMethodMatcher(expression);

		final GeneratorContext context = this.createContext();
		final Type type = context.getType( Test3.class.getName() );
		
		final Method foo = type.getMethod( "foo", Collections.EMPTY_LIST );
		assertTrue(expression, matcher.matches(foo));
		
		final Method bar = type.getMethod( "bar", Collections.EMPTY_LIST );
		assertTrue(expression, matcher.matches(bar));
		
		final Method baz = type.getMethod( "baz", Collections.EMPTY_LIST );
		assertFalse(expression, matcher.matches(baz));		
	}

	static class Test3{
		public void foo(){			
		}
		public void bar(){			
		}
		public void baz(){			
		}
	}

	public void testManyMatchersWithinExpressionIgnoresNonPublicMethods() {
		final String expression = "foo.*,bar.*";
		final MethodMatcher matcher = this.createMethodMatcher(expression);

		final GeneratorContext context = this.createContext();
		final Type type = context.getType( Test4.class.getName() );
		
		final Method foo = type.getMethod( "foo", Collections.EMPTY_LIST );
		assertFalse(expression, matcher.matches(foo));
		
		final Method bar = type.getMethod( "bar", Collections.EMPTY_LIST );
		assertFalse(expression, matcher.matches(bar));
		
		final Method baz = type.getMethod( "baz", Collections.EMPTY_LIST );
		assertFalse(expression, matcher.matches(baz));		
	}

	static class Test4{
		protected void foo(){			
		}
		protected void bar(){			
		}
		protected void baz(){			
		}
	}

	/**
	 * Factory method which creats a new MethodMatcher
	 * @param expression
	 * @return
	 */
	MethodMatcher createMethodMatcher(final String expression) {
		return new MethodMatcherFactory().create(expression);
	}

	
	GeneratorContext createContext(){
		return new JavaGeneratorContext();
	}
}
