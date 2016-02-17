package org.systemx.util.string;

/**
 * Thrown when a string value cannot be converted to some type.
 * 
 */
public final class StringValueConversionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param message
	 *            exception message
	 */
	public StringValueConversionException(final String message) {
		super(message);
	}

	/**
	 * Constructor.
	 * 
	 * @param message
	 *            exception message
	 * @param cause
	 *            exception cause
	 */
	public StringValueConversionException(final String message,
			final Throwable cause) {
		super(message, cause);
	}
}
