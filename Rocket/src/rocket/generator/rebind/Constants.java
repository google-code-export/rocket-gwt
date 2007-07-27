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
package rocket.generator.rebind;

/**
 * A collection of constants used during the code generation process.
 * 
 * @author Miroslav Pokorny
 * 
 * TODO Delete will be replaced when v2.* is moved up.
 */
public class Constants {
	/**
	 * A comma separated list of all reserved javascript keywords.
	 */
	final static String JAVASCRIPT_RESERVED_KEYWORDS = "abstract,boolean,break,byte,case,catch,char,class,const,continue,debugger,default,delete,do,double,else,enum,export,extends,false,final,finally,float,for,function,goto,if,implements,import,in,instanceof,int,interface,long,native,new,null,package,private,protected,public,return,short,static,super,switch,synchronized,this,throw,throws,transient,true,try,typeof,var,void,volatile,while,with";

	/**
	 * A comma separated literal name black list of java keywords
	 */
	final static String JAVA_RESERVED_KEYWORDS = "abstract,continue,for,new,switch,assert,default,goto,package,synchronized,boolean,do,if,private,this,break,double,implements,protected,throw,byte,else,import,public,throws,case,enum,instanceof,return,transient,catch,extends,int,short,try,char,final,interface,static,void,class,finally,long,strictfp,volatile,const,float,native,super,while";

	public final static String BOOLEAN = Boolean.TYPE.getName();

	public final static String BYTE = Byte.TYPE.getName();

	public final static String SHORT = Short.TYPE.getName();

	public final static String INT = Integer.TYPE.getName();

	public final static String LONG = Long.TYPE.getName();

	public final static String FLOAT = Float.TYPE.getName();

	public final static String DOUBLE = Double.TYPE.getName();

	public final static String CHAR = Character.TYPE.getName();

	public final static String VOID = Void.TYPE.getName();

	public final static String OBJECT = Object.class.getName();

	public final static String STRING = String.class.getName();
}
