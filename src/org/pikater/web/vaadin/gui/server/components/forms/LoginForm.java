package org.pikater.web.vaadin.gui.server.components.forms;

import org.pikater.web.vaadin.gui.server.components.forms.base.CustomFormLayout;
import org.pikater.web.vaadin.gui.server.components.forms.base.FormFieldFactory;

import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;

public class LoginForm extends CustomFormLayout
{
	private static final long serialVersionUID = -2356468027629344476L;
	
	private final TextField loginField;
	private final PasswordField passwordField;
	
	public LoginForm()
	{
		super(null);
		
		this.loginField = FormFieldFactory.getLoginField("sj", true, false);
		this.passwordField = FormFieldFactory.getGeneralPasswordField("Password:", "123", true, false);
		
		addField("login", loginField);
		addField("password", passwordField);
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
}