package org.pikater.web.vaadin.gui.server.components.forms.validators;

import com.vaadin.data.Validator;

public class TrueValidator implements Validator
{
	private static final long serialVersionUID = 557262227834779100L;

	@Override
	public void validate(Object value) throws InvalidValueException
	{
		// doing nothing means the validations succeeds
	}
}