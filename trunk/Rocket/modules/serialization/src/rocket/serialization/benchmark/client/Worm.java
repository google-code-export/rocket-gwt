package rocket.serialization.benchmark.client;

import java.io.Serializable;

import rocket.util.client.Tester;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Worm extends Pest implements Serializable, IsSerializable{
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