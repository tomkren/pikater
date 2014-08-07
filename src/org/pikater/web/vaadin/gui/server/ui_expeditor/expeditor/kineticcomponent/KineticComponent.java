package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.kineticcomponent;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;
import org.pikater.shared.experiment.universalformat.UniversalConnector;
import org.pikater.shared.experiment.universalformat.UniversalElement;
import org.pikater.shared.experiment.webformat.server.BoxInfoServer;
import org.pikater.shared.experiment.webformat.server.ExperimentGraphServer;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.web.config.AgentInfoCollection;
import org.pikater.web.config.ServerConfigurationInterface;

import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentClientRpc;
import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentServerRpc;
import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentState;
import org.pikater.web.vaadin.gui.server.components.popups.MyNotifications;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.CustomTabSheetTabComponent;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.ExpEditor;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.ExpEditor.ExpEditorToolbox;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.BoxManagerToolbox;
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
	 * The dynamic mapping of box IDs to box information providers. This field is
	 * the base for all format conversions and some other commands.
	 */
	private ExperimentGraphServer experimentGraph;
	
	/**
	 * Reference to experiment last used in the {@link #importExperiment(UniversalComputationDescription)}
	 * method.
	 */
	private Integer previouslyLoadedExperimentID;
	
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
		
		this.experimentGraph = new ExperimentGraphServer();
		this.previouslyLoadedExperimentID = null;
		
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
			
			/**
			 * This is assumed to only be invoked when the user creates a box (no edges are thus
			 * yet attached).
			 */
			@Override
			public void command_boxSetChange(RegistrationOperation opKind, BoxGraphItemShared[] boxes)
			{
				for(BoxGraphItemShared boxShared : boxes)
				{
					experimentGraph.getBox(boxShared.getID()).setRegistered(opKind == RegistrationOperation.REGISTER);
				}
				
				// TODO: pozice boxů se musí přenášet na server
			}
			
			/**
			 * This is assumed to only be invoked when the user creates an edge. If it is unregistered,
			 * boxes should not be affected because {@link #command_boxSetChange()}.
			 */
			@Override
			public void command_edgeSetChange(RegistrationOperation opKind, EdgeGraphItemShared[] edges)
			{
				if(opKind == RegistrationOperation.REGISTER)
				{
					for(EdgeGraphItemShared edge : edges)
					{
						// does all kinds of checks and throws exceptions
						experimentGraph.connect(edge.fromBoxID, edge.toBoxID);
					}
				}
				else
				{
					for(EdgeGraphItemShared edge : edges)
					{
						// does all kinds of checks and throws exceptions
						experimentGraph.disconnect(edge.fromBoxID, edge.toBoxID); // this will invalidate any actual slot connections
					}
				}
			}
			
			@Override
			public void command_selectionChange(Integer[] selectedBoxesIDs)
			{
				if(bindOptionsManagerWithSelectionChanges)
				{
					// convert to agent information array
					BoxInfoServer[] selectedBoxesInformation = new BoxInfoServer[selectedBoxesIDs.length];
					for(int i = 0; i < selectedBoxesIDs.length; i++)
					{
						if(experimentGraph.containsBox(selectedBoxesIDs[i]))
						{
							selectedBoxesInformation[i] = experimentGraph.getBox(selectedBoxesIDs[i]);
						}
						else
						{
							throw new IllegalStateException(String.format("Kinetic state out of sync. "
									+ "No agent info was found for box ID '%d'.", selectedBoxesIDs[i]));
						}
					}
					
					// get the toolbox
					BoxManagerToolbox toolbox = (BoxManagerToolbox) parentEditor.getToolbox(ExpEditorToolbox.BOX_MANAGER);
					
					// set the new content to it and display the toolbox if needed
					if(toolbox.setContentFromSelectedBoxes(selectedBoxesInformation))
					{
						parentEditor.openToolbox(ExpEditorToolbox.BOX_MANAGER);
					}
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
	 * Used BEFORE saving experiments to database.</br> 
	 */
	public static interface IOnExperimentExported
	{
		/**
		 * Handle the exported experiment in this method.
		 * @param exportedExperiment
		 * @param experimentSavedCallback callback for when the experiment is successfully saved to database
		 */
		void handleExperiment(UniversalComputationDescription exportedExperiment, IOnExperimentSaved experimentSavedCallback);
	}
	
	/**
	 * Used AFTER saving experiments to database to keep this component's inner state in sync.
	 */
	public static interface IOnExperimentSaved
	{
		/**
		 * Call this method when your experiment has successfully been saved to database to keep
		 * the component in consistent state.
		 * @param newBatchID the ID of the newly saved experiment
		 */
		void experimentSaved(JPABatch newExperimentEntity);
	}
	
	public void importExperiment(JPABatch experiment)
	{
		try
		{
			// first and foremost, reset current state (both server and client)
			resetEnvironment();
			
			// transform to universal format
			UniversalComputationDescription uniFormat = UniversalComputationDescription.fromXML(experiment.getXML());
			
			// transform universal format to server format and store the result
			experimentGraph = uniToWeb(uniFormat);
			
			// transform to client format and send it to the client
			getClientRPC().command_receiveExperimentToLoad(experimentGraph.toClientFormat());
			
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
	
	public void exportExperiment(IOnExperimentExported exportCallback)
	{
		try
		{
			UniversalComputationDescription result = webToUni(experimentGraph);
			
			/*
			// test case - redirect the same experiment into a new tab and test the conversion cycle
			JPABatch newBatch = new JPABatch("poliket", "bla bla", result.toXML(), null);
			parentEditor.loadExperimentIntoNewTab(newBatch);
			*/
			
			exportCallback.handleExperiment(result, new IOnExperimentSaved()
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
		catch (ConversionException e)
		{
			PikaterLogger.logThrowable("", e);
			MyNotifications.showError("Could not export experiment", "Please, contact the administrators.");
		}
	}
	
	//---------------------------------------------------------------
	// MISCELLANEOUS PUBLIC INTERFACE
	
	public void setParentTab(CustomTabSheetTabComponent parentTab)
	{
		this.parentTab = parentTab;
	}
	
	public Integer getPreviouslyLoadedExperimentID()
	{
		return previouslyLoadedExperimentID;
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
	
	@Deprecated
	public boolean isContentModified()
	{
		return true; // TODO: until problems with the "modified" status are resolved, always return true
		// return getState().serverThinksThatSchemaIsModified;
	}
	
	public BoxInfoServer createBox(AgentInfo info, int absX, int absY, boolean sendToClient)
	{
		BoxInfoServer result = experimentGraph.addBox(new BoxInfoServer(
				info.clone(), // agent info needs to be cloned because options may be changed by user later
				absX - absoluteLeft,
				absY - absoluteTop
		));
		if(sendToClient)
		{
			getClientRPC().command_createBox(result.toClientFormat());
		}
		return result;
	}
	
	public void reloadVisualStyle()
	{
		getClientRPC().request_reloadVisualStyle();
	}
	
	//---------------------------------------------------------------
	// MISCELLANEOUS PRIVATE INTERFACE
	
	private void resetEnvironment()
	{
		/*
		 * Server side reset could be done in a response call from the client but
		 * then we would have to wait for it before the current thread proceeds.
		 * As such, NEVER reset server-side state from the client :).
		 */
		
		// client side reset
		getClientRPC().command_resetKineticEnvironment();
		
		// server side reset
		experimentGraph.clear();
		experimentGraph = null;
	}
	
	private KineticComponentClientRpc getClientRPC()
	{
		return getRpcProxy(KineticComponentClientRpc.class);
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
	private static UniversalComputationDescription webToUni(ExperimentGraphServer webFormat) throws ConversionException
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

			// create uni-format master elements for all boxes that are registered
			// from now on, only iterate this collection
			Map<BoxInfoServer, UniversalElement> webBoxToUniBox = new HashMap<BoxInfoServer, UniversalElement>();
			for(BoxInfoServer webBox : webFormat.leafBoxes.values())
			{
				if(webBox.isRegistered())
				{
					UniversalElement uniBox = new UniversalElement();
					webBoxToUniBox.put(webBox, uniBox);
					result.addElement(uniBox);
				}
			}

			// traverse all boxes and pass all available/needed information to result uni-format
			for(Entry<BoxInfoServer, UniversalElement> entry : webBoxToUniBox.entrySet())
			{
				// determine basic information and references
				BoxInfoServer webBox = entry.getKey();
				UniversalElement uniBox = entry.getValue();

				// create edge leading from the currently processed box (will be later added to all neighbour uni-boxes)
				UniversalConnector connector = new UniversalConnector();
				connector.setFromElement(uniBox);
				
				// TODO: connectors need to be created for each connected slot...

				// initialize the FIRST of the 2 child objects
				try
				{
					uniBox.getOntologyInfo().setOntologyClass(Class.forName(webBox.getAssociatedAgent().getOntologyClassName()));
					uniBox.getOntologyInfo().setAgentClass(Class.forName(webBox.getAssociatedAgent().getAgentClassName()));
				}
				catch (ClassNotFoundException e)
				{
					throw new IllegalStateException(String.format(
							"Could not convert '%s' to a class instance. Has it been hardcoded to an agent and renamed? "
							+ "Or is the pikater core running in different version than web?", webBox.getAssociatedAgent().getOntologyClassName()
							), e
					);
				}
				uniBox.getOntologyInfo().setOptions(webBox.getAssociatedAgent().getOptions());
				if(webFormat.edgesDefinedFor(webBox.getID()))
				{
					// transform edges, if they're "valid"
					for(Integer neighbourWebBoxID : webFormat.edges.get(webBox.getID()))
					{
						UniversalElement neighbourUniBox = webBoxToUniBox.get(webFormat.leafBoxes.get(neighbourWebBoxID));
						if(neighbourUniBox == null)
						{
							// edge leads to an unknown or unregistered box
							throw new IllegalStateException("Can not transform an edge with an unregistered endpoint.");
						}
						else
						{
							neighbourUniBox.getOntologyInfo().addInputSlot(connector);
						}
					}
				}

				// initialize the SECOND of the 2 child objects
				uniBox.getGuiInfo().setX(webBox.getPosX());
				uniBox.getGuiInfo().setY(webBox.getPosY());
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
	 * This method is very sensitive to changes (because of serialization to XML
	 * and back) to:
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
	private static ExperimentGraphServer uniToWeb(UniversalComputationDescription uniFormat) throws ConversionException
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
				ExperimentGraphServer webFormat = new ExperimentGraphServer();
	
				// first convert all boxes, set box positions
				Map<UniversalElement, Integer> uniBoxToWebBoxID = new HashMap<UniversalElement, Integer>();
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
					BoxInfoServer webBox = webFormat.addBox(new BoxInfoServer(agentInfo, element.getGuiInfo().getX(), element.getGuiInfo().getY()));
					uniBoxToWebBoxID.put(element, webBox.getID());
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
						
						// TODO: update internal state (connected slots)
					}
				}
				
				/*
				 * TODO later:
				 * Experiment XMLs need to be fully alliased to allow injecting other types (beside
				 * the original). Experiment XMLs would then be pretty much "standardized" while
				 * the universal format objects could be fluid as the water :).
				 */
				
				// and finally, options... THIS IS THE TRICKY PART
				for(UniversalElement element : uniFormat.getAllElements())
				{
					BoxInfoServer boxInfo = webFormat.getBox(uniBoxToWebBoxID.get(element));
					boxInfo.getAssociatedAgent().getOptions().mergeWith(element.getOntologyInfo().getOptions());
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