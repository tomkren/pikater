package org.pikater.web.vaadin.gui.server.components.forms;

import org.pikater.web.vaadin.gui.server.components.forms.fields.LoginField;
import org.pikater.web.vaadin.gui.server.components.forms.fields.PasswordField;

import com.vaadin.ui.Button.ClickListener;

public class LoginForm extends CustomFormLayout
{
	private static final long serialVersionUID = -2356468027629344476L;
	
	private final LoginField loginField;
	private final PasswordField passwordField;
	
	public LoginForm()
	{
		super(null);
		
		this.loginField = new LoginField("sj", true, false);
		this.passwordField = new PasswordField("123", true, false);
		
		addField(this.loginField, "login");
		addField(this.passwordField, "password");
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
}