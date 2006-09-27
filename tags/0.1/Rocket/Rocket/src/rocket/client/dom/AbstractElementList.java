/*
 * Copyright 2006 NSW Police Government Australia
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package rocket.client.dom;

import java.util.AbstractList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import rocket.client.collection.IteratorView;
import rocket.client.util.ObjectHelper;
import rocket.client.util.PrimitiveHelper;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;

/**
 * This list represents a list of DomElements. Rather than directly working with the actual DOM element.
 * this list automatically wraps and unwraps them as elements are set/getted.
 * This allows a user to work with nice bean classes rather than nasty manipulation of an element using DOM.setAttribute etc.
 * @author Miroslav Pokorny (mP)
 */
public abstract class AbstractElementList extends AbstractList {


    protected AbstractElementList(){
        super();

        this.createWrappers();
    }
    /**
     * This map maintains an association between elements and wrappers
     */
    private Map wrappers;

    protected Map getWrappers(){
        ObjectHelper.checkNotNull("field:wrappers", wrappers );
        return wrappers;
    }
    protected void setWrappers( final Map wrappers ){
        ObjectHelper.checkNotNull("parameter:wrappers", wrappers );
        this.wrappers = wrappers;
    }

    protected void createWrappers(){
        this.setWrappers( new HashMap() );
    }

	protected abstract Object createWrapper( final Element element);

	protected Element visitWrappedElement(final ElementWrapper wrapper) {
		return wrapper.getElement();
	}

	protected abstract void checkElementType(final Object wrapper);

	public int size(){
	    return this.size0( this.getCollection() );
    }

    private native int size0( final JavaScriptObject collection )/*-{
     return collection.length;
     }-*/;

	public boolean add(final Object element) {
		this.checkElementType(element);

        final ElementWrapper wrapper = (ElementWrapper) element;
		this.add0( this.getCollection(), this.visitWrappedElement(wrapper));
        this.getWrappers().put( wrapper.getElement(), wrapper );
		this.incrementModificationCounter();
		return false;
	}

	public native void add0(final JavaScriptObject collection, final Element element) /*-{
	 var index = collection.length;
	 collection[ index ] = element;

	 if( collection[ index ] != element ){
	 @rocket.client.dom.DomHelper::handleUnableToSetCollectionItem(Lcom/google/gwt/core/client/JavaScriptObject;I)(collection,index);
	 }
	 }-*/;

	public boolean remove(final Object element) {
		final int index = this.indexOf(element);
		final boolean removable = index != -1;
		if (removable) {
			final Element removed = remove0(this.getCollection(), index);
            this.getWrappers().remove( removed );
			this.incrementModificationCounter();
		}
		return removable;
	}

    /**
     * Escapes to javascript to delete an item in the collection returning the deleted element.
     * @param collection
     * @param index
     * @return
     */
	public native Element remove0(final JavaScriptObject collection, final int index) /*-{
	 var removed = collection[ index ];
	 delete collection[ index ];
	 if( collection.length >= index && collection[ index ] == removed ){
	     @rocket.client.dom.DomHelper::handleUnableToRemoveCollectionItem(Lcom/google/gwt/core/client/JavaScriptObject;I)(collection,index);
	 }
     return removed;
	 }-*/;

	public Object get(final int index) {
        PrimitiveHelper.checkBetween("parameter:index", index, 0, size());

        Object wrapper = null;
        final Element element = this.get0(this.getCollection(), index);
        if (null != element) {
            wrapper = this.getWrappers().get(element);

            // cache does not contain wrapper - need to create & save wrapper
            if (null == wrapper) {
                wrapper = this.createWrapper(element);
                this.getWrappers().put(element, wrapper);
            }
        }
        return wrapper;
    }

	private native Element get0( final JavaScriptObject collection, final int index )/*-{
	 	return collection[ index ];
	 }-*/;

	public Object set(final int index, final Object object) {
		this.checkElementType( object );

        final ElementWrapper wrapper = (ElementWrapper) object;
        final Element wrappedElement = wrapper.getElement();

        final Map wrappers = this.getWrappers();
        final Element previousElement = this.set0( wrappedElement, this.getCollection(), index );
        Object previousWrapper = null;

        // if there was an element at the given index locate/create a wrapper.
        if( null != previousElement ){
            previousWrapper = wrappers.get( previousElement );
            if( null != previousWrapper ){
                previousWrapper = this.createWrapper( previousElement );
                wrappers.remove( previousElement );
            }
        }

		wrappers.put( wrappedElement, wrapper );
		return previousWrapper;
	}


