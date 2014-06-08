package org.pikater.web.vaadin.gui.server.components.popups;

import org.pikater.web.vaadin.CustomConfiguredUI;
import org.pikater.web.vaadin.MyResources;
import org.vaadin.alump.fancylayouts.FancyNotification;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.UI;

public class MyNotifications
{
	//----------------------------------------------------------------
	// PUBLIC ROUTINES FOR DISPLAYING GENERAL USE NOTIFICATIONS
	
	public static void showSuccess(String title, String description)
	{
		getCurrentNotificationsManager().showNotification(null, title, description, MyResources.img_notificationSuccessIcon);
	}
	
	public static void showInfo(String title, String description)
	{
		getCurrentNotificationsManager().showNotification(null, title, description, MyResources.img_notificationInfoIcon);
	}
	
	public static void showWarning(String title, String description)
	{
		getCurrentNotificationsManager().showNotification(null, title, description, MyResources.img_notificationWarnIcon);
	}
	
	public static void showError(String title, String description)
	{
		getCurrentNotificationsManager().showNotification(null, title, description, MyResources.img_notificationErrorIcon);
	}
	
	//----------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private static MyFancyNotifications getCurrentNotificationsManager()
	{
		return ((CustomConfiguredUI) UI.getCurrent()).getNotificationsComponent();
	}
    
	@SuppressWarnings("unused")
    private static void showHTMLNotification(String title, String message, String styleName)
    {
    	FancyNotification notif = new FancyNotification(null,
                "Hello <span style=\"text-decoration: underline;\">World</span>",
                "Lorem <span style=\"font-size: 80%;\">ipsum.</span>"
        );
        notif.getTitleLabel().setContentMode(ContentMode.HTML);
        notif.getDescriptionLabel().setContentMode(ContentMode.HTML);
        getCurrentNotificationsManager().showNotification(notif);
    }
}
