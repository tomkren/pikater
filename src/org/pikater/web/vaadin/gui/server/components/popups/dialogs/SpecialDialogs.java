package org.pikater.web.vaadin.gui.server.components.popups.dialogs;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.security.PikaterRole;
import org.pikater.web.vaadin.gui.server.components.forms.ChangePasswordForm;
import org.pikater.web.vaadin.gui.server.components.forms.CreateAccountForm;
import org.pikater.web.vaadin.gui.server.components.forms.LoginForm;
import org.pikater.web.vaadin.gui.server.components.forms.SaveExperimentForm;

import com.vaadin.ui.Component;

import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBox;

public class SpecialDialogs extends DialogCommons
{
	public static MessageBox loginDialog(final IDialogResultHandler resultHandler)
	{
		LoginForm loginForm = new LoginForm();
		MyComponentMessageBoxListenerWithExternalResultHandler<LoginForm> listener = 
				new MyComponentMessageBoxListenerWithExternalResultHandler<LoginForm>(loginForm, resultHandler)
				{
			@Override
			protected boolean handleCustomButton(ButtonId button)
			{
				if(button == ButtonId.CUSTOM_1)
				{
					createAccountDialog(new CreateAccountForm()
					{
						private static final long serialVersionUID = 7554808434070423018L;

						@Override
						public boolean handleResult(Object[] args)
						{
							DAOs.userDAO.storeEntity(new JPAUser(
									(String) args[0],
									(String) args[1],
									(String) args[2],
									DAOs.roleDAO.getByPikaterRole(PikaterRole.ADMIN)
									));
							return true;
						}
					});
					return true;
				}
				return false;
			}
				};
				MessageBox mb = MessageBox.showCustomized(
						Icon.NONE,
						"Please, authenticate yourself",
						loginForm,
						listener,
						ButtonId.OK, ButtonId.CUSTOM_1
						);
				listener.setParentBox(mb); // don't forget this!
				setupMessageBox(mb, false);
				bindActionsToKeyboard(mb, ButtonId.OK, false);
				mb.getButton(ButtonId.CUSTOM_1).setCaption("Create account");
				return mb;
	}

	public static MessageBox createAccountDialog(CreateAccountForm caForm)
	{
		MyComponentMessageBoxListener<CreateAccountForm> listener = new MyComponentMessageBoxListener<CreateAccountForm>(caForm);
		MessageBox mb = MessageBox.showCustomized(
				Icon.NONE,
				"Create a new account",
				caForm,
				listener,
				ButtonId.OK, ButtonId.CANCEL
				);
		listener.setParentBox(mb); // don't forget this!
		setupMessageBox(mb, false);
		bindActionsToKeyboard(mb, ButtonId.OK, true);
		return mb;
	}

	public static MessageBox passwordChangeDialog(ChangePasswordForm cpForm)
	{
		MyComponentMessageBoxListener<ChangePasswordForm> listener = new MyComponentMessageBoxListener<ChangePasswordForm>(cpForm);
		MessageBox mb = MessageBox.showCustomized(
				Icon.NONE,
				"Change password",
				cpForm,
				listener,
				ButtonId.OK, ButtonId.CANCEL
				);
		listener.setParentBox(mb); // don't forget this!
		setupMessageBox(mb, false);
		bindActionsToKeyboard(mb, ButtonId.OK, true);
		return mb;
	}

	public static MessageBox saveExperimentDialog(SaveExperimentForm seForm, IDialogResultHandler resultHandler)
	{
		MyComponentMessageBoxListenerWithExternalResultHandler<SaveExperimentForm> listener = 
				new MyComponentMessageBoxListenerWithExternalResultHandler<SaveExperimentForm>(seForm, resultHandler);
		MessageBox mb = MessageBox.showCustomized(
				Icon.NONE,
				"Save experiment from active tab",
				seForm,
				listener,
				ButtonId.SAVE, ButtonId.CANCEL
				);
		listener.setParentBox(mb); // don't forget this!
		setupMessageBox(mb, false);
		bindActionsToKeyboard(mb, ButtonId.SAVE, true);
		return mb;
	}

	public static <T extends Component & IDialogComponent> MessageBox loadExperimentDialog(T component)
	{
		MyComponentMessageBoxListener<T> listener = new MyComponentMessageBoxListener<T>(component);
		MessageBox mb = MessageBox.showCustomized(
				Icon.NONE,
				"Choose experiment to load into a new tab",
				component,
				listener,
				ButtonId.OK, ButtonId.CANCEL
				);
		listener.setParentBox(mb); // don't forget this!
		setupMessageBox(mb, false);
		bindActionsToKeyboard(mb, ButtonId.OK, true);
		return mb;
	}
}