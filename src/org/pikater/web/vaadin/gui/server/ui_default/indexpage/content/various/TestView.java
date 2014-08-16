package org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.various;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.util.ResultFormatter;
import org.pikater.web.ssh.SSHSession;
import org.pikater.web.ssh.SSHSession.ISSHSessionNotificationHandler;
import org.pikater.web.vaadin.gui.server.components.anchor.Anchor;
import org.pikater.web.vaadin.gui.server.components.console.SimpleConsoleComponent;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogResultHandler;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogTaskResult;
import org.pikater.web.vaadin.gui.server.layouts.matrixlayout.IMatrixDataSource;
import org.pikater.web.vaadin.gui.server.layouts.matrixlayout.MatrixLayout;
import org.pikater.web.vaadin.gui.server.ui_default.indexpage.content.ContentProvider.IContentComponent;
import org.pikater.web.vaadin.gui.server.ui_visualization.VisualizationUI.DSVisOneUIArgs;
import org.pikater.web.visualisation.DatasetVisualizationEntryPoint;
import org.pikater.web.visualisation.definition.result.DSVisOneResult;

import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.Label;
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
		// testVisualization();
		// testMatrixViewer();
		testSingleDatasetVisualization();
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
	
	public void testSingleDatasetVisualization()
	{
		// determine arguments
		final JPADataSetLO iris = new ResultFormatter<JPADataSetLO>(DAOs.dataSetDAO.getByDescription("iris")).getSingleResultWithNull();
		final List<String> attrNames = new ArrayList<String>(); 
		for(JPAAttributeMetaData attrMetaData : iris.getAttributeMetaData())
		{
			attrNames.add(attrMetaData.getName());
		}
		final String attrTarget = iris.getAttributeMetaData().get(iris.getNumberOfAttributes() - 1).getName();
		
		// show progress dialog
		ProgressDialog.show("Vizualization progress...", new ProgressDialog.IProgressDialogTaskHandler()
		{
			private DatasetVisualizationEntryPoint underlyingTask;

			@Override
			public void startTask(IProgressDialogResultHandler contextForTask) throws Throwable
			{
				// start the task and bind it with the progress dialog
				underlyingTask = new DatasetVisualizationEntryPoint(contextForTask);
				underlyingTask.visualizeDataset(
						iris,
						attrNames.toArray(new String[0]),
						attrTarget
				);
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
				DSVisOneUIArgs uiArgs = new DSVisOneUIArgs((DSVisOneResult) result); 
				Page.getCurrent().setLocation(uiArgs.toRedirectURL());
			}
		});
	}
	
	public void testMatrixViewer()
	{
		MatrixLayout<String> matrix = new MatrixLayout<String>(new IMatrixDataSource<String, Component>()
		{
			@Override
			public Collection<String> getLeftIndexSet()
			{
				return Arrays.asList(
						"firstIndex1",
						"firstIndex2",
						"firstIndex3",
						"firstIndex4",
						"firstIndex5",
						"firstIndex6",
						"firstIndex7",
						"firstIndex8",
						"firstIndex9",
						"firstIndex10",
						"firstIndex11",
						"firstIndex12",
						"firstIndex13",
						"firstIndex14",
						"firstIndex15",
						"firstIndex16",
						"firstIndex17",
						"firstIndex18",
						"firstIndex19",
						"firstIndex20"
				);
			}

			@Override
			public Collection<String> getTopIndexSet()
			{
				return Arrays.asList( "secondIndex1", "secondIndex2" );
			}

			@Override
			public Component getElement(String index1, String index2)
			{
				return new Label(String.format("[%s; %s]", index1, index2));
			}
		}, null);
		matrix.setWidth("600px");
		matrix.setHeight("600px");
		addComponent(matrix);
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