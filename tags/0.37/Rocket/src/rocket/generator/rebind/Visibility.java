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

import rocket.util.client.StringHelper;

/**
 * A enum like class that represents any of the possible levels of visibility
 * for any compilation component including, classes, methods and fields
 * 
 * @author Miroslav Pokorny
 */
public class Visibility {
	/**
	 * Factory method that returns a Visibility object given a visibility
	 * option.
	 * 
	 * @param visibilityName
	 * @return A visibility instance.
	 */
	static public Visibility getVisibility(final String visibilityName) {
		StringHelper.checkNotEmpty("parameter:visibilityName", visibilityName);

		Visibility visibility = null;

		while (true) {
			if (visibilityName.equals("public")) {
				visibility = PUBLIC;
				break;
			}
			if (visibilityName.equals("protected")) {
				visibility = PROTECTED;
				break;
			}
			if (visibilityName.equals("packagePrivate")) {
				visibility = PACKAGE_PRIVATE;
				break;
			}
			if (visibilityName.equals("private")) {
				visibility = PRIVATE;
				break;
			}

			throw new RuntimeException("Invalid visibility [" + visibilityName + "]");
		}

		return visibility;
	}

	public final static Visibility PUBLIC = new Visibility("public", "public");

	public final static Visibility PROTECTED = new Visibility("protected", "protected");

	public final static Visibility PACKAGE_PRIVATE = new Visibility("packagePrivate", "");

	public final static Visibility PRIVATE = new Visibility("private", "private") {
		protected boolean equals(final Visibility otherVisibility) {
			return false;
		}
	};

	private Visibility(final String name, final String javaName) {
		super();
		this.setName(name);
		this.setJavaName(javaName);
	}

	public boolean equals(final Object otherObject) {
		return otherObject instanceof Visibility ? this.equals((Visibility) otherObject) : false;
	}

	protected boolean equals(final Visibility otherVisibility) {
		return this == otherVisibility;
	}

	private String name;

	public String getName() {
		return name;
	}

	protected void setName(final String name) {
		this.name = name;
	}

	private String javaName;

	public String getJavaName() {
		return javaName;
	}

	protected void setJavaName(final String javaName) {
		this.javaName = javaName;
	}

	public String toString() {
		return this.javaName;
	}
}
