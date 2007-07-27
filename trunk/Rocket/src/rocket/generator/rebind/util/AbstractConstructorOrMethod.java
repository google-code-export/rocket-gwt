/**
 * 
 */
package rocket.generator.rebind.util;

import java.util.List;
import java.util.Set;

import rocket.util.client.ObjectHelper;

/**
 * Base class for both constructors and methods
 * 
 * @author Miroslav Pokorny
 */
abstract public class AbstractConstructorOrMethod extends AbstractConstructorMethodOrField {
	/**
	 * A lazy loaded list containing all the parameters for this method
	 */
	private List parameters;

	public List getParameters() {
		if (false == hasParameters()) {
			this.setParameters(this.createParameters());
		}
		return this.parameters;
	}

	protected boolean hasParameters() {
		return this.parameters != null;
	}

	protected void setParameters(final List parameters) {
		ObjectHelper.checkNotNull("parameter:parameters", parameters);
		this.parameters = parameters;
	}

	abstract protected List createParameters();

	/**
	 * A lazy loaded set containing all the thrown types for this method
	 */
	private Set thrownTypes;

	public Set getThrownTypes() {
		if (false == hasThrows()) {
			this.setThrownTypes(this.createThrownTypes());
		}
		return this.thrownTypes;
	}

	protected boolean hasThrows() {
		return this.thrownTypes != null;
	}

	protected void setThrownTypes(final Set thrownTypes) {
		ObjectHelper.checkNotNull("parameter:thrownTypes", thrownTypes);
		this.thrownTypes = thrownTypes;
	}

	abstract protected Set createThrownTypes();
}
