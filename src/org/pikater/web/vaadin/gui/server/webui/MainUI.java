package org.pikater.web.vaadin.gui.server.webui;

import javax.servlet.annotation.WebServlet;

import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.config.ServerConfigurationInterface.ServerConfItem;
import org.pikater.web.vaadin.CustomConfiguredUI;
import org.pikater.web.vaadin.CustomConfiguredUIServlet;
import org.pikater.web.vaadin.gui.MainUIExtension;
import org.pikater.web.vaadin.gui.server.AuthHandler;
import org.pikater.web.vaadin.gui.server.webui.indexpage.IndexPage;
import org.pikater.web.vaadin.gui.server.webui.welcometour.WelcomeTourWizard;

import com.porotype.iconfont.FontAwesome;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

@Theme("pikater")
@Push(value = PushMode.AUTOMATIC)
public class MainUI extends CustomConfiguredUI
{
	private static final long serialVersionUID = 1964653532060950402L;
	
	/*
	 * Automatic UI finding takes this even one step further and allows you to leave out @VaadinServletConfiguration
	 * completely if you define your servlet class as a static inner class to your UI class.
	 * For clarity the variant with @VaadinServletConfiguration is likely the better option. Please do note that
	 * @VaadinServletConfiguration comes with defaults for some parameters, most importantly legacyPropertyToStringMode,
	 * which might be important if you are migrating an older application. 
	 */
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = MainUI.class, widgetset = "org.pikater.web.vaadin.gui.PikaterWidgetset")
	public static class Servlet extends CustomConfiguredUIServlet
	{
		private static final long serialVersionUID = -3494370492799211606L;
	}
	
	@Override
	protected void init(VaadinRequest request)
	{
		/*
		 * NOTE: do not remove or replace this code. 
		 */
		
		super.init(request);
		getPage().setTitle("Pikatorium");
		FontAwesome.load();
		MainUIExtension mainUIExtension = new MainUIExtension(); // TODO: this might eventually be a field
		mainUIExtension.extend(this);
		ServerConfigurationInterface.setField(ServerConfItem.UNIVERSAL_CLIENT_CONNECTOR, mainUIExtension.getClientRPC());
		
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

					return authenticateUser(login, password, new IAuthenticationSuccessful()
					{
						@Override
						public void onSuccessfulAuth()
						{
							displayApplicationSetupWizard();
						}
					});
				}
			});
		}
		else
		{
			displayIndexPageWhenAuthenticated();
		}
	}
	
	//-------------------------------------------------------------------
	// UI BUILDING METHODS
	
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
				
				displayIndexPageWhenAuthenticated();
			}
		}));
	}
	
	/**
	 * Display index page if authenticated or make the user authenticate first and then display it.
	 */
	private void displayIndexPageWhenAuthenticated()
	{
		if(AuthHandler.isUserAuthenticated(getSession()))
		{
			displayIndexPage();
		}
		else
		{
			forceUserToAuthenticate(new MyDialogs.ILoginDialogResult()
			{
				@Override
				public boolean handleResult(String login, String password)
				{
					return authenticateUser(login, password, new IAuthenticationSuccessful()
					{
						@Override
						public void onSuccessfulAuth()
						{
							displayIndexPage();
						}
					});
				}
			});
		}
	}
	
	/**
	 * The final method to be called in the UI bulding chain. Always being displayed if user is authenticated.
	 */
	private void displayIndexPage()
	{
		// return new IndexPage for each UI unless you want all browser tabs to be synchronized and display the same content
		setContent(new IndexPage());
	}
	
	//-------------------------------------------------------------------
	// HELPER METHODS
	
	/**
	 * Display a login dialog and do the provided action when the user clicks the "ok" button.
	 * @param authHandler the action
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
	
	private boolean authenticateUser(String login, String password, IAuthenticationSuccessful action)
	{
		if(AuthHandler.authenticateUser(getSession(), login, password)) // authentication succeeded
		{
			action.onSuccessfulAuth();
			return true;
		}
		else // and finally, if authentication failed
		{
			Notification.show("Invalid auth info.", Type.WARNING_MESSAGE);
			return false;
		}
	}
	
	private interface IAuthenticationSuccessful
	{
		void onSuccessfulAuth();
	}
}