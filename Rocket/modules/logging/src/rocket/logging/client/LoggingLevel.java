package rocket.logging.client;

import java.io.Serializable;

/**
 * This enum holds all the possible logging levels.
 * 
 * @author Miroslav Pokorny
 * @TODO Make LoggingLevel a true enum.
 */
final public class LoggingLevel implements Serializable {

	/**
	 * This method only exists to assist the custom deserializer.
	 * 
	 * @param loggingLevel
	 * @return
	 */
	static LoggingLevel getLoggingLevel(final int loggingLevel) {
		LoggingLevel level = null;

		while (true) {
			if (LoggingLevel.DEBUG.getValue() == loggingLevel) {
				level = LoggingLevel.DEBUG;
				break;
			}
			if (LoggingLevel.INFO.getValue() == loggingLevel) {
				level = LoggingLevel.INFO;
				break;
			}
			if (LoggingLevel.WARN.getValue() == loggingLevel) {
				level = LoggingLevel.WARN;
				break;
			}
			if (LoggingLevel.ERROR.getValue() == loggingLevel) {
				level = LoggingLevel.ERROR;
				break;
			}
			if (LoggingLevel.FATAL.getValue() == loggingLevel) {
				level = LoggingLevel.FATAL;
				break;
			}
			if (LoggingLevel.NONE.getValue() == loggingLevel) {
				level = LoggingLevel.NONE;
				break;
			}
			throw new IllegalArgumentException("Unknown rocket.logging.client.LoggingLevel, value: \"" + loggingLevel + "\".");
		}

		return level;

	}

	static public LoggingLevel getLoggingLevel(final String levelName) {
		LoggingLevel level = null;

		while (true) {
			if (LoggingConstants.DEBUG.equals(levelName)) {
				level = LoggingLevel.DEBUG;
				break;
			}
			if (LoggingConstants.INFO.equals(levelName)) {
				level = LoggingLevel.INFO;
				break;
			}
			if (LoggingConstants.WARN.equals(levelName)) {
				level = LoggingLevel.WARN;
				break;
			}
			if (LoggingConstants.ERROR.equals(levelName)) {
				level = LoggingLevel.ERROR;
				break;
			}
			if (LoggingConstants.FATAL.equals(levelName)) {
				level = LoggingLevel.FATAL;
				break;
			}
			if (LoggingConstants.NONE.equals(levelName)) {
				level = LoggingLevel.NONE;
				break;
			}
			throw new IllegalArgumentException("Unknown rocket.logging.client.LoggingLevel, name: \"" + levelName + "\".");
		}

		return level;
	}

	static public final LoggingLevel DEBUG = new LoggingLevel(LoggingConstants.DEBUG, 0);

	static public final LoggingLevel INFO = new LoggingLevel(LoggingConstants.INFO, 1);

	static public final LoggingLevel WARN = new LoggingLevel(LoggingConstants.WARN, 2);

	static public final LoggingLevel ERROR = new LoggingLevel(LoggingConstants.ERROR, 3);

	static public final LoggingLevel FATAL = new LoggingLevel(LoggingConstants.FATAL, 4);

	static public final LoggingLevel NONE = new LoggingLevel(LoggingConstants.NONE, 5);

	/**
	 * Dont call this constructor it only exists to keep the serialization
	 * generator happy. A custom FieldSerializer takes care of avoiding extra
	 * instances being created.
	 */
	public LoggingLevel() {

	}

	protected LoggingLevel(final String name, final int value) {
		super();

		this.name = name;
		this.value = value;
	}

	public boolean less(final LoggingLevel otherLoggingLevel) {
		return this.value < otherLoggingLevel.value;
	}

	public boolean lessOrEqual(final LoggingLevel otherLoggingLevel) {
		return this.value <= otherLoggingLevel.value;
	}

	private String name;

	private int value;

	int getValue() {
		return this.value;
	}

	public String toString() {
		return this.name;
	}
}