package org.pikater.web.vaadin;

import org.pikater.shared.logging.PikaterLogger;
import org.pikater.web.RequestReconstructor;
import org.pikater.web.RequestReconstructor.RequestComponent;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.gui.server.MyDialogs;
import org.pikater.web.vaadin.gui.server.welcometour.WelcomeTourWizard;

import com.porotype.iconfont.FontAwesome;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

public abstract class CustomConfiguredUI extends UI
{
	private static final long serialVersionUID = 3280691990478021417L;
	
	private UniversalUIExtension universalUIExt = null;

	@Override
	protected void init(VaadinRequest request)
	{
		/*
		 * Set some basic stuff.
		 */
		getPage().setTitle("Pikatorium");
		FontAwesome.load();
		
		/*
		 * Enable sending client errors to the server, where they will be logged.
		 */
		universalUIExt = new UniversalUIExtension();
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
				Notification.show("An error on the server occured. Please contact the administrators.", Type.ERROR_MESSAGE);
			}
		});
		
		/*
		 * Default initialization of the UI.
		 */
		if(!ServerConfigurationInterface.isApplicationReadyToServe()) // application has not yet been setup and pikater has not yet been launched
		{
			// force the user to authenticate so that he can setup and launch pikater on remote machines
			forceUserToAuthenticate(new MyDialogs.ILoginDialogResult()
			{
				@Override
				public boolean handleResult(String login, String password) // authentication info is provided in the args
				{
					/*
					 * First check whether the default admin account is allowed and was provided by the user.
					 */

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
							Notification.show("Access denied.", Type.WARNING_MESSAGE);
							return false;
						}
					}
					else
					{
						Notification.show("Invalid auth info.", Type.WARNING_MESSAGE);
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
		setContent(new WelcomeTourWizard(new Button.ClickListener()
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
	private void forceUserToAuthenticate(MyDialogs.ILoginDialogResult authHandler)
	{
		/*
		 * This is necessary as a precaution to the user entering invalid auth info. If no content
		 * component is supplied to the UI, the login dialog disappears after the "init()" method finishes.
		 */
		setContent(new Label());
		
		// display the login dialog
		MyDialogs.createLoginDialog(this, authHandler);
	}
	
	/**
	 * A high-level implementation with built-in authentication mechanism that calls a custom callback,
	 * when authentication is successful. 
	 * @param authHandler the callback
	 */
	protected void forceUserToAuthenticate(final IAuthenticationSuccessful authHandler)
	{
		forceUserToAuthenticate(new MyDialogs.ILoginDialogResult()
		{
			@Override
			public boolean handleResult(String login, String password)
			{
				/* 
				 * Try to authenticate using the database.
				 * Note: database connection is assumed to have been checked in {@link StartupAndQuitListener}. 
				 */

				if(ManageAuth.authenticateUser(VaadinSession.getCurrent(), login, password)) // authentication succeeded
				{
					authHandler.onSuccessfulAuth();
					return true;
				}
				else
				{
					Notification.show("Invalid auth info.", Type.WARNING_MESSAGE);
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
	
	public static String getBaseAppURLFromLastRequest()
	{
		return RequestReconstructor.getRequestPrefix(VaadinServletService.getCurrentServletRequest(), RequestComponent.P4_APPCONTEXT);
	}
}
