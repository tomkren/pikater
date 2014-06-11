package org.pikater.web.vaadin.gui.server.components.forms;

import org.pikater.web.vaadin.gui.server.components.forms.abstractform.CustomFormLayout;
import org.pikater.web.vaadin.gui.server.components.forms.fields.FormTextField;
import org.pikater.web.vaadin.gui.server.components.forms.fields.FormFieldGenerator;
import org.pikater.web.vaadin.gui.server.components.forms.fields.PasswordField;

import com.vaadin.ui.Button.ClickListener;

public class CreateAccountForm extends CustomFormLayout
{
	private static final long serialVersionUID = 1751781460173551178L;
	
	private final FormTextField loginField;
	private final PasswordField passwordField;
	private final FormTextField emailField;

	public CreateAccountForm()
	{
		super(null);
		
		this.loginField = FormFieldGenerator.getLoginField(null, true, false);
		this.passwordField = new PasswordField("Password:", null, true, false);
		this.emailField = FormFieldGenerator.getEmailField(null, true, false);
		
		addField(this.loginField, "login");
		addField(this.passwordField, "password");
		addField(this.emailField, "email");
	}
	
	@Override
	public ClickListener getActionButtonListener()
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
