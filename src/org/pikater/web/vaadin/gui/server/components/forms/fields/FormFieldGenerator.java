package org.pikater.web.vaadin.gui.server.components.forms.fields;

import org.pikater.shared.database.jpa.JPAUser;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.IntegerRangeValidator;

public class FormFieldGenerator
{
	//-------------------------------------------------------------------
	// PUBLIC INTERFACE - SPECIFIC FORM FIELD GENERATION
	
	public static FormTextField getGeneralStringField(String caption, String inputPrompt, String value, boolean required, boolean readOnly)
	{
		return new FormTextField(caption, inputPrompt, value, required, readOnly);
	}
	
	public static FormTextField getLoginField(String loginValue, boolean required, boolean readOnly)
	{
		return getGeneralStringField("Login:", "Enter a login", loginValue, required, readOnly);
	}
	
	public static FormTextField getEmailField(String emailValue, boolean required, boolean readOnly)
	{
		final FormTextField result = getGeneralStringField("Email:", "Enter an email", emailValue, required, readOnly);
		result.addValidator(new EmailValidator("Invalid email address.")
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
					result.getOwnerForm().updateFieldStatus(result, false);
				}
			}
		});
		return result;
	}
	
	public static FormTextField getUserPriorityForExperimentField(JPAUser user)
	{
		FormTextField result = getGeneralStringField("Priority:", "Enter your priority", String.valueOf(user.getPriorityMax()), true, false);
		addRangedIntegerValidator(result, 0, user.getPriorityMax());
		return result;
	}
	
	public static FormTextField getComputationEstimateInHoursField()
	{
		FormTextField result = getGeneralStringField("Est. computation time (hours):", "How many hours?", null, true, false);
		addMinimumIntegerValidator(result, 0);
		return result;
	}
	
	public static FormCheckField getSendEmailWhenFinished()
	{
		return new FormCheckField("Send email when finished:", true, false);
	}
	
	//-------------------------------------------------------------------
	// PRIVATE INTERFACE - SPECIAL FIELD BEHAVIOUR
	
	@SuppressWarnings("unused")
	private static void addUnboundedIntegerValidator(FormTextField field)
	{
		field.addValidator(new IntegerRangeValidator("Not an integer number.", null, null));
	}
	
	@SuppressWarnings("unused")
	private static void addMaximumIntegerValidator(FormTextField field, Integer maxValue)
	{
		field.addValidator(new IntegerRangeValidator(String.format("Not an integer from negative infinity to %d.", maxValue), null, maxValue));
	}
	
	private static void addMinimumIntegerValidator(FormTextField field, Integer minValue)
	{
		field.addValidator(new IntegerRangeValidator(String.format("Not an integer from %d to positive infinity.", minValue), minValue, null));
	}
	
	private static void addRangedIntegerValidator(FormTextField field, Integer minValue, Integer maxValue)
	{
		field.addValidator(new IntegerRangeValidator(String.format("Not an integer between %d and %d.", minValue, maxValue), minValue, maxValue));
	}
}