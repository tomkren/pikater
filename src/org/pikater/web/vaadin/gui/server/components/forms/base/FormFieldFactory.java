package org.pikater.web.vaadin.gui.server.components.forms.base;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class FormFieldFactory
{
	//-------------------------------------------------------------------
	// GENERAL FIELD GENERATION
	
	public static TextField getGeneralTextField(String caption, String inputPrompt, String value, boolean required, boolean readOnly)
	{
		final TextField result = new TextField(caption);
		setupTextFieldToWorkWithCustomForms(result, value, inputPrompt, required, readOnly);
		return result;
	}
	
	public static PasswordField getGeneralPasswordField(String caption, String value, boolean required, boolean readOnly)
	{
		// TODO: implement proper constraint eventually
		// addValidator(new StringLengthValidator("Five to twenty characters are required.", 5, 20, false));
		
		PasswordField result = new PasswordField(caption);
		setupTextFieldToWorkWithCustomForms(result, value, "Enter password", required, readOnly);
		return result;
	}
	
	public static TextArea getGeneralTextArea(String caption, String inputPrompt, String value, boolean required, boolean readOnly)
	{
		final TextArea result = new TextArea(caption);
		result.setWordwrap(true);
		setupTextFieldToWorkWithCustomForms(result, value, inputPrompt, required, readOnly);
		return result;
	}
	
	public static ComboBox getGeneralComboBox(String caption, boolean required)
	{
		ComboBox result = new ComboBox(caption);
		result.setTextInputAllowed(false);
		result.setNullSelectionAllowed(false);
		result.setRequired(required);
		return result;
	}
	
	public static CheckBox getGeneralCheckField(String caption, boolean initialState, boolean readOnly)
	{
		final CheckBox result = new CheckBox(caption, initialState);
		result.setReadOnly(readOnly);
		
		if(!readOnly)
		{
			result.setValidationVisible(true);
			// value change event is triggered everytime the value is changed by default
		}
		
		return result;
	}
	
	//-------------------------------------------------------------------
	// SPECIFIC FIELD GENERATION
	
	public static TextField getLoginField(String loginValue, boolean required, boolean readOnly)
	{
		// TODO: eventually add a special validator too
		return getGeneralTextField("Login:", "Enter a login", loginValue, required, readOnly);
	}
	
	public static TextField getEmailField(String emailValue, boolean required, boolean readOnly)
	{
		final TextField result = getGeneralTextField("Email:", "Enter an email", emailValue, required, readOnly);
		result.addValidator(new EmailValidator("Invalid email address."));
		return result;
	}
	
	public static TextField getComputationEstimateInHoursField()
	{
		TextField result = getGeneralTextField("Est. computation time (hours):", "How many hours?", null, true, false);
		addMinimumIntegerValidator(result, 0);
		return result;
	}
	
	//-------------------------------------------------------------------
	// PRIVATE INTERFACE - SPECIAL FIELD BEHAVIOUR
	
	private static void setupTextFieldToWorkWithCustomForms(final AbstractTextField field, String value, String inputPrompt, boolean required, boolean readOnly)
	{
		field.setValue(value == null ? "" : value);
		field.setInputPrompt(inputPrompt);
		field.setReadOnly(readOnly);
		field.setRequired(required);
		
		if(!readOnly)
		{
			field.setValidationVisible(true);
			field.setTextChangeTimeout(0); // timeout to validate the field after text change event
			field.addTextChangeListener(new FieldEvents.TextChangeListener()
			{
				private static final long serialVersionUID = 1895549466379801259L;

				@Override
				public void textChange(TextChangeEvent event)
				{
					// necessary for auto form update to work:
					field.setValue(event.getText()); // triggers a value change event
				}
			});
		}
		
		/*
		 * Validators are never checked if the field is empty so there's no point
		 * in defining a validator that checks whether the field is required but empty.
		 * However, we need to define a default validator that succeeds when the field
		 * is not empty and even if read-only. If other validators are set, their
		 * failure will result in validation failure.
		 */
		field.addValidator(new TrueValidator());
	}
	
	@SuppressWarnings("unused")
	private static void addUnboundedIntegerValidator(TextField field)
	{
		field.addValidator(new IntegerRangeValidator("Not an integer number.", null, null));
	}
	
	@SuppressWarnings("unused")
	private static void addMaximumIntegerValidator(TextField field, Integer maxValue)
	{
		field.addValidator(new IntegerRangeValidator(String.format("Not an integer from negative infinity to %d.", maxValue), null, maxValue));
	}
	
	@SuppressWarnings("unused")
	private static void addMinimumIntegerValidator(TextField field, Integer minValue)
	{
		field.addValidator(new IntegerRangeValidator(String.format("Not an integer from %d to positive infinity.", minValue), minValue, null));
	}
	
	@SuppressWarnings("unused")
	private static void addRangedIntegerValidator(TextField field, Integer minValue, Integer maxValue)
	{
		field.addValidator(new IntegerRangeValidator(String.format("Not an integer between %d and %d.", minValue, maxValue), minValue, maxValue));
	}
}