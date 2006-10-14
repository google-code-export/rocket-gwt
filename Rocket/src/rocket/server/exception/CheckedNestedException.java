/*
 * Copyright 2006 NSW Police Government Australia
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

/**
 * This base exception class provides support for wrapping exceptions.
 * 
 * <h3>PrintStackTrace and getMessage</h3>
 * The {@link #printStackTrace }method has been overridden to show the wrapped exceptions stack trace combined with the stack of this
 * exception. This general purpose class should be extended by application exceptions that wish to wrap other possible checked exceptions.
 * 
 * @author Miroslav Pokorny
 * @version 1.0
 */
public class CheckedNestedException extends AbstractPossiblyNestedCheckedException {

    public CheckedNestedException(final Throwable cause) {
        super(cause);
    }

    public CheckedNestedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public final boolean mustHaveCause() {
        return true;
    }
}