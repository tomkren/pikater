package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.util.ResultFormatter;
import org.pikater.shared.util.Tuple;
import org.pikater.web.ssh.SSHSession;
import org.pikater.web.ssh.SSHSession.ISSHSessionNotificationHandler;
import org.pikater.web.vaadin.gui.server.components.anchor.Anchor;
import org.pikater.web.vaadin.gui.server.components.console.SimpleConsoleComponent;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogResultHandler;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogTaskResult;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.IContentComponent;
import org.pikater.web.vaadin.gui.server.ui_visualization.VisualizationUI.DSVisTwoUIArgs;
import org.pikater.web.visualisation.DatasetVisualizationEntryPoint;
import org.pikater.web.visualisation.definition.AttrComparisons;
import org.pikater.web.visualisation.definition.AttrMapping;
import org.pikater.web.visualisation.definition.result.DSVisTwoResult;

import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
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
		// testMatrixViewer();
		testDatasetComparison();
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
	
	public void testDatasetComparison()
	{
		// determine basic information & references
		final JPADataSetLO iris = new ResultFormatter<JPADataSetLO>(DAOs.dataSetDAO.getByDescription("iris")).getSingleResultWithNull();
		final JPADataSetLO weather = new ResultFormatter<JPADataSetLO>(DAOs.dataSetDAO.getByDescription("weather")).getSingleResultWithNull();
		final JPAAttributeMetaData iris_attrTarget = iris.getAttributeMetaData().get(iris.getNumberOfAttributes() - 1);
		final JPAAttributeMetaData weather_attrTarget = weather.getAttributeMetaData().get(iris.getNumberOfAttributes() - 1);
		
		// determine attribute sets to (potentially) compare
		Set<AttrMapping> irisAttrSet = new HashSet<AttrMapping>();
		for(JPAAttributeMetaData attrX : iris.getAttributeMetaData())
		{
			for(JPAAttributeMetaData attrY : iris.getAttributeMetaData())
			{
				if(attrX.isVisuallyCompatible(attrY))
				{
					irisAttrSet.add(new AttrMapping(attrX, attrY, iris_attrTarget));
				}
			}
		}
		Set<AttrMapping> weatherAttrSet = new HashSet<AttrMapping>();
		for(JPAAttributeMetaData attrX : weather.getAttributeMetaData())
		{
			for(JPAAttributeMetaData attrY : weather.getAttributeMetaData())
			{
				if(attrX.isVisuallyCompatible(attrY))
				{
					weatherAttrSet.add(new AttrMapping(attrX, attrY, weather_attrTarget));
				}
			}
		}
		
		// data source
		final AttrComparisons attrComparisons = new AttrComparisons();
		for(AttrMapping mapping1 : irisAttrSet)
		{
			for(AttrMapping mapping2 : weatherAttrSet)
			{
				if(mapping1.getAttrX().isVisuallyCompatible(mapping2.getAttrX()))
				{
					attrComparisons.add(new Tuple<AttrMapping, AttrMapping>(mapping1, mapping2));
				}
			}
		}
		
		// show progress dialog
		ProgressDialog.show("Vizualization progress...", new ProgressDialog.IProgressDialogTaskHandler()
		{
			private DatasetVisualizationEntryPoint underlyingTask;

			@Override
			public void startTask(IProgressDialogResultHandler contextForTask) throws Throwable
			{
				// start the task and bind it with the progress dialog
				underlyingTask = new DatasetVisualizationEntryPoint(contextForTask);
				underlyingTask.visualizeDatasetComparison(iris, weather, attrComparisons);
			}

			@Override
			public void abortTask()
			{
				underlyingTask.abortVisualization();
			}

			@Override
			public void onTaskFinish(IProgressDialogTaskResult result)
			{
				// and when the task finishes, construct the UI
				DSVisTwoUIArgs uiArgs = new DSVisTwoUIArgs(iris, weather, (DSVisTwoResult) result); 
				Page.getCurrent().setLocation(uiArgs.toRedirectURL());
			}
		});
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