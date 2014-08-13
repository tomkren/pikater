package org.pikater.shared.experiment.webformat.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.Slot;
import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;
import org.pikater.shared.experiment.universalformat.UniversalConnector;
import org.pikater.shared.experiment.universalformat.UniversalElement;
import org.pikater.shared.experiment.webformat.IExperimentGraph;
import org.pikater.shared.experiment.webformat.client.ExperimentGraphClient;
import org.pikater.shared.util.SimpleIDGenerator;
import org.pikater.web.config.AgentInfoCollection;
import org.pikater.web.config.ServerConfigurationInterface;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.kineticcomponent.ConversionException;

public class ExperimentGraphServer implements IExperimentGraph<Integer, BoxInfoServer>, ISlotConnectionsContext<Integer, BoxInfoServer>
{
	/**
	 * ID generator for boxes. 
	 */
	private final SimpleIDGenerator boxIDGenerator;

	/**
	 * The 1:1 map containing all the boxes.
	 */
	private final Map<Integer, BoxInfoServer> leafBoxes;
	
	/**
	 * Collection of oriented edges between boxes, sorted by the "from end point".</br>
	 * Unlike slots, only one edge is allowed between a pair of boxes. The purpose of
	 * this is to eliminate the need of having a multi-graph.
	 */
	private final Map<Integer, Set<Integer>> edges;
	
	/**
	 * Stores connection related information about all (input, output) slots of every box.</br>
	 * Unlike edges, slots of a single box may be connected to slots of multiple other boxes.
	 */
	private final SlotConnections slotConnections;
	
	// ------------------------------------------------------------------
	// CONSTRUCTOR

	/** PUBLIC DEFAULT CONSTRUCTOR keeps Vaadin happy. */
	public ExperimentGraphServer()
	{
		this.boxIDGenerator = new SimpleIDGenerator();
		this.leafBoxes = new HashMap<Integer, BoxInfoServer>();
		this.edges = new HashMap<Integer, Set<Integer>>();
		this.slotConnections = new SlotConnections(this);
	}
	
	// ------------------------------------------------------------------
	// INHERITED INTERFACE
	
	@Override
	public Set<BoxInfoServer> getFromNeighbours(Integer boxID)
	{
		Set<BoxInfoServer> result = new HashSet<BoxInfoServer>();
		if(edgesDefinedFor(boxID))
		{
			for(Integer otherBoxID : edges.get(boxID))
			{
				if(edgeExistsBetween(boxID, otherBoxID))
				{
					result.add(getBox(otherBoxID));
				}
			}
		}
		return result;
	}

	@Override
	public Set<BoxInfoServer> getToNeighbours(Integer boxID)
	{
		Set<BoxInfoServer> result = new HashSet<BoxInfoServer>();
		for(BoxInfoServer otherBox : leafBoxes.values())
		{
			if(edgeExistsBetween(otherBox.getID(), boxID))
			{
				result.add(otherBox);
			}
		}
		return result;
	}
	
	@Override
	public boolean edgeExistsBetween(Integer fromBoxID, Integer toBoxID)
	{
		if(!containsBox(fromBoxID) || !containsBox(toBoxID))
		{
			throw new IllegalStateException("There seems to be a BIG problem somewhere...");
		}
		else
		{
			return (edges.get(fromBoxID) != null) && edges.get(fromBoxID).contains(toBoxID); 
		}
	}
	
	@Override
	public boolean containsBox(Integer boxID)
	{
		return leafBoxes.containsKey(boxID);
	}
	
	@Override
	public BoxInfoServer getBox(Integer boxID)
	{
		return leafBoxes.get(boxID);
	}
	
	@Override
	public BoxInfoServer addBox(BoxInfoServer box)
	{
		box.setID(boxIDGenerator.getAndIncrement());
		leafBoxes.put(box.getID(), box);
		return box;
	}
	
	@Override
	public void clear()
	{
		leafBoxes.clear();
		edges.clear();
		boxIDGenerator.reset();
	}
	
	@Override
	public boolean isEmpty()
	{
		return leafBoxes.isEmpty();
	}
	
	@Override
	public boolean edgesDefinedFor(Integer boxID)
	{
		return (edges.get(boxID) != null) && !edges.get(boxID).isEmpty();
	}
	
	@Override
	public void connect(Integer fromBoxKey, Integer toBoxKey)
	{
		doEdgeAction(fromBoxKey, toBoxKey, true);
	}
	
	@Override
	public void disconnect(Integer fromBoxKey, Integer toBoxKey)
	{
		doEdgeAction(fromBoxKey, toBoxKey, false);
	}
	
	// ------------------------------------------------------------------
	// OTHER PUBLIC INTERFACE
	
