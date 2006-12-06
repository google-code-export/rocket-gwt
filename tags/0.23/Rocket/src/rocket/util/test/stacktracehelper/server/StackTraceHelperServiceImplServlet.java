package rocket.util.test.stacktracehelper.server;

import rocket.util.test.stacktracehelper.client.StackTraceHelperTestService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * This servlet throws an Exception verifying that serialization works.
 * @author mP
 *
 */
public class StackTraceHelperServiceImplServlet extends RemoteServiceServlet implements StackTraceHelperTestService {

    public void invoke( Exception ignored ) throws Exception {
        this.twoFramesAwayFromMethodWhichThrowsException();
    }
    
    protected void twoFramesAwayFromMethodWhichThrowsException() throws Exception{
        this.oneFrameAwayFromMethodWhichThrowsException();
    }

    protected void oneFrameAwayFromMethodWhichThrowsException() throws Exception{
        this.throwException();
    }

    protected void throwException() throws Exception{
        System.out.println("SERVER About to throw Exception.");
        throw new Exception();
    }
}
