package org.pikater.web.vaadin.gui.server.components.forms.base;


public interface IFormField<T>
{
	CustomFormLayout getOwnerForm();
	void setOwnerForm(CustomFormLayout ownerForm);
	T getValueBackup();
	void setValueBackup(T value);
	boolean isUpdated();
	void setValueAndIgnoreReadOnly(T value);
}
