package rocket.serialization.benchmark.client;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

import rocket.util.client.ObjectHelper;

public class Worm extends Pest implements Serializable, IsSerializable{
	boolean legless;
	
	String text;
	
	public boolean equals( final Object otherObject ){
		boolean same = false;
		
		if( otherObject instanceof Worm ){
			final Worm otherWorm = (Worm) otherObject;
			if( this.legless == otherWorm.legless ){
				same = ObjectHelper.nullSafeEquals( this.text, otherWorm.text );
			}				
		}
		
		return same;			
	}
}