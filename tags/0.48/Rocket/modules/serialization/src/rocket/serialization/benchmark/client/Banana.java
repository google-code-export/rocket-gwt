package rocket.serialization.benchmark.client;

public class Banana implements Fruit {
	int weight;
	
	public boolean equals( final Object otherObject ){
		return otherObject instanceof Banana ? ((Banana)otherObject).weight == this.weight : false;
	}
}