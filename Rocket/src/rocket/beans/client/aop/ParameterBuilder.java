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
package rocket.beans.client.aop;

import java.util.ArrayList;
import java.util.List;

/**
 * This builder takes care of assembling an array of Objects automatically
 * wrapping primitive values in wrappers as necessary.
 * 
 * @author Miroslav Pokorny
 */
public class ParameterBuilder {

	public ParameterBuilder() {
		super();

		this.setParameters(new ArrayList());
	}

	public ParameterBuilder add(final boolean booleanValue) {
		this.add(Boolean.valueOf(booleanValue));
		return this;
	}

	public ParameterBuilder add(final byte byteValue) {
		this.add(new Byte(byteValue));
		return this;
	}

	public ParameterBuilder add(final short shortValue) {
		this.add(new Short(shortValue));
		return this;
	}

	public ParameterBuilder add(final int intValue) {
		this.add(new Integer(intValue));
		return this;
	}

	public ParameterBuilder add(final long longValue) {
		this.add(new Long(longValue));
		return this;
	}

	public ParameterBuilder add(final float floatValue) {
		this.add(new Float(floatValue));
		return this;
	}

	public ParameterBuilder add(final double doubleValue) {
		this.add(new Double(doubleValue));
		return this;
	}

	public ParameterBuilder add(final char characterValue) {
		this.add(new Character(characterValue));
		return this;
	}

	public ParameterBuilder add(final Object object) {
		this.getParameters().add(object);
		return this;
	}

	/**
	 * This list accumulates all the parameters that are added to this builder.
	 */
	private List parameters;

	protected List getParameters() {
		return this.parameters;
	}

	protected void setParameters(final List parameters) {
		this.parameters = parameters;
	}

	public Object[] toParameters() {
		return this.getParameters().toArray();
	}
}
