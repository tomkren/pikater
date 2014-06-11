package org.pikater.web.vaadin.gui.server.components.forms;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.gui.server.components.forms.abstractform.CustomFormLayout;
import org.pikater.web.vaadin.gui.server.components.forms.fields.FormTextField;
import org.pikater.web.vaadin.gui.server.components.forms.fields.FormFieldGenerator;
import org.pikater.web.vaadin.gui.server.components.forms.fields.PasswordField;
import org.pikater.web.vaadin.gui.server.components.popups.MyDialogs;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class UserProfileForm extends CustomFormLayout
{
	private static final long serialVersionUID = 1654056604776473039L;

	private JPAUser currentUser;
	
	private final FormTextField loginField;
	private final PasswordField passwordField; 
	private final FormTextField emailField;
	
	private final Button btn_changePassword;
	
	public UserProfileForm()
	{
		super("Save changes");
		
		this.currentUser = null;
		
		this.loginField = FormFieldGenerator.getLoginField(null, false, true); // not required since completely read-only
		this.passwordField = new PasswordField("Password:", null, true, true);
		this.emailField = FormFieldGenerator.getEmailField(null, true, false);
		
		addField(loginField, "login");
		addField(passwordField, "password");
		addField(emailField, "email");
		
		this.btn_changePassword = new Button("Change password", new ClickListener()
		{
			private static final long serialVersionUID = -8548997603012122979L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				final ChangePasswordForm cpForm = new ChangePasswordForm(currentUser);
				MyDialogs.passwordChangeDialog(cpForm, new MyDialogs.DialogResultHandler()
				{
					@Override
					public boolean handleResult()
					{
						if(cpForm.isFormValidAndUpdated()) // check all required conditions and display notifications
						{
							passwordField.setValue(cpForm.getChangedPassword());
							return true;
						}
						else
						{
							return false;
						}
					}
				});
			}
		});
		addCustomButton(btn_changePassword);
	}
	
	public void enter(JPAUser currentUser)
	{
		this.currentUser = currentUser;
		
		loginField.setValueAndIgnoreReadOnly(currentUser.getLogin());
		loginField.setValueBackup(currentUser.getLogin());
		passwordField.setValueAndIgnoreReadOnly(currentUser.getPassword());
		passwordField.setValueBackup(currentUser.getPassword());
		emailField.setValue(currentUser.getEmail());
		emailField.setValueBackup(currentUser.getEmail());
	}

	@Override
	public ClickListener getActionButtonListener()
	{
		return new ClickListener()
		{
			private static final long serialVersionUID = -7839086774827537333L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				passwordField.setValueBackup(passwordField.getValue());
				emailField.setValueBackup(emailField.getValue());
				
				if(!ServerConfigurationInterface.avoidUsingDBForNow())
				{
					currentUser.setPassword(passwordField.getValue());
					currentUser.setEmail(emailField.getValue());
					DAOs.userDAO.updateEntity(currentUser);
				}
				
				MyNotifications.showSuccess(null, "Changes were successfully saved.");
			}
		};
	}
}