package org.systemx.util.quest;

import static org.systemx.util.string.USF.HEAD;
import static org.systemx.util.string.USF.p;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.systemx.util.validate.AbstractValidator;
import org.systemx.util.validate.IValidator;
import org.systemx.util.validate.IntegerValidator;
import org.systemx.util.validate.RangePair;

public enum EAnswer implements IAnswer {

	YES("y"), NO("n"), STOP("s"), NUMERIC("0-9"), CHARS("any symbols");

	private final String shortString;

	private String answerObject;

	private String answerText;

	private AbstractValidator<? extends Object> validator;

	public EAnswer setValidator(AbstractValidator<? extends Object> validator) {
		this.validator = validator;
		return this;
	}

	public IValidator getValidator() {
		return this.validator;
	}

	private EAnswer(String shortString) {
		this.shortString = shortString;
	}

	public String getAnswerName() {
		return name();
	}

	public String getShortName() {
		return shortString;
	}

	public boolean equals(String s) {

		IValidator validator = getValidator();

		if (EAnswer.NUMERIC == this) {

			try {
				if (validator != null) {

					return validator.validate(s);

				} else {

					int i = Integer.valueOf(s);

					return true;
				}
			} catch (NumberFormatException ex) {
				return false;
			}

		} else if (EAnswer.CHARS == this) {

			if (validator != null)

				return validator.validate(s);

			return (s != null && !s.isEmpty());

		} else {

			s = s.toUpperCase();

			try {

				if (s.equalsIgnoreCase(getShortName()))
					return true;

				else if (EAnswer.valueOf(s) == this)
					return true;

				else
					return false;

			} catch (IllegalArgumentException ex) {
				return false;
			}
		}

	}

	public static EAnswer getAnswer(EAnswer[] answers, String answer) {

		if (answer == null)
			return null;

		for (EAnswer a : answers)

			if (a.equals(answer))

				return a.setAnswerObject(answer);

		return null;

	}

	public String getAnswerObject() {
		return this.answerObject;
	}

	public EAnswer setAnswerObject(String answer) {
		this.answerObject = answer;
		return this;
	}

	public static CharSequence toString(EAnswer... answers) {

		if (answers == null || answers.length == 0)
			return "";

		StringBuilder sb = new StringBuilder();

		sb.append("[ ");

		next: for (EAnswer a : answers)

			switch (a) {

			case YES: {

				String text = (a.getPossibleAnswer() == null) ? a
						.getShortName() : a.getPossibleAnswer();

				appendStringAnswer(sb, a, "(", text, "), ");

				continue next;
			}
			case NO: {

				String text = (a.getPossibleAnswer() == null) ? a
						.getShortName() : a.getPossibleAnswer();

				appendStringAnswer(sb, a, "(", text, "), ");

				continue next;
			}
			case STOP: {

				String text = (a.getPossibleAnswer() == null) ? a
						.getShortName() : a.getPossibleAnswer();

				appendStringAnswer(sb, a, "(", text, "), ");

				continue next;
			}
			case NUMERIC: {

				String text = (a.getPossibleAnswer() == null) ? a
						.getShortName() : a.getPossibleAnswer();

				appendStringAnswer(sb, "(", text, "), ");

				continue next;
			}
			case CHARS: {

				String text = (a.getPossibleAnswer() == null) ? a
						.getShortName() : a.getPossibleAnswer();

				if (text.trim().isEmpty())
					appendStringAnswer(sb, ", ");
				else
					appendStringAnswer(sb, "(", text, "), ");

				continue next;
			}
			}

		sb.delete(sb.length() - 2, sb.length());

		sb.append(" ]");

		return sb;
	}

	private static CharSequence appendStringAnswer(StringBuilder sb,
			Object... args) {

		for (Object a : args)
			sb.append(a);

		return sb;
	}

	public String QUEST(String quest) throws IOException {

		p(HEAD(quest));

		BufferedReader bufReader = new BufferedReader(new InputStreamReader(
				System.in));

		String s = bufReader.readLine();

		// System.out.println(s);

		return s;
	}

	public static EAnswer QUEST(String quest, EAnswer... answers) {

		p(HEAD(quest + EAnswer.toString(answers)));

		BufferedReader bufReader = new BufferedReader(new InputStreamReader(
				System.in));

		EAnswer answerObject = null;
		try {

			while (true) {

				String inputString = bufReader.readLine();
				// p("Input|" + inputString);
				answerObject = EAnswer.getAnswer(answers, inputString);

				if (answerObject != null)
					return answerObject;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}

	public static void main(String[] args) {

		EAnswer ans = QUEST("Hello?",
				NUMERIC.setValidator(new IntegerValidator()
						.setRangePair(new RangePair(1, 12))));

		p("Answer [%s]", ans.getAnswerObject());

	}

	public String getPossibleAnswer() {
		return answerText;
	}

	public EAnswer setPossibleAnswer(String answerText) {
		this.answerText = answerText;
		return this;
	}

}
