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
package rocket.logging.client;

import java.io.Serializable;

import rocket.remoting.client.JavaRpcService;
import rocket.remoting.client.Rpc;
import rocket.util.client.Checker;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Sends all messages that are above the logging threashhold to the server. No
 * attempt is made to batch messages but rather each and every messages results
 * in another call to the server.
 * 
 * @author Miroslav Pokorny
 */
abstract public class ServerLogger<R> extends LoggerImpl implements JavaRpcService {

	protected ServerLogger(final String name) {
		super(name);
	}

	/**
	 * Sub classes must override this method to return the url of the matching
	 * server logging service.
	 * 
	 * @return
	 */
	abstract protected String getServiceEntryPoint();

	/**
	 * A cache copy of a service proxy.
	 */
	protected LoggingServiceAsync service;

	protected LoggingServiceAsync getService() {
		if (null == this.service) {
			this.setService(this.createLoggingService());
		}

		Checker.notNull("field:service", service);
		return service;
	}

	protected void setService(final LoggingServiceAsync service) {
		Checker.notNull("parameter:service", service);
		this.service = service;
	}

	protected LoggingServiceAsync createLoggingService() {
		final LoggingServiceAsync service = (LoggingServiceAsync) GWT.create(LoggingService.class);
		Rpc.setServiceDefTarget(service, this.getServiceEntryPoint());
		return service;
	}

	@Override
	protected void log(final LoggingLevel level, final String message) {
		this.sendEvent(level, message, null);
	}

	@Override
	protected void log(final LoggingLevel level, final String message, final Throwable throwable) {
		this.sendEvent(level, message, throwable);
	}

	protected void sendEvent(final LoggingLevel loggingLevel, final String message, final Throwable throwable) {
		Checker.notNull("parameter:loggingLevel", loggingLevel);
		Checker.notNull("parameter:message", message);

		final LoggingEvent loggingEvent = new LoggingEvent();
		loggingEvent.setLoggingLevel(loggingLevel);
		loggingEvent.setMessage(message);
		loggingEvent.setName(this.getName());

		// only send serializable throwables.
		loggingEvent.setThrowable(throwable instanceof Serializable ? throwable : null);

		this.sendEvent(loggingEvent);
	}

	protected void sendEvent(final LoggingEvent event) {
		this.getService().log(event, this.getCallback());
	}

	/**
	 * Caches the callback that will consume server responses.
	 */
	private AsyncCallback<R> callback;

	protected AsyncCallback<R> getCallback() {
		if (null == this.callback) {
			this.setCallback(this.createCallback());
		}

		Checker.notNull("field:callback", callback);
		return this.callback;
	}

	protected void setCallback(final AsyncCallback<R> callback) {
		Checker.notNull("parameter:callback", callback);
		this.callback = callback;
	}

	protected AsyncCallback<R> createCallback() {
		return new AsyncCallback<R>() {
			public void onSuccess(final R result) {
				ServerLogger.this.handleServiceSuccess(result);
			}

			public void onFailure(final Throwable throwable) {
				ServerLogger.this.handleServiceFailure(throwable);
			}
		};
	}

	protected void handleServiceSuccess(final R result) {
		// do nothing...
	}

	protected void handleServiceFailure(final Throwable throwable) {
		throwable.printStackTrace();

		final GWT.UncaughtExceptionHandler handler = GWT.getUncaughtExceptionHandler();
		if (null != handler) {
			handler.onUncaughtException(throwable);
		} else {
			if (throwable instanceof RuntimeException) {
				throw (RuntimeException) throwable;
			}
			if (throwable instanceof Error) {
				throw (Error) throwable;
			}
			throw new RuntimeException("Logging service failed, reason: " + throwable.getMessage(), throwable);
		}
	}

	public String toString() {
		return super.toString() + ", serviceEntryPoint: \"" + this.getServiceEntryPoint() + "\".";
	}
}
