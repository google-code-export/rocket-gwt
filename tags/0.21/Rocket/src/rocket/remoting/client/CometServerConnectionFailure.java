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
package rocket.remoting.client;


/**
 * This exception is thrown when the hidden iframe used by the CometClient fails to connect to the server component.
 * @author Miroslav Pokorny (mP)
 *
 */
public class CometServerConnectionFailure extends RuntimeException {
    public CometServerConnectionFailure() {
        super();
    }

    public CometServerConnectionFailure(final String message) {
        super(message);
    }

    public CometServerConnectionFailure(final String message, final Throwable caught) {
        super(message, caught);
    }

    public CometServerConnectionFailure(final Throwable caught) {
        super(caught);
    }
    }

