package rocket.beans.rebind;

import java.util.Iterator;
import java.util.Properties;

import rocket.beans.rebind.bean.Bean;
import rocket.util.client.ObjectHelper;

/**
 * This class visits all values replacing placeholders with actual values found
 * in an accompanying properties file.
 * 
 * @author Miroslav Pokorny FIXME not yet completed.
 */
public class PlaceHolderReplacer {

	public void replacePlaceHolders(final BeanFactoryGeneratorContext context) {
		final Iterator beans = context.getBeans().values().iterator();
		while (beans.hasNext()) {
			final Bean bean = (Bean) beans.next();
			this.visitBean(bean);
		}
	}

	protected void visitBean(final Bean bean) {
		bean.setId(this.replace(bean.getId()));
		// bean.setInitMethod(initMethod);
		bean.setTypeName(this.replace(bean.getId()));
	}

	protected String replace(final String text) {
		return text;
	}

	/**
	 * The properties file that will be source of all placeholders.
	 */
	private Properties properties;

	protected Properties getProperties() {
		ObjectHelper.checkNotNull("field:properties", properties);
		return properties;
	}

	public void setProperties(final Properties properties) {
		ObjectHelper.checkNotNull("parameter:properties", properties);
		this.properties = properties;
	}
}
