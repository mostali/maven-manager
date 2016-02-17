package org.systemx.util.string;

import java.util.Collection;

/**
 * 
 */
public class Args {
	/**
	 * Checks argument is not null
	 * 
	 * @param <T>
	 * @param argument
	 * @param name
	 * @return The 'argument' parameter
	 * @throws IllegalArgumentException
	 */
	public static <T> T notNull(final T argument, final String name) {
		if (argument == null) {
			throw new IllegalArgumentException("Argument '" + name
					+ "' may not be null.");
		}
		return argument;
	}

	/**
	 * Checks argument is not empty (not null and has a non-whitespace
	 * character)
	 * 
	 * @param <T>
	 *            the type of the argument to check for emptiness
	 * 
	 * @param argument
	 *            the argument to check for emptiness
	 * @param name
	 *            the name to use in the error message
	 * @return The {@code argument} parameter if not empty
	 * @throws IllegalArgumentException
	 *             when the passed {@code argument} is empty
	 */
	public static <T extends CharSequence> T notEmpty(final T argument,
			final String name) {
		
		if (US.isEmpty(argument)) {
			throw new IllegalArgumentException("Argument '" + name
					+ "' may not be null or empty.");
		}
		
		return argument;
	}

	/**
	 * 
	 * Checks argument is not null or empty
	 * 
	 * @param collection
	 * @param message
	 * @param params
	 * @throws IllegalArgumentException
	 *             if the passed collection is either null or empty
	 */
	public static <T extends Collection<?>> T notEmpty(final T collection,
			final String message, final Object... params) {
		if (collection == null || collection.isEmpty()) {
			throw new IllegalArgumentException(Args.format(message, params));
		}
		return collection;
	}

	/**
	 * Checks argument is not null or empty
	 * 
	 * @param collection
	 * @param name
	 * @throws IllegalArgumentException
	 *             if the passed collection is either null or empty
	 */
	public static <T extends Collection<?>> T notEmpty(final T collection,
			final String name) {
		return notEmpty(collection,
				"Collection '%s' may not be null or empty.", name);
	}

	/**
	 * Checks if argument is within a range
	 * 
	 * @param <T>
	 * @param min
	 * @param max
	 * @param value
	 * @param name
	 * @throws IllegalArgumentException
	 */
	public static <T extends Comparable<T>> T withinRange(final T min,
			final T max, final T value, final String name) {
		notNull(min, name);
		notNull(max, name);
		if ((value.compareTo(min) < 0) || (value.compareTo(max) > 0)) {
			throw new IllegalArgumentException(
					String.format(
							"Argument '%s' must have a value within [%s,%s], but was %s",
							name, min, max, value));
		}
		return value;
	}

	/**
	 * Check if argument is true
	 * 
	 * @param argument
	 * @param msg
	 * @param params
	 * @return argument
	 */
	public static boolean isTrue(final boolean argument, final String msg,
			final Object... params) {
		if (argument == false) {
			throw new IllegalArgumentException(format(msg, params));
		}
		return argument;
	}

	/**
	 * Check if argument is false
	 * 
	 * @param argument
	 * @param msg
	 * @param params
	 * @return argument
	 */
	public static boolean isFalse(final boolean argument, final String msg,
			final Object... params) {
		if (argument == true) {
			throw new IllegalArgumentException(format(msg, params));
		}
		return argument;
	}

	/**
	 * Format the message. Allow for "{}" as well as %s etc. (see Formatter).
	 * 
	 * @param msg
	 * @param params
	 * @return formatted message
	 */
	static String format(String msg, final Object... params) {
		msg = msg.replaceAll("\\{\\}", "%s");
		return String.format(msg, params);
	}
}
