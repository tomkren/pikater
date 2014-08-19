package org.pikater.web.vaadin.gui.server.components.popups.dialogs;

import java.util.List;

import com.vaadin.ui.TextField;

import de.steinwedel.messagebox.ButtonId;
import de.steinwedel.messagebox.Icon;
import de.steinwedel.messagebox.MessageBox;

public class GeneralDialogs extends DialogCommons
{
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
	
	public static MessageBox componentDialog(String title, IDialogComponent content)
	{
		MyComponentMessageBoxListener<IDialogComponent> listener = new MyComponentMessageBoxListener<IDialogComponent>(content);
		MessageBox mb = MessageBox.showCustomized(
				Icon.NONE,
				title != null ? title : "",
				content,
				listener,
				ButtonId.OK, ButtonId.CANCEL
				);
		listener.setParentBox(mb); // don't forget this!
		setupMessageBox(mb, false);
		bindActionsToKeyboard(mb, ButtonId.OK, true);
		return mb;
	}
	
	public static MessageBox componentDialog(String title, IDialogResultPreparer content, IDialogResultHandler resultHandler)
	{
		MyComponentMessageBoxListenerWithExternalResultHandler<IDialogResultPreparer> listener = 
				new MyComponentMessageBoxListenerWithExternalResultHandler<IDialogResultPreparer>(content, resultHandler);
		MessageBox mb = MessageBox.showCustomized(
				Icon.NONE,
				title != null ? title : "",
				content,
				listener,
				ButtonId.OK, ButtonId.CANCEL
				);
		listener.setParentBox(mb); // don't forget this!
		setupMessageBox(mb, false);
		bindActionsToKeyboard(mb, ButtonId.OK, true);
		return mb;
	}
}