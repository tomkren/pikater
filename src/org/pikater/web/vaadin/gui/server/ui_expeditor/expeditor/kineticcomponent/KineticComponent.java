package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.kineticcomponent;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;
import org.pikater.shared.experiment.universalformat.UniversalConnector;
import org.pikater.shared.experiment.universalformat.UniversalElement;
import org.pikater.shared.experiment.universalformat.UniversalGui;
import org.pikater.shared.experiment.universalformat.UniversalOntology;
import org.pikater.shared.experiment.webformat.BoxInfo;
import org.pikater.shared.experiment.webformat.BoxType;
import org.pikater.shared.experiment.webformat.ExperimentGraph;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.shared.util.SimpleIDGenerator;
import org.pikater.web.config.AgentInfoCollection;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentClientRpc;
import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentServerRpc;
import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentState;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.CustomTabSheetTabComponent;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.ExpEditor;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.ExpEditor.ExpEditorToolbox;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.toolboxes.BoxOptionsToolbox;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.ClickMode;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.graphitems.AbstractGraphItemShared.RegistrationOperation;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.graphitems.BoxGraphItemShared;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.graphitems.EdgeGraphItemShared;

import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.AbstractComponent;

@JavaScript(value = "kinetic-v4.7.3-dev.js")
public class KineticComponent extends AbstractComponent
{
	private static final long serialVersionUID = -539901377528727478L;
	
	//---------------------------------------------------------------
	// GUI COMPONENTS TO KEEP TRACK OF
	
	/**
	 * Constant reference to the parent editor component.
	 */
	private final ExpEditor parentEditor;
	
	/**
	 * Reference to the tab containing this component.
	 */
	private CustomTabSheetTabComponent parentTab;
	
	//---------------------------------------------------------------
	// EXPERIMENT RELATED FIELDS
	
	/**
	 * ID generator for boxes.
	 * This generator never falls back and thus it is ensured that no two
	 * boxes will have the same ID.
	 */
	private final SimpleIDGenerator boxIDGenerator;
	
	/**
	 * The dynamic mapping between boxes and agent information. Only a portion of agent information
	 * is sent to the client (+ some added value), wrapped in BoxInfo instance.
	 * This field is the base for all format conversions and some other commands.
	 */
	private final Map<String, AgentInfo> boxIDToAgentInfo;
	
	/**
	 * Reference to experiment last used in the {@link #importExperiment(UniversalComputationDescription)}
	 * method.
	 */
	private Integer previouslyLoadedExperimentID;
	
	/**
	 * Callback for exported experiments.
	 * @see {@link IOnExperimentReceivedFromClient}   
	 */
	private IOnExperimentReceivedFromClient exportedExperimentCallback;
	
	//---------------------------------------------------------------
	// OTHER PROGRAMMATIC FIELDS
	
	private boolean bindOptionsManagerWithSelectionChanges;
	
	/*
	 * Dynamic information from the client side - absolute left corner position of the Kinetic stage.
	 */
	private int absoluteLeft;
	private int absoluteTop;
	
	//---------------------------------------------------------------
	// CONSTRUCTOR
	
