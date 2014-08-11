package org.pikater.web.vaadin.gui.server.layouts.formlayout;

import java.util.Collection;

import org.pikater.web.vaadin.gui.server.components.forms.validators.NumberRangeValidator;
import org.pikater.web.vaadin.gui.server.components.forms.validators.RequiredValidator;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.OptionGroup;
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
	
	public static ComboBox getGeneralComboBox(String caption, Collection<?> options, Object defaultOption, boolean required, boolean readonly)
	{
		ComboBox result = new ComboBox(caption, options);
		result.setNewItemsAllowed(false);
		result.setImmediate(true);
		result.setTextInputAllowed(false);
		result.setNullSelectionAllowed(false);
		result.setRequired(required);
		if(defaultOption != null)
		{
			result.select(defaultOption);
		}
		result.setReadOnly(readonly);
		return result;
	}
	
	public static CheckBox getGeneralCheckField(String caption, boolean initialState, boolean readOnly)
	{
		CheckBox result = new CheckBox(caption, initialState);
		result.setReadOnly(readOnly);
		// ComboBox result = getGeneralComboBox(caption, Arrays.asList(Boolean.TRUE, Boolean.FALSE), initialState, required, readOnly);
		// result.setConverter(StringToBooleanConverter.class); // TODO: is this even necessary?
		return result;
	}
	
	public static OptionGroup getGeneralOptionGroup(String caption, boolean required)
	{
		OptionGroup result = new OptionGroup(caption);
		result.setImmediate(true);
		result.setRequired(required);
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
	
	/**
	 * A generic method to create a range-validated number field: integer float or double by default.
	 * @param caption
	 * @param value
	 * @param min
	 * @param max
	 * @param required
	 * @param readOnly
	 * @return
	 */
	public static <N extends Number> TextField getNumericField(String caption, N value, N min, N max, boolean required, boolean readOnly)
	{
		if(value == null)
		{
			throw new NullPointerException();
		}
		else
		{
			final TextField result = getGeneralTextField(caption, "Enter a number", String.valueOf(value), required, readOnly);
			result.addValidator(new RequiredValidator(result));
			result.addValidator(new NumberRangeValidator<N>(value.getClass(), min, max));
			return result;
		}
	}
	
	//-------------------------------------------------------------------
	// PRIVATE INTERFACE - SPECIAL FIELD BEHAVIOUR
	
	private static void setupTextFieldToWorkWithCustomForms(final AbstractTextField field, String value, String inputPrompt, boolean required, boolean readOnly)
	{
		field.setValue(value == null ? "" : value);
		field.setInputPrompt(inputPrompt);
		field.setReadOnly(readOnly);
		field.setRequired(required);
		field.setRequiredError("This field is required.");
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
	}
}