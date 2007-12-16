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
package rocket.generator.rebind.util;

import rocket.generator.rebind.SourceWriter;
import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

/**
 * This SourceBuffer captures all printed text which may be retrieved later via
 * {@link #getBuffer()}
 * 
 * @author Miroslav Pokorny
 */
public class StringBufferSourceWriter implements SourceWriter {

	public StringBufferSourceWriter() {
		super();

		this.setStringBuffer(this.createStringBuffer());
		this.setIndentation("");
		this.setBeginningOfLine(true);
		this.setJavadoc(false);
	}

	/**
	 * When true indicates that a new line has just been started. Any attempts
	 * to print will require indentation etc.
	 */
	private boolean beginningOfLine;

	protected boolean isBeginningOfLine() {
		return this.beginningOfLine;
	}

	protected void setBeginningOfLine(final boolean beginningOfLine) {
		this.beginningOfLine = beginningOfLine;
	}

	/**
	 * If a new line has not just been started write an end of line. After this
	 * write the indentation and if inside javadoc the javadoc comment
	 * decoration.
	 */
	protected void beginNewLine() {
		final StringBuffer buffer = this.getStringBuffer();

		if (false == this.isBeginningOfLine()) {
			buffer.append(Constants.EOL);
			this.setBeginningOfLine(false);
		}

		buffer.append(this.getIndentation());
	}

	/**
	 * Begin emitting a JavaDoc comment.
	 */
	public void beginJavaDocComment() {
		if (false == this.isJavadoc()) {
			this.beginNewLine();

			final StringBuffer buffer = this.getStringBuffer();
			buffer.append(Constants.JAVADOC_BEGIN);
			buffer.append(Constants.EOL);

			this.setIndentation(this.getIndentation() + Constants.JAVADOC);
			this.setBeginningOfLine(true);
		}
		this.setJavadoc(true);
	}

	/**
	 * End emitting a JavaDoc comment.
	 */
	public void endJavaDocComment() {
		if (this.isJavadoc()) {
			this.setJavadoc(false);

			final String indentation = this.getIndentation();
			final int star = indentation.indexOf(Constants.JAVADOC);
			this.setIndentation(indentation.substring(0, star));

			this.beginNewLine();
			final StringBuffer buffer = this.getStringBuffer();
			buffer.append(Constants.JAVADOC_END);
			buffer.append(Constants.EOL);

			this.setBeginningOfLine(true);
		}
	}

	/**
	 * When true indicates this writer is in javadoc mode.
	 */
	protected boolean javadoc;

	protected boolean isJavadoc() {
		return this.javadoc;
	}

	protected void setJavadoc(final boolean insideJavadoc) {
		this.javadoc = insideJavadoc;
	}

	public void indent() {
		this.setIndentation(this.getIndentation() + "  ");
	}

	public void outdent() {
		String indent = this.getIndentation();
		final int length = indent.length();
		if (length < 2) {
			indent = "";
		} else {
			if (false == indent.endsWith(" * ")) {
				indent = indent.substring(0, length - 2);
			}
		}
		this.setIndentation(indent);
	}

	/**
	 * The current indentation
	 */
	private String indentation;

	protected String getIndentation() {
		StringHelper.checkNotNull("field:indentation", indentation);
		return indentation;
	}

	protected void setIndentation(final String indentation) {
		StringHelper.checkNotNull("parameter:indentation", indentation);
		this.indentation = indentation;
	}

	public void indentln(final String string) {
		StringHelper.checkNotNull("parameter:string", string);

		this.indent();
		this.println(string);
		this.outdent();
	}

	public void print(final String string) {
		StringHelper.checkNotNull("parameter:string", string);

		final StringBuffer buffer = this.getStringBuffer();
		boolean beginningOfLine = this.isBeginningOfLine();
		final char[] chars = string.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			if (beginningOfLine) {
				this.beginNewLine();
				beginningOfLine = false;
			}
			final char c = chars[i];
			buffer.append(c);

			if ('\n' == c) {
				beginningOfLine = true;
			}
		}

		this.setBeginningOfLine(beginningOfLine);
	}

	public void println() {
		this.print(Constants.EOL);
	}

	public void println(final String string) {
		this.print(string);
		this.println();
	}

	public void commit() {
	}
	public void rollback(){
		throw new UnsupportedOperationException();
	}

	/**
	 * This StringBuffer will accumulate all written text
	 */
	private StringBuffer stringBuffer;

	protected StringBuffer getStringBuffer() {
		ObjectHelper.checkNotNull("field:stringBuffer", stringBuffer);
		return this.stringBuffer;
	}

	protected void setStringBuffer(final StringBuffer stringBuffer) {
		ObjectHelper.checkNotNull("parameter:stringBuffer", stringBuffer);
		this.stringBuffer = stringBuffer;
	}

	protected StringBuffer createStringBuffer() {
		return new StringBuffer();
	}

	public String getBuffer() {
		return this.getStringBuffer().toString();
	}
}
