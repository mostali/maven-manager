package org.systemx.happ.utils;

import java.io.File;
import java.io.IOException;

public enum LFileName {

	EMPTY, ILLEGAL_CHARS, FS;

	private static final char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t',
			'\0', '\f', '`', '?', '*', '\\', '<', '>', '|', '\"', ':' };

	public boolean matches(String fileName) {

		switch (this) {

		case EMPTY:
			return empty(fileName);

		case ILLEGAL_CHARS: {

			if (!empty(fileName))
				return false;

			return illegal_chars(fileName);
		}
		case FS: {

			if (!empty(fileName))
				return false;

			return fs(fileName);
		}

		}

		return true;

	}

	public void checkTE(String fileName) {

		boolean ok = matches(fileName);

		if (!ok)
			throw new RuntimeException("Invalid file name [" + fileName + "]");

	}

	private boolean empty(String fileName) {

		if (fileName == null || fileName.length() == 0
				|| (fileName = fileName.trim()).length() == 0)
			return false;

		return true;
	}

	private boolean illegal_chars(String fileName) {

		if (fileName == null || fileName.length() == 0
				|| fileName.trim().length() == 0)
			return false;

		for (char ch : ILLEGAL_CHARACTERS)

			for (int i = 0; i < fileName.length(); i++) {

				if (ch == fileName.charAt(i))

					return false;
			}

		return true;
	}

	public boolean fs(String fileName) {

		File f = new File(fileName);

		try {
			f.getCanonicalPath();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public static void main(String args[]) throws Exception {

		System.out.println(new File(".").getAbsolutePath());
		System.out.println(new File(new File("d:\\dirx"), ".")
				.getAbsolutePath());

		// true
		System.out.println(FS.matches(" "));
		System.out.println(FS.matches("prn.TXT"));
		System.out.println(FS.matches(""));

		// false
		System.out.println(FS.matches("test.T*T"));
		System.out.println(FS.matches("test|.TXT"));
		System.out.println(FS.matches("te?st.TXT"));

		System.out.println(FS.matches("con.TXT")); // windows
		System.out.println(FS.matches("prna.TXTd")); // windows
	}
}
