package rocket.serialization.benchmark.client;

import java.io.Serializable;

import rocket.util.client.ObjectHelper;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Tree implements Serializable, IsSerializable {
			Fruit apple;
			Fruit banana;
			Fruit superBanana;
			Tree tree;
			
			transient Object transientField;
			
			public boolean equals( final Object otherObject ){
				boolean same = false;
				
				while( true ){
					if( false == otherObject instanceof Tree ){
						break;
					}
					if( this == otherObject ){
						same = true;
						break;
					}
					
					final Tree otherTree = (Tree)otherObject;
					if( false == ObjectHelper.nullSafeEquals( this.apple, otherTree.apple )){
						break;
					}
					if( false == ObjectHelper.nullSafeEquals( this.banana, otherTree.banana )){
						break;
					}
					if( false == ObjectHelper.nullSafeEquals( this.superBanana, otherTree.superBanana )){
						break;
					}
					if( this != this.tree || otherTree != otherTree.tree ){
						break;
					}
					same = true;
					break;
				}
				
				return same;
			}
}
