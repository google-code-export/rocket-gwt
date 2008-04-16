/*
 * Copyright 2007 Google Inc.
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
package com.google.gwt.dev.jjs.ast;

import com.google.gwt.dev.jjs.SourceInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for any reference type.
 */
public abstract class JReferenceType extends JType implements CanBeAbstract,
    CanBeFinal {

  public List/* <JField> */fields = new ArrayList/* <JField> */();
  public List/* <JMethod> */methods = new ArrayList/* <JMethod> */();
  public JClassType extnds;
  public List/* <JInterfaceType> */implments = new ArrayList/* <JInterfaceType> */();

  public JReferenceType(JProgram program, SourceInfo info, String name) {
    super(program, info, name, program.getLiteralNull());
  }

  public String getJavahSignatureName() {
    return "L" + name.replaceAll("_", "_1").replace('.', '_') + "_2";
  }

  public String getJsniSignatureName() {
    return "L" + name.replace('.', '/') + ';';
  }

  public JProgram getProgram() {
    return program;
  }

  public String getShortName() {
    int dotpos = name.lastIndexOf('.');
    return name.substring(dotpos + 1);
  }

	/**
	 * Returns the name of the packaging containing this type.
	 * @return
	 * 
	 * TODO ROCKET When upgrading from GWT 1.4.60 reapply changes.
	 */
	public String getPackageName(){
		final String name = this.getName();
		final String shortName = this.getShortName();
		return name.substring( 0, name.length() - shortName.length() - 1 );
	}
	
	public boolean hasStaticInitializer(){
		return this.getJProgram().typeOracle.hasClinit( this );
	}
}