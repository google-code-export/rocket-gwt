package rocket.remoting.test.comet.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TestCometPayload implements IsSerializable {
	private long timestamp;

	public long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(final long timestamp) {
		this.timestamp = timestamp;
	}

	static long sequenceSource = 0;
	
	private long sequence = TestCometPayload.sequenceSource++;
	
	public long getSequence(){
		return this.sequence;
	}
	
	public String toString() {
		return super.toString() + ", timestamp: " + timestamp;
	}
}
