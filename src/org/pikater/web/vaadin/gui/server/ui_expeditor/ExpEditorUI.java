package org.pikater.web.vaadin.gui.server.ui_expeditor;

import javax.servlet.annotation.WebServlet;

import org.pikater.shared.experiment.universalformat.UniversalGui;
import org.pikater.shared.experiment.webformat.BoxInfo;
import org.pikater.shared.experiment.webformat.BoxInfoCollection;
import org.pikater.shared.experiment.webformat.BoxType;
import org.pikater.shared.experiment.webformat.Experiment;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.config.ServerConfigurationInterface.ServerConfItem;
import org.pikater.web.vaadin.CustomConfiguredUI;
import org.pikater.web.vaadin.CustomConfiguredUIServlet;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.ExpEditor;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.communication.PushMode;

@Theme("pikater")
@Push(value = PushMode.MANUAL)
@Title("Experiments")
public class ExpEditorUI extends CustomConfiguredUI
{
	private static final long serialVersionUID = -797960197800185978L;
	
	@WebServlet(value = {"/experimentEditor", "/experimentEditor/*", "/VAADIN/*"}, asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = ExpEditorUI.class, widgetset = "org.pikater.web.vaadin.gui.PikaterWidgetset")
	public static class ExpEditorUIServlet extends CustomConfiguredUIServlet
	{
		private static final long serialVersionUID = 2623580800316091787L;
		
		/**
		 * Always keep this in sync with what's in the WebServlet annotation under the "value" parameter.
		 */
		public static final String mainURLPattern = "/experimentEditor";
	}

	@Override
	protected void init(VaadinRequest request)
	{
		/*
		 * Don't forget to call this. Also note, that you shouldn't update the UI until
		 * you receive the {@link #displayChildContent()} call.
		 */
		super.init(request);
		
		/*
		 * TODO: eventually delete the following:
		 */
		
		BoxInfo boxInfo1 = new BoxInfo("Bla1", "bla", "Bla1", BoxType.INPUT, "", "");
		BoxInfo boxInfo2 = new BoxInfo("Bla2", "bla", "Bla2", BoxType.RECOMMENDER, "", "");
		BoxInfo boxInfo3 = new BoxInfo("Bla3", "bla", "Bla3", BoxType.VISUALIZER, "", "");
		
		BoxInfoCollection boxDefinitions = new BoxInfoCollection();
		boxDefinitions.addDefinition(boxInfo1);
		boxDefinitions.addDefinition(boxInfo2);
		boxDefinitions.addDefinition(boxInfo3);
		ServerConfigurationInterface.setField(ServerConfItem.BOX_DEFINITIONS, boxDefinitions);
	}
	
	@Override
	protected void displayChildContent()
	{
		/*
		 * First check whether launched pikater has already gathered and sent over information
		 * of all available experiment related agents.
		 */
		
		if(ServerConfigurationInterface.getLatestBoxDefinitions() == null)
		{
			// if not, let the user select an option to wait until box definitions are available 
			
			// TODO:
		}
		else
		{
			// simply create a new empty editor and let the user handle the rest
			ExpEditor editor = new ExpEditor(!getSession().getConfiguration().isProductionMode());
			setContent(editor);
		}
	}
	
	protected static void loadSampleExperiment(ExpEditor editor)
	{
		UniversalGui guiInfo1 = new UniversalGui(10, 10);
		UniversalGui guiInfo2 = new UniversalGui(500, 10);
		UniversalGui guiInfo3 = new UniversalGui(400, 300);
		
		Experiment newExperiment = new Experiment();
		Integer b1 = newExperiment.addLeafBoxAndReturnID(guiInfo1, new BoxInfo("Bla1", "bla", "Bla1", BoxType.INPUT, "", ""));
		Integer b2 = newExperiment.addLeafBoxAndReturnID(guiInfo2, new BoxInfo("Bla2", "bla", "Bla2", BoxType.RECOMMENDER, "", ""));
		newExperiment.addLeafBoxAndReturnID(guiInfo3, new BoxInfo("Bla3", "bla", "Bla3", BoxType.VISUALIZER, "", ""));
		newExperiment.connect(b1, b2);
		
		editor.loadExperimentIntoNewTab("test experiment", newExperiment);
	}
}
