package org.systemx.util.validate;

import org.apache.commons.validator.routines.AbstractFormatValidator;

//http://commons.apache.org/proper/commons-validator/apidocs/org/apache/commons/validator/routines/package-summary.html#package_description
public abstract class AbstractValidator<O> implements IValidator<O> {

	private AbstractFormatValidator validator;

	public AbstractValidator() {
	}

	public boolean validate(O object) {
		if (object == null)
			return false;

		return true;
	}

	public boolean validate(String object) {

		if (object == null)
			return false;

		return true;
	}

	public AbstractFormatValidator getValidator() {
		return validator;
	}

	public void setValidator(AbstractFormatValidator validator) {
		this.validator = validator;
	}

}
