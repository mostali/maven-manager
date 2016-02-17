package org.systemx.util.string;

public class US {

	public static final String LINE_SEP = System.getProperty("line.separator");

	public static String trim(String string) {

		if (string == null)
			return null;

		return string.trim();
	}

	public static String trim(String string, boolean removeControlSymbol) {

		if (string == null)
			return null;

		if (removeControlSymbol)
			string = removeControlSymbol(string);

		return string.trim();
	}

	/**
	 * Remove control characters
	 * 
	 * @param string
	 * @return
	 */
	public static String removeControlSymbol(String string) {

		if (string == null)
			return null;

		string = string.replaceAll("\\p{Cc}", "");

		return string;
	}

	public static String decodeProtectedSymbol(String s) {

		if (s == null)
			return null;

		StringBuilder sb = new StringBuilder();

		char checkChar;

		for (int index = 0; index < s.length(); index++) {

			if (s.charAt(index) == '\\')

				if (index != s.length() - 1) {

					checkChar = s.charAt(index + 1);

					if (checkChar == ';' || checkChar == '{'
							|| checkChar == '}') {

					} else

						sb.append(s.charAt(index));

				} else

					sb.append(s.charAt(index));
			else
				sb.append(s.charAt(index));
		}
		return sb.toString();
	}

	public static boolean isTrue(final String s)
			throws StringValueConversionException {

		if (s != null) {

			if (s.equalsIgnoreCase("true"))
				return true;

			if (s.equalsIgnoreCase("false"))
				return false;

			if (s.equalsIgnoreCase("on") || s.equalsIgnoreCase("yes")
					|| s.equalsIgnoreCase("y") || s.equalsIgnoreCase("1"))
				return true;

			if (s.equalsIgnoreCase("off") || s.equalsIgnoreCase("no")
					|| s.equalsIgnoreCase("n") || s.equalsIgnoreCase("0"))
				return false;

			if (isEmpty(s))
				return false;

			throw new StringValueConversionException("Boolean value \"" + s
					+ "\" not recognized");
		}

		return false;
	}

	/**
	 * Checks whether two strings are equals taken care of 'null' values and
	 * treating 'null' same as trim(string).equals("")
	 * 
	 * @param string1
	 * @param string2
	 * @return true, if both strings are equal
	 */
	public static boolean isEqual(final String string1, final String string2) {

		if ((string1 == null) && (string2 == null))
			return true;

		if (isEmpty(string1) && isEmpty(string2))
			return true;

		if ((string1 == null) || (string2 == null))
			return false;

		return string1.equals(string2);
	}

	public static boolean isEqual(final String string1, final String string2,
			boolean caseSensitivity) {

		if ((string1 == null) && (string2 == null))
			return true;

		if (isEmpty(string1) && isEmpty(string2))
			return true;

		if ((string1 == null) || (string2 == null))
			return false;

		return caseSensitivity ? string1.equals(string2) : string1
				.equalsIgnoreCase(string2);
	}

	public static boolean isEqual(final String string1, final String string2,
			boolean caseSensitivity, boolean trim, boolean removeControlSymbol) {

		if ((string1 == null) && (string2 == null))
			return true;

		if (isEmpty(string1) && isEmpty(string2))
			return true;

		if ((string1 == null) || (string2 == null))
			return false;

		String trim1 = null;
		String trim2 = null;

		if (trim) {
			trim1 = string1.trim();
			trim2 = string2.trim();
		}

		if (removeControlSymbol) {
			trim1 = removeControlSymbol(trim1);
			trim2 = removeControlSymbol(trim2);
		}
		return caseSensitivity ? string1.equals(string2) : string1
				.equalsIgnoreCase(string2);
	}

	public static boolean isEmpty(final String s) {
		return (s == null) || (s.length() == 0) || (s.trim().length() == 0);
	}

	public static boolean isEmpty(final CharSequence s) {
		return (s == null) || (s.length() == 0)
				|| (s.toString().trim().length() == 0);
	}

	public static Object parseNumeric(String string) {
		try {
			Integer num = Integer.parseInt(string);
			return num;

		} catch (NumberFormatException ex) {
			ex.printStackTrace();

			try {

				Double num = Double.parseDouble(string);

				return num;

			} catch (NumberFormatException exD) {

				exD.printStackTrace();

			}

		}

		return null;
	}

	// public static void main(String[] args) {}

	public static String format(String string, Object... objs) {

		String s = String.format(string, objs);
		return s;
	}

	public static String normalizeWindowsNewLine(String s) {
		return s.replaceAll("\n", LINE_SEP);
	}

	public static String removeFirstString(String first, String s) {

		if (isEmpty(s))
			return s;

		if (!s.startsWith(first))
			return s;

		return s.substring(first.length(), s.length());

	}

	public static String normalizeSpace(String s) {

		StringBuilder sb = new StringBuilder();

		int first = 0;
		for (int i = 0; i < s.length(); ++i) {

			if (s.charAt(i) == ' ') {

				if (first == 0) {

					sb.append(s.charAt(i));

					first = 1;

				}

			} else {
				sb.append(s.charAt(i));
				first = 0;
			}
		}
		return sb.toString();
	}

	public static int getInteger(Object s, Integer def, boolean printErrors) {

		if (s == null)
			return def;

		if (s instanceof Integer)
			return (Integer) s;

		if (s instanceof Double)
			return ((Double) s).intValue();

		if (s instanceof String)
			try {
				int i = Integer.parseInt((String) s);
				return i;
			} catch (NumberFormatException ex) {
				if (printErrors)
					ex.printStackTrace();
				return def;
			}

		return def;
	}

	public static String getString(String s, String def) {
		if (isEmpty(s))
			return def;
		return s;
	}

}
