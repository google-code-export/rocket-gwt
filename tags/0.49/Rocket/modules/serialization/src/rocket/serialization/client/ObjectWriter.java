package rocket.serialization.client;

public interface ObjectWriter {

	void write(Object object, ObjectOutputStream objectOutputStream);
}
