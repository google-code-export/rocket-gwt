/**
 * 
 */
package rocket.generator.rebind;

import java.util.List;

/**
 * This interface is implemented by compile time components that can potentially
 * include meta data.
 * 
 * @author Miroslav Pokorny
 */
public interface HasMetadata {
	List getMetadataValues(String name);
}
