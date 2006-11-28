package rocket.widget.client.menu;

import com.google.gwt.user.client.ui.Widget;

/**
 * Convenient base class that implements all {@see MenuListener} methods with do nothing methods.
 * @author Miroslav Pokorny (mP)
 */
public abstract class MenuListenerImpl implements MenuListener {

    protected MenuListenerImpl(){
        super();
    }
    
    public void onMenuCancelled(final Widget widget) {
    }

    public boolean onBeforeMenuOpened(final Widget widget) {
        return true;
    }

    public void onMenuOpened(final Widget widget) {
    }

}