	public KineticComponent(final ExpEditor parentEditor)
	{
		super();
		setSizeFull();
		
		/*
		 * Init.
		 */
		
		this.parentEditor = parentEditor;
		this.parentTab = null;
		
		this.boxIDGenerator = new SimpleIDGenerator();
		this.boxIDToAgentInfo = new HashMap<String, AgentInfo>();
		this.previouslyLoadedExperimentID = null;
		this.exportedExperimentCallback = null;
		
		this.bindOptionsManagerWithSelectionChanges = areSelectionChangesBoundWithOptionsManagerByDefault();
		this.absoluteLeft = 0;
		this.absoluteTop = 0;
		
		/*
		 * Register handlers for client commands.
		 */
		
		registerRpc(new KineticComponentServerRpc()
		{
			private static final long serialVersionUID = -2769231541745495584L;
			
			/**
			 * Currently unsupported.
			 */
			@Deprecated
			@Override
			public void command_setExperimentModified(boolean modified)
			{
				/*
				getState().serverThinksThatSchemaIsModified = modified;
				parentTab.setTabContentModified(modified);
				parentEditor.getExtension().setKineticContentModified(KineticComponent.this, modified);
				
				MyNotifications.showInfo("Modification note", String.valueOf(modified));
				*/
			}

			@Override
			public void command_onLoadCallback(int absoluteX, int absoluteY)
			{
				KineticComponent.this.absoluteLeft = absoluteX;
				KineticComponent.this.absoluteTop = absoluteY;
			}
			
			@Override
			public void command_alterClickMode(ClickMode newClickMode)
			{
				getState().clickMode = newClickMode;
				KineticComponent.this.parentEditor.getToolbar().onClickModeAlteredOnClient(newClickMode);
			}
			
			@Override
			public void command_boxSetChange(RegistrationOperation opKind, BoxGraphItemShared[] boxes)
			{
				// TODO Auto-generated method stub
				// MyNotifications.showInfo(null, "Box set changed");
			}

			@Override
			public void command_edgeSetChange(RegistrationOperation opKind, EdgeGraphItemShared[] edges)
			{
				// TODO Auto-generated method stub
				// MyNotifications.showInfo(null, "Edge set changed");
			}
			
			@Override
			public void command_selectionChange(String[] selectedBoxesIDs)
			{
				if(bindOptionsManagerWithSelectionChanges)
				{
					// convert to agent information array
					AgentInfo[] selectedBoxesInformation = new AgentInfo[selectedBoxesIDs.length];
					for(int i = 0; i < selectedBoxesIDs.length; i++)
					{
						if(boxIDToAgentInfo.containsKey(selectedBoxesIDs[i]))
						{
							selectedBoxesInformation[i] = boxIDToAgentInfo.get(selectedBoxesIDs[i]);
						}
						else
						{
							throw new IllegalStateException(String.format("Kinetic state out of sync. "
									+ "No agent info was found for box ID '%s'.", selectedBoxesIDs[i]));
						}
					}
					
					// get the toolbox
					BoxOptionsToolbox toolbox = (BoxOptionsToolbox) parentEditor.getToolbox(ExpEditorToolbox.METHOD_OPTION_MANAGER);
					
					// set the new content to it and display the toolbox if needed
					if(toolbox.setContentFromSelectedBoxes(selectedBoxesInformation))
					{
						parentEditor.openToolbox(ExpEditorToolbox.METHOD_OPTION_MANAGER);
					}
				}
			}

			@Override
			public void response_sendExperimentToSave(ExperimentGraph experiment)
			{
				UniversalComputationDescription result = null;
				try
				{
					result = webToUni(experiment);
				}
				catch (ConversionException e)
				{
					PikaterLogger.logThrowable("Could not convert to universal format because of the error below.", e);
				}
				
				try
				{
					if(result != null)
					{
						exportedExperimentCallback.handleExperiment(result, new IOnExperimentSaved()
						{
							@Override
							public void experimentSaved(JPABatch newExperimentEntity)
							{
								if(newExperimentEntity.getId() == 0)
								{
									throw new IllegalStateException("The given new experiment has not been saved yet.");
								}
								else
								{
									previouslyLoadedExperimentID = newExperimentEntity.getId();
									parentTab.setCaption(newExperimentEntity.getName());
								}
							}
						});
					}
				}
				finally
				{
					exportedExperimentCallback = null;
				}
			}
		});
	}
	
	//---------------------------------------------------------------
	// INHERITED INTERFACE
	
	@Override
	public KineticComponentState getState()
	{
		return (KineticComponentState) super.getState();
	}
	
	//---------------------------------------------------------------
	// EXPERIMENT IMPORT/EXPORT RELATED ROUTINES/TYPES
	
	/**
	 * Used BEFORE saving experiments.</br>
	 * The server has to issue an asynchronous command to the client and wait for it to
	 * send response. The experiment from response is converted into universal format
	 * and passed to the {@link IOnExperimentReceivedFromClient#handleExperiment(UniversalComputationDescription)
	 * handleExperiment(UniversalComputationDescription)} method.
	 */
	public static interface IOnExperimentReceivedFromClient
	{
		/**
		 * Handle the exported experiment in this method.
		 * @param exportedExperiment
		 * @param experimentSavedCallback provide a callback for when the experiment is successfully saved
		 */
		void handleExperiment(UniversalComputationDescription exportedExperiment, IOnExperimentSaved experimentSavedCallback);
	}
	
	/**
	 * Used AFTER saving experiments to keep this component's inner state in sync.
	 */
	public static interface IOnExperimentSaved
	{
		/**
		 * Call this method when your experiment has successfully been saved.
		 * @param newBatchID the ID of the newly saved experiment
		 */
		void experimentSaved(JPABatch newExperimentEntity);
	}
	
	public void importExperiment(JPABatch experiment)
	{
		try
		{
			// first and foremost
			resetEnvironment();
			
			// deserialization from XML should always be ok, as long as we don't change universal format
			UniversalComputationDescription uniFormat = UniversalComputationDescription.fromXML(experiment.getXML());
			
			// however, correct conversion to web format is not a simple matter (see the inside of the method)
			// also updates server-side state
			getClientRPC().command_receiveExperimentToLoad(uniToWeb(uniFormat));
			
			// finish updating server-side state
			previouslyLoadedExperimentID = experiment.getId();
			
			// and some final visual changes
			parentTab.setCaption(experiment.getName());
		}
		catch (ConversionException e)
		{
			PikaterLogger.logThrowable("", e);
			MyNotifications.showError("Could not import experiment", "Please, contact the administrators.");
		}
	}
	
	public synchronized void exportExperiment(IOnExperimentReceivedFromClient callback)
	{
		if(exportedExperimentCallback != null)
		{
			MyNotifications.showWarning("Export ignored", "Another call pending...");
		}
		else
		{
			// register callback
			exportedExperimentCallback = callback;
			
			// send command to the client
			getClientRPC().request_sendExperimentToSave();
		}
	}
	
	//---------------------------------------------------------------
	// MISCELLANEOUS PUBLIC INTERFACE
	
	public void setParentTab(CustomTabSheetTabComponent parentTab)
	{
		this.parentTab = parentTab;
	}
	
	public void createBoxAndSendItToClient(AgentInfo info, int absX, int absY)
	{
		getClientRPC().command_createBox(createBoxAndUpdateState(info, absX - absoluteLeft, absY - absoluteTop));
	}
	
	public void reloadVisualStyle()
	{
		getClientRPC().request_reloadVisualStyle();
	}
	
	public boolean areSelectionChangesBoundWithOptionsManager()
	{
		return bindOptionsManagerWithSelectionChanges;
	}
	
	public static boolean areSelectionChangesBoundWithOptionsManagerByDefault()
	{
		return true;
	}

	public void setBindOptionsManagerWithSelectionChanges(boolean bindOptionsManagerWithSelectionChanges)
	{
		this.bindOptionsManagerWithSelectionChanges = bindOptionsManagerWithSelectionChanges;
	}
	
	public boolean isContentModified()
	{
		return true; // TODO: until problems with the "modified" status are resolved, always return true
		// return getState().serverThinksThatSchemaIsModified;
	}
	
	public Integer getPreviouslyLoadedExperimentID()
	{
		return previouslyLoadedExperimentID;
	}
	
	//---------------------------------------------------------------
	// MISCELLANEOUS PRIVATE INTERFACE
	
	private KineticComponentClientRpc getClientRPC()
	{
		return getRpcProxy(KineticComponentClientRpc.class);
	}
	
	private BoxInfo createBoxAndUpdateState(AgentInfo info, int relX, int relY)
	{
		BoxType type = BoxType.fromAgentInfo(info);
		String newBoxID = String.valueOf(boxIDGenerator.getAndIncrement());
		boxIDToAgentInfo.put(newBoxID, info.clone()); // agent info need to be cloned because options might be changed by user
		return new BoxInfo(
				newBoxID,
				type.name(),
				info.getName(),
				relX,
				relY,
				ExpEditor.getBoxPictureURL(type)
		);
	}
	
	private void resetEnvironment()
	{
		/*
		 * Server side reset could be done in a response call from the client but
		 * then we would have to wait for it before the current thread proceeds.
		 * As such, NEVER reset server-side state from the client :).
		 */
		getClientRPC().command_resetKineticEnvironment(); // client side reset
		boxIDToAgentInfo.clear(); // server side reset
	}
	
	//---------------------------------------------------------------
	// FORMAT CONVERSIONS
	
	/**
	 * Converts web experiment format into universal experiment format.
	 * This conversion is substantially simpler than its counterpart. It should always work.
	 * @param webFormat
	 * @return
	 * @throws ConversionException
	 */
	private UniversalComputationDescription webToUni(ExperimentGraph webFormat) throws ConversionException
	{
		try
		{
			// first some checks
			AgentInfoCollection agentInfoProvider = ServerConfigurationInterface.getKnownAgents();
			if(webFormat == null)
			{
				throw new NullPointerException("The argument web format is null.");
			}
			else if(agentInfoProvider == null)
			{
				throw new NullPointerException("Agent information has not yet been received from pikater.");
			}

			// create the result uni-format experiment
			UniversalComputationDescription result = new UniversalComputationDescription();

			// create uni-format master elements for all boxes
			Map<BoxInfo, UniversalElement> webBoxToUniBox = new HashMap<BoxInfo, UniversalElement>();
			for(BoxInfo webBox : webFormat.leafBoxes.values())
			{
				UniversalElement uniBox = new UniversalElement();
				uniBox.setOntologyInfo(new UniversalOntology()); // needed for real-time edge registration to work (see below)
				webBoxToUniBox.put(webBox, uniBox);
			}

			// traverse all boxes and pass all available/needed information to result uni-format
			for(Entry<String, BoxInfo> entry : webFormat.leafBoxes.entrySet())
			{
				// determine basic information and references
				String webBoxID = entry.getKey();
				BoxInfo webBox = entry.getValue();
				UniversalElement uniBox = webBoxToUniBox.get(webBox);
				AgentInfo agentInfo = boxIDToAgentInfo.get(webBoxID);
				if(agentInfo == null)
				{
					throw new IllegalStateException(String.format(
							"No agent info was found for box '%s@%s'.", webBox.boxTypeName, webBox.displayName));
				}

				// create edge leading from the currently processed box (will be later added to all neighbour uni-boxes)
				UniversalConnector connector = new UniversalConnector();
				connector.setFromElement(uniBox);

				// create and initialize the FIRST of the 2 child objects
				UniversalOntology uniBoxInfo = new UniversalOntology();
				try
				{
					uniBoxInfo.setOntologyClass(Class.forName(agentInfo.getOntologyClassName()));
					uniBoxInfo.setAgentClass(Class.forName(agentInfo.getAgentClassName()));
				}
				catch (ClassNotFoundException e)
				{
					throw new IllegalStateException(String.format(
							"Could not convert '%s' to a class instance. Has it been hardcoded to an agent and renamed? "
							+ "Or is the pikater core running in different version than web?", agentInfo.getOntologyClassName()
							), e
					);
				}
				uniBoxInfo.setOptions(agentInfo.getOptions());
				for(String neighbourWebBoxID : webFormat.edges.get(webBoxID)) // transform edges
				{
					UniversalElement neighbourUniBox = webBoxToUniBox.get(webFormat.leafBoxes.get(neighbourWebBoxID));
					neighbourUniBox.getOntologyInfo().addInputSlot(connector);
				}

				// create and initialize the SECOND of the 2 child objects
				UniversalGui uniBoxPositionInfo = new UniversalGui(webBox.initialX, webBox.initialY);

				// glue it all together and register in the result uni-format
				uniBox.setGUIInfo(uniBoxPositionInfo);
				uniBox.setOntologyInfo(uniBoxInfo);
				result.addElement(uniBox);
			}
			return result;
		}
		catch(Throwable t)
		{
			throw new ConversionException(t);
		}
	}

	/**
	 * Converts universal format experiments into web format experiments that are used
	 * to do the actual loading in the client's kinetic environment.</br> 
	 * This method is very sensitive to changes to (because of serialization to XML
	 * and back):
	 * <ul>
	 * <li> Universal format.
	 * <li> NewOption ontology. 
	 * </ul>
	 * These changes may cause exceptions when trying to convert (previously converted)
	 * experiments back to web format.
	 * @param uniFormat
	 * @return
	 * @throws ConversionException
	 */
	private ExperimentGraph uniToWeb(UniversalComputationDescription uniFormat) throws ConversionException
	{
		try
		{
			// first some checks
			AgentInfoCollection agentInfoProvider = ServerConfigurationInterface.getKnownAgents();
			if(agentInfoProvider == null)
			{
				throw new NullPointerException("Agent information has not yet been received from pikater.");
			}
			else if(uniFormat == null)
			{
				throw new NullPointerException("The argument uni-format is null.");
			}
			else if(!uniFormat.isGUICompatible())
			{
				throw new IllegalArgumentException(String.format(
						"The universal format below is not fully compatible with the GUI (web) format.\n%s", uniFormat.toXML()));
			}
			else
			{
				// and then onto the conversion
				ExperimentGraph webFormat = new ExperimentGraph();
	
				// first convert all boxes, set box positions
				Map<UniversalElement, String> uniBoxToWebBoxID = new HashMap<UniversalElement, String>();
				for(UniversalElement element : uniFormat.getAllElements())
				{
					// determine agent info instance
					AgentInfo agentInfo = null;
					try
					{
						// guarantees the correct result object or an exception
						agentInfo = agentInfoProvider.getUnique(
								element.getOntologyInfo().getOntologyClass().getName(),
								element.getOntologyInfo().getAgentClass().getName()
						);
					}
					catch (Throwable t)
					{
						throw new IllegalStateException(String.format(
								"No agent info instance was found for ontology '%s'.", element.getOntologyInfo().getOntologyClass().getName()));
					}
					
					// create web-format box and link it to uni-format box
					BoxInfo info = createBoxAndUpdateState(agentInfo, element.getGUIInfo().x, element.getGUIInfo().y);
					String convertedBoxID = webFormat.addLeafBoxAndReturnID(info);
					uniBoxToWebBoxID.put(element, convertedBoxID);
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
				
				/*
				 * TODO later:
				 * - use input slots in AgentInfo and detect box types can be mutually connected by an edge
				 * - make the conversion smarter - best effort to display old experiments whether they're still compatible or not
				 */
				
				// and finally, options... THIS IS THE TRICKY PART
				for(UniversalElement element : uniFormat.getAllElements())
				{
					AgentInfo agentInfo = boxIDToAgentInfo.get(uniBoxToWebBoxID.get(element));
					agentInfo.getOptions().mergeWith(element.getOntologyInfo().getOptions());
				}
				
				// conversion is finished, return:
				return webFormat;
			}
		}
		catch (Throwable t)
		{
			throw new ConversionException(t);
		}
	}
}