package rocket.client.dom;
/**
 * This interface indicates that the given instance should be destroyed() by calling
 * {@link #destroy} when the reference is no longer needed.
 * @author Miroslav Pokorny (mP)
 */
public interface Destroyable {
    void destroy();
}
