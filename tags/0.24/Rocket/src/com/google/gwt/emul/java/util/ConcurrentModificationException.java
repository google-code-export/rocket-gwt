package java.util;

/**
 * This exception should be thrown when an iterator view of a collection has detected a modification.
 * 
 * @author Miroslav Pokorny (mP)
 * 
 * TODO need to modify emulated Vector/List/HashMap etc to throw ConcurrentModificationException.
 */
public class ConcurrentModificationException extends RuntimeException {
    public ConcurrentModificationException() {
        super();
    }

    public ConcurrentModificationException(String message) {
        super(message);
    }
}
