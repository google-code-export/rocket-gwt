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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rocket.util.client.Checker;

/**
 * Convenient base class for any implementation of the CometConnection
 * interface. Instances of this class buffer messages until they are committed
 * by the CometServlet etc.
 * 
 * @author Miroslav Pokorny
 */
public class CometConnectionImpl implements CometConnection {

	public CometConnectionImpl() {
		super();

		this.setMessages(this.createMessages());
		this.setSequence(0);
	}

	/**
	 * Pushes a single object over this comet connection.
	 * 
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public void push(final Object object) {
		final long sequence = this.getSequence();
		final ObjectPayload objectPayload = new ObjectPayload(object, sequence);
		this.pushMessage(objectPayload);
		this.incrementSequence();
	}

	/**
	 * Sends a terminate message to the client. All subsequent pushes will fail
	 * and this session will be terminated.
	 */
	public void terminate() {
		final long sequence = this.getSequence();
		this.pushMessage(new Terminate(sequence));
		this.setTerminated(true);
		this.incrementSequence();
	}

	protected void terminatedGuard() {
		if (this.isTerminated()) {
			throw new IllegalStateException("This comet connect has already been terminated, no further payloads may be sent.");
		}
	}

	/**
	 * This flag tracks whether a connection has been terminated.
	 */
	private boolean terminated;

	protected boolean isTerminated() {
		return this.terminated;
	}

	protected void setTerminated(final boolean terminated) {
		this.terminated = terminated;
	}

	protected void pushMessage(final Message message) {
		Checker.notNull("parameter:message", message);
		this.terminatedGuard();

		this.getMessages().add(message);
	}

	/**
	 * This list accumulates any messages that will be sent to the client.
	 */
	protected List<Message> messages;

	protected List<Message> getMessages() {
		Checker.notNull("field:messages", messages);
		return this.messages;
	}

	protected void setMessages(final List<Message> messages) {
		Checker.notNull("parameter:messages", messages);
		this.messages = messages;
	}

	protected List<Message> createMessages() {
		return new ArrayList<Message>();
	}

	/**
	 * A sequence number is included in all payloads as a means to ensure payloads arent lost or delivered twice.
	 */
	private long sequence;

	public long getSequence() {
		return this.sequence;
	}

	void setSequence(final long sequence) {
		this.sequence = sequence;
	}
	
	void incrementSequence(){
		this.setSequence( this.getSequence() + 1 );
	}
}
