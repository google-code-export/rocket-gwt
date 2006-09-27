package rocket.client.widget.xgesture;

import rocket.client.util.ObjectHelper;

/**
 * Each instance of this class represent part of a complete Gesture, such as a mouse button being pressed
 * or a change in direction.
 *  
 * Gestures are defined as a series of lines or changes in direction of a moving mouse.
 * Typically the start bean of this component is shared when the end of the previous CapturedGestureComponent.
 * The same is true of this end and the next CapturedGestureComponent.
 * @author Miroslav Pokorny (mP)
 */
public class CapturedGestureComponent {

	/**
	 * The state of the mouse at the beginning of the capture
	 */
	private MouseState start;

	public MouseState getStart() {
		ObjectHelper.checkNotNull("field:start", start);
		return start;
	}

	public void setStart(final MouseState start) {
		ObjectHelper.checkNotNull("parameter:start", start);
		this.start = start;
	}

	private long startTime;

	public long getStartTime() {
		return this.startTime;
	}

	public void setStartTime(final long startTime) {
		this.startTime = startTime;
	}

	/**
	 * The state of the mouse at the end of the capture
	 */
	private MouseState end;

	public MouseState getEnd() {
		ObjectHelper.checkNotNull("field:end", end);
		return end;
	}

	public void setEnd(final MouseState end) {
		ObjectHelper.checkNotNull("parameter:end", end);
		this.end = end;
	}

	private long endTime;

	public long getEndTime() {
		return this.endTime;
	}

	public void setEndTime(final long endTime) {
		this.endTime = endTime;
	}
}
