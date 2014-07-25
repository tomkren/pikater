package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.various;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.utils.ResultFormatter;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.shared.quartz.PikaterJobScheduler;
import org.pikater.shared.ssh.SSHSession;
import org.pikater.shared.ssh.SSHSession.ISSHSessionNotificationHandler;
import org.pikater.shared.util.IOUtils;
import org.pikater.web.quartzjobs.MatrixPNGGeneratorJob;
import org.pikater.web.servlets.download.DownloadRegistrar;
import org.pikater.web.servlets.download.IDownloadResource;
import org.pikater.web.vaadin.gui.server.components.anchor.Anchor;
import org.pikater.web.vaadin.gui.server.components.console.SimpleConsoleComponent;
import org.pikater.web.vaadin.gui.server.components.popups.MyDialogs;
import org.pikater.web.vaadin.gui.server.components.popups.MyDialogs.IProgressDialogTaskContext;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.IContentComponent;
import org.quartz.JobKey;

import com.google.common.net.MediaType;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.VerticalLayout;

public class TestView extends VerticalLayout implements IContentComponent
{
	private static final long serialVersionUID = -6639847503623900929L;

	public TestView()
	{
		super();
		setSizeFull();
		
		// testJSCH();
		// testAnchor();
		testVisualization();
	}

	@Override
	public void enter(ViewChangeEvent event)
	{
	}

	@Override
	public boolean hasUnsavedProgress()
	{
		return false;
	}

	@Override
	public String getCloseDialogMessage()
	{
		return null;
	}
	
	// -------------------------------------------------------------------
	// TEST GUI INITIALIZATONS
	
	public void testVisualization()
	{
		final JPADataSetLO iris = new ResultFormatter<JPADataSetLO>(DAOs.dataSetDAO.getByDescription("iris")).getSingleResultWithNull();
		
		addComponent(new Button("Start", new Button.ClickListener()
		{
			private static final long serialVersionUID = 4769508433785670365L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				// create an image file to which the visualization module will generate the image
				final File tmpFile = IOUtils.createTemporaryFile("visualization-generated", ".png");
				
				// then display progress dialog
				MyDialogs.progressDialog("Vizualization progress...", new MyDialogs.IProgressDialogHandler()
				{
					private JobKey jobKey = null;
					
					@Override
					public void startTask(IProgressDialogTaskContext context) throws Throwable
					{
						// start the task and bind it with the progress dialog
						jobKey = PikaterJobScheduler.getJobScheduler().defineJob(MatrixPNGGeneratorJob.class, new Object[]
						{
							context,
							iris,
							tmpFile.getAbsolutePath()
						});
					}
					
					@Override
					public void abortTask()
					{
						if(jobKey == null)
						{
							PikaterLogger.logThrowable("", new NullPointerException("Can not abort a task that has not started."));
						}
						else
						{
							try
							{
								PikaterJobScheduler.getJobScheduler().interruptJob(jobKey);
							}
							catch (Throwable t)
							{
								PikaterLogger.logThrowable(String.format("Could not interrupt job: '%s'. What now?", jobKey.toString()), t);
							}
						}
					}
					
					@Override
					public void onTaskFinish()
					{
						String generatedImageURL = DownloadRegistrar.issueDownloadURL(new IDownloadResource()
						{
							@Override
							public InputStream getStream() throws Throwable
							{
								return new FileInputStream(tmpFile);
							}
							
							@Override
							public long getSize()
							{
								return tmpFile.length();
							}
							
							@Override
							public String getMimeType()
							{
								return MediaType.PNG.toString();
							}
							
							@Override
							public String getFilename()
							{
								return tmpFile.getName();
							}
						});
						
						// TODO: call visualization UI with the proper url arguments
					}
				});
			}
		}));
	}
	
	public void testAnchor()
	{
		addComponent(new Anchor("test1", new ClickListener() 
		{
			private static final long serialVersionUID = -485684054380631770L;

			@Override
			public void click(com.vaadin.event.MouseEvents.ClickEvent event)
			{
				MyNotifications.showInfo("Yay", "yay");
			}
		}));
		addComponent(new Anchor("test2", "function() { alert(42); }"));
	}
	
	public void testJSCH()
	{
		addComponent(new SimpleConsoleComponent(new SSHSession(
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
		));
	}
	
	// -------------------------------------------------------------------
	// TIPS AND TRICKS
	
	protected static void callJSFunctionOnTheClient()
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
	
	protected static void callJSNIAddedFunctionOnTheClient()
	{
		JavaScript.getCurrent().execute("window.ns_pikater.setAppMode(\"DEBUG\");"); // calls the function on the client
	}
	
	protected static void backgroundThreadDoingSomeJobPeriodically() // quartz could be used too...
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
	
	protected static void redirectToCustomServletWithoutChangingURL()
	{
		new Button.ClickListener()
		{
			private static final long serialVersionUID = -3016596327398677231L;

			@Override
			public void buttonClick(ClickEvent event)
			{
				// getUI().getPage().setLocation("/NewPikater/static/hokus_pokus.txt"); // ./WEB-INF/static/hokus_pokus.txt
				// getUI().getPage().setLocation("/Pikater/staticDownload"); // servlet mapped to /staticDownload
			}
		};
	}
}