package rocket.util.test.stacktracehelper.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

public interface StackTraceHelperTestServiceAsync extends RemoteService {
    void invoke(Exception exception, AsyncCallback callback);
}
