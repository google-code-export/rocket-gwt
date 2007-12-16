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
package rocket.generator.rebind.visitor;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import rocket.generator.rebind.Visibility;
import rocket.generator.rebind.method.Method;
import rocket.generator.rebind.methodparameter.MethodParameter;
import rocket.generator.rebind.packagee.Package;
import rocket.generator.rebind.type.Type;
import rocket.util.client.ObjectHelper;

/**
 * This visitor visits all the virtual methods for the entire type heirarchy of
 * the given type. This visitor skips methods that are not virutal
 * <ul>
 * <li>any static method</li>
 * <li>package private methods belonging to a different package than the
 * starting type</li>
 * <li>any private method</li>
 * </ul>
 * 
 * @author Miroslav Pokorny
 */
abstract public class VirtualMethodVisitor {

	/**
	 * Starts the visiting process starting at the most derived type towards
	 * java.lang.Object.
	 * 
	 * @param derivedType
	 */
	public void start(final Type derivedType) {
		ObjectHelper.checkNotNull("parameter:derivedType", derivedType);

		final Package packagee = derivedType.getPackage();
		final Set visited = this.createVisited();

		final AllMethodsVisitor visitor = new AllMethodsVisitor() {
			protected boolean visit(final Method method) {
				boolean skipRemaining = false;
				while (true) {
					if (method.isStatic()) {
						break;
					}
					final Visibility visibility = method.getVisibility();
					if (Visibility.PRIVATE == visibility) {
						break;
					}
					// different pacakge skip this method.
					if (Visibility.PACKAGE_PRIVATE == visibility) {
						final Type enclosingType = method.getEnclosingType();
						if (false == packagee.equals(enclosingType.getPackage())) {
							break;
						}
					}

					if (false == visited.contains(method)) {
						skipRemaining = VirtualMethodVisitor.this.visit(method);
						visited.add(method);
					}

					break;
				}

				return skipRemaining;
			}

			protected boolean skipJavaLangObjectMethods() {
				return VirtualMethodVisitor.this.skipJavaLangObjectMethods();
			}
		};
		visitor.start(derivedType);
	}

	/**
	 * Each virutal method is presented to this method.
	 * 
	 * @param method
	 * @return Return true to skip remaining methods, false continues
	 */
	abstract protected boolean visit(Method method);

	/**
	 * When true indicates that all methods belonging to java.lang.Object are
	 * not visited.
	 * 
	 * @return
	 */
	abstract protected boolean skipJavaLangObjectMethods();

	/**
	 * Creates a Set that indexes itself on the method signature.
	 * 
	 * @return
	 */
	protected Set createVisited() {
		final Comparator comparator = new Comparator() {
			public int compare(final Object object, final Object otherObject) {
				int value = 0;
				while (true) {
					final Method method = (Method) object;
					final Method otherMethod = (Method) otherObject;
					value = method.getName().compareTo(otherMethod.getName());
					if (value != 0) {
						break;
					}

					final List methodParameters = method.getParameters();
					final List otherMethodParameters = otherMethod.getParameters();
					value = methodParameters.size() - otherMethodParameters.size();
					if (value != 0) {
						break;
					}

					final Iterator parametersIterator = methodParameters.iterator();
					final Iterator otherParametersIterator = otherMethodParameters.iterator();
					while (parametersIterator.hasNext()) {
						final MethodParameter parameter = (MethodParameter) parametersIterator.next();
						final MethodParameter otherParameter = (MethodParameter) otherParametersIterator.next();
						value = parameter.getType().getName().compareTo(otherParameter.getType().getName());
						if (value != 0) {
							break;
						}
					}

					break;
				}

				return value;
			}
		};

		return new TreeSet(comparator);
	}
}
