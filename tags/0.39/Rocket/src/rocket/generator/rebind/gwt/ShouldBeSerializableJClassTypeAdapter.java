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
package rocket.generator.rebind.gwt;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import rocket.generator.rebind.type.Type;

import com.google.gwt.core.ext.typeinfo.JClassType;

/**
 * This Adapter includes a special test for any of the jdk container types which are all serializable.
 * @author Miroslav Pokorny
 * 
 * TODO Once all the GWT emulated jdk classes all implement Serializable this type will no longer be needed.
 */
public class ShouldBeSerializableJClassTypeAdapter extends JClassTypeTypeAdapter {
	/**
	 * In addition to the actual interfaces implemented by this type this class also adds Serializable.
	 */
	protected Set createInterfaces() {
		final Set interfaces = new HashSet();
		
		final JClassType[] actualImplementingInterfacess = this.getJClassType().getImplementedInterfaces();
		interfaces.addAll( Arrays.asList(actualImplementingInterfacess) );
		
		interfaces.add( this.getSerializable() );
		
		return interfaces;
	}
	
	public boolean isAssignableFrom(final Type otherType) {
		boolean assignable = false;

		if( otherType.equals( this.getSerializable() )){
			assignable = true;
		} else {
			assignable = super.isAssignableFrom(otherType);
		}
		return assignable;
	}

	public boolean isAssignableTo(final Type otherType) {
		boolean assignable = false;

		if( otherType.equals( this.getSerializable() )){
			assignable = true;
		} else {
			assignable = super.isAssignableTo(otherType);
		}
		return assignable;
	}
	
	protected Type getSerializable(){
		return this.getGeneratorContext().getType( Constants.SERIALIZABLE );
	}
}
