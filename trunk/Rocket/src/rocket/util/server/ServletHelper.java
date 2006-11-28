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
package rocket.util.server;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import rocket.util.client.StringHelper;

/**
 * A collection of helpful servlet methods
 * 
 * @author Miroslav Pokorny (mP)
 */
public class ServletHelper {
    public static void writeBytes(final String mimeType, final byte[] image, final HttpServletResponse response)
            throws IOException {
        StringHelper.checkNotEmpty("parameter:mimeType", mimeType);
        ObjectHelper.checkNotNull("parameter:image", image);
        ObjectHelper.checkNotNull("parameter:response", response);

        OutputStream out = null;

        try {
            response.setContentType(mimeType);
            response.setContentLength(image.length);

            out = response.getOutputStream();
            out.write(image);
            out.flush();
        } finally {
            IoHelper.closeIfNecessary(out);
        }
    }
}