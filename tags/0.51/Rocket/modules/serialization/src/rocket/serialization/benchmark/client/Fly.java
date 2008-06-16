package rocket.serialization.benchmark.client;

public class Fly extends Pest {
	int eyeCount;

	public boolean equals(final Object otherObject) {
		return otherObject instanceof Fly ? ((Fly) otherObject).eyeCount == this.eyeCount : false;
	}
}