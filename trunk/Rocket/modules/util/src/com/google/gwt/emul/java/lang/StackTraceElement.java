/*
 * Copyright 2008 Google Inc.
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
package java.lang;

import java.io.Serializable;

/**
 * Included for hosted mode source compatibility. Not yet implemented.
 * 
 * @skip
 */
public final class StackTraceElement implements Serializable {

	/**
	 * ROCKET Constructor added to as part of support for web mode stacktraces.
	 * @param declaringClass
	 * @param methodName
	 * @param fileName
	 * @param lineNumber
	 */
	public StackTraceElement(final String declaringClass, final String methodName, final String fileName, final int lineNumber) {
		super();

		this.setClassName(declaringClass);
		this.setMethodName(methodName);
		this.setFileName(fileName);
		this.setLineNumber(lineNumber);
	}
	
	public StackTraceElement(){
		super();
	}
	
  private String className;

  private String fileName;

  private int lineNumber;

  private String methodName;

  public String getClassName() {
    return className;
  }

  public String getFileName() {
    return fileName;
  }

  public int getLineNumber() {
    return lineNumber;
  }

  public String getMethodName() {
    return methodName;
  }
  

	// ROCKET The setters below were added to support web mode stacktraces
	void setClassName(final String className) {
		this.className = className;
	}

	void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	void setLineNumber(final int lineNumber) {
		this.lineNumber = lineNumber;
	}

	void setMethodName(final String methodName) {
		this.methodName = methodName;
	}
}
