package org.pikater.web.vaadin.gui.server.components.forms;

import org.pikater.web.vaadin.gui.server.components.forms.base.CustomFormLayout;
import org.pikater.web.vaadin.gui.server.components.forms.fields.FormTextField;
import org.pikater.web.vaadin.gui.server.components.forms.fields.FormFieldGenerator;
import org.pikater.web.vaadin.gui.server.components.forms.fields.PasswordField;

import com.vaadin.ui.Button.ClickListener;

public class LoginForm extends CustomFormLayout
{
	private static final long serialVersionUID = -2356468027629344476L;
	
	private final FormTextField loginField;
	private final PasswordField passwordField;
	
	public LoginForm()
	{
		super(null);
		
		this.loginField = FormFieldGenerator.getLoginField("sj", true, false);
		this.passwordField = new PasswordField("Password:", "123", true, false);
		
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