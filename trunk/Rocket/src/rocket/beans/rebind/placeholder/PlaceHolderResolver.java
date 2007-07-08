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
package rocket.beans.rebind.placeholder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;
import rocket.util.server.IoHelper;
import rocket.util.server.UncheckedIOException;

/**
 * Is used to merge string values that can contain placeholders replacing the
 * replace place holders with actual values. Place holder values can include
 * references to other place holders. These will be followed until a value is
 * completely resolved.
 * 
 * @author Miroslav Pokorny
 */
public class PlaceHolderResolver {

	public PlaceHolderResolver() {
		super();

		this.setValues(this.createValues());
	}

	public void load(final String fileName) {
		StringHelper.checkNotEmpty("parameter:fileName", fileName );
		
			final InputStream file = this.getClass().getResourceAsStream(fileName);
			if( null == file ){
				throw new UncheckedIOException("Unable to find properties file [" + fileName + "]");
			}
			this.merge(file);
	}

	public void merge(final InputStream inputStream) {
		ObjectHelper.checkNotNull("parameter:inputStream", inputStream );
		
		try {
			this.getValues().load(inputStream);
		} catch (final IOException io) {
			throw new UncheckedIOException(io);
		} finally {
			IoHelper.closeIfNecessary(inputStream);
		}
	}

	public String resolve(final String string) {
		String input = string;
		String output = string;
		final Properties values = this.getValues();
		
		while( true ){
			output = StringHelper.format( input , values );
			
			if( input.equals( output )){
				break;
			}
			input = output;
		}
		
		return output;
	}

	/**
	 * A properites object that holds the values for placeholders found in the
	 * xml file.
	 */
	private Properties values;

	protected Properties getValues() {
		ObjectHelper.checkNotNull("field:values", values);
		return this.values;
	}

	protected void setValues(final Properties values) {
		ObjectHelper.checkNotNull("parameter:values", values);
		this.values = values;
	}

	protected Properties createValues() {
		return new Properties();
	}

}
