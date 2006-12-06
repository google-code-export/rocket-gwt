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
package rocket.server.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import rocket.util.client.ObjectHelper;
import rocket.util.client.StringHelper;
import rocket.util.client.SystemHelper;
import rocket.util.server.IoHelper;

/**
 * A variety of helper and assertions relating to working with Throwable objects.
 * 
 * @author Miroslav Pokorny (mP)
 * @version 1.0
 */
public class ExceptionHelper {

    public static boolean isChecked(final Throwable throwable) {
        ObjectHelper.checkNotNull("parameter:throwable", throwable);
        return throwable instanceof Exception && (false == (throwable instanceof RuntimeException));
    }

    public static boolean isUnchecked(final Throwable throwable) {
        ObjectHelper.checkNotNull("parameter:throwable", throwable);
        return throwable instanceof RuntimeException;
    }

    public static void assertChecked(final String name, final Throwable throwable) {
        if (ExceptionHelper.isUnchecked(throwable)) {
            SystemHelper.handleAssertFailure(name, "The " + name
                    + " is unchecked and when it should be checked, throwable: " + throwable);
        }
    }

    public static void assertUnchecked(final String name, final Throwable throwable) {
        if (ExceptionHelper.isChecked(throwable)) {
            SystemHelper.handleAssertFailure(name, "The " + name
                    + " is checked and when it should be unchecked, throwable: " + throwable);
        }
    }

    public static String stackTraceToString(final Throwable throwable) {
        return ExceptionHelper.stackTraceToStringUsingPrintWriter(throwable);
    }

    protected static String stackTraceToStringUsingPrintWriter(final Throwable throwable) {
        ObjectHelper.checkNotNull("parameter:throwable", throwable);

        StringWriter writer = null;
        PrintWriter printer = null;
        try {
            writer = new StringWriter();
            printer = new PrintWriter(writer);
            throwable.printStackTrace(printer);
            printer.flush();
        } finally {
            IoHelper.closeIfNecessary(writer);
            IoHelper.closeIfNecessary(printer);
        }
        return writer.getBuffer().toString();
    }

    /**
     * Accepts any subclass of Throwable and returns a checked or Exception, wrapping the given throwable if necessary.
     * 
     * @param throwable
     *            Any exception
     * @return a Exception
     */
    public static Exception makeIntoException(final Throwable throwable) {
        ObjectHelper.checkNotNull("parameter:throwable", throwable);
        return throwable instanceof Exception ? (Exception) throwable : new CheckedNestedException(throwable);
    }

    /**
     * Accepts any subclass of Throwable and returns a unchecked or runtime exception, wrapping the given throwable if necessary.
     * 
     * @param throwable
     *            Any exception
     * @return a RuntimeException
     */
    public static RuntimeException makeIntoUncheckedException(final Throwable throwable) {
        ObjectHelper.checkNotNull("parameter:throwable", throwable);

        RuntimeException unchecked = null;
        while (true) {
            if (throwable instanceof RuntimeException) {
                unchecked = (RuntimeException) throwable;
                break;
            }
            unchecked = new UncheckedNestedException(throwable);
            break;
        }
        return unchecked;
    }

    /**
     * Builds an appropriate message for one exception possibly combining
     * 
     * @param wrapperMessage
     *            The message from the wrapper which may be null.
     * @param cause
     *            The cause which must not be null.
     * @return String
     */
    public static String buildExceptionMessage(final String wrapperMessage, final Throwable cause) {
        ObjectHelper.checkNotNull("parameter:cause", cause);
        return buildExceptionMessageWithPossibleNullCause(wrapperMessage, cause);
    }

    public static String buildExceptionMessageWithPossibleNullCause(final String wrapperMessage, final Throwable cause) {
        String causeMessage = null;
        if (cause != null) {
            causeMessage = cause.getMessage();
        }

        final boolean hasWrapperMessage = !StringHelper.isNullOrEmpty(wrapperMessage);
        final boolean hasCauseMessage = !StringHelper.isNullOrEmpty(causeMessage);

        final StringBuffer buffer = new StringBuffer();
        if (hasWrapperMessage) {
            buffer.append(wrapperMessage);

            if (hasCauseMessage) {
                buffer.append(", causeMessage: ");
            }
        }

        if (hasCauseMessage) {
            buffer.append(causeMessage);
        }

        if (buffer.length() == 0) {
            buffer.append((Object) null);
        }
        return buffer.toString();
    }

    protected ExceptionHelper() {
    }
}