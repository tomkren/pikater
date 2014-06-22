package org.pikater.web.vaadin;

import org.pikater.shared.logging.PikaterLogger;
import org.pikater.web.RequestReconstructor;
import org.pikater.web.RequestReconstructor.RequestComponent;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.gui.server.components.popups.MyDialogs;
import org.pikater.web.vaadin.gui.server.components.popups.MyDialogs.IDialogResultHandler;
import org.pikater.web.vaadin.gui.server.components.popups.MyFancyNotifications;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.web.vaadin.gui.server.welcometour.WelcomeTourWizard;

import com.porotype.iconfont.FontAwesome;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;

/**
 * A UI encapsulating all other specific UIs in this application so that child UIs don't
 * have to bother with the same old routines of settings everything up.</br>
 * Takes care of various things:
 * <ol>
 * <li> Checking whether the application has been launched properly and if not, handles
 * all required actions.
 * <li> Setting up notifications so that they can be used with the "MyNotifications.show"
 * notation anywhere in the application. 
 * <li> Sets the default error handler to prevent errors bubbling right to the client's browser.
 * <li> Provides some additional interface for child UIs.
 * </ol>
 */
public abstract class CustomConfiguredUI extends UI
{
	private static final long serialVersionUID = 3280691990478021417L;
	
	private final UniversalUIExtension universalUIExt = new UniversalUIExtension();
	private final MyFancyNotifications notifications = new MyFancyNotifications();
	
	@Override
	protected void init(VaadinRequest request)
	{
		/*
		 * Set some basic stuff.
		 */
		FontAwesome.load();
		
		/*
		 * Enable sending client errors to the server, where they will be logged.
		 */
		universalUIExt.extend(this);
		
		/*
		 * Prevent errors from directly being displayed on the client. Rather log the errors on server and
		 * send a notification to the client.
		 * NOTE: this is the last hook to catch the exception. If another error handler is set on a child
		 * component of a UI, this error handler will not be used.
		 */
		setErrorHandler(new DefaultErrorHandler()
		{
			private static final long serialVersionUID = -4395506937938101756L;

			@Override
			public void error(com.vaadin.server.ErrorEvent event)
			{
				PikaterLogger.logThrowable("Default UI error handler caught the following error:", event.getThrowable());
				MyNotifications.showError("Server error", "Your last request spawned an error. Please contact the administrators.");
			}
		});
		
		/*
		 * Default initialization of the UI.
		 */
		if(!ServerConfigurationInterface.isApplicationReadyToServe()) // application has not yet been setup and pikater has not yet been launched
		{
			// force the user to authenticate so that he can setup and launch pikater on remote machines
			forceUserToAuthenticate(new IDialogResultHandler()
			{
				@Override
				public boolean handleResult(Object[] args)
				{
					/*
					 * First check whether the default admin account is allowed and was provided by the user.
					 */

					String login = (String) args[0];
					String password = (String) args[1];
					if(ServerConfigurationInterface.getConfig().defaultAdminAccountAllowed)
					{
						if(login.equals("pikater") && password.equals("pikater")) // check whether the given auth info matches the default account
						{
							displayApplicationSetupWizard();
							return true; // necessary for the login dialog to close
						}
					}

					/* 
					 * If authentication using the default admin account failed, try to authenticate using the database.
					 * Note: database connection is assumed to have been checked in {@link StartupAndQuitListener}. 
					 */

					if(ManageAuth.authenticateUser(VaadinSession.getCurrent(), login, password)) // authentication succeeded
					{
						if(ManageAuth.getUserEntity(VaadinSession.getCurrent()).isAdmin()) // final access check
						{
							displayApplicationSetupWizard();
							return true;
						}
						else
						{
							ManageAuth.logout(VaadinSession.getCurrent());
							MyNotifications.showWarning("Access denied", "Application needs to be launched first. Log in as administrator.");
							return false;
						}
					}
					else
					{
						MyNotifications.showWarning("Access denied", "Invalid auth information.");
						return false;
					}
				}
			});
		}
		else
		{
			displayChildContent();
		}
	}
	
	//-------------------------------------------------------------------
	// PRIVATE INTERFACE - UI building
	
