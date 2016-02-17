package org.systemx.util.string;

import java.io.File;

public class UA {

	public static void checkExist(File f, String message) {
		if (f == null || !f.exists())
			throw new NullPointerException(message);

	}

	public static void checkArg(String a, String message) {

		if (a == null || isEmpty(a))
			throw new NullPointerException(message);

	}

	public static boolean isEmpty(String a) {

		if (a == null || a.length() == 0 || a.trim().length() == 0)
			return true;

		return false;
	}

	public static void checkArg(Object o, String message) {
		if (o == null)
			throw new NullPointerException(message);

	}

	public static void checkEnum(String value, Class clazz, String message) {

		checkArg(value, f("Value is null. Checking enum [%s]", clazz));

		try {

			Enum.valueOf(clazz, value);

		} catch (IllegalArgumentException ex) {

			// e("Error check enum[%s], enum class [%s]", value, clazz);

			throw new IllegalArgumentException(ex);

		}

	}

	public static void e(String msg, Object... args) {
		System.err.println(String.format(msg, args));
	}

	public static void p(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

	public static String f(String string, Object... objs) {
		String s = String.format(string, objs);
		return s;
	}

	public static boolean isEquals(Object s1, Object s2) {

		if ((s1 == null && s2 == null) || s1 == s2)
			return true;

		return s1.equals(s2);

	}
}
