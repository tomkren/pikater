package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.kineticcomponent;

import java.util.HashMap;
import java.util.Map;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.shared.XStreamHelper;
import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;
import org.pikater.shared.experiment.universalformat.UniversalConnector;
import org.pikater.shared.experiment.universalformat.UniversalElement;
import org.pikater.shared.experiment.webformat.BoxInfo;
import org.pikater.shared.experiment.webformat.BoxType;
import org.pikater.shared.experiment.webformat.ExperimentGraph;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.web.config.AgentInfoCollection;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentClientRpc;
import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentServerRpc;
import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentState;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.CustomTabSheetTabComponent;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.ExpEditor;
import org.pikater.web.vaadin.gui.shared.KineticComponentClickMode;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractComponent;

@JavaScript(value = "kinetic-v4.7.3-dev.js")
public class KineticComponent extends AbstractComponent
{
	private static final long serialVersionUID = -539901377528727478L;
	
	//---------------------------------------------------------------
	// GUI RELATED FIELDS
	
	/**
	 * Constant reference to the parent editor component.
	 */
	private final ExpEditor parentEditor;
	
	/**
	 * Reference to the experiment editor tab linked to this content component.
	 */
	private CustomTabSheetTabComponent parentTab;
	
	//---------------------------------------------------------------
	// PROGRAMMATIC FIELDS
	
	private final KineticComponentServerRpc serverRPC;
	
	/*
	 * Dynamic information from the client side - absolute left corner position of the Kinetic stage.
	 */
	private int absoluteLeft;
	private int absoluteTop;
	
	/**
	 * 
	 */
	private final Map<String, AgentInfo> boxIDToAgentInfo;
	
	//---------------------------------------------------------------
	// CONSTRUCTOR
	
	/*
	 * TODO: 
	 * - createBox must create a mapping
	 * - loading experiments has to create mappings 
	 */
	
	public KineticComponent(ExpEditor parentEditor)
	{
		super();
		setSizeFull();
		
		/*
		 * Init.
		 */
		
		this.parentEditor = parentEditor;
		
		this.absoluteLeft = 0;
		this.absoluteTop = 0;
		
		this.boxIDToAgentInfo = new HashMap<String, AgentInfo>();
		
		/*
		 * Register actions to do on client commands.
		 */
		
		this.serverRPC = new KineticComponentServerRpc()
		{
			private static final long serialVersionUID = -2769231541745495584L;

			@Override
			public void command_setExperimentModified(boolean modified)
			{
				getState().serverThinksThatSchemaIsModified = modified;
				parentTab.setTabContentModified(modified);
			}

			@Override
			public void command_onLoadCallback(int absoluteX, int absoluteY)
			{
				KineticComponent.this.absoluteLeft = absoluteX;
				KineticComponent.this.absoluteTop = absoluteY;
			}
			
			@Override
			public void command_alterClickMode(KineticComponentClickMode newClickMode)
			{
				getState().clickMode = newClickMode;
				KineticComponent.this.parentEditor.getToolbar().onClickModeAlteredOnClient(newClickMode);
			}

			@Override
			public void command_openOptionsManager(Integer[] selectedBoxesAgentIDs)
			{
				// TODO Auto-generated method stub
			}

			@Override
			public void response_reloadVisualStyle()
			{
				// TODO Auto-generated method stub
			}

			@Override
			public void response_sendExperimentToSave(ExperimentGraph experiment)
			{
				// TODO Auto-generated method stub
			}
		};
		registerRpc(this.serverRPC);
	}
	
	//---------------------------------------------------------------
	// INHERITED INTERFACE
	
	@Override
	public KineticComponentState getState()
	{
		return (KineticComponentState) super.getState();
	}
	
	//---------------------------------------------------------------
	// CLIENT RPC RELATED INTERFACE
	
	public void command_createBox(AgentInfo info, int absX, int absY)
	{
		getClientRPC().command_createBox(getBoxInfo(info, absX, absY));
	}
	
	public void reloadVisualStyle()
	{
		getClientRPC().request_reloadVisualStyle();
	}
	
	public void importExperiment(UniversalComputationDescription uniFormat)
	{
		try
		{
			ExperimentGraph webFormat = uniToWeb(uniFormat, ServerConfigurationInterface.getKnownAgents());
			
			// if no conversion problems:
			resetEnvironment();
			getClientRPC().command_receiveExperimentToLoad(webFormat);
		}
		catch (ConversionException e)
		{
			PikaterLogger.logThrowable("", e);
			MyNotifications.showError(null, "Could not import experiment. Contact the administrators.");
		}
	}
	
	public UniversalComputationDescription exportExperiment() throws ConversionException
	{
		// TODO: send command to the client and wait for an answer...omg
		return null;
	}
	
	//---------------------------------------------------------------
	// MISCELLANEOUS PUBLIC INTERFACE
	
	public void setParentTab(CustomTabSheetTabComponent parentTab)
	{
		this.parentTab = parentTab;
	}
	
	public boolean isContentModified()
	{
		return getState().serverThinksThatSchemaIsModified;
	}
	
	//---------------------------------------------------------------
	// MISCELLANEOUS PRIVATE INTERFACE
	
	private KineticComponentClientRpc getClientRPC()
	{
		return getRpcProxy(KineticComponentClientRpc.class);
	}
	
	private BoxInfo getBoxInfo(AgentInfo info, int absX, int absY)
	{
		/*
		return new BoxInfo(
				boxID,
				agentInfoID,
				BoxType.fromAgentInfo(info),
				info.getName(),
				absX - absoluteLeft,
				absY - absoluteTop
		);
		*/
		return null;
	}
	
	private void resetEnvironment()
	{
		getClientRPC().command_resetKineticEnvironment();
		serverRPC.command_setExperimentModified(false);
		boxIDToAgentInfo.clear();
	}
	
	//---------------------------------------------------------------
	// FORMAT CONVERSIONS
	
	private UniversalComputationDescription webToUni(ExperimentGraph webFormat) throws ConversionException
	{
		// TODO:
		return null;
	}
	
	private ExperimentGraph uniToWeb(UniversalComputationDescription uniFormat, AgentInfoCollection agentInfoProvider) throws ConversionException
	{
		// first some checks
		if(uniFormat == null)
		{
			throw new ConversionException(new NullPointerException("The argument universal format is null."));
		}
		else if(agentInfoProvider == null)
		{
			throw new ConversionException(new NullPointerException("The argument box info provider is null."));
		}
		
		// and then onto the conversion
		if(uniFormat.isGUICompatible())
		{
			ExperimentGraph webFormat = new ExperimentGraph();

			// first convert all boxes
			Map<UniversalElement, String> uniBoxToWebBoxID = new HashMap<UniversalElement, String>();
			for(UniversalElement element : uniFormat.getAllElements())
			{
				AgentInfo agentInfo =  agentInfoProvider.getByOntologyClass(element.getOntologyInfo().getType());
				if(agentInfo == null)
				{
					throw new ConversionException(new IllegalStateException(String.format(
							"No agent info instance was found for ontology '%s'.", element.getOntologyInfo().getType().getName())));
				}
				else
				{
					BoxInfo info = getBoxInfo(agentInfo, element.getGUIInfo().x, element.getGUIInfo().y);
					String convertedBoxID = webFormat.addLeafBoxAndReturnID(info);
					uniBoxToWebBoxID.put(element, convertedBoxID);
				}
			}
			
			// then convert all edges
			for(UniversalElement element : uniFormat.getAllElements())
			{
				for(UniversalConnector edge : element.getOntologyInfo().getInputSlots())
				{
					webFormat.connect(
							uniBoxToWebBoxID.get(edge.getFromElement()),
							uniBoxToWebBoxID.get(element)
					);
				}
			}
			
			// TODO: wrapper boxes, options & stuff
			
			return webFormat;
		}
		else
		{
			String xml = XStreamHelper.serializeToXML(uniFormat, XStreamHelper.getSerializerWithProcessedAnnotations(UniversalComputationDescription.class));
			throw new ConversionException(new IllegalArgumentException(String.format(
					"The universal format below is not fully compatible with the GUI (web) format.\n%s", xml)));
		}
	}
}