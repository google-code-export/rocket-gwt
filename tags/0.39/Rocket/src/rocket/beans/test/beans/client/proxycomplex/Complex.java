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
package rocket.beans.test.beans.client.proxycomplex;

public class Complex {

	public boolean advisedXor(final boolean a, final boolean b) {
		return a ^ b;
	}

	public byte advisedAdd(final byte a, final byte b) {
		return (byte) (a + b);
	}

	public short advisedAdd(final short a, final short b) {
		return (short) (a + b);
	}

	public int advisedAdd(final int a, final int b) {
		return (int) (a + b);
	}

	public long advisedAdd(final long a, final long b) {
		return (long) (a + b);
	}

	public float advisedAdd(final float a, final float b) {
		return (float) (a + b);
	}

	public double advisedAdd(final double a, final double b) {
		return (double) (a + b);
	}

	public char advisedToUpperCase(final char a) {
		return Character.toUpperCase(a);
	}

	public String advisedToLowerCase(final String a) {
		return a.toLowerCase();
	}

	public void advisedDummy() {
	}

	public Object advisedReturnsObject(final boolean a, final byte b, final short s, final int i, final long l, final float f,
			final double d, final Object o) {
		return o;
	}

	public Object advisedReturnsNull(final boolean a, final byte b, final short s, final int i, final long l, final float f,
			final double d, final Object o) {
		return null;
	}

	public void advisedThrowCheckedException() throws Exception {
		throw new Exception();
	}

	public void advisedThrowUncheckedException() throws RuntimeException {
		throw new RuntimeException();
	}

	public boolean unadvisedXor(final boolean a, final boolean b) {
		return a ^ b;
	}

	public byte unadvisedAdd(final byte a, final byte b) {
		return (byte) (a + b);
	}

	public short unadvisedAdd(final short a, final short b) {
		return (short) (a + b);
	}

	public int unadvisedAdd(final int a, final int b) {
		return (int) (a + b);
	}

	public long unadvisedAdd(final long a, final long b) {
		return (long) (a + b);
	}

	public float unadvisedAdd(final float a, final float b) {
		return (float) (a + b);
	}

	public double unadvisedAdd(final double a, final double b) {
		return (double) (a + b);
	}

	public char unadvisedToUpperCase(final char a) {
		return Character.toUpperCase(a);
	}

	public String unadvisedToLowerCase(final String a) {
		return a.toLowerCase();
	}

	public void unadvisedDummy() {
	}

	public Object unadvisedReturnsObject(final boolean a, final byte b, final short s, final int i, final long l, final float f,
			final double d, final Object o) {
		return o;
	}

	public Object unadvisedReturnsNull(final boolean a, final byte b, final short s, final int i, final long l, final float f,
			final double d, final Object o) {
		return null;
	}

	public void unadvisedThrowCheckedException() throws Exception {
		throw new Exception();
	}

	public void unadvisedThrowUncheckedException() throws RuntimeException {
		throw new RuntimeException();
	}
}
