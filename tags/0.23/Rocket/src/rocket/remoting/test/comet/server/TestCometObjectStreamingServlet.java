package rocket.remoting.test.comet.server;

import java.util.Date;

import rocket.remoting.server.CometServerServlet;
import rocket.remoting.test.comet.client.TestCometPayload;

/**
 * A simple implementation of the server side portion of a Comet subsystem.
 * 
 * @author Miroslav Pokorny (mP)
 */
public class TestCometObjectStreamingServlet extends CometServerServlet {

    /**
     * Hardcodes the connectionTimeout and maximumBytesWritten messages.
     */
    public void init() {
        this.setConnectionTimeout(10 * 1000);
        this.setMaximumBytesWritten(1 * 1024);
    }

    /**
     * This method returns a List with two objects as well as inserting a sleep for two seconds.
     */
    protected Object queryObjectSource() {
        try {
            Thread.sleep(2000);
        } catch (final InterruptedException ignored) {
        }

        final TestCometPayload payload = new TestCometPayload();
        payload.setDate(new Date());
        return payload;
    }
}
