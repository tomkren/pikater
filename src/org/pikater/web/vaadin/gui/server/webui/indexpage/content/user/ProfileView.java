package org.pikater.web.vaadin.gui.server.webui.indexpage.content.user;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.gui.server.AuthHandler;
import org.pikater.web.vaadin.gui.server.webui.MyDialogs;
import org.pikater.web.vaadin.gui.server.webui.MyDialogs.ITextPromptDialogResult;
import org.pikater.web.vaadin.gui.server.webui.indexpage.content.ContentProvider.IContentComponent;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;

public class ProfileView extends FormLayout implements IContentComponent
{
	private static final long serialVersionUID = -5751678204210363235L;
	
	private final TextField tf_login;
	/**
	 * Password will eventually only be a hash so there's no need to display it and
	 * just a mockup of password field will be used for simplicity.
	 */
	private final Button btn_changePassword;
	private final TextField tf_email;
	private final Button btn_saveChanges;
	
	public ProfileView()
	{
		super();
		
		tf_login = new TextField("Login:");
		btn_changePassword = new Button("Change password", new Button.ClickListener()
		{
			private static final long serialVersionUID = 53892736347329152L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				String currentPassword = ServerConfigurationInterface.avoidUsingDBForNow() ? getDummyPassword() : AuthHandler.getUserEntity(getSession()).getPassword();
				MyDialogs.createPasswordChangeDialog(getUI(), currentPassword, new ITextPromptDialogResult()
				{
					@Override
					public boolean handleResult(String newPassword)
					{
						// all required checks were passed
						setPasswordIntoTheField(newPassword); // register the change
						btn_saveChanges.setEnabled(true);
						return true;
					}
				});
			}
		});
		tf_email = new TextField("Email:");
		tf_email.addValidator(new EmailValidator("Invalid email address."));
		tf_email.setValidationVisible(true);
		tf_email.setTextChangeTimeout(1000); // 1 second
		tf_email.addTextChangeListener(new FieldEvents.TextChangeListener()
		{
			private static final long serialVersionUID = 1895549466379801259L;

			@Override
			public void textChange(TextChangeEvent event)
			{
				tf_email.setValue(event.getText()); // necessary for auto-validation to work
			}
		});
		btn_saveChanges = new Button("Save changes", new Button.ClickListener()
		{
			private static final long serialVersionUID = 8079462098771877855L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				updateChanges();
			}
		});
		btn_saveChanges.setEnabled(false);
		
		addComponent(tf_login);
		addComponent(btn_changePassword);
		addComponent(tf_email);
		addComponent(btn_saveChanges);
	}
	
	@Override
	public void attach()
	{
		super.attach();
		
		if(ServerConfigurationInterface.avoidUsingDBForNow())
		{
			tf_login.setValue(getDummyLogin());
			setPasswordIntoTheField(getDummyPassword());
			tf_email.setValue(getDummyEmail());
		}
		else
		{
			JPAUser user = AuthHandler.getUserEntity(getSession());
			tf_login.setValue(user.getLogin());
			setPasswordIntoTheField(user.getPassword());
			tf_email.setValue(user.getEmail());
		}
		
		tf_login.setReadOnly(true);
	}
	
	@Override
	public boolean hasUnsavedProgress()
	{
		return btn_saveChanges.isEnabled();
	}

	@Override
	public String getCloseDialogMessage()
	{
		return "Changes were not stored yet. Discard them and continue?";
	}
	
	private void updateChanges()
	{
		if(!ServerConfigurationInterface.avoidUsingDBForNow())
		{
			JPAUser user = AuthHandler.getUserEntity(getSession());
			user.setEmail(tf_email.getValue());
			user.setPassword(getPasswordFromTheField());
			DAOs.userDAO.updateEntity(user);
		}
		
		btn_saveChanges.setEnabled(false);
		Notification.show("Changes were successfully saved.", Type.HUMANIZED_MESSAGE);
	}
	
	private void setPasswordIntoTheField(String password)
	{
		btn_changePassword.setData(password);
	}
	
	private String getPasswordFromTheField()
	{
		return (String) btn_changePassword.getData();
	}
	
	private String getDummyLogin()
	{
		return "dummy_user";
	}
	
	private String getDummyPassword()
	{
		return "dummy_password";
	}
	
	private String getDummyEmail()
	{
		return "dummy_user@mail.com";
	}
}