	/**
	 * Make the user launch the application first (to be usable at all) and then display index page to him if authenticated for that.
	 */
	private void displayApplicationSetupWizard()
	{
		setMyContent(new WelcomeTourWizard(new Button.ClickListener()
		{
			private static final long serialVersionUID = -8250998657726465300L;

			/**
			 * Event fired the "finish" button is clicked in the wizard. All required checks are done
			 * in the wizard itself, so this method should only:
			 * <ul>
			 * <li> Further initialize the application.
			 * <li> Update the user interface accordingly (so that the authenticated admin may be redirected
			 * to the default page).
			 * </ul>
			 */
			@Override
			public void buttonClick(ClickEvent event)
			{
				/*
				 * Further initializations. 
				 */
				
				// initialize and start the cron job scheduler
				/*
				if(!PikaterJobScheduler.init(AppHelper.getAbsolutePath(AppHelper.getAbsoluteWEBINFCLASSESPath(), PikaterJobScheduler.class)))
				{
					throw new IllegalStateException("Application won't serve until the above errors are fixed.");
				}
				*/
				
				/*
				 * Update UI. 
				 */
				displayChildContent();
			}
		}));
	}
	
	/**
	 * A special primary routine for setting this UI's content. All methods in this class should
	 * use this method instead of the inherited {@link #setContent()} which is now solely dedicated
	 * to the child classes.
	 * 
	 * REASON:
	 * The notifications feature has a tendency to cause a lot of confusion because the notifications
	 * component has to always be present in UI's layout and this should be done transparently...
	 * 
	 * @param content
	 */
	private void setMyContent(Component content)
	{
		if(content == null) // mostly likely Vaadin's own initialization, unfortunately...
		{
			/*
			 * We have to set the null content and return because nothing is initialized yet in here.
			 * If this is not after all Vaadin's initialization, notifications will not generate any
			 * errors but still won't work because the notifications component will have not been added
			 * to the UI.
			 */
			super.setContent(content);
		}
		else
		{
			VerticalLayout vLayout = new VerticalLayout();
			vLayout.setSizeFull();
			vLayout.addComponent(notifications); // always ensure that notifications component is added
			vLayout.addComponent(content); // causes a NullPointerException if null
			vLayout.setExpandRatio(content, 1);
			super.setContent(vLayout);
		}
	}
	
	@Override
	public void setContent(Component content)
	{
		/*
		 * Reserved for setting the child content.
		 */
		setMyContent(content);
	}
	
	//-----------------------------------------------------------
	// SOME ABSTRACT INTERFACE TO IMPLEMENT BY CHILD UIs
	
	/**
	 * Callback to the child indicating that it can start setting the UI as it wishes. Until
	 * that time, it must not change the UI content in any way.
	 */
	protected abstract void displayChildContent();
	
	//-------------------------------------------------------------------
	// AUTHENTICATION RELATED INTERFACE
	
	/**
	 * A low-level implementation that supports custom authentication mechanism. Should not be seen by children.
	 * @param authHandler the authentication mechanism
	 */
	private void forceUserToAuthenticate(final IDialogResultHandler resultHandler)
	{
		/*
		 * This is necessary as a precaution to the user entering invalid auth info. If no content
		 * component is supplied to the UI, the login dialog disappears after the "init()" method finishes.
		 */
		setMyContent(new Label());
		
		// display the login dialog
		MyDialogs.loginDialog(resultHandler);
	}
	
	/**
	 * A high-level implementation with built-in authentication mechanism that calls a custom callback,
	 * when authentication is successful.
	 * Should be used in child classes.
	 * @param authHandler the callback
	 */
	protected void forceUserToAuthenticate(final IAuthenticationSuccessful authHandler)
	{
		forceUserToAuthenticate(new IDialogResultHandler()
		{
			@Override
			public boolean handleResult(Object[] args)
			{
				/* 
				 * Try to authenticate using the database.
				 * Note: database connection is assumed to have been checked in {@link StartupAndQuitListener}. 
				 */

				String login = (String) args[0];
				String password = (String) args[1];
				if(ManageAuth.authenticateUser(VaadinSession.getCurrent(), login, password)) // authentication succeeded
				{
					authHandler.onSuccessfulAuth();
					return true;
				}
				else
				{
					MyNotifications.showWarning("Access denied", "Invalid auth information.");
					return false;
				}
			}
		});
	}
	
	protected interface IAuthenticationSuccessful
	{
		void onSuccessfulAuth();
	}
	
	//-----------------------------------------------------------
	// PUBLIC INTERFACE - miscellaneous methods
	
	public UniversalUIExtension getUniversalUIExtension()
	{
		return universalUIExt;
	}
	
	public MyFancyNotifications getNotificationsComponent()
	{
		return notifications;
	}
	
	public static String getBaseAppURLFromLastRequest()
	{
		return RequestReconstructor.getRequestPrefix(VaadinServletService.getCurrentServletRequest(), RequestComponent.P4_APPCONTEXT);
	}
}
