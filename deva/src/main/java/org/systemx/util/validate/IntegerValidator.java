package org.systemx.util.validate;

public class IntegerValidator extends AbstractValidator<Integer> {

	private RangePair<Integer, Integer> rangePair;

	private final org.apache.commons.validator.routines.IntegerValidator apacheValidator = org.apache.commons.validator.routines.IntegerValidator
			.getInstance();

	public IntegerValidator() {
	}

	@Override
	public boolean validate(String object) {

		if (object == null)

			return false;

		try {

			int i = Integer.valueOf(object);

			return validate(i);

		} catch (NumberFormatException ex) {
			return false;
		}
	}

	@Override
	public boolean validate(Integer object) {

		if (!super.validate(object))
			return false;

		rangePair = getRangePair();

		if (rangePair != null)
			if (!apacheValidator.isInRange(object, rangePair.getMin(),
					rangePair.getMax()))
				return false;

		return true;
	}

	public RangePair<Integer, Integer> getRangePair() {
		return rangePair;
	}

	public IntegerValidator setRangePair(RangePair<Integer, Integer> rangePair) {
		this.rangePair = rangePair;
		return this;
	}

	public org.apache.commons.validator.routines.IntegerValidator getApacheValidator() {
		return apacheValidator;
	}

}