	public SlotConnections getSlotConnections()
	{
		return slotConnections;
	}
	
	/**
	 * NOT USED AT ALL
	 */
	@Deprecated
	public boolean isValid()
	{
		// no need to check boxes - they are simple data holders
		for(Entry<Integer, Set<Integer>> entry : edges.entrySet())
		{
			BoxInfoServer boxFrom = leafBoxes.get(entry.getKey());
			for(Integer boxToID : entry.getValue())
			{
				BoxInfoServer boxTo = leafBoxes.get(boxToID);
				if(boxFrom.isRegistered() != boxTo.isRegistered())
				{
					return false;
				}
				// TODO: should we delete the edge if both endpoints are not registered?
			}
		}
		return true;
	}
	
	// ------------------------------------------------------------------
	// PRIVATE INTERFACE

	private void doEdgeAction(Integer fromBoxKey, Integer toBoxKey, boolean connect)
	{
		/*
		 * Let this method have a transaction-like manner and only alter the data when
		 * everything has been checked and approved.
		 */

		// first, all kinds of checks before actually doing anything significant
		if((fromBoxKey == null) || (toBoxKey == null)) // boxes have not been added to the structure
		{
			throw new IllegalArgumentException("Cannot add this edge because at least one of the boxes was not added to the structure. "
					+ "Call the 'addBox()' method first and try again.");
		}
		if(!leafBoxes.containsKey(fromBoxKey) || !leafBoxes.containsKey(toBoxKey))
		{
			throw new IllegalArgumentException("One of the supplied box keys represents a wrapper box. Cannot add edges to wrapper boxes.");
		}
		if(connect) // we want to connect the boxes
		{
			if((edges.get(fromBoxKey) != null) && edges.get(fromBoxKey).contains(toBoxKey)) // an edge already exists
			{
				throw new IllegalStateException("Cannot add an edge from box '" + String.valueOf(fromBoxKey) + "' to box '" +
						String.valueOf(toBoxKey) + "': they are already connected.");
			}
		}
		else // we want to disconnect the boxes
		{
			if((edges.get(fromBoxKey) == null) || !edges.get(fromBoxKey).contains(toBoxKey)) // the edge doesn't exist
			{
				throw new IllegalStateException("Cannot remove the edge from box '" + String.valueOf(fromBoxKey) + "' to box '" +
						String.valueOf(toBoxKey) + "': they are not connected.");
			}
		}

		// and finally, let's add or remove the edge
		if(connect)
		{
			// add edge
			if(!edges.containsKey(fromBoxKey))
			{
				edges.put(fromBoxKey, new HashSet<Integer>());
			}
			edges.get(fromBoxKey).add(toBoxKey);
		}
		else
		{
			// remove edge
			edges.get(fromBoxKey).remove(toBoxKey);
		}
	}
	
	//---------------------------------------------------------------
	// FORMAT CONVERSIONS
	
	public ExperimentGraphClient toClientFormat()
	{
		ExperimentGraphClient result = new ExperimentGraphClient();
		for(BoxInfoServer box : leafBoxes.values())
		{
			result.addBox(box.toClientFormat());
		}
		result.edges = new HashMap<Integer, Set<Integer>>(edges); // simply copy, all IDs are kept intact
		return result;
	}

	/**
	 * Converts web experiment format into universal experiment format.
	 * This conversion is substantially simpler than its counterpart. It should always work.
	 * @param webFormat
	 * @return
	 * @throws ConversionException
	 */
	public UniversalComputationDescription toUniversalFormat() throws ConversionException
	{
		try
		{
			// first some checks
			AgentInfoCollection agentInfoProvider = ServerConfigurationInterface.getKnownAgents();
			if(agentInfoProvider == null)
			{
				throw new NullPointerException("Agent information has not yet been received from pikater.");
			}

			// create the result uni-format experiment
			UniversalComputationDescription result = new UniversalComputationDescription();

			// create uni-format master elements for all boxes that are registered
			// from now on, only iterate this collection
			Map<BoxInfoServer, UniversalElement> webBoxToUniBox = new HashMap<BoxInfoServer, UniversalElement>();
			for(BoxInfoServer webBox : leafBoxes.values())
			{
				if(webBox.isRegistered())
				{
					UniversalElement uniBox = new UniversalElement();
					webBoxToUniBox.put(webBox, uniBox);
				}
			}

			// traverse all boxes and pass all available/needed information to result uni-format
			for(Entry<BoxInfoServer, UniversalElement> entry : webBoxToUniBox.entrySet())
			{
				// determine basic information and references
				BoxInfoServer webBox = entry.getKey();
				UniversalElement uniBox = entry.getValue();
				
				/*
				 * Set the static stuff.
				 */
				
				try
				{
					uniBox.getOntologyInfo().setOntologyClass(Class.forName(webBox.getAssociatedAgent().getOntologyClassName()));
					uniBox.getOntologyInfo().setAgentClass(webBox.getAssociatedAgent().getAgentClassName());
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
				
				/*
				 * Set the dynamic stuff.
				 */
				
				// position
				uniBox.getGuiInfo().setX(webBox.getPosX());
				uniBox.getGuiInfo().setY(webBox.getPosY());
				
				// edges
				if(edgesDefinedFor(webBox.getID())) // these edges are all valid (currently registered)
				{
					for(Integer neighbourWebBoxID : edges.get(webBox.getID())) // these edges are all valid (currently registered)
					{
						BoxInfoServer neighbourWebBox = getBox(neighbourWebBoxID);
						UniversalElement neighbourUniBox = webBoxToUniBox.get(leafBoxes.get(neighbourWebBoxID));
						if(neighbourUniBox == null)
						{
							// edge leads to an unknown or unregistered box
							throw new IllegalStateException("Can not transform an edge with an unknown/unregistered endpoint.");
						}
						else
						{
							/*
							 * SERIALIZATION SCHEME:
							 * - edge: A (uniBox; webBox) -> B (neighbourUniBox, neighbourWebBox)
							 * - connector: outputDataType (output slot) -> inputDataType (input slot)
							 */
							
							for(Slot outputSlot : webBox.getAssociatedAgent().getOutputSlots())
							{
								for(Slot inputSlot : neighbourWebBox.getAssociatedAgent().getInputSlots())
								{
									if(slotConnections.areSlotsConnected(inputSlot, outputSlot))
									{
										// remember the edge, with exact slot connection
										UniversalConnector connector = new UniversalConnector();
										connector.setFromElement(uniBox);
										connector.setOutputDataType(outputSlot.getDataType());
										connector.setInputDataType(inputSlot.getDataType());
										neighbourUniBox.getOntologyInfo().addInputSlot(connector);
									}
								}
							}
							if(neighbourUniBox.getOntologyInfo().getInputSlots().isEmpty()) // no slot connections are defined
							{
								// at least remember the edge, with no slot connections whatsoever
								UniversalConnector connector = new UniversalConnector();
								connector.setFromElement(uniBox);
								neighbourUniBox.getOntologyInfo().addInputSlot(connector);
							}
						}
					}
				}
			}
			
			for(UniversalElement element : webBoxToUniBox.values())
			{
				result.addElement(element);
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
	public static ExperimentGraphServer fromUniversalFormat(UniversalComputationDescription uniFormat) throws ConversionException
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
			else if(!uniFormat.isValid())
			{
				throw new NullPointerException("The argument uni-format is not valid.");
			}
			else if(!uniFormat.isGUICompatible())
			{
				throw new IllegalArgumentException(String.format(
						"The universal format below is not fully compatible with the GUI (web) format.\n%s", uniFormat.toXML()));
			}
			else
			{
				/*
				 * IMPORTANT: from this point, we assume that the argument universal format is valid.
				 */
				
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
								element.getOntologyInfo().getAgentClass()
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
						/*
						 * SERIALIZATION SCHEME:
						 * - edge: A (edge.getFromElement(); fromBox) -> B (element, toBox)
						 * - connector: outputDataType (output slot) -> inputDataType (input slot)
						 */
						
						// connect boxes
						BoxInfoServer fromBox = webFormat.getBox(uniBoxToWebBoxID.get(edge.getFromElement()));
						BoxInfoServer toBox = webFormat.getBox(uniBoxToWebBoxID.get(element));
						if(!webFormat.edgeExistsBetween(fromBox.getID(), toBox.getID())) // protection against multiple slot connections through the same edge
						{
							webFormat.connect(fromBox.getID(), toBox.getID());
						}
						
						// also connect slots if needed
						if((edge.getInputDataType() != null) && (edge.getOutputDataType() != null))
						{
							Slot fromBoxSlot = fromBox.getAssociatedAgent().fetchOutputSlotByDataType(edge.getOutputDataType());
							Slot toBoxSlot = toBox.getAssociatedAgent().fetchInputSlotByDataType(edge.getInputDataType());
							if((fromBoxSlot == null) || (toBoxSlot == null))
							{
								throw new IllegalStateException("Could not find a slot by name in resolved agent info. Invalid binding.");
							}
							else
							{
								webFormat.getSlotConnections().connect(
										new BoxSlot(fromBox, fromBoxSlot),
										new BoxSlot(toBox, toBoxSlot)
								);
							}
						}
					}
				}

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