package org.pikater.web.vaadin.gui.server.components.forms.fields;

import com.vaadin.data.validator.EmailValidator;

public class EmailField extends AbstractFormField
{
	private static final long serialVersionUID = -124557455467157576L;
	
	/**
	 * Creates an email text field with preset caption, suitable for form layouts.
	 * @param emailValue
	 */
	public EmailField(String emailValue, boolean required, boolean readOnly)
	{
		super("Email:", "Enter an email", emailValue, required, readOnly);
		
		addValidator(new EmailValidator("Invalid email address.")
		{
			/*
			 * Since there is no validation listener or callback on the field components,
			 * we have to wrap the built-in email validator and enable the 'btn_saveChanges' as needed. 
			 */
			
			private static final long serialVersionUID = 4089601013465687331L;
			
			private String lastCheckedValue = null;

			@Override
			public void validate(Object value) throws InvalidValueException
			{
				try
				{
					super.validate(value);
					update((String) value, true);
				}
				catch (InvalidValueException e)
				{
					update((String) value, false);
					throw e;
				}
			}
			
			private void update(String currentValue, boolean validated)
			{
				/* 
				 * The {@link org.pikater.web.vaadin.gui.server.components.forms.CustomFormLayout#updateFieldStatus()} method
				 * calls validation on inner form fields. If any of them implemented a special validator just like this class,
				 * an infinite loop would be created.
				 * To avoid this, only execute the {@link org.pikater.web.vaadin.gui.server.components.forms.CustomFormLayout#updateFieldStatus()}
				 * call when the field's value is not the same as last time... That way, we know we may safely update the parent form
				 * because it is not the caller. 
				 */
				if((lastCheckedValue != null) && lastCheckedValue.equals(currentValue))
				{
					return;
				}
				else
				{
					lastCheckedValue = currentValue;
					getOwnerForm().updateFieldStatus(EmailField.this, false);
				}
			}
		});
	}
}