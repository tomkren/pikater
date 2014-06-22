package org.pikater.web.vaadin.gui.server.components.forms;

import org.pikater.web.vaadin.gui.server.components.forms.base.CustomFormLayout;
import org.pikater.web.vaadin.gui.server.components.forms.base.FormFieldFactory;

import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

public class CreateAccountForm extends CustomFormLayout
{
	private static final long serialVersionUID = 1751781460173551178L;
	
	private final TextField loginField;
	private final PasswordField passwordField;
	private final TextField emailField;

	public CreateAccountForm()
	{
		super(null);
		
		this.loginField = FormFieldFactory.getLoginField(null, true, false);
		this.passwordField = FormFieldFactory.getGeneralPasswordField("Password:", null, true, false);
		this.emailField = FormFieldFactory.getEmailField(null, true, false);
		
		addField("login", this.loginField);
		addField("password", this.passwordField);
		addField("email", this.emailField);
	}
	
	@Override
	public IOnSubmit getSubmitAction()
	{
		return null;
	}
	
	public String getLogin()
	{
		return loginField.getValue();
	}
	
	public String getPassword()
	{
		return passwordField.getValue();
	}
	
	public String getEmail()
	{
		return emailField.getValue();
	}
}
