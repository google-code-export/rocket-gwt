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
package rocket.remoting.test.comet.server;

import rocket.remoting.test.comet.client.CometServerActionService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * This service is used as a sink for any messages from the client to terminate an existing
 * comet session. 
 * @author Miroslav Pokorny
 */
public class CometServerActionServiceImpl extends RemoteServiceServlet implements CometServerActionService {

	/**
	 * When true the test comet servlet will send a 500 and close the connection when doGet is invoked
	 */
	private static boolean failNextConnection;
	
	static public boolean isFailNextConnection(){
		try{
			return failNextConnection;
		} finally {
			failNextConnection = false;
		}
	}
	
	public void failNextConnection(){
		failNextConnection = true;
	}
	
	/**
	 * When true the next time the poll method of the test comet service is invoked it will throw an exception 
	 */
	private static boolean failNextPoll;
	
	static public boolean isFailNextPoll(){
		try{
			return failNextPoll;
		} finally {
			failNextPoll = false;
		}
	}
	
	public void failNextPoll(){
		failNextPoll = true;
	}
	
	/**
	 * When true the next time the test comet service is polled it will make a request to terminate the session from the server.
	 */
	private static boolean terminated;
	
	static public boolean isTerminated(){
		try{
			return terminated;
		} finally {
			terminated = false;
		}
	}
	
	public void terminate() {
		terminated = true;
	}
	
	/**
	 * When true the next time the service is polled it will enter a really long sleep which will cause the connection to time out
	 * , close and force the client(browser) to open a new connection.
	 */
	private static boolean timeoutNextPoll;
	
	static public boolean isTimeoutNextPoll(){
		try{
			return timeoutNextPoll;
		} finally {
			timeoutNextPoll = false;
		}
	}
	
	public void timeoutNextPoll(){
		timeoutNextPoll = true;
	}	
}
