/**
 * 
 */
package rocket.generator.rebind.util;

import java.util.Set;

import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * Base class for both constructors and methods
 * 
 * @author Miroslav Pokorny
 */
abstract public class AbstractConstructorOrMethod extends AbstractConstructorMethodOrField {

	/**
	 * A lazy loaded set containing all the thrown types for this method
	 */
	private Set<Type> thrownTypes;

	public Set<Type> getThrownTypes() {
		if (false == hasThrows()) {
			this.setThrownTypes(this.createThrownTypes());
		}
		return this.thrownTypes;
	}

	protected boolean hasThrows() {
		return this.thrownTypes != null;
	}

	protected void setThrownTypes(final Set<Type> thrownTypes) {
		Checker.notNull("parameter:thrownTypes", thrownTypes);
		this.thrownTypes = thrownTypes;
	}

	abstract protected Set createThrownTypes();
}
