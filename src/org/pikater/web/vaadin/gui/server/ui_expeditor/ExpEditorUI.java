package org.pikater.web.vaadin.gui.server.ui_expeditor;

import javax.servlet.annotation.WebServlet;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;
import org.pikater.shared.experiment.webformat.BoxType;
import org.pikater.web.config.AgentInfoCollection;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.config.ServerConfigurationInterface.ServerConfItem;
import org.pikater.web.vaadin.CustomConfiguredUI;
import org.pikater.web.vaadin.CustomConfiguredUIServlet;
import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.ExpEditor;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.communication.PushMode;

@Theme("pikater")
@Push(value = PushMode.AUTOMATIC)
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
		 * Don't forget to call this.
		 * IMPORTANT:
		 * 1) You shouldn't update the UI in this method. You only provide the content component
		 * when you're asked to in the {@link #displayChildContent()} method.
		 * 2) When {@link #displayChildContent()} is called, this method is still not finished.
		 * You shouldn't have any initializing code after the super.init() all.
		 */
		super.init(request);
	}
	
	@Override
	protected void displayChildContent()
	{
		/*
		 * TODO: eventually delete the following:
		 */
		
		AgentInfoCollection agentInfos = new AgentInfoCollection();
		for(BoxType type : BoxType.values())
		{
			if(type.toOntology() != null)
			{
				AgentInfo agentInfo = new AgentInfo();
				agentInfo.setOntologyClassName(type.toOntology());
				agentInfo.setDescription(String.format("Some kind of a '%s' box.", type.name()));
				
				String name = null;
				switch(type)
				{
					case COMPUTE:
						name = "Vepřová kýta";
						break;
					case INPUT:
						name = "Brambory";
						break;
					case METHOD:
						name = "Chleba";
						break;
					case CHOOSE:
						name = "Klobása";
						break;
					case SEARCH:
						name = "Cibule";
						break;
					case DISPLAY:
						name = "Bobkovej list";
						break;
					case MULTIBOX:
						name = "Protlak";
						break;
					default:
						break;
					
				}
				agentInfo.setName(name);
				
				agentInfos.addDefinition(agentInfo);
			}
		}
		ServerConfigurationInterface.setField(ServerConfItem.BOX_DEFINITIONS, agentInfos);
		
		/*
		 * First check whether launched pikater has already gathered and sent over information
		 * of all available experiment related agents.
		 */
		
		if(ServerConfigurationInterface.getKnownAgents() == null)
		{
			// if not, let the user select an option to wait until box definitions are available 
			
			// TODO:
		}
		else
		{
			/*
			 * Display editor if authenticated or make the user authenticate first and then display it.
			 */
			if(ManageAuth.isUserAuthenticated(getSession()))
			{
				displayExperimentEditor();
			}
			else
			{
				forceUserToAuthenticate(new CustomConfiguredUI.IAuthenticationSuccessful()
				{
					@Override
					public void onSuccessfulAuth()
					{
						displayExperimentEditor();
					}
				});
			}
		}
	}
	
	private void displayExperimentEditor()
	{
		// TODO: 
		
		// simply create a new empty editor and let the user handle the rest
		ExpEditor editor = new ExpEditor(!getSession().getConfiguration().isProductionMode());
		setContent(editor);
	}
	
	protected static void loadSampleExperiment(ExpEditor editor)
	{
		editor.loadExperimentIntoNewTab("test experiment", UniversalComputationDescription.getDummy());
	}
}