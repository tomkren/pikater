package org.newpikater.util.experiment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.shared.interval.*;
import org.shared.parameters.*;
import org.shared.slots.*;
import org.newpikater.util.*;
import org.newpikater.util.boxes.types.AbstractBox;
import org.newpikater.util.boxes.types.LeafBox;
import org.shared.slots.AbstractSlot;

public class SchemaDataSource
{
	/**
	 * The 1:1 map containing all the boxes (wrapper boxes and leaf boxes). Integer keys are used as references in other variables
	 * and simplify serialization of this class to XML files.
	 */
	private final BidiMap<Integer, AbstractBox> allBoxes;
	
	/**
	 * Collection of oriented edges between boxes, sorted by the "from end point". 
	 */
	private final Map<Integer, Set<Integer>> edges;
	
	/**
	 * The collection holding all defined global parameters that are going to travel from providers to leaf consumers.
	 */
	private final Collection<AbstractParameter> globalParameters;
	
	/**
	 * Used for the bidirectional map of boxes - generates unique Integer keys.
	 */
	private final IDGenerator idGenerator;
	
	/**
	 * IDs of boxes that encapsulate the default (top-level) schema view.
	 */
	private final Collection<Integer> topLevelView;
	
	public SchemaDataSource()
	{
		super();
		
		this.allBoxes = new DualHashBidiMap<Integer, AbstractBox>();
		this.edges = new HashMap<Integer, Set<Integer>>();
		this.globalParameters = new ArrayList<AbstractParameter>();
		this.idGenerator = new IDGenerator();
		this.topLevelView = new ArrayList<Integer>();
	}
	
	// ------------------------------------------------------------------
	// PUBLIC GETTERS
	
	public Collection<AbstractBox> getAllBoxes()
	{
		return allBoxes.values();
	}
	
	public Collection<AbstractParameter> getGlobalParameters()
	{
		return globalParameters;
	}
	
	public Collection<Integer> getIDsOfTopLevelBoxes()
	{
		return topLevelView; // TODO: compute this automatically and remove the field
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
		return allBoxes.get(id);
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
	
	public void addGlobalParameter(AbstractParameter parameter)
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
		Integer fromBoxKey = allBoxes.getKey(fromBox);
		Integer toBoxKey = allBoxes.getKey(toBox);
		
		// first all kinds of checks
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
		
		// match slots that need to be connected/disconnected and connect/disconnect them
		// TODO: make this linear somehow?
		for(AbstractSlot outputSlot : fromBox.getOutputSlots())
		{
			for(AbstractSlot inputSlot : toBox.getInputSlots())
			{
				if(outputSlot.equals(inputSlot)) // not only instance equality but also semantic - see the insides of the method
				{
					if(connect)
					{
						// connect the slots
						outputSlot.removeConnectedSlot(inputSlot);
						inputSlot.removeConnectedSlot(outputSlot);
					}
					else
					{
						// connect the slots
						outputSlot.removeConnectedSlot(inputSlot);
						inputSlot.removeConnectedSlot(outputSlot);
					}
				}
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
