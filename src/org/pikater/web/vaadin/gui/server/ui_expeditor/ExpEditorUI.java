package org.pikater.web.vaadin.gui.server.ui_expeditor;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.newOption.NewOptionList;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.values.DoubleValue;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkRange;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkSet;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;
import org.pikater.shared.experiment.webformat.BoxType;
import org.pikater.web.config.AgentInfoCollection;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.config.ServerConfigurationInterface.ServerConfItem;
import org.pikater.web.vaadin.CustomConfiguredUI;
import org.pikater.web.vaadin.ManageAuth;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.ExpEditor;

import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.communication.PushMode;

@Theme("pikater")
@Push(value = PushMode.AUTOMATIC)
@Title("Experiments")
public class ExpEditorUI extends CustomConfiguredUI
{
	private static final long serialVersionUID = -797960197800185978L;
	
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
					case CHOOSE:
						name = "Klobása";
						break;
					case SEARCH:
						name = "Cibule";
						break;
					case MULTIBOX:
						name = "Protlak";
						break;
					default:
						break;
					
				}
				agentInfo.setName(name);
				
				NewOptionList options = new NewOptionList();
				options.addOption(new NewOption("IntRange", new IntegerValue(5), new RangeRestriction(new IntegerValue(2), new IntegerValue(10))));
				options.addOption(new NewOption("IntSet", new IntegerValue(5), new SetRestriction(new ArrayList<IValueData>(Arrays.asList(
						new IntegerValue(2),
						new IntegerValue(3),
						new IntegerValue(5),
						new IntegerValue(10))))
				));
				options.addOption(new NewOption("Double", new DoubleValue(1)));
				options.addOption(new NewOption("Boolean", new BooleanValue(true)));
				options.addOption(new NewOption("Float", new FloatValue(1)));
				options.addOption(new NewOption("QuestionMarkRange", new QuestionMarkRange(
						new IntegerValue(5), new IntegerValue(10), 3)));
				options.addOption(new NewOption("QuestionMarkSet", new QuestionMarkSet(new ArrayList<IValueData>(Arrays.asList(
						new IntegerValue(5), new IntegerValue(10))), 3)));
				
				agentInfo.setOptions(options);
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
			if(ManageAuth.isUserAuthenticated(VaadinSession.getCurrent()))
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
		ExpEditor editor = new ExpEditor(isDebugModeActive());
		// editor.loadExperimentIntoNewTab("test experiment", UniversalComputationDescription.getDummy());
		setContent(editor);
	}
}