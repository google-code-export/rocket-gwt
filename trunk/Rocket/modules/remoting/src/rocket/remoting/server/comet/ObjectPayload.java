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
package rocket.remoting.server.comet;

import rocket.remoting.client.CometConstants;

/**
 * Instances represent a single object being pushed from the server to the client.
 * 
 * @author Miroslav Pokorny
 */
public class ObjectPayload implements Message{
  
	public ObjectPayload( final Object object ){
    this.setObject(object);
  }
  
  public int getCommand(){
    return CometConstants.OBJECT_PAYLOAD;
  }
  
  private Object object;
  public Object getObject(){
    return object;
  }
  void setObject( final Object object ){
    this.object = object;
  }
}