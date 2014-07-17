package org.pikater.web.vaadin.gui.server.components.forms.validators;

import com.vaadin.data.Validator;
import com.vaadin.ui.AbstractField;

public class RequiredValidator implements Validator
{
	private static final long serialVersionUID = -5779429225318441902L;
	
	private final AbstractField<? extends Object> field;
	
	public RequiredValidator(AbstractField<? extends Object> field)
	{
		this.field = field;
	}

	@Override
	public void validate(Object value) throws InvalidValueException
	{
		if(field.isRequired() && ((value == null) || ((String) value).isEmpty()))
		{
			throw new InvalidValueException(field.getRequiredError());
		}
	}
}