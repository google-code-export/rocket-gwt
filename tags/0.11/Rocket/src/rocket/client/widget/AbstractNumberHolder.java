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
package rocket.client.widget;

import rocket.client.util.ObjectHelper;

import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ChangeListenerCollection;
import com.google.gwt.user.client.ui.Composite;

public abstract class AbstractNumberHolder extends Composite implements NumberHolder {

    protected AbstractNumberHolder(){
        this.setChangeListenerCollection( new ChangeListenerCollection() );
    }

    /**
     * A list containing listeners to the various value change events.
     */
    private ChangeListenerCollection changeListenerCollection;

    public ChangeListenerCollection getChangeListenerCollection() {
        ObjectHelper.checkNotNull("field:changeListenerCollection", changeListenerCollection);
        return changeListenerCollection;
    }

    public void setChangeListenerCollection(final ChangeListenerCollection changeListenerCollection) {
        ObjectHelper.checkNotNull("parameter:changeListenerCollection", changeListenerCollection);
        this.changeListenerCollection = changeListenerCollection;
    }

    public void addChangeListener(final ChangeListener changeListener) {
        ObjectHelper.checkNotNull("parameter:changeListener", changeListener);

        this.getChangeListenerCollection().add(changeListener);
    }

    public void removeChangeListener(final ChangeListener changeListener) {
        ObjectHelper.checkNotNull("parameter:changeListener", changeListener);

        this.getChangeListenerCollection().remove(changeListener);
    }

    protected void fireValueChanged(){
        this.getChangeListenerCollection().fireChange( this );
    }
}
