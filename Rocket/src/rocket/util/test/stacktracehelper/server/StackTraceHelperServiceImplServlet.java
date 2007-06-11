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
package rocket.util.test.stacktracehelper.server;

import rocket.util.test.stacktracehelper.client.ServerServiceException;
import rocket.util.test.stacktracehelper.client.StackTraceHelperTestService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * This servlet throws an Exception verifying that serialization works.
 * 
 * @author Miroslav Pokorny
 */
public class StackTraceHelperServiceImplServlet extends RemoteServiceServlet implements StackTraceHelperTestService {

    public void invoke(Exception ignored) throws ServerServiceException {
        this.twoFramesAwayFromMethodWhichThrowsException();
    }

    protected void twoFramesAwayFromMethodWhichThrowsException() throws ServerServiceException {
        this.oneFrameAwayFromMethodWhichThrowsException();
    }

    protected void oneFrameAwayFromMethodWhichThrowsException() throws ServerServiceException {
        this.throwException();
    }

    protected void throwException() throws ServerServiceException {
        System.out.println("SERVER About to throw Exception.");
        throw new ServerServiceException( "SERVER");
    }
}
