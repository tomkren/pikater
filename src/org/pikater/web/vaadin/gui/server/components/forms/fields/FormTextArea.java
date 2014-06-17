package org.pikater.web.vaadin.gui.server.components.forms.fields;

import org.pikater.web.vaadin.gui.server.components.forms.base.CustomFormLayout;
import org.pikater.web.vaadin.gui.server.components.forms.base.IFormField;

import com.vaadin.ui.TextArea;

public class FormTextArea extends TextArea implements IFormField<String>
{
	private static final long serialVersionUID = 205845540876552005L;
	
	private CustomFormLayout ownerForm;

	public FormTextArea(String caption, String inputPrompt, String value, boolean required, boolean readOnly)
	{
		super(caption);
		
		setInputPrompt(inputPrompt);
		setRequired(required);
		setWordwrap(true);
		setValue(value);
		setReadOnly(readOnly);
		
		this.ownerForm = null;
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