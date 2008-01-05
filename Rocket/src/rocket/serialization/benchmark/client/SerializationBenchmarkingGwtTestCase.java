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
package rocket.serialization.benchmark.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rocket.remoting.client.HasSerializer;
import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.client.ObjectOutputStream;
import rocket.serialization.client.SerializationFactory;
import rocket.serialization.client.SerializationFactoryComposer;
import rocket.util.client.Tester;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.impl.ClientSerializationStreamReader;
import com.google.gwt.user.client.rpc.impl.ClientSerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.Serializer;

/**
 * This class it not strictly an actual test case but rather leverages the GWTTestCase to perform a number of timings of several gwt powered methods.
 * 
 * During and after each test it spits out a number of stats which can then considered a very simple rough report. The layout isnt flash but the stats are an interesting read.
 *
 */
public class SerializationBenchmarkingGwtTestCase extends GWTTestCase {

	//final int SEED_COUNT = 2;

	final int DELAY = 60 * 1000;

	final String URL = "./service";
	
	final static String GWT = "GWT";
	final static String ROCKET = "Rocket";
	
	final static String SERIALIZATION = "serialization";
	final static String DESERIALIZATION = "deserialization";

	final static String COLOUR = "Blue";
	final static boolean SHINY = true;
	final static int WEIGHT = 123;
	final static boolean LEGLESS = true;
	final static int EYECOUNT = 4567;
	
	public String getModuleName() {
		return "rocket.serialization.benchmark.SerializationBenchmarkingGwtTestCase";
	}

	public void testGwtWithSmallNumberOfObjects() {
		this.executeGwtTests(5, 50);
	}

	public void testRocketWithSmallNumberOfObjects() {
		this.executeRocketTests(5, 50);
	}

	public void testGwtWithMediumNumberOfObjects() {
		this.executeGwtTests(50, 20);
	}
	public void testRocketWithMediumNumberOfObjects() {
		this.executeRocketTests(50, 20);
	}
	
	public void testGwtWithLargishNumberOfObjects() {
		this.executeGwtTests(200, 10);
	}
	
	public void testRocketWithLargishNumberOfObjects() {
		this.executeRocketTests(200, 10);
	}

	
	public void executeGwtTests(final int elementCount, final int iterations) {
		try {
			logNewBenchmark( GWT);
			
			log( GWT, "Need to perform 2x RPC in order to capture server produced serialized form for client side deserialization.");
			
			final Tree tree = createTree(elementCount);

			final SerializerHackRemoteServiceAsync rpcProxy = (SerializerHackRemoteServiceAsync) com.google.gwt.core.client.GWT.create(SerializerHackRemoteService.class);
			final ServiceDefTarget serviceDefTarget = (ServiceDefTarget) rpcProxy;
			serviceDefTarget.setServiceEntryPoint(URL);

			final HasSerializer serializerHost = (HasSerializer) rpcProxy;
			final Serializer serializer = serializerHost.getSerializer();

			this.delayTestFinish(DELAY);

			rpcProxy.echo(tree, new AsyncCallback() {
				public void onSuccess(final Object result) {
					rpcProxy.sendBackLastResponse(new AsyncCallback() {
						public void onSuccess(final Object result) {
							try {
								performGwtSerializationTimings(serializer, tree, iterations);
								final String encoded = (String) result;
								performGwtDeserializationTimings(serializer, tree, encoded.substring(4), iterations);
								log( GWT, "Benchmark completed successfully\n");
								
							} catch (final Exception caught) {
								caught.printStackTrace();
								fail(caught.getMessage());
							}
						}

						public void onFailure(final Throwable cause) {
							cause.printStackTrace();
							fail(cause.getMessage());
						}
					});
				}

				public void onFailure(final Throwable cause) {
					cause.printStackTrace();
					fail(cause.getMessage());
				}
			});
			
		} catch (Exception ignored) {
			ignored.printStackTrace();

			log( GWT, "Benchmark failed " + ignored);
			fail(ignored.getMessage());
		}
	}

	protected void performGwtSerializationTimings(final Serializer serializer, final Tree tree, final int iterations) throws Exception {
		log( GWT, "Starting timing...");
		final long start = System.currentTimeMillis();
		String clientSerializedForm = null;

		for (int i = 0; i < iterations; i++) {
			// write
			final ClientSerializationStreamWriter writer = new ClientSerializationStreamWriter(serializer);
			writer.prepareToWrite();
			writer.writeObject(tree);
			clientSerializedForm = writer.toString();
		}
		final long end = System.currentTimeMillis();

		log( GWT, "End of timing.");
		logBenchmark(GWT, SERIALIZATION, iterations, start, end);
		log( GWT, "ClientSerializedForm: " + clientSerializedForm.length() + "=" + clientSerializedForm);
	}

