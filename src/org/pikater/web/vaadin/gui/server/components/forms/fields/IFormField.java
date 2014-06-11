package org.pikater.web.vaadin.gui.server.components.forms.fields;

import org.pikater.web.vaadin.gui.server.components.forms.abstractform.CustomFormLayout;

public interface IFormField<T>
{
	CustomFormLayout getOwnerForm();
	void setOwnerForm(CustomFormLayout ownerForm);
	T getValueBackup();
	void setValueBackup(T value);
	boolean isUpdated();
	void setValueAndIgnoreReadOnly(T value);
}
