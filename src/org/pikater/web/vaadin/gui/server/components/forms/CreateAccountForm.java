package org.pikater.web.vaadin.gui.server.components.forms;

import org.pikater.web.vaadin.gui.server.components.forms.fields.EmailField;
import org.pikater.web.vaadin.gui.server.components.forms.fields.LoginField;
import org.pikater.web.vaadin.gui.server.components.forms.fields.PasswordField;

import com.vaadin.ui.Button.ClickListener;

public class CreateAccountForm extends CustomFormLayout
{
	private static final long serialVersionUID = 1751781460173551178L;
	
	private final LoginField loginField;
	private final PasswordField passwordField;
	private final EmailField emailField;

	public CreateAccountForm()
	{
		super(null);
		
		this.loginField = new LoginField(null, true, false);
		this.passwordField = new PasswordField(null, true, false);
		this.emailField = new EmailField(null, true, false);
		
		addField(this.loginField, "login");
		addField(this.passwordField, "password");
		addField(this.emailField, "email");
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

	@Override
	public ClickListener getActionButtonListener()
	{
		return null;
	}
}
