package org.pikater.web.experiment_backup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.pikater.shared.util.SimpleIDGenerator;
import org.pikater.shared.util.collections.BidiMap;
import org.pikater.web.experiment_backup.box.AbstractBox;
import org.pikater.web.experiment_backup.box.LeafBox;
import org.pikater.web.experiment_backup.box.LeafBox.SlotType;
import org.pikater.web.experiment_backup.options.AbstractOption;
import org.pikater.web.experiment_backup.resources.Resource;
import org.pikater.web.experiment_backup.slots.AbstractSlot;

public class SchemaDataSource
{
	/**
	 * The 1:1 map containing all the boxes (wrapper boxes and leaf boxes). Integer keys are used as references in other variables
	 * to simplify serialization of this class to XML files.
	 */
	private final BidiMap<Integer, AbstractBox> allBoxes;
	
	/**
	 * Collection of oriented edges between boxes, sorted by the "from end point". 
	 */
	private final Map<Integer, Set<Integer>> edges;
	
	/**
	 * The collection holding all defined global parameters that are going to travel from providers to leaf consumers.
	 */
	private final Collection<AbstractOption> globalParameters; // TODO: integrate this functionality
	
	/**
	 * Used for the bidirectional map of boxes - generates unique Integer keys.
	 */
	private final SimpleIDGenerator idGenerator;
	
	public SchemaDataSource()
	{
		super();
		
		this.allBoxes = new BidiMap<Integer, AbstractBox>();
		this.edges = new HashMap<Integer, Set<Integer>>();
		this.globalParameters = new ArrayList<AbstractOption>();
		this.idGenerator = new SimpleIDGenerator();
	}
	
	// ------------------------------------------------------------------
	// PUBLIC GETTERS
	
	public Collection<AbstractBox> getAllBoxes()
	{
		return allBoxes.valueSet();
	}
	
	public Collection<AbstractOption> getGlobalParameters()
	{
		return globalParameters;
	}
	
	public Collection<Integer> getIDsOfLeafBoxes()
	{
		Collection<Integer> result = new ArrayList<Integer>();
		for(Entry<Integer, AbstractBox> entry : allBoxes.entrySet())
		{
			if(entry.getValue() instanceof LeafBox)
			{
				result.add(entry.getKey());
			}
		}
		return result;
	}
	
	public AbstractBox getBox(Integer id)
	{
		return allBoxes.getValue(id);
	}

	// ------------------------------------------------------------------
	// OTHER PUBLIC INTERFACE
	
	public void addBox(AbstractBox box)
	{
		if(allBoxes.containsValue(box))
		{
			throw new IllegalStateException("Box already added.");
		}
		else
		{
			allBoxes.put(idGenerator.getAndIncrement(), box);
		}
	}
	
	public void addGlobalParameter(AbstractOption parameter)
	{
		this.globalParameters.add(parameter);
	}

	public Collection<LeafBox> getConsumers()
	{
		Collection<LeafBox> result = new ArrayList<LeafBox>();
		for (Integer leafBoxID : getIDsOfLeafBoxes()) 
		{
			LeafBox box = (LeafBox) getBox(leafBoxID);
			if (box.isOnlyConsumer())
			{
				result.add(box);
			}
		}
		return result;
	}

	public void connect(LeafBox fromBox, LeafBox toBox)
	{
		interboxConnectionAction(fromBox, toBox, true);
	}
	
	public void disconnect(LeafBox fromBox, LeafBox toBox)
	{
		interboxConnectionAction(fromBox, toBox, false);
	}
	
	// ------------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private void interboxConnectionAction(LeafBox fromBox, LeafBox toBox, boolean connect)
	{
		/*
		 * Let this method have a transaction-like manner and only change the program state when
		 * everything has been checked and approved.
		 */
		
		Integer fromBoxKey = allBoxes.getKey(fromBox);
		Integer toBoxKey = allBoxes.getKey(toBox);
		
		/*
		 * First, get all shared resources that can be connected/disconnected.
		 * It is important to note that standard Set implementations use the equals method to compare objects. This is
		 * exactly required when dealing with Resource type as it implements a custom, overriden equals method.
		 */
		Set<Resource> sharedResources = new HashSet<Resource>(fromBox.getSlots(SlotType.OUTPUT).keySet());
		sharedResources.retainAll(toBox.getSlots(SlotType.INPUT).keySet());
		
		// all kinds of checks before actually doing anything significant
		if((fromBoxKey == null) || (toBoxKey == null)) // boxes have not been added to the structure
		{
			throw new IllegalArgumentException("Cannot add this edge because at least one of the boxes was not added to the structure. "
					+ "Call the 'addBox()' method first and try again.");
		}
		if(connect) // we want to connect the boxes
		{
			if((edges.get(fromBoxKey) != null) && edges.get(fromBoxKey).contains(toBoxKey)) // an edge already exists
			{
				throw new IllegalStateException(String.format("Cannot add an edge from box '%s' to box '%s': they are already connected.",
						String.valueOf(fromBoxKey) + fromBox.displayName,
						String.valueOf(toBoxKey) + toBox.displayName)
				);
			}
		}
		else // we want to disconnect the boxes
		{
			if((edges.get(fromBoxKey) == null) || !edges.get(fromBoxKey).contains(toBoxKey)) // the edge doesn't exist
			{
				throw new IllegalStateException(String.format("Cannot remove the edge from box '%s' to box '%s': they are not connected.",
						String.valueOf(fromBoxKey) + fromBox.displayName,
						String.valueOf(toBoxKey) + toBox.displayName)
				);
			}
		}
		for(Resource resource : sharedResources)
		{
			AbstractSlot outputSlot = fromBox.getSlotByResource(SlotType.OUTPUT, resource);
			AbstractSlot inputSlot = fromBox.getSlotByResource(SlotType.INPUT, resource);
			
			if(connect)
			{
				if(inputSlot.canAcceptMoreConnections()) // output slots are assumed to be unbounded
				{
					// connect the slots
					outputSlot.addConnectedSlot(inputSlot);
					inputSlot.addConnectedSlot(outputSlot);
				}
				else
				{
					// TODO:
				}
			}
			else
			{
				// disconnect the slots
				outputSlot.removeConnectedSlot(inputSlot);
				inputSlot.removeConnectedSlot(outputSlot);
			}
		}
		
		// after the checks, let's get to the main event
		
		// then connect/disconnect the shared resources one by one
		for(Resource resource : sharedResources)
		{
			AbstractSlot outputSlot = fromBox.getSlotByResource(SlotType.OUTPUT, resource);
			AbstractSlot inputSlot = fromBox.getSlotByResource(SlotType.INPUT, resource);
			
			if(connect)
			{
				if(inputSlot.canAcceptMoreConnections()) // output slots are assumed to be unbounded
				{
					// connect the slots
					outputSlot.addConnectedSlot(inputSlot);
					inputSlot.addConnectedSlot(outputSlot);
				}
				else
				{
					// TODO:
				}
			}
			else
			{
				// disconnect the slots
				outputSlot.removeConnectedSlot(inputSlot);
				inputSlot.removeConnectedSlot(outputSlot);
			}
		}
		
		// add or remove the edge
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
}
