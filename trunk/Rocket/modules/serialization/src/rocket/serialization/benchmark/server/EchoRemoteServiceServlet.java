package rocket.serialization.benchmark.server;


import rocket.serialization.benchmark.client.Tree;
import rocket.serialization.benchmark.client.SerializationBenchmarker.FieldSerializerHackRemoteService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class EchoRemoteServiceServlet extends RemoteServiceServlet implements FieldSerializerHackRemoteService {
	public Tree echo(final Tree tree){
		return tree;
	}
	public String sendBackLastResponse(){
		return lastResponse;
	}
	
	protected void onAfterResponseSerialized(String serializedResponse) {
		this.lastResponse = serializedResponse;
	}
	
	static String lastResponse;
}
