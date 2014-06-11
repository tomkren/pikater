package org.pikater.web.vaadin.gui.server.components.forms;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.web.vaadin.gui.server.components.forms.abstractform.CustomFormLayout;
import org.pikater.web.vaadin.gui.server.components.forms.fields.PasswordField;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;

import com.vaadin.ui.Button.ClickListener;

public class ChangePasswordForm extends CustomFormLayout
{
	private static final long serialVersionUID = 5179296189317359241L;

	private final JPAUser currentUser;
	
	private final PasswordField pf_currentPassword;
	private final PasswordField pf_newPassword;
	private final PasswordField pf_newPasswordAgain;
	
	public ChangePasswordForm(JPAUser user)
	{
		super(null);
		
		this.pf_currentPassword = new PasswordField("Current password:", null, true, false);
		this.pf_newPassword = new PasswordField("New password:", null, true, false);
		this.pf_newPasswordAgain = new PasswordField("New password again:", null, true, false);
		
		addField(pf_currentPassword, "current password");
		addField(pf_newPassword, "new password");
		addField(pf_newPasswordAgain, "new password again");
		
		this.currentUser = user;
	}
	
	@Override
	public boolean isFormValidAndUpdated()
	{
		// TODO: looks like each field needs to be validated in a special manner... displaying a notification of what's wrong (unless everything's visible)
		boolean validated = super.isFormValidAndUpdated();
		if(!validated)
		{
			return false; // a notification of some sort is assumed to have been shown
		}
		else if(!pf_currentPassword.getValue().equals(currentUser.getPassword()))
		{
			MyNotifications.showError(null, "Current password is not correct.");
			return false;
		}
		else if(!pf_newPassword.getValue().equals(pf_newPasswordAgain.getValue()))
		{
			MyNotifications.showError(null, "New passwords don't match.");
			return false;
		}
		else
		{
			return true;
		}
	}

	@Override
	public ClickListener getActionButtonListener()
	{
		return null;
	}
	
	public String getChangedPassword()
	{
		return pf_newPassword.getValue();
	}
}