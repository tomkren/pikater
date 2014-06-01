package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.user;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.gui.server.MyDialogs;
import org.pikater.web.vaadin.gui.server.MyDialogs.ITextPromptDialogResult;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.IContentComponent;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.VaadinSession;
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
	
	private boolean passwordChanged;
	private boolean emailChanged;
	
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
				MyDialogs.createPasswordChangeDialog(getUI(), getPasswordBackup(), new ITextPromptDialogResult()
				{
					@Override
					public boolean handleResult(String newPassword)
					{
						// all required checks were passed
						setPasswordBackup(newPassword); // register the change
						passwordChanged = true;
						updateSaveChangesBtnStatus();
						return true;
					}
				});
			}
		});
		
		tf_email = new TextField("Email:");
		tf_email.addValidator(new EmailValidator("Invalid email address.")
		{
			/*
			 * Since there is no validation listener or callback on the field components,
			 * we have to wrap the built-in email validator and enable the 'btn_saveChanges' as needed. 
			 */
			
			private static final long serialVersionUID = 4089601013465687331L;

			@Override
			public void validate(Object value) throws InvalidValueException
			{
				try
				{
					super.validate(value);
					emailChanged = !getEmailBackup().equals((String) value);
					updateSaveChangesBtnStatus();
				}
				catch (InvalidValueException e)
				{
					btn_saveChanges.setEnabled(false);
					throw e;
				}
			}
		});
		tf_email.setValidationVisible(true);
		tf_email.setTextChangeTimeout(1); // 1 millisecond, so immediately
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
				// update changes
				
				if(!ServerConfigurationInterface.avoidUsingDBForNow())
				{
					JPAUser user = ManageAuth.getUserEntity(VaadinSession.getCurrent());
					user.setEmail(tf_email.getValue());
					user.setPassword(getPasswordBackup());
					DAOs.userDAO.updateEntity(user);
				}
				resetChangeState();
				updateSaveChangesBtnStatus();
				
				Notification.show("Changes were successfully saved.", Type.HUMANIZED_MESSAGE);
			}
		});
		
		resetChangeState();
		
		addComponent(tf_login);
		addComponent(btn_changePassword);
		addComponent(tf_email);
		addComponent(btn_saveChanges);
	}
	
	@Override
	public void enter(ViewChangeEvent event)
	{
		resetChangeState();
		
		JPAUser user = ServerConfigurationInterface.avoidUsingDBForNow() ? JPAUser.getDummy() : ManageAuth.getUserEntity(VaadinSession.getCurrent());
		setLogin(user.getLogin());
		setPasswordBackup(user.getPassword());
		setEmailBackup(user.getEmail()); // backup for future comparing to new values
		tf_email.setValue(user.getEmail());
		updateSaveChangesBtnStatus();
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
	
	//--------------------------------------------------
	// PRIVATE INTERFACE
	
	private void setLogin(String login)
	{
		tf_login.setReadOnly(false);
		tf_login.setValue(login);
		tf_login.setReadOnly(true);
	}
	
	private String getPasswordBackup()
	{
		return (String) btn_changePassword.getData();
	}
	
	private void setPasswordBackup(String password)
	{
		btn_changePassword.setData(password);
	}
	
	private String getEmailBackup()
	{
		return (String) tf_email.getData();
	}
	
	private void setEmailBackup(String email)
	{
		tf_email.setData(email);
	}
	
	private void updateSaveChangesBtnStatus()
	{
		btn_saveChanges.setEnabled(passwordChanged || emailChanged);
	}
	
	private void resetChangeState()
	{
		passwordChanged = false;
		emailChanged = false;
	}
}
