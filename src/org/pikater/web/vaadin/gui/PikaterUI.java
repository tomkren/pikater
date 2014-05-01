package org.pikater.web.vaadin.gui;

import javax.servlet.annotation.WebServlet;

import org.pikater.shared.ssh.SSHSession;
import org.pikater.shared.ssh.SSHSession.ISSHSessionNotificationHandler;
import org.pikater.web.config.ServerConfiguration;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.CustomConfiguredUIServlet;
import org.pikater.web.vaadin.gui.welcometour.WelcomeTourWizard;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;

@SuppressWarnings("serial")
@Theme("pikater")
@com.vaadin.annotations.JavaScript(value = "public/kinetic-v4.7.3-dev.js")
@Push(value = PushMode.MANUAL)
public class PikaterUI extends UI
{
	/*
	 * Automatic UI finding takes this even one step further and allows you to leave out @VaadinServletConfiguration
	 * completely if you define your servlet class as a static inner class to your UI class.
	 * For clarity the variant with @VaadinServletConfiguration is likely the better option. Please do note that
	 * @VaadinServletConfiguration comes with defaults for some parameters, most importantly legacyPropertyToStringMode,
	 * which might be important if you are migrating an older application. 
	 */
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = PikaterUI.class, widgetset = "org.pikater.web.vaadin.gui.PikaterWidgetset")
	public static class Servlet extends CustomConfiguredUIServlet
	{
	}

	@Override
	protected void init(VaadinRequest request)
	{
		primary_displayWelcomeWizard();
		
		// test_console();
		// test_editor();
		// test_simpleButton();
		// test_JSCH();
	}
	
	// -------------------------------------------------------------------
	// PRIMARY GUI INITIALIZATONS
	
	private void primary_displayWelcomeWizard()
	{
		/*
		 * This is necessary as a precaution to the user entering invalid auth info. If no content is supplied,
		 * the login dialog disappears after the "init()" method finishes. 
		 */
		setContent(new Label());
		
		// TODO: try to connect to the database
		
		MyDialogs.createLoginDialog(this, new MyDialogs.ILoginDialogResult()
		{
			@Override
			public boolean handleResult(String login, String password)
			{
				// first check the default admin account
				ServerConfiguration conf = ServerConfigurationInterface.getConfig(); 
				if(conf.defaultAdminAccountAllowed)
				{
					if(login.equals("pikater") && password.equals("pikater")) // check whether the given auth info matches the default account
					{
						setContent(new WelcomeTourWizard(new Button.ClickListener()
						{
							@Override
							public void buttonClick(ClickEvent event)
							{
								display_defaultPage();
							}
						}));
						return true;
					}
				}
				
				// TODO: and then check the database accounts
				
				Notification.show("Invalid auth info.", Type.HUMANIZED_MESSAGE);
				return false;
			}
		});
	}
	
	private void display_defaultPage()
	{
		// TODO: construct a proper index page
		setContent(new Label("Yay. You've just killed the wizard!"));
	}
	
	// -------------------------------------------------------------------
	// TEST GUI INITIALIZATONS
	
	private void test_editor()
	{
		VerticalLayout vLayout = new VerticalLayout();
		setContent(vLayout);
		
		KineticEditor editor = new KineticEditor();
		// kineticComponent.setWidth("800px");
		// kineticComponent.setHeight("600px");
		
		vLayout.addComponent(editor);
	}
	
	@SuppressWarnings("unused")
	private void test_JSCH()
	{
		VerticalLayout vLayout = new VerticalLayout();
		setContent(vLayout);
		
		SimpleConsoleComponent consoleComponent = new SimpleConsoleComponent(new SSHSession(
				"nassoftwerak.ms.mff.cuni.cz",
				"e2:dc:09:34:e5:94:11:7f:fd:ee:00:09:b8:1e:f5:d4",
				"softwerak",
				"SrapRoPy",
				new ISSHSessionNotificationHandler()
				{
					@Override
					public void notifySessionClosed()
					{
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void notifyChannelClosed(int exitStatus)
					{
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void handleError(String description, Throwable t)
					{
						// TODO Auto-generated method stub
						
					}
				})
		);
		vLayout.addComponent(consoleComponent);
		
		// has to be called after the component is attached to the UI
		consoleComponent.setWidth("600px");
		consoleComponent.setHeight("400px");
	}
	
	@SuppressWarnings("unused")
	private void test_simpleButton()
	{
		VerticalLayout vLayout = new VerticalLayout();
		Button btn = new Button("Test", new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				// getUI().getPage().setLocation("/NewPikater/static/hokus_pokus.txt"); // ./WEB-INF/static/hokus_pokus.txt
				getUI().getPage().setLocation("/Pikater/staticDownload"); // servlet mapped to /staticDownload
			}
		});
		vLayout.addComponent(btn);
		setContent(vLayout);
	}
	
	// -------------------------------------------------------------------
	// PRIVATE INTERFACE
	
	@SuppressWarnings("unused")
	@Deprecated
	private void logout()
	{
		Button logout = new Button("Logout");
	    logout.addClickListener(new Button.ClickListener()
	    {
	        @Override
	        public void buttonClick(ClickEvent event)
	        {
	            // Redirect from the page
	            getUI().getPage().setLocation("/myapp/logoutpage.html");
	            
	            // Close the VaadinSession
	            getSession().close();
	        }
	    });
	}
}