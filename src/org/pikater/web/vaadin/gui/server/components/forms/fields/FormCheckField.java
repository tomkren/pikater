package org.pikater.web.vaadin.gui.server.components.forms.fields;

import org.pikater.web.vaadin.gui.server.components.forms.abstractform.CustomFormLayout;

import com.vaadin.data.Property;
import com.vaadin.ui.CheckBox;

public class FormCheckField extends CheckBox implements IFormField<Boolean>
{
	private static final long serialVersionUID = -7915848245741371972L;
	
	private CustomFormLayout ownerForm;

	public FormCheckField(String caption, boolean initialState, boolean readOnly)
	{
		super(caption, initialState);
		
		setValueBackup(initialState);
		setReadOnly(readOnly);
		
		if(!readOnly)
		{
			setValidationVisible(true);
			addValueChangeListener(new Property.ValueChangeListener()
			{
				private static final long serialVersionUID = -12152676961906749L;

				@Override
				public void valueChange(Property.ValueChangeEvent event)
				{
					setValue((Boolean) event.getProperty().getValue()); // necessary for auto-validation to work
				}
			});
		}
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
	public Boolean getValueBackup()
	{
		return (Boolean) getData();
	}

	@Override
	public void setValueBackup(Boolean value)
	{
		setData(value);
	}

	@Override
	public boolean isUpdated()
	{
		return !getValueBackup().equals(getValue());
	}

	@Override
	public void setValueAndIgnoreReadOnly(Boolean value)
	{
		boolean readOnly = isReadOnly();
		setReadOnly(false);
		setValue(value);
		setReadOnly(readOnly);
	}
}