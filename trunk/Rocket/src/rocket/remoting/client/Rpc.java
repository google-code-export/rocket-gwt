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
package rocket.remoting.client;

import rocket.remoting.client.support.rpc.RpcServiceClient;

import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * A collection of helper methods related to rpc.
 * @author Miroslav Pokorny
 */
public class Rpc {
	/**
	 * Sets the service def target upon the given rpc client.
	 * 
	 * @param client A rpc client be it a gwt, rocket java or json client.
	 * @param url The url to set
	 */
	public static void setServiceDefTarget( final Object client, final String url ){
		final ServiceDefTarget serviceDefTarget = Rpc.getServiceDefTarget(client);
		serviceDefTarget.setServiceEntryPoint( url );
	}
	
	/**
	 * Helper which checks and then casts the given client to ServiceDefTarget reference.
	 * @param client A rpc client.
	 * @return
	 */
	static ServiceDefTarget getServiceDefTarget( final Object client ){
		if( client instanceof ServiceDefTarget ){
			throw new RuntimeException( "The parameter:client is not a rpc client (gwt or rocket): " + client );
		}
		return (ServiceDefTarget)client;
	}
	
	/**
	 * Sets the authentication credentials upon the given rpc client.
	 * @param client A rpc client 
	 * @param username The username to set
	 * @param password The password to set
	 */
	public static void setCredentials( final Object client, final String username, final String password ){
		final RpcServiceClient rpcServiceClient = Rpc.getServiceClient(client); 
		rpcServiceClient.setUsername( username );
		rpcServiceClient.setPassword( password );
	}

	/**
	 * Sets the common timeout for any future requests upon the given rpc client.
	 * @param client A rpc client
	 * @param timeout Timeout value in milliseconds
	 */
	public static void setTimeout( final Object client, final int timeout ){
		final RpcServiceClient rpcServiceClient = Rpc.getServiceClient(client); 
		rpcServiceClient.setTimeout( timeout );
	}

	/**
	 * Helper which checks and then casts the given client to RpcServiceClient reference.
	 * @param client A rpc client.
	 * @return
	 */
	static RpcServiceClient getServiceClient( final Object client ){
		if( client instanceof RpcServiceClient ){
			throw new RuntimeException( "The parameter:client is not a Java or Json rpc client, proxy: " + client );
		}
		return (RpcServiceClient)client;
	}
}
