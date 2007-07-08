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
package rocket.beans.test.client.manyvalues;

import java.util.Set;

public class ClassWithManyValues {

	private String apple;

	public String getApple() {
		return this.apple;
	}

	public void setApple(final String apple) {
		this.apple = apple;
	}

	private String banana;

	public String getBanana() {
		return this.banana;
	}

	public void setBanana(final String banana) {
		this.banana = banana;
	}
	
	private String carrot;

	public String getCarrot() {
		return this.carrot;
	}

	public void setCarrot(final String carrot) {
		this.carrot = carrot;
	}
}
