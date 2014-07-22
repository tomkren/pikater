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
import com.vaadin.ui.Component;
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
	
	public static MessageBox info(String title, String message)
	{
		MessageBox mb = MessageBox.showPlain(
				Icon.INFO,
				title == null ? "Notification" : title,
				message,
				ButtonId.CLOSE
		);
		setupMessageBox(mb, true);
		bindActionsToKeyboard(mb, ButtonId.CLOSE, true);
		return mb;
	}
	
	public static MessageBox error(String title, String message)
	{
		MessageBox mb = MessageBox.showPlain(Icon.ERROR, title == null ? "Error" : title, message, ButtonId.OK);
		setupMessageBox(mb, true);
		bindActionsToKeyboard(mb, ButtonId.OK, true);
		return mb;
	}
	
	public static MessageBox confirm(String title, String message, IDialogResultHandler resultHandler)
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
		return mb;
	}
	
	public static MessageBox textPrompt(String title, String inputLabel, final IDialogResultHandler resultHandler)
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
		return mb;
	}
	
	// -------------------------------------------------------------------------
	// ROUTINES FOR DISPLAYING SPECIALIZED DIALOGS
	
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
	
	public static MessageBox saveExperimentDialog(SaveExperimentForm seForm)
	{
		MyComponentMessageBoxListener<SaveExperimentForm> listener = 
				new MyComponentMessageBoxListener<SaveExperimentForm>(seForm);
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
	
	// -------------------------------------------------------------------------
	// PUBLIC TYPES
	
	public static interface IDialogResultValidator
	{
		/**
		 * This method is called when the "OK" button is clicked in the dialog. Use
		 * it to indicate whether conditions are prepared for handling the dialog result. 
		 * @return true if no errors will occur when dialog result is handled
		 */
		boolean isResultReadyToBeHandled();
	}
	
	public static interface IDialogResultPreparer extends IDialogResultValidator
	{
		/**
		 * This method is called to gather the dialog result arguments/variables before
		 * being passed to {@link IDialogResultHandler#handleResult(Object[])}.</br>
		 * Only called if the {@link #isResultReadyToBeHandled()} method returns true.
		 * @param arguments the list to add arguments to
		 */
		void addArgs(List<Object> arguments);
	}
	
	public static interface IDialogResultHandler
	{
		/**
		 * Custom action to be called when the dialog's main accept button is clicked.
		 * @param args
		 * @return true if the dialog is no longer needed and should close
		 */
		boolean handleResult(Object[] args);
	}
	
	public static interface IDialogComponent extends IDialogResultPreparer, IDialogResultHandler
	{
	}
	
	// -------------------------------------------------------------------------
	// SPECIAL INNER TYPES

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
		 * This method is called to gather the dialog result arguments/variables before
		 * being passed to {@link IDialogResultHandler#handleResult(Object[])}.</br>
		 * Only called if the {@link IDialogComponent#isResultReadyToBeHandled()} method returns true.
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
	
	private static class MyComponentMessageBoxListenerWithExternalResultHandler<T extends Component & IDialogResultPreparer> extends MyMessageBoxListener
	{
		private final T component; 

		public MyComponentMessageBoxListenerWithExternalResultHandler(T component, IDialogResultHandler externalResultHandler)
		{
			super(externalResultHandler);
			
			this.component = component;
		}
		
		@Override
		protected boolean allowOKHandle()
		{
			return component.isResultReadyToBeHandled();
		}
		
		@Override
		protected void addArgs(List<Object> arguments)
		{
			component.addArgs(arguments);
		}
	}
	
	private static class MyComponentMessageBoxListener<T extends Component & IDialogComponent> extends MyMessageBoxListener
	{
		private final T component; 

		public MyComponentMessageBoxListener(T component)
		{
			super(component);
			
			this.component = component;
		}
		
		@Override
		protected boolean allowOKHandle()
		{
			return component.isResultReadyToBeHandled();
		}
		
		@Override
		protected void addArgs(List<Object> arguments)
		{
			component.addArgs(arguments);
		}
	}
}