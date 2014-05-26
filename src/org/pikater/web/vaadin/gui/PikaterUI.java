package org.pikater.web.vaadin.gui;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.annotation.WebServlet;

import org.json.JSONArray;
import org.json.JSONException;
import org.pikater.shared.experiment.universalformat.UniversalGui;
import org.pikater.shared.experiment.webformat.BoxInfo;
import org.pikater.shared.experiment.webformat.BoxInfoCollection;
import org.pikater.shared.experiment.webformat.BoxType;
import org.pikater.shared.experiment.webformat.Experiment;
import org.pikater.shared.ssh.SSHSession;
import org.pikater.shared.ssh.SSHSession.ISSHSessionNotificationHandler;
import org.pikater.web.HttpContentType;
import org.pikater.web.config.ServerConfiguration;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.config.ServerConfigurationInterface.ServerConfItem;
import org.pikater.web.vaadin.CustomConfiguredUIServlet;
import org.pikater.web.vaadin.gui.server.MainUIExtension;
import org.pikater.web.vaadin.gui.server.MyDialogs;
import org.pikater.web.vaadin.gui.server.components.SimpleConsoleComponent;
import org.pikater.web.vaadin.gui.server.components.experimenteditor.ExperimentEditor;
import org.pikater.web.vaadin.gui.server.components.upload.IUploadedFileHandler;
import org.pikater.web.vaadin.gui.server.components.upload.MyUploads;
import org.pikater.web.vaadin.gui.server.welcometour.WelcomeTourWizard;

import com.porotype.iconfont.FontAwesome;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.communication.PushMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("pikater")
@Push(value = PushMode.AUTOMATIC)
public class PikaterUI extends UI
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
	@VaadinServletConfiguration(productionMode = false, ui = PikaterUI.class, widgetset = "org.pikater.web.vaadin.gui.PikaterWidgetset")
	public static class Servlet extends CustomConfiguredUIServlet
	{
		private static final long serialVersionUID = -3494370492799211606L;
	}
	
	/*
	 * UIs are already session-bound, so we can safely do the following, as long as the field is serializable:
	 */
	private MyUploads thisUsersUploads;

	@Override
	protected void init(VaadinRequest request)
	{
		FontAwesome.load();
		getPage().setTitle("Pikatorium");
		
		/*
		 * NOTE: do not remove or replace this code. 
		 */
		
		MainUIExtension mainUIExtension = new MainUIExtension();
		mainUIExtension.extend(this);
		ServerConfigurationInterface.setField(ServerConfItem.UNIVERSAL_CLIENT_CONNECTOR, mainUIExtension.getClientRPC());
		
		thisUsersUploads = new MyUploads();
		
		/*
		 * TODO: box definition changes will take an application restart... only make the RPC method a one-time push?
		 * TODO: BoxInfo reference should be a reversible IDs... since box definitions have no decent IDs, we have to make
		 * them in a fashion that will allow us to find the substitute, unless referenced directly, as it is now. In that
		 * case we will have to manually check for newer versions when validating the experiments.
		 * TODO: cellBrowserDnD drags the label component instead of the custom inner layout sometimes and class cast issues occur...
		 * TODO: adding datasets: ARFF, CSV, XLS
		 */ 
		
		/*
		 * Which application scenario should be loaded when the webpages are accessed?
		 */
		
		primary_displayWelcomeWizard();
		
		// test_multiFileUpload();
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
							private static final long serialVersionUID = -8250998657726465300L;

							@Override
							public void buttonClick(ClickEvent event)
							{
								// initialize and start the cron job scheduler
								/*
								if(!PikaterJobScheduler.init(AppHelper.getAbsolutePath(AppHelper.getAbsoluteWEBINFCLASSESPath(), PikaterJobScheduler.class)))
								{
									throw new IllegalStateException("Application won't serve until the above errors are fixed.");
								}
								*/
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
	
	private void test_multiFileUpload()
	{
		VerticalLayout vLayout = new VerticalLayout();
		setContent(vLayout);
		
		vLayout.addComponent(thisUsersUploads.getNewComponent(
				Arrays.asList(HttpContentType.APPLICATION_JAR),
				new IUploadedFileHandler()
				{
					@Override
					public void handleFile(InputStream streamToLocalFile, String fileName, String mimeType, long sizeInBytes)
					{
						// TODO: upload the file to DB
					}
				}
		));
	}

	private void test_editor()
	{
		// TODO: use the server interface to get box definitions
		
		BoxInfo boxInfo1 = new BoxInfo("Bla1", "bla", "Bla1", BoxType.INPUT, "", "");
		BoxInfo boxInfo2 = new BoxInfo("Bla2", "bla", "Bla2", BoxType.RECOMMENDER, "", "");
		BoxInfo boxInfo3 = new BoxInfo("Bla3", "bla", "Bla3", BoxType.VISUALIZER, "", "");
		
		UniversalGui guiInfo1 = new UniversalGui(10, 10);
		UniversalGui guiInfo2 = new UniversalGui(500, 10);
		UniversalGui guiInfo3 = new UniversalGui(400, 300);
		
		BoxInfoCollection boxDefinitions = new BoxInfoCollection();
		boxDefinitions.addDefinition(boxInfo1);
		boxDefinitions.addDefinition(boxInfo2);
		boxDefinitions.addDefinition(boxInfo3);
		ServerConfigurationInterface.setField(ServerConfItem.BOX_DEFINITIONS, boxDefinitions);
		
		Experiment newExperiment = new Experiment();
		Integer b1 = newExperiment.addLeafBoxAndReturnID(guiInfo1, boxInfo1);
		Integer b2 = newExperiment.addLeafBoxAndReturnID(guiInfo2, boxInfo2);
		newExperiment.addLeafBoxAndReturnID(guiInfo3, boxInfo3);
		newExperiment.connect(b1, b2);
		
		ExperimentEditor editor = new ExperimentEditor(!getSession().getConfiguration().isProductionMode());
		setContent(editor);
		
		// editor.loadExperiment(newExperiment);
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
			private static final long serialVersionUID = -3016596327398677231L;

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
	// JAVASCRIPT TESTING
	
	@SuppressWarnings("unused")
	private void something1()
	{
		JavaScript.getCurrent().addFunction("pikater_setAppMode", new JavaScriptFunction()
		{
			private static final long serialVersionUID = 4291049321598205127L;

			@Override
			public void call(JSONArray arguments) throws JSONException
			{
				// this is called on the server when the function is called on the client
				System.out.println(arguments.length());
				System.out.println(arguments.getBoolean(0));
			}
		});
		JavaScript.getCurrent().execute("window.pikater_setAppMode(true)"); // calls the function on the client
	}
	
	@SuppressWarnings("unused")
	private void something2()
	{
		// just an example
		JavaScript.getCurrent().execute("window.ns_pikater.setAppMode(\"DEBUG\");"); // calls the function on the client
	}
	
	// -------------------------------------------------------------------
	// TIPS AND TRICKS
	
	private void smth()
	{
	    // The background thread that updates clock times once every second.
        new Timer().scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
			}
		}, new Date(), 1000);
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
			private static final long serialVersionUID = 6957710062047165748L;

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