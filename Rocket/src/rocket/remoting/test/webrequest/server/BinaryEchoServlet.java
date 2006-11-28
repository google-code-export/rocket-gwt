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
package rocket.remoting.test.webrequest.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rocket.util.server.IoHelper;

/**
 * This servlet simply re-writes any post data back.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class BinaryEchoServlet extends HttpServlet {
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        final String contentType = request.getContentType();
        response.setContentType(contentType);

        InputStream inputStream = null;
        final byte[] buffer = new byte[4 * 1024];
        final OutputStream outputStream = response.getOutputStream();
        try {
            inputStream = request.getInputStream();

            while (true) {
                final int readByteCount = inputStream.read(buffer);
                if (-1 == readByteCount) {
                    break;
                }
                outputStream.write(buffer, 0, readByteCount);
            }
            outputStream.flush();
        } finally {
            IoHelper.closeIfNecessary(inputStream);
            IoHelper.closeIfNecessary(outputStream);
        }
    }
}