	protected void performGwtDeserializationTimings(final Serializer serializer, final Tree tree, final String serverEncodedResponse, final int iterations) throws Exception {
		log( GWT, "Starting timing...");
		final long start = System.currentTimeMillis();
		Tree reconstituted = null;

		for (int i = 0; i < iterations; i++) {
			final ClientSerializationStreamReader reader = new ClientSerializationStreamReader(serializer);
			reader.prepareToRead(serverEncodedResponse);
			reconstituted = (Tree) reader.readObject();
		}
		final long end = System.currentTimeMillis();

		assertEquals("comparing original and reconsituted", tree, reconstituted);

		log( GWT, "End of timing.");
		logBenchmark(GWT, DESERIALIZATION, iterations, start, end);
		log( GWT, "ServerEncodedResponse: " + serverEncodedResponse.length() + "=" + serverEncodedResponse);
		this.finishTest();
	}

	/**
	 * Need a rpc interface which will be realised in order to get access to the serializer for the Tree class( which is why it appears in the method signature).
	 */
	static public interface SerializerHackRemoteService extends RemoteService {
		Tree echo(Tree tree);

		String sendBackLastResponse();
	}

	static public interface SerializerHackRemoteServiceAsync {
		void echo(Tree tree, AsyncCallback callback);

		void sendBackLastResponse(AsyncCallback callback);
	}

	public void executeRocketTests(final int elementCount, final int iterations) {
		try {
			logNewBenchmark( ROCKET);
			
			final SerializationFactory factory = (SerializationFactory) com.google.gwt.core.client.GWT.create(TestSerializationFactoryComposer.class);
			final Tree tree = this.createTree(elementCount);

			final String serializedForm = this.performRocketSerializationTimings(tree, factory, iterations);
			this.performRocketDeserializationTimings(factory, tree, serializedForm, iterations);

			log(ROCKET, " SerializedForms:" + serializedForm.length() + "=" + serializedForm);
			log( ROCKET, "Benchmark completed successfully\n");
			
		} catch (final Exception ignored) {
			ignored.printStackTrace();

			log(ROCKET, "Benchmark failed " + ignored);
			fail(ignored.getMessage());
		}
	}

	protected String performRocketSerializationTimings(final Tree tree, final SerializationFactory factory, final int iterations)
			throws Exception {
		log( ROCKET, "Starting of timing.");
		
		final long start = System.currentTimeMillis();
		String serializedForm = null;

		for (int i = 0; i < iterations; i++) {
			final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
			objectOutputStream.writeObject(tree);
			serializedForm = objectOutputStream.getText();
		}
		final long end = System.currentTimeMillis();

		log( ROCKET, "End of timing.");
		logBenchmark(ROCKET, SERIALIZATION, iterations, start, end);

		return serializedForm;
	}

