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

import com.google.gwt.dev.js.ast.JsContext;
import com.google.gwt.dev.js.ast.JsExpression;
import com.google.gwt.dev.js.ast.JsIntegralLiteral;
import com.google.gwt.dev.js.ast.JsPostfixOperation;
import com.google.gwt.dev.js.ast.JsPrefixOperation;
import com.google.gwt.dev.js.ast.JsUnaryOperation;
import com.google.gwt.dev.js.ast.JsUnaryOperator;
import com.google.gwt.dev.util.TextOutput;

/**
 * This class overrides the original GWT class changing a single behaviour namely negative literals are not 
 * surrounded by parenthesis.
 * @author Miroslav Pokorny
 */
public class JsSourceGenerationVisitor extends com.google.gwt.dev.js.JsSourceGenerationVisitor {
	public JsSourceGenerationVisitor(final TextOutput out) {
		super(out);
		this.p = out;
	}

	/**
	 * Need to keep a copy because of visibility problems with super class.
	 */
	private final TextOutput p;

	/**
	 * Unsure why parenthesis are inserted around negative numbers, they seem to just make the generated js a bit bigger.
	 */
	public boolean visit(JsIntegralLiteral x, JsContext ctx) {
		String s = x.getValue().toString();
		p.print(s);
		return false;
	}
	 
	  public boolean XXXvisit(final JsPostfixOperation x, final JsContext ctx) {
		    final JsUnaryOperator op = x.getOperator();
		    final JsExpression arg = x.getArg();
		    
		    boolean outputted = false;
		    
		    while( true ){
		    	// not the ! operator
		    	if( op != JsUnaryOperator.NOT ){
		    		break;
		    	}
		    	// not a nested unary operation...
		    	if( false == arg instanceof JsUnaryOperation ){
		    		break;
		    	}
		    	
		    	// check nested unary operation...
		    	final JsUnaryOperation unaryOperation = (JsUnaryOperation)arg;		    	
		    	final JsUnaryOperator op2 = unaryOperation.getOperator();
			    final JsExpression arg2 = unaryOperation.getArg();
		    	
			    // nested unary operation is not a !
			    if( op2 != JsUnaryOperator.NOT ){
			    	break;
			    }
			    
			    // nested is not a unary
			    if( false == arg instanceof JsUnaryOperation ){
		    		break;
		    	}
			    
			    // appear to have found a double !!(expression) output less the parenthesis.
			    p.print("!!");
			    accept(arg2);
			    
			    outputted = true;
			    System.out.println( "JS POSTFIX OP\t" + Compiler.getSource( x ));
			    break;
		    }
		    
		    if( ! outputted ){
		    	super.visit(x, ctx);
		    }
		    return false;
		  }
	  
	  public boolean XXXvisit(final JsPrefixOperation x, final JsContext ctx) {
		    final JsUnaryOperator op = x.getOperator();
		    final JsExpression arg = x.getArg();
		    
		    boolean outputted = false;
		    
		    while( true ){
		    	// not the ! operator
		    	if( op != JsUnaryOperator.NOT ){
		    		break;
		    	}
		    	// not a nested unary operation...
		    	if( false == arg instanceof JsUnaryOperation ){
		    		break;
		    	}
		    	
		    	// check nested unary operation...
		    	final JsUnaryOperation unaryOperation = (JsUnaryOperation)arg;		    	
		    	final JsUnaryOperator op2 = unaryOperation.getOperator();
			    final JsExpression arg2 = unaryOperation.getArg();
		    	
			    // nested unary operation is not a !
			    if( op2 != JsUnaryOperator.NOT ){
			    	break;
			    }
			    
			    // nested is not a unary
			    if( false == arg instanceof JsUnaryOperation ){
		    		break;
		    	}
			    
			    // appear to have found a double !!(expression) output less the parenthesis.
			    p.print("!!");
			    accept(arg2);
			    
			    outputted = true;
			    
			    System.out.println( "JS PREFIX OP\t" + Compiler.getSource( x ));
			    break;
		    }
		    
		    if( ! outputted ){
		    	super.visit(x, ctx);
		    }
		    return false;
		  }

}
