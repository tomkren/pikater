package org.pikater.web.vaadin.gui.server.components.popups.dialogs;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.KeyCodes;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Component;

import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.MessageBox;
import de.steinwedel.messagebox.MessageBoxListener;

public class DialogCommons
{
	/**
	 * Applies general message box settings.
	 * @param box
	 * @param enterButton which button to bind with the ENTER key
	 * @param closeWithAnyButton whether the dialog should close after clicking ANY of its buttons
	 * @param escapeToClose
	 */
	protected static void setupMessageBox(MessageBox box, boolean closeWithAnyButton)
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
	protected static void bindActionsToKeyboard(MessageBox box, ButtonId enterButton, boolean escapeToClose)
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
	
	// ---------------------------------------------------------------------------------------------------------
	// PROTECTED TYPES - HANDLING BUTTON CLICKS, INTEGRATION WITH RESULT HANDLING AND PREPARATION TO IT
	
	protected abstract static class MyMessageBoxListener implements MessageBoxListener
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
					handleClose();
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
		 * Method to close the dialog when it's no longer needed.
		 */
		protected void handleClose()
		{
			parentBox.close();
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
	
	protected static class MyComponentMessageBoxListenerWithExternalResultHandler<T extends Component & IDialogResultPreparer> extends MyMessageBoxListener
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
	
	protected static class MyComponentMessageBoxListener<T extends Component & IDialogComponent> extends MyMessageBoxListener
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
	
	// -------------------------------------------------------------------------
	// PUBLIC TYPES - GENERAL DIALOG RESULT HANDLING AND PREPARATION TO IT

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
}