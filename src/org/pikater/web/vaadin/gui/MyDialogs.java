package org.pikater.web.vaadin.gui;

import org.vaadin.dialogs.ConfirmDialog;

import com.google.gwt.event.dom.client.KeyCodes;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

@SuppressWarnings("serial")
public class MyDialogs
{
	// -------------------------------------------------------------------------
	// GENERAL DIALOGS
	
	public static ConfirmDialog createSimpleConfirmDialog(UI parentUI, String message, Button.ClickListener okAction)
	{
		ConfirmDialog result = ConfirmDialog.getFactory().create(null, message, "OK", "Cancel");
		setDialogProperties(result);
		if(okAction != null)
		{
			result.getOkButton().addClickListener(okAction);
		}
		result.show(parentUI, null, true);
		parentUI.setFocusedComponent(result);
		return result;
	}
	
	public static Window createComponentConfirmDialog(UI parentUI, String caption, Component subContent, final OnOkClicked okAction)
	{
		// define underlying components
        Label spacer = new Label("");
        spacer.setSizeFull();
        
        final Button cancel = new Button("Cancel");
        final Button ok = new Button("OK");
        ok.addClickListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				if(okAction.handleOkEvent())
				{
					cancel.click();
				}
			}
		});
        ok.setClickShortcut(KeyCode.ENTER, null);

        // create the buttons component
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSizeFull();
        buttons.setSpacing(true);
        buttons.addComponent(spacer);
        buttons.setExpandRatio(spacer, 1f);
        buttons.addComponent(ok);
        buttons.setComponentAlignment(ok, Alignment.MIDDLE_RIGHT);
        buttons.addComponent(cancel);
        buttons.setComponentAlignment(cancel, Alignment.MIDDLE_RIGHT);
        
        // wrap the content component
        VerticalLayout content = new VerticalLayout();
        content.setSpacing(true);
        content.addComponent(subContent);
        content.addComponent(buttons);

        // create the dialog
        final Window result = new Window(caption, content);
        content.setSizeUndefined();
        setDialogProperties(result);
        result.setSizeFull();
        result.setSizeUndefined();
        cancel.addClickListener(new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				result.close();
			}
		});
        parentUI.addWindow(result);
        parentUI.setFocusedComponent(result);
        
        return result;
	}
	
	public static Window createTextPromptDialog(UI parentUI, String caption, String oldValue, final ITextPromptDialogResult newValueHandler) 
	{
		final TextField tf = new TextField("The new value:");
		if(oldValue != null)
		{
			tf.setValue(oldValue);
		}
		return createComponentConfirmDialog(parentUI, caption, tf, new OnOkClicked()
		{
			@Override
			public boolean handleOkEvent()
			{
				return newValueHandler.handleResult(tf.getValue());
			}
		});
	}
	
	// -------------------------------------------------------------------------
	// SPECIFIC DIALOGS
	
	public static Window createLoginDialog(UI parentUI, final ILoginDialogResult resultHandler)
	{
		FormLayout form = new FormLayout();
		final TextField login = new TextField("Login:");
		final PasswordField password = new PasswordField("Password:");
		form.addComponent(login);
		form.addComponent(password);
		return createComponentConfirmDialog(parentUI, "Please, authenticate yourself", form, new OnOkClicked()
		{
			@Override
			public boolean handleOkEvent()
			{
				return resultHandler.handleResult(login.getValue(), password.getValue());
			}
		});
	}
	
	// -------------------------------------------------------------------------
	// DIALOG RESULTS
	
	public interface OnOkClicked
	{
		/**
		 * Custom action to be called when the user clicks the OK button.
		 * @return true if the dialog is no longer needed and it should close
		 */
		boolean handleOkEvent();
	}
	
	public interface ITextPromptDialogResult
	{
		/**
		 * Custom action to check the user's input.
		 * @return true if the dialog is no longer needed and it should close
		 */
		boolean handleResult(String promptResultString);
	}
	
	public interface ILoginDialogResult
	{
		/**
		 * Custom action to check the user's input.
		 * @return true if the dialog is no longer needed and it should close
		 */
		boolean handleResult(String login, String password);
	}
	
	// -------------------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private static void setDialogProperties(Window window)
	{
		window.setClosable(true);
		window.setDraggable(false);
		window.setModal(true);
		window.setResizable(false);
		window.setCloseShortcut(KeyCodes.KEY_ESCAPE, null);
	}
}
