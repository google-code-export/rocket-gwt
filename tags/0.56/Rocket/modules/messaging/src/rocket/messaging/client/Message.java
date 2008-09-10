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
package rocket.messaging.client;

import rocket.util.client.Checker;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A value object used to transmit messages between two points.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class Message implements IsSerializable {
	/**
	 * The name of the destination topic/queue
	 */
	public String destination;

	public String getDestination() {
		Checker.notEmpty("field:destination", destination);
		return destination;
	}

	public void setDestination(final String destination) {
		Checker.notEmpty("parameter:destination", destination);
		this.destination = destination;
	}

	/**
	 * The payload or data for the message
	 */
	public Payload payload;

	public Payload getPayload() {
		Checker.notNull("field:payload", payload);
		return payload;
	}

	public boolean hasPayload() {
		return this.payload != null;
	}

	public void setPayload(final Payload payload) {
		Checker.notNull("parameter:payload", payload);
		this.payload = payload;
	}

	public void setNullPayload() {
	}

	public String toString() {
		return super.toString() + ", destination\"" + destination + "\", payload: " + payload;
	}
}