	protected void performRocketDeserializationTimings(final SerializationFactory factory, final Tree tree, final String serializedForm,
			final int iterations) throws Exception {
		log( ROCKET, "Starting of timing.");
		Tree reconstituted = null;

		final long start = System.currentTimeMillis();

		for (int i = 0; i < iterations; i++) {
			final ObjectInputStream objectInputStream = factory.createObjectInputStream(serializedForm);
			reconstituted = (Tree) objectInputStream.readObject();
		}
		final long end = System.currentTimeMillis();
		assertEquals("comparing original and reconsituted", tree, reconstituted);

		log( ROCKET, "End of timing.");
		logBenchmark(ROCKET, DESERIALIZATION, iterations, start, end);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.benchmark.client.SerializationBenchmarkingGwtTestCase.Tree
	 * @serialization-writableTypes rocket.serialization.benchmark.client.SerializationBenchmarkingGwtTestCase.Tree
	 */
	static interface TestSerializationFactoryComposer extends SerializationFactoryComposer {

	}
		
	void logNewBenchmark( final String system ){
		System.out.println();
		log( system, "Starting a new benchmark run.");
	}
	
	void logBenchmark( final String system, final String action, final int iterations, final long startedAt, final long endedAt ){
		
		log( system, "Time taken " + (endedAt - startedAt) +" (ms) for " + iterations + " iterations." );
	}
	
	void log(final String system, final String message) {
		System.out.println( system + " " + new Date() + " " + message);
	}
	
	static Tree createTree( final int elementCount ){
		final Tree tree = new Tree();
		tree.tree = tree;
		
		final Apple apple = new Apple();
		apple.colour = COLOUR;
		tree.apple = apple;
		
		final Banana banana = new Banana();
		banana.weight = WEIGHT;
		tree.banana = banana;
		
		final SuperBanana superBanana = new SuperBanana();
		superBanana.weight = WEIGHT;
		superBanana.shiny = SHINY;
		tree.superBanana = superBanana;
		
		superBanana.pests = new ArrayList();
				
		for( int i = 0; i < elementCount; i++ ){
			Pest pest = null;
			
			if( ( i & 1 ) == 0 ){
				final Worm worm = new Worm();
				worm.legless = LEGLESS;
				worm.text = "" + i;
				pest = worm;
			} else {
				final Fly fly = new Fly();
				fly.eyeCount = EYECOUNT + i;
				pest = fly;
			}
			superBanana.pests.add( pest );	
		}
		
		return tree;
	}
	
	static public class Tree implements Serializable, IsSerializable {
		Fruit apple;
		Fruit banana;
		Fruit superBanana;
		Tree tree;
		
		transient Object transientField;
		
		public boolean equals( final Object otherObject ){
			boolean same = false;
			
			while( true ){
				if( false == otherObject instanceof Tree ){
					break;
				}
				if( this == otherObject ){
					same = true;
					break;
				}
				
				final Tree otherTree = (Tree)otherObject;
				if( false == Tester.nullSafeEquals( this.apple, otherTree.apple )){
					break;
				}
				if( false == Tester.nullSafeEquals( this.banana, otherTree.banana )){
					break;
				}
				if( false == Tester.nullSafeEquals( this.superBanana, otherTree.superBanana )){
					break;
				}
				if( this != this.tree || otherTree != otherTree.tree ){
					break;
				}
				same = true;
				break;
			}
			
			return same;
		}
	}
	
	static interface Fruit extends Serializable, IsSerializable {
		
	}
	static public class Apple implements Fruit {
		String colour;
		
		public boolean equals( final Object otherObject ){
			return otherObject instanceof Apple ? this.colour.equals( ((Apple)otherObject).colour) : false;
		}
	}
	
	static public class Banana implements Fruit {
		int weight;
		
		public boolean equals( final Object otherObject ){
			return otherObject instanceof Banana ? ((Banana)otherObject).weight == this.weight : false;
		}
	}
	
	static public class SuperBanana extends Banana {
		boolean shiny;

		/**
		 * Need to tell both generators the element type of the list.
		 * @gwt.typeArgs <rocket.serialization.benchmark.client.SerializationBenchmarkingGwtTestCase.Pest>
		 * @serialization-listElementType rocket.serialization.benchmark.client.SerializationBenchmarkingGwtTestCase.Pest
		 */
		List pests;
		
		public boolean equals( final Object otherObject ){
			boolean same = false;
			
			while( true ){
				if( false == otherObject instanceof SuperBanana ){
					break;
				}
				if( false == super.equals(otherObject)){
					break;
				}
				final SuperBanana otherSuperBanana = (SuperBanana)otherObject;
				if( this.shiny != otherSuperBanana.shiny ){
					break;
				}
				if( null == pests && null == otherSuperBanana.pests ){
					same = true;
					break;
				}				
				if( null != pests && null == otherSuperBanana.pests ){
					break;
				}
				if( null == pests && null != otherSuperBanana.pests ){
					break;
				}
				same = this.pests.equals( otherSuperBanana.pests);
				break;
			}
			
			return same; 
		}
	}
	
	static abstract public class Pest implements Serializable, IsSerializable {
	}
	
	static public class Worm extends Pest{
		boolean legless;
		
		String text;
		
		public boolean equals( final Object otherObject ){
			boolean same = false;
			
			if( otherObject instanceof Worm ){
				final Worm otherWorm = (Worm) otherObject;
				if( this.legless == otherWorm.legless ){
					same = Tester.nullSafeEquals( this.text, otherWorm.text );
				}				
			}
			
			return same;			
		}
	}
	
	static public class Fly extends Pest{
		int eyeCount;
		
		public boolean equals( final Object otherObject ){
			return otherObject instanceof Fly ? ((Fly) otherObject ).eyeCount == this.eyeCount : false;
		}
	}
}
