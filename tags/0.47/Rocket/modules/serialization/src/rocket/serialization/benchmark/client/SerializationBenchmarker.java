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

import java.util.ArrayList;
import java.util.Date;

import rocket.event.client.MouseClickEvent;
import rocket.event.client.MouseEventAdapter;
import rocket.remoting.client.HasSerializer;
import rocket.serialization.client.ObjectInputStream;
import rocket.serialization.client.ObjectOutputStream;
import rocket.serialization.client.SerializationFactory;
import rocket.serialization.client.SerializationFactoryComposer;
import rocket.util.client.StackTrace;
import rocket.widget.client.Button;
import rocket.widget.client.Html;
import rocket.widget.client.Label;
import rocket.widget.client.TextBox;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.impl.ClientSerializationStreamReader;
import com.google.gwt.user.client.rpc.impl.ClientSerializationStreamWriter;
import com.google.gwt.user.client.rpc.impl.Serializer;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * This class it not strictly an actual test case but rather leverages the
 * GWTTestCase to perform a number of timings of several gwt powered methods.
 * 
 * During and after each test it spits out a number of stats which can then
 * considered a very simple rough report. The layout isnt flash but the stats
 * are an interesting read.
 */
public class SerializationBenchmarker implements EntryPoint {

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

	public void onModuleLoad() {
		com.google.gwt.core.client.GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			public void onUncaughtException(final Throwable caught) {
				caught.printStackTrace();
				com.google.gwt.core.client.GWT.log("uncaught exception", caught);
				Window.alert(StackTrace.asString(caught));
			}
		});

		final RootPanel rootPanel = RootPanel.get();

		final VerticalPanel panel = new VerticalPanel();
		rootPanel.add(panel);

		panel.add(new Label("Array elements"));
		final TextBox arrayElementCount = new TextBox();
		arrayElementCount.setText("1");
		panel.add(arrayElementCount);

		final Button createInstance = new Button("Create instance");
		createInstance.addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent event) {
				final int elementCount = Integer.parseInt(arrayElementCount.getText());
				SerializationBenchmarker.this.setObject(createTree( elementCount ));
				SerializationBenchmarker.this.log( "", "Created instance that will be serialized, with " + elementCount + " elements.");
			}
		});
		panel.add(createInstance);

		final Button prepareGwtStream = new Button("Prepare " + GWT + " Stream");
		prepareGwtStream.addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent event) {
				SerializationBenchmarker.this.prepareGwtStream();
			}
		});
		panel.add(prepareGwtStream);

		final Button prepareRocketStream = new Button("Prepare " + ROCKET + " Stream");
		prepareRocketStream.addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent event) {
				SerializationBenchmarker.this.prepareRocketStream();
			}
		});
		panel.add(prepareRocketStream);

		panel.add(new Label("Iterations"));
		final TextBox iterations = new TextBox();
		iterations.setText("1");
		panel.add(iterations);

		final Button runGwtDeserialize = new Button("Run " + GWT + " Deserialize");
		runGwtDeserialize.addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent event) {
				final int iterationsCount = Integer.parseInt(iterations.getText());
				SerializationBenchmarker.this.performGwtDeserializationTimings(iterationsCount);
			}
		});
		panel.add(runGwtDeserialize);

		final Button runGwtSerialize = new Button("Run " + GWT + " Serialize");
		runGwtSerialize.addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent event) {
				final int iterationsCount = Integer.parseInt(iterations.getText());
				SerializationBenchmarker.this.performGwtSerializationTimings(iterationsCount);
			}
		});
		panel.add(runGwtSerialize);

		final Button runRocketDeserialize = new Button("Run " + ROCKET + " Deserialize");
		runRocketDeserialize.addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent event) {
				final int iterationsCount = Integer.parseInt(iterations.getText());
				SerializationBenchmarker.this.performRocketDeserializationTimings(iterationsCount);
			}
		});
		panel.add(runRocketDeserialize);

		final Button runRocketSerialize = new Button("Run " + ROCKET + " Serialize");
		runRocketSerialize.addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent event) {
				final int iterationsCount = Integer.parseInt(iterations.getText());
				SerializationBenchmarker.this.performRocketSerializationTimings(iterationsCount);
			}
		});
		panel.add(runRocketSerialize);

		final Button clearLog = new Button("Clear Log");
		clearLog.addMouseEventListener(new MouseEventAdapter() {
			public void onClick(final MouseClickEvent event) {
				SerializationBenchmarker.this.clearLog();
			}
		});
		panel.add(clearLog);
		
		final Html logger = new Html();
		this.setLogger(logger);
		rootPanel.add(logger);
	}

	void prepareGwtStream() {
		try {
			log(GWT, "Invoking serverside service to capture server produced serialized form for client side deserialization.");

			final Tree tree = (Tree) this.getObject();

			final FieldSerializerHackRemoteServiceAsync service = this.getService();
			service.echo(tree, new AsyncCallback() {
				public void onSuccess(final Object result) {
					service.sendBackLastResponse(new AsyncCallback() {
						public void onSuccess(final Object result) {
							String stream = (String) result;
							stream = stream.substring(4);
							SerializationBenchmarker.this.setGwtStream(stream);
							log(GWT, "Saved server produced serialized form for client side deserialization, stream[" + result + "]");
						}

						public void onFailure(final Throwable cause) {
							log(GWT, cause);
						}
					});
				}

				public void onFailure(final Throwable cause) {
					log(GWT, cause);
				}
			});

		} catch (final RuntimeException runtime) {
			log(GWT, runtime);
			throw runtime;
		}
	}

	void prepareRocketStream() {
		final SerializationFactory factory = this.getSerializationFactory();
		Object instance = this.getObject();

		log(ROCKET, "About to capture serialized instance for later deserialization.");

		final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
		objectOutputStream.writeObject(instance);
		final String stream = objectOutputStream.getText();
		this.setRocketStream(stream);

		log(ROCKET, "Saved serialized instance [" + stream + "]");
	}

	FieldSerializerHackRemoteServiceAsync getService() {
		final FieldSerializerHackRemoteServiceAsync service = (FieldSerializerHackRemoteServiceAsync) com.google.gwt.core.client.GWT
				.create(FieldSerializerHackRemoteService.class);
		final ServiceDefTarget serviceDefTarget = (ServiceDefTarget) service;
		serviceDefTarget.setServiceEntryPoint(URL);

		return service;
	}

	Serializer getSerializer() {
		final HasSerializer serializerHost = (HasSerializer) this.getService();
		final Serializer serializer = serializerHost.getSerializer();
		return serializer;
	}

	protected void performGwtSerializationTimings(final int iterations) {
		try {
			performGwtSerializationTimings0(iterations);
		} catch (final RuntimeException runtimeException) {
			throw runtimeException;
		} catch (final Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	protected void performGwtSerializationTimings0(final int iterations) throws Exception {
		final Object instance = this.getObject();
		final Serializer serializer = this.getSerializer();
		String clientSerializedForm = null;

		log(GWT, "Start timing...");
		final long start = System.currentTimeMillis();

		for (int i = 0; i < iterations; i++) {
			final ClientSerializationStreamWriter writer = new ClientSerializationStreamWriter(serializer);
			writer.prepareToWrite();
			writer.writeObject(instance);
			clientSerializedForm = writer.toString();
		}
		final long end = System.currentTimeMillis();

		log(GWT, "End of timing.");
		logBenchmark(GWT, SERIALIZATION, iterations, start, end);
	}

	protected void performGwtDeserializationTimings(final int iterations) {
		try {
			performGwtDeserializationTimings0(iterations);
		} catch (final RuntimeException runtimeException) {
			throw runtimeException;
		} catch (final Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	protected void performGwtDeserializationTimings0(final int iterations) throws Exception {
		final Serializer serializer = this.getSerializer();
		Object reconstituted = null;
		final String serverEncodedResponse = this.getGwtStream();

		log(GWT, "Start timing...");
		final long start = System.currentTimeMillis();

		for (int i = 0; i < iterations; i++) {
			final ClientSerializationStreamReader reader = new ClientSerializationStreamReader(serializer);
			reader.prepareToRead(serverEncodedResponse);
			reconstituted = reader.readObject();
		}
		final long end = System.currentTimeMillis();

		log(GWT, "End of timing.");
		logBenchmark(GWT, DESERIALIZATION, iterations, start, end);
	}

	/**
	 * Need a rpc interface which will be realised in order to get access to the
	 * serializer for the Tree class( which is why it appears in the method
	 * signature).
	 */
	static public interface FieldSerializerHackRemoteService extends RemoteService {
		Tree echo(Tree tree);

		String sendBackLastResponse();
	}

	static public interface FieldSerializerHackRemoteServiceAsync {
		void echo(Tree tree, AsyncCallback callback);

		void sendBackLastResponse(AsyncCallback callback);
	}

	protected void performRocketSerializationTimings(final int iterations) {
		final SerializationFactory factory = this.getSerializationFactory();
		String serializedForm = null;
		Object instance = this.getObject();
		final long start = System.currentTimeMillis();

		log(ROCKET, "Starting of timing.");

		for (int i = 0; i < iterations; i++) {
			final ObjectOutputStream objectOutputStream = factory.createObjectOutputStream();
			objectOutputStream.writeObject(instance);
			serializedForm = objectOutputStream.getText();
		}
		final long end = System.currentTimeMillis();

		log(ROCKET, "End of timing.");
		logBenchmark(ROCKET, SERIALIZATION, iterations, start, end);
	}

	protected void performRocketDeserializationTimings(final int iterations) {
		final SerializationFactory factory = this.getSerializationFactory();
		Object reconstituted = null;

		log(ROCKET, "Starting of timing.");

		final String stream = this.getRocketStream();
		final long start = System.currentTimeMillis();

		for (int i = 0; i < iterations; i++) {
			final ObjectInputStream objectInputStream = factory.createObjectInputStream(stream);
			reconstituted = objectInputStream.readObject();
		}
		final long end = System.currentTimeMillis();

		log(ROCKET, "End of timing.");
		logBenchmark(ROCKET, DESERIALIZATION, iterations, start, end);
	}

	SerializationFactory getSerializationFactory() {
		return (SerializationFactory) com.google.gwt.core.client.GWT.create(TestSerializationFactoryComposer.class);
	}

	/**
	 * @serialization-readableTypes rocket.serialization.benchmark.client.Tree
	 * @serialization-writableTypes rocket.serialization.benchmark.client.Tree
	 */
	static interface TestSerializationFactoryComposer extends SerializationFactoryComposer {

	}

	void logNewBenchmark(final String system) {
		this.getLogger().setText("");

		log(system, "Starting a new benchmark run.");
	}

	void logBenchmark(final String system, final String action, final int iterations, final long startedAt, final long endedAt) {
		log(system, "Time taken " + (endedAt - startedAt) + " (ms) for " + iterations + " iterations.");
	}

	void log(final String system, final Throwable caught) {
		log(system, "Benchmark failed " + caught);
		caught.printStackTrace();
	}

	void log(final String system, final String message) {
		final String lineOfText = system + " " + new Date() + " " + message;
		final Html logger = this.getLogger();
		logger.setHtml(logger.getHtml() + lineOfText + "<br>");
	}
	
	void clearLog(){
		this.getLogger().setHtml("");
	}


	private Html logger;

	Html getLogger() {
		return this.logger;
	}

	void setLogger(final Html logger) {
		this.logger = logger;
	}
	
	/**
	 * An instance of the Tree being serialized/deserialized.
	 */
	Object object;

	Object getObject() {
		return this.object;
	}

	void setObject(final Object object) {
		this.object = object;
	}

	private String gwtStream;

	String getGwtStream() {
		return this.gwtStream;
	}

	void setGwtStream(final String gwtStream) {
		this.gwtStream = gwtStream;
	}

	private String rocketStream;

	String getRocketStream() {
		return this.rocketStream;
	}

	void setRocketStream(final String rocketStream) {
		this.rocketStream = rocketStream;
	}

	static Tree createTree(final int elementCount) {
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

		for (int i = 0; i < elementCount; i++) {
			Pest pest = null;

			if ((i & 1) == 0) {
				final Worm worm = new Worm();
				worm.legless = LEGLESS;
				worm.text = "" + i;
				pest = worm;
			} else {
				final Fly fly = new Fly();
				fly.eyeCount = EYECOUNT + i;
				pest = fly;
			}
			superBanana.pests.add(pest);
		}

		return tree;
	}
}