	private native Element set0(final Element element, final JavaScriptObject collection, final int index)/*-{
     var previousElement = collection[ index ];
	 collection[ index ] = element;

	 // check item was actually modified.
	 if( collection[ index ] != element ){
	 	@rocket.client.dom.DomHelper::handleUnableToSetCollectionItem(Lcom/google/gwt/core/client/JavaScriptObject;I)(collection,index);
	 }
     return previousElement;
	 }-*/;

	public boolean contains(final Object element) {
		return this.indexOf(element) != -1;
	}

	public Iterator iterator() {
		final AbstractElementListIterator iterator = new AbstractElementListIterator();
		iterator.setList(this);
		iterator.syncModificationCounters();
		return iterator;
	}

	/**
	 * Provides the iterator view of thi@author Miroslav Pokorny (mP)thor Miroslav Pokorny (mP)
	 */
	class AbstractElementListIterator extends IteratorView{
        protected boolean hasNext0() {
            return this.getIndex() < this.getList().size();
        }

        protected Object next0(int type) {
            return this.getList().get( this.getIndex() );
        }

        protected void leavingNext() {
            this.setIndex( this.getIndex() + 1 );
        }

        protected void remove0() {
            final AbstractElementList list = this.getList();
            list.remove( this.getIndex() - 1 );
        }

        protected int getParentModificationCounter() {
            return this.getList().getModificationCounter();
        }

        AbstractElementList list;

        AbstractElementList getList() {
            ObjectHelper.checkNotNull("field:list", list);
            return this.list;
        }

        void setList(final AbstractElementList list) {
            ObjectHelper.checkNotNull("parameter:list", list);
            this.list = list;
        }

        int index;

        int getIndex() {
            return this.index;
        }

        void setIndex(final int index) {
            this.index = index;
        }
	};

	public Object[] toArray() {
		return this.toArray(new Object[this.size()]);
	}

	public Object[] toArray(Object[] array) {
		ObjectHelper.checkNotNull("parameter:array", array);

		for (int i = 0; i < array.length; i++) {
			array[i] = this.get(i);
		}
		return array;
	}

	public int indexOf(final Object element) {
		int foundIndex = -1;

		if (null != element) {
			int size = this.size();
			for (int i = 0; i < size; i++) {
				if (element == this.get(i)) {
					foundIndex = i;
					break;
				}
			}
		}

		return foundIndex;
	}

	public int lastIndexOf(final Object element) {

		int foundIndex = -1;
		if (null == element) {
			int i = this.size();
			while (i > 0) {
				i--;
				if (element == this.get(i)) {
					foundIndex = i;
					break;
				}
			}
		}

		return foundIndex;
	}

	public void clear() {
		throw new UnsupportedOperationException("clear()");
	}

	public void add(final int index, final Object element) {
		throw new UnsupportedOperationException("add()");
	}

	public Object remove(final int index) {
		throw new UnsupportedOperationException("remove()");
	}

	public boolean containsAll(final Collection collection) {
		throw new UnsupportedOperationException("containsAll()");
	}

	public boolean addAll(final Collection collection) {
		throw new UnsupportedOperationException("addAll()");
	}

	public boolean addAll(int arg0, final Collection collection) {
		throw new UnsupportedOperationException("addAll()");
	}

	public boolean removeAll(final Collection collection) {
		throw new UnsupportedOperationException("removeAll()");
	}

	public boolean retainAll(final Collection collection) {
		throw new UnsupportedOperationException("retainAll()");
	}

	/**
	 * The javascript collection or collection object itself
	 */
	private JavaScriptObject collection;

	public native JavaScriptObject getCollection()/*-{
	 var collection = this.@rocket.client.dom.AbstractElementList::collection;

	 @rocket.client.util.ObjectHelper::checkNotNull(Ljava/lang/String;Ljava/lang/Object;)( "field:collection", collection );
	 return collection;
	 }-*/;

    public native void setCollection(final JavaScriptObject collection)/*-{
	 @rocket.client.util.ObjectHelper::checkNotNull(Ljava/lang/String;Ljava/lang/Object;)( "parameter:collection", collection );
	 this.@rocket.client.dom.AbstractElementList::collection = collection;
	 }-*/;

	/**
	 * This counter is used by iterators to keep track of concurrent modifications.
	 * Each time the list is modified this counter is incremented.
	 */
	private int modificationCounter;

	protected int getModificationCounter() {
		return modificationCounter;
	}

	protected void setModificationCounter(final int modificationCounter) {
		this.modificationCounter = modificationCounter;
	}

	protected void incrementModificationCounter() {
		this.setModificationCounter(this.getModificationCounter() + 1);
	}

	public String toString() {
		return super.toString() + ", collection: " + collection
				+ ", modificationCounter: " + modificationCounter;
	}
}