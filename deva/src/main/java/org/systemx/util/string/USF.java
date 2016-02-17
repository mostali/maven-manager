package org.systemx.util.string;

public class USF {
	private static int defaultLengthLine = 80;
	private static char defaultLineChar = '-';

	private int lmax = -1;

	private char insert = ' ';

	public USF() {
		this(16, ' ');
	}

	public USF(int lengthMax) {
		this(lengthMax, ' ');
	}

	public USF(String maxWord) {
		this(maxWord.length(), ' ');
	}

	public USF(char _char) {
		this(16, _char);
	}

	public USF(int lengthMax, char _char) {

		this.lmax = (lengthMax < 2) ? lmax = 16 : lengthMax;

	}

	public int getMaxLength() {

		return lmax;
	}

	public String bw(String s) {
		return bw(s, insert, lmax);
	}

	public static String bw(String s, char _char, int length_max) {

		int length = s.length();

		int r = length_max - length;

		if (r < 0)
			return "";

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i <= r; ++i)
			sb.append(_char);

		return sb.toString();

	}

	public String aw(String s) {
		return aw(s, insert, lmax);

	}

	public static String aw(String s, char _char, int length_max) {

		s = new W(s).toString();

		int length = s.length();

		int r = length_max - length;

		if (r < 0)
			return "";

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i <= r; ++i)
			sb.append(_char);

		return sb.toString();

	}

	public static class W {

		private final String w;

		public W(String w) {
			this.w = w;
		}

		@Override
		public String toString() {
			return w;
		}
	}

	public static String HEAD(String s) {

		int l = s.length();

		StringBuilder sb = new StringBuilder(l * 3);

		sb.append("").append(LINE((int) (80), '-')).append("\n");

		sb.append(s);

		sb.append("\n").append(LINE((int) (80), '-')).append("");

		return sb.toString();
	}

	public static CharSequence LINE() {

		StringBuilder sb = new StringBuilder(defaultLengthLine);

		for (int i = 0; i < defaultLengthLine; i++)
			sb.append(defaultLineChar);

		return sb;
	};

	public static CharSequence LINE(int length, char _char) {

		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; i++)
			sb.append(_char);

		return sb;
	};

	public static String f(String s, Object... args) {
		return String.format(s, args);
	}

	public static void p(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

	public static void PLINE() {
		p(LINE().toString());
	}
}
