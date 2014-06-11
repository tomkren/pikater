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
	 * Applies settings common to all message boxes of this application.
	 * @param box
	 * @param enterButton which button to bind with the ENTER key
	 * @param closeWithAnyButton whether the dialog should close after clicking ANY of its buttons
	 * @param escapeToClose
	 */
	private static void setupMessageBox(MessageBox box, ButtonId enterButton, boolean closeWithAnyButton, boolean escapeToClose)
	{
		box.getWindow().setResizable(false);
		box.getWindow().setDraggable(false);
		if(!closeWithAnyButton)
		{
			box.setAutoClose(closeWithAnyButton);
		}
		if(escapeToClose)
		{
			box.getWindow().setCloseShortcut(KeyCode.ESCAPE, null);
		}
		box.getButton(enterButton).setClickShortcut(KeyCodes.KEY_ENTER, null);
		
		// TODO:
		// content.setStyleName("pikaterDialogContent");
		// parentUI.setFocusedComponent(box);
	}
	
	//----------------------------------------------------------------
	// PUBLIC ROUTINES FOR DISPLAYING GENERAL USE DIALOGS
	
	public static void confirm(String title, String message, final DialogResultHandler resultHandler)
	{
		MyMessageBoxListener listener = new MyMessageBoxListener(resultHandler);
		MessageBox mb = MessageBox.showPlain(
				Icon.QUESTION,
				title == null ? "Confirm" : title,
				message,
				listener,
				ButtonId.YES, ButtonId.NO
		);
		listener.setParentBox(mb); // don't forget this!
		setupMessageBox(mb, ButtonId.YES, true, true);
	}
	
	public static void textPrompt(String title, String inputLabel, final DialogResultHandler resultHandler)
	{
		final TextField tf = new TextField();
		tf.setInputPrompt("Enter value");
		MyMessageBoxListener listener = new MyMessageBoxListener(resultHandler)
		{
			@Override
			protected boolean handleOK()
			{
				getResultHandler().addArg(tf.getValue());
				return super.handleOK();
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
		setupMessageBox(mb, ButtonId.OK, false, true);
	}
	
	public static void error(String title, String message)
	{
		MessageBox mb = MessageBox.showPlain(Icon.ERROR, title == null ? "Error" : title, message, ButtonId.OK);
		setupMessageBox(mb, ButtonId.OK, true, true);
	}
	
	// -------------------------------------------------------------------------
	// PUBLIC ROUTINES FOR DISPLAYING SPECIALIZED DIALOGS
	
	public static void loginDialog(final DialogResultHandler resultHandler)
	{
		final LoginForm loginForm = new LoginForm();
		MyMessageBoxListener listener = new MyMessageBoxListener(resultHandler)
		{
			@Override
			protected boolean handleOK()
			{
				// if(loginForm.isFormValidAndUpdated()) // TODO: uncomment
				if(true)
				{
					getResultHandler().addArg(loginForm.getLogin());
					getResultHandler().addArg(loginForm.getPassword());
					return super.handleOK();
				}
				else
				{
					return false;
				}
			}
			
			@Override
			protected boolean handleCustomButton(ButtonId button)
			{
				if(button == ButtonId.CUSTOM_1)
				{
					createAccountDialog(new DialogResultHandler()
					{
						@Override
						public boolean handleResult()
						{
							DAOs.userDAO.storeEntity(new JPAUser(
									(String) getArg(0),
									(String) getArg(1),
									(String) getArg(2),
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
		setupMessageBox(mb, ButtonId.OK, false, false);
		mb.getButton(ButtonId.CUSTOM_1).setCaption("Create account");
	}
	
	public static void createAccountDialog(final DialogResultHandler resultHandler)
	{
		final CreateAccountForm caForm = new CreateAccountForm();
		MyMessageBoxListener listener = new MyMessageBoxListener(resultHandler)
		{
			@Override
			protected boolean handleOK()
			{
				if(caForm.isFormValidAndUpdated())
				{
					getResultHandler().addArg(caForm.getLogin());
					getResultHandler().addArg(caForm.getPassword());
					getResultHandler().addArg(caForm.getEmail());
					return super.handleOK();
				}
				else
				{
					MyNotifications.showError(null, "Email not valid.");
					return false;
				}
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
		setupMessageBox(mb, ButtonId.OK, false, true);
	}
	
	public static void passwordChangeDialog(ChangePasswordForm cpForm, final DialogResultHandler resultHandler)
	{
		MyMessageBoxListener listener = new MyMessageBoxListener(resultHandler);
		MessageBox mb = MessageBox.showCustomized(
				Icon.NONE,
				"Change password",
				cpForm,
				listener,
				ButtonId.OK, ButtonId.CANCEL
		);
		listener.setParentBox(mb); // don't forget this!
		setupMessageBox(mb, ButtonId.OK, false, true);
	}
	
	public static void saveExperimentDialog(final DialogResultHandler resultHandler)
	{
		final SaveExperimentForm seForm = new SaveExperimentForm();
		MyMessageBoxListener listener = new MyMessageBoxListener(resultHandler)
		{
			@Override
			protected boolean handleOK()
			{
				getResultHandler().addArg(seForm.getExperimentName());
				getResultHandler().addArg(seForm.getPriorityAssignedByUser());
				getResultHandler().addArg(seForm.getComputationEstimateInHours());
				getResultHandler().addArg(seForm.getSendEmailWhenFinished());
				getResultHandler().addArg(seForm.getExperimentNote());
				return super.handleOK();
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
		setupMessageBox(mb, ButtonId.OK, false, true);
	}
	
	// -------------------------------------------------------------------------
	// PUBLIC DIALOG RESULTS
	
	public static abstract class DialogResultHandler
	{
		private final List<Object> args;
		
		public DialogResultHandler()
		{
			this.args = new ArrayList<Object>();
		}
		
		protected Object getArg(int position)
		{
			return args.get(position);
		}
		
		public void addArg(Object arg)
		{
			args.add(arg);
		}
		
		/**
		 * Custom action to be called when the dialog's main accept button is clicked. Use the
		 * {@link getArg(int position)} method to get your needed arguments. 
		 * @return true if the dialog is no longer needed and should close
		 */
		public abstract boolean handleResult();
	}
	
	// -------------------------------------------------------------------------
	// SPECIAL INNER TYPE

	private static class MyMessageBoxListener implements MessageBoxListener
	{
		private final DialogResultHandler resultHandler;
		private MessageBox parentBox;

		public MyMessageBoxListener(DialogResultHandler resultHandler)
		{
			this.resultHandler = resultHandler;
			this.parentBox = null;
		}
		
		protected DialogResultHandler getResultHandler()
		{
			return resultHandler;
		}
		
		protected MessageBox getParentBox()
		{
			return parentBox;
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
					if(handleOK())
					{
						getParentBox().close();
					}
					break;
				
				case ABORT:
				case CANCEL:
				case CLOSE:
				case NO:
					getParentBox().close();
					break;
					
				default:
					if(!handleCustomButton(button))
					{
						throw new IllegalStateException(String.format("No action is mapped to the '%s' button.", getParentBox().getButton(button).getCaption()));
					}
					break;
			}
		}
		
		/**
		 * The super implementation only calls {@link DialogResultHandler#handleResult()}. Override
		 * to add arguments to the linked {@link DialogResultHandler} and then call:
		 * <code>return super.handleOK();</code> or <code>return false;</code>.
		 * @return true if the dialog is no longer needed and should close 
		 */
		protected boolean handleOK()
		{
			return getResultHandler().handleResult();
		}
		
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
	}
}