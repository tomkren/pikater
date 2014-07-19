package org.pikater.web.vaadin.gui.server.components.popups;

import java.util.ArrayList;
import java.util.List;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.security.PikaterRole;
import org.pikater.web.vaadin.gui.server.components.forms.ChangePasswordForm;
import org.pikater.web.vaadin.gui.server.components.forms.CreateAccountForm;
import org.pikater.web.vaadin.gui.server.components.forms.LoginForm;
import org.pikater.web.vaadin.gui.server.components.forms.SaveExperimentForm;
import org.pikater.web.vaadin.gui.server.components.forms.base.CustomFormLayout;

import com.google.gwt.event.dom.client.KeyCodes;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.TextField;

import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBox;
import de.steinwedel.messagebox.MessageBoxListener;

public class MyDialogs
{
	// -------------------------------------------------------------------------
	// PRIVATE INTERFACE
	
	/**
	 * Applies general message box settings.
	 * @param box
	 * @param enterButton which button to bind with the ENTER key
	 * @param closeWithAnyButton whether the dialog should close after clicking ANY of its buttons
	 * @param escapeToClose
	 */
	private static void setupMessageBox(MessageBox box, boolean closeWithAnyButton)
	{
		box.getWindow().setResizable(false);
		box.getWindow().setDraggable(false);
		if(!closeWithAnyButton)
		{
			box.setAutoClose(closeWithAnyButton);
		}
		
		// TODO:
		// content.setStyleName("pikaterDialogContent");
		// parentUI.setFocusedComponent(box);
	}
	
	/**
	 * Applies custom actions to some keyboard input, namely:
	 * <ul>
	 * <li> Button to be "clicked" when user hits the "ENTER" key.
	 * <li> Message box closing when user hits the "ESCAPE" key.
	 * </ul>
	 *   
	 * @param box
	 * @param enterButton
	 * @param escapeToClose
	 */
	private static void bindActionsToKeyboard(MessageBox box, ButtonId enterButton, boolean escapeToClose)
	{
		if(enterButton != null)
		{
			box.getButton(enterButton).setClickShortcut(KeyCodes.KEY_ENTER, null);
		}
		if(escapeToClose)
		{
			box.getWindow().setCloseShortcut(KeyCode.ESCAPE, null);
		}
	}
	
	//----------------------------------------------------------------
	// PUBLIC ROUTINES FOR DISPLAYING GENERAL USE DIALOGS
	
	public static void info(String title, String message)
	{
		MessageBox mb = MessageBox.showPlain(
				Icon.INFO,
				title == null ? "Notification" : title,
				message,
				ButtonId.CLOSE
		);
		setupMessageBox(mb, true);
		bindActionsToKeyboard(mb, ButtonId.CLOSE, true);
	}
	
	public static void error(String title, String message)
	{
		MessageBox mb = MessageBox.showPlain(Icon.ERROR, title == null ? "Error" : title, message, ButtonId.OK);
		setupMessageBox(mb, true);
		bindActionsToKeyboard(mb, ButtonId.OK, true);
	}
	
	public static void confirm(String title, String message, IDialogResultHandler resultHandler)
	{
		MyMessageBoxListener listener = MyMessageBoxListener.getDefault(resultHandler);
		MessageBox mb = MessageBox.showPlain(
				Icon.QUESTION,
				title == null ? "Confirm" : title,
				message,
				listener,
				ButtonId.YES, ButtonId.NO
		);
		listener.setParentBox(mb); // don't forget this!
		setupMessageBox(mb, true);
		bindActionsToKeyboard(mb, ButtonId.YES, true);
	}
	
	public static void textPrompt(String title, String inputLabel, final IDialogResultHandler resultHandler)
	{
		final TextField tf = new TextField();
		tf.setInputPrompt("Enter value");
		MyMessageBoxListener listener = new MyMessageBoxListener(resultHandler)
		{
			@Override
			protected boolean allowOKHandle()
			{
				return true;
			}
			
			@Override
			protected void addArgs(List<Object> arguments)
			{
				arguments.add(tf.getValue());
			}
		};
		MessageBox mb = MessageBox.showCustomized(
				Icon.QUESTION,
				title == null ? "Text prompt" : title,
				tf,
				listener,
				ButtonId.OK
		);
		listener.setParentBox(mb); // don't forget this!
		setupMessageBox(mb, false);
		bindActionsToKeyboard(mb, ButtonId.OK, true);
	}
	
	// -------------------------------------------------------------------------
	// ROUTINES FOR DISPLAYING SPECIALIZED DIALOGS
	
