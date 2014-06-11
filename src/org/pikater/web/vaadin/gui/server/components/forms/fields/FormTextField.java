package org.pikater.web.vaadin.gui.server.components.forms.fields;

import org.pikater.web.vaadin.gui.server.components.forms.abstractform.CustomFormLayout;

import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.ui.TextField;

public class FormTextField extends TextField implements IFormField<String>
{
	private static final long serialVersionUID = 2547606599715321365L;
	
	private CustomFormLayout ownerForm;

	public FormTextField(String caption, String inputPrompt, String value, boolean required, boolean readOnly)
	{
		super(caption);
		
		this.ownerForm = null;
		
		setInputPrompt(inputPrompt);
		setValueBackup(value == null ? "" : value);
		setValue(value == null ? "" : value);
		setReadOnly(readOnly);
		setRequired(required);
		
		if(!readOnly)
		{
			setValidationVisible(true);
			setTextChangeTimeout(1); // 1 millisecond, so immediately
			addTextChangeListener(new FieldEvents.TextChangeListener()
			{
				private static final long serialVersionUID = 1895549466379801259L;

				@Override
				public void textChange(TextChangeEvent event)
				{
					setValue(event.getText()); // necessary for auto-validation to work
				}
			});
		}
		
		/*
		 * Validators are never checked if the field is empty so there's no point
		 * in defining a validator that checks whether the field is required but empty.
		 * However, we need to define a default validator that succeeds when the field
		 * is not empty and even if read-only. If other validators are set in child
		 * classes, their failure will result in validation failure.
		 */
		addValidator(new TrueValidator());
	}
	
	@Override
	public CustomFormLayout getOwnerForm()
	{
		return ownerForm;
	}
	
	@Override
	public void setOwnerForm(CustomFormLayout ownerForm)
	{
		this.ownerForm = ownerForm;
	}
	
	@Override
	public String getValueBackup()
	{
		return (String) getData();
	}
	
	@Override
	public void setValueBackup(String value)
	{
		setData(value);
	}
	
	@Override
	public boolean isUpdated()
	{
		return !getValueBackup().equals(getValue());
	}
	
	@Override
	public void setValueAndIgnoreReadOnly(String value)
	{
		boolean readOnly = isReadOnly();
		setReadOnly(false);
		setValue(value);
		setReadOnly(readOnly);
	}
}