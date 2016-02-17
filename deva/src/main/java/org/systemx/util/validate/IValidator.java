package org.systemx.util.validate;

//http://docs.jboss.org/hibernate/validator/4.3/reference/en-US/html/validator-gettingstarted.html
public interface IValidator<O> {

	boolean validate(O object);

	boolean validate(String object);

}
