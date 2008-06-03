package rocket.serialization.benchmark.client;

public class Apple implements Fruit {
	String colour;

	public boolean equals(final Object otherObject) {
		return otherObject instanceof Apple ? this.colour.equals(((Apple) otherObject).colour) : false;
	}
}