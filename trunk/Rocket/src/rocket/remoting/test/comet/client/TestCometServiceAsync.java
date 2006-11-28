package rocket.remoting.test.comet.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;

public interface TestCometServiceAsync extends RemoteService{
    void invoke( AsyncCallback callback);
}
