package org.pikater.web.vaadin.gui.server.webui.indexpage.content;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.config.ServerConfigurationInterface.ServerConfItem;
import org.pikater.web.vaadin.gui.server.components.SimpleConsoleComponent;
import org.pikater.web.vaadin.gui.server.components.upload.IUploadedFileHandler;
import org.pikater.web.vaadin.gui.server.components.upload.UserUploadManager;
import org.pikater.web.vaadin.gui.server.webui.experimenteditor.ExperimentEditor;

import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Button.ClickEvent;

public class TestContent
{
	// -------------------------------------------------------------------
	// TEST GUI INITIALIZATONS
		
	public static AbstractComponent testMultiFileUpload()
	{
		return new UserUploadManager().getNewComponent(
				Arrays.asList(HttpContentType.APPLICATION_JAR),
				new IUploadedFileHandler()
				{
					@Override
					public void handleFile(InputStream streamToLocalFile, String fileName, String mimeType, long sizeInBytes)
					{
						// TODO: upload the file to DB
					}
				}
		);
	}

	public static AbstractComponent testEditor(boolean isDebugMode)
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
		
		ExperimentEditor editor = new ExperimentEditor(isDebugMode);
		editor.loadExperimentIntoNewTab("test experiment", newExperiment); 
		return editor;
	}
	
	public static AbstractComponent testJSCH()
	{
		return new SimpleConsoleComponent(new SSHSession(
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
	
	protected static void redirectToCustomServlet()
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
