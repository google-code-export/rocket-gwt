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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;

/**
 * Takes one or more messages and publishes them to their respective consumers
 * 
 * @author Miroslav Pokorny (mP)
 */
public class MessageBroker {
    /**
     * A reference to the singleton.
     */
    private static MessageBroker singleton;

    public static synchronized MessageBroker getSingleton() {
        if (singleton == null) {
            singleton = new MessageBroker();
        }
        return singleton;
    }

    /**
     * Only a single instance needs to exist as it is stateless.
     */
    private MessageBroker() {
        this.setSubscribers(new HashMap());
    }

    public void publishMessages(final List messages) {
        ObjectHelper.checkNotNull("parameter:messages", messages);

        final Iterator iterator = messages.iterator();
        while (iterator.hasNext()) {
            final Message message = (Message) iterator.next();
            this.publish(message);
        }
    }

    public void publish(final Message message) {
        ObjectHelper.checkNotNull("parameter:message", message);

        final String destination = message.getDestination();
        final Object payload = message.hasPayload() ? message.getPayload() : null;
        final List subscribers = (List) this.getSubscribers().get(destination);
        if (null != subscribers) {
            final Iterator iterator = subscribers.iterator();
            while (iterator.hasNext()) {
                final TopicSubscriber subscriber = (TopicSubscriber) iterator.next();
                subscriber.onMessage(payload);
            }
        }
    }

    /**
     * A map which contains a registry of subscribers, key = commandName value = Command.
     */
    private Map subscribers;

    protected Map getSubscribers() {
        ObjectHelper.checkNotNull("field:subscribers", subscribers);
        return subscribers;
    }

    protected void setSubscribers(final Map subscribers) {
        ObjectHelper.checkNotNull("parameter:subscribers", subscribers);
        this.subscribers = subscribers;
    }

    /**
     * Registers or adds a new Subsriber to the registry.
     * 
     * @param name
     * @param subscriber
     */
    public void subscribe(final String name, final TopicSubscriber subscriber) {
        StringHelper.checkNotEmpty("parameter:name", name);
        ObjectHelper.checkNotNull("parameter:subscriber", subscriber);

        final Map subscribers = this.getSubscribers();
        List listeners = (List) subscribers.get(name);
        if (null == listeners) {
            listeners = new ArrayList();
            subscribers.put(name, listeners);
        }
        listeners.add(subscriber);
    }

    public void unsubscribe(final String name, final TopicSubscriber subscriber) {
        StringHelper.checkNotEmpty("parameter:name", name);
        ObjectHelper.checkNotNull("parameter:subscriber", subscriber);

        final Map subscribers = this.getSubscribers();
        List listeners = (List) subscribers.get(name);
        if (null != listeners) {
            listeners.remove(subscriber);

            if (listeners.isEmpty()) {
                subscribers.remove(name);
            }
        }
    }
}
