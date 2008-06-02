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
package rocket.beans.rebind;

import rocket.beans.rebind.beanreference.BeanReference;
import rocket.beans.rebind.beanreference.BeanReferenceImpl;
import rocket.beans.rebind.value.Value;
import rocket.generator.rebind.SourceWriter;
import rocket.generator.rebind.type.Type;
import rocket.util.client.Checker;

/**
 * Represents a nested anonymous bean being constructed.
 * 
 * @author Miroslav Pokorny
 */
public class NestedBean extends Bean implements Value, BeanReference{

	public NestedBean() {
		super();		
		
		this.setBeanReference( this.createBeanReference() );
	}
	
	private BeanReferenceImpl beanReference;
	
	protected BeanReferenceImpl getBeanReference(){
		Checker.notNull("field:beanReference", beanReference);
		return this.beanReference;
	}
	
	protected void setBeanReference( final BeanReferenceImpl beanReference ){
		Checker.notNull("parameter:beanReference", beanReference);
		this.beanReference = beanReference;
	}
	
	protected BeanReferenceImpl createBeanReference(){
		return new BeanReferenceImpl(){
			@Override
			public String getId(){
				return NestedBean.this.getId();
			}
			@Override
			public Type getType(){
				return NestedBean.this.getValueType();
			}
		};
	}
	
	public void setPropertyType( final Type ignored ){		
	}
	
	public boolean isCompatibleWith(final Type type) {
		return this.getBeanReference().isCompatibleWith(type);
	}
	
	public boolean isEmpty() {
		return this.getBeanReference().isEmpty();
	}
	
	public void write(final SourceWriter writer) {
		this.getBeanReference().write( writer );
	}
}