	public static void loginDialog(final IDialogResultHandler resultHandler)
	{
		final LoginForm loginForm = new LoginForm();
		MyFormMessageBoxListener listener = new MyFormMessageBoxListener(loginForm, resultHandler)
		{
			@Override
			protected void addArgs(List<Object> arguments)
			{
				arguments.add(loginForm.getLogin());
				arguments.add(loginForm.getPassword());
			}
			
			@Override
			protected boolean handleCustomButton(ButtonId button)
			{
				if(button == ButtonId.CUSTOM_1)
				{
					createAccountDialog(new IDialogResultHandler()
					{
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
	}
	
	public static void createAccountDialog(final IDialogResultHandler resultHandler)
	{
		final CreateAccountForm caForm = new CreateAccountForm();
		MyFormMessageBoxListener listener = new MyFormMessageBoxListener(caForm, resultHandler)
		{
			@Override
			protected void addArgs(List<Object> arguments)
			{
				arguments.add(caForm.getLogin());
				arguments.add(caForm.getPassword());
				arguments.add(caForm.getEmail());
			}
		};
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
	}
	
	public static void passwordChangeDialog(JPAUser currentUser, final IDialogResultHandler resultHandler)
	{
		final ChangePasswordForm cpForm = new ChangePasswordForm(currentUser);
		MyFormMessageBoxListener listener = new MyFormMessageBoxListener(cpForm, resultHandler)
		{
			@Override
			protected void addArgs(List<Object> arguments)
			{
				arguments.add(cpForm.getChangedPassword());
			}
		};
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
	}
	
	public static void saveExperimentDialog(final IDialogResultHandler resultHandler)
	{
		final SaveExperimentForm seForm = new SaveExperimentForm();
		MyFormMessageBoxListener listener = new MyFormMessageBoxListener(seForm, resultHandler)
		{
			@Override
			protected void addArgs(List<Object> arguments)
			{
				arguments.add(seForm.getExperimentName());
				arguments.add(seForm.getPriorityAssignedByUser());
				arguments.add(seForm.getComputationEstimateInHours());
				arguments.add(seForm.getSendEmailWhenFinished());
				arguments.add(seForm.getExperimentNote());
			}
		};
		MessageBox mb = MessageBox.showCustomized(
				Icon.NONE,
				"Save experiment from active tab",
				seForm,
				listener,
				ButtonId.OK, ButtonId.CANCEL
		);
		listener.setParentBox(mb); // don't forget this!
		setupMessageBox(mb, false);
		bindActionsToKeyboard(mb, ButtonId.OK, true);
	}
	
	// -------------------------------------------------------------------------
	// PUBLIC DIALOG RESULTS
	
	public static interface IDialogResultHandler
	{
		/**
		 * Custom action to be called when the dialog's main accept button is clicked.
		 * @param args
		 * @return true if the dialog is no longer needed and should close
		 */
		public boolean handleResult(Object[] args);
	}
	
	// -------------------------------------------------------------------------
	// SPECIAL INNER TYPE

	private abstract static class MyMessageBoxListener implements MessageBoxListener
	{
		private final IDialogResultHandler resultHandler;
		private final List<Object> arguments;
		private MessageBox parentBox;

		public MyMessageBoxListener(IDialogResultHandler resultHandler)
		{
			this.resultHandler = resultHandler;
			this.arguments = new ArrayList<Object>();
			this.parentBox = null;
		}
		
		public void setParentBox(MessageBox parentBox)
		{
			this.parentBox = parentBox;
		}
		
		@Override
		public void buttonClicked(ButtonId button)
		{
			switch(button)
			{
				case OK:
				case YES:
				case SAVE:
					if(allowOKHandle())
					{
						addArgs(arguments);
						if(resultHandler.handleResult(arguments.toArray()))
						{
							parentBox.close();
						}
						else
						{
							arguments.clear();
						}
					}
					break;
				
				case ABORT:
				case CANCEL:
				case CLOSE:
				case NO:
					parentBox.close();
					break;
					
				default:
					if(!handleCustomButton(button))
					{
						throw new IllegalStateException(String.format("No action is mapped to the '%s' button.", parentBox.getButton(button).getCaption()));
					}
					break;
			}
		}
		
		/**
		 * This method is called after the "ok" button is clicked on the dialog.
		 * @return whether {@link #handleOK()} method should be called next
		 */
		protected abstract boolean allowOKHandle();
		
		/**
		 * This method is called after the {@link #allowOKHandle()} method.
		 * Used to add arguments to the result handler passed to this listener.
		 * @param arguments the list to add arguments to
		 */
		protected abstract void addArgs(List<Object> arguments);
		
		/**
		 * The super implementation does nothing and is called for any button clicks that are not handled
		 * by default.
		 * Override to add custom actions for these buttons.
		 * @param button
		 * @return True whether the button has been successfully processed. If not, an exception is 
		 * thrown to indicate that no action is mapped to a button.
		 */
		protected boolean handleCustomButton(ButtonId button)
		{
			return false;
		}
		
		public static MyMessageBoxListener getDefault(IDialogResultHandler resultHandler)
		{
			return new MyMessageBoxListener(resultHandler)
			{
				@Override
				protected boolean allowOKHandle()
				{
					return true;
				}
				
				@Override
				protected void addArgs(List<Object> arguments)
				{
				}
			};
		}
	}
	
	private abstract static class MyFormMessageBoxListener extends MyMessageBoxListener
	{
		private final CustomFormLayout form; 

		public MyFormMessageBoxListener(CustomFormLayout form, IDialogResultHandler resultHandler)
		{
			super(resultHandler);
			
			this.form = form;
		}
		
		@Override
		protected boolean allowOKHandle()
		{
			return form.isFormValidAndUpdated();
		}
	}
}