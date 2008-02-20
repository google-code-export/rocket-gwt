package rocket.serialization.benchmark.client;

import java.util.List;

public class SuperBanana extends Banana {
	boolean shiny;

	/**
	 * Need to tell both generators the element type of the list.
	 * @gwt.typeArgs <rocket.serialization.benchmark.client.Pest>
	 * @serialization-type rocket.serialization.benchmark.client.Pest
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
