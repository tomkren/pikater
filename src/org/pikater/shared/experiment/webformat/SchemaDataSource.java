package org.pikater.shared.experiment.webformat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.pikater.shared.experiment.webformat.box.AbstractBox;
import org.pikater.shared.experiment.webformat.box.LeafBox;
import org.pikater.shared.util.BidiMap;
import org.pikater.shared.util.SimpleIDGenerator;

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
	 * Used for the bidirectional map of boxes - generates unique Integer keys.
	 */
	private final SimpleIDGenerator idGenerator;
	
	public SchemaDataSource()
	{
		super();
		
		this.allBoxes = new BidiMap<Integer, AbstractBox>();
		this.edges = new HashMap<Integer, Set<Integer>>();
		this.idGenerator = new SimpleIDGenerator();
	}
	
	// ------------------------------------------------------------------
	// PUBLIC GETTERS
	
	public Collection<AbstractBox> getAllBoxes()
	{
		return allBoxes.valueSet();
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
						String.valueOf(fromBoxKey) + fromBox.getDisplayName(),
						String.valueOf(toBoxKey) + toBox.getDisplayName())
				);
			}
		}
		else // we want to disconnect the boxes
		{
			if((edges.get(fromBoxKey) == null) || !edges.get(fromBoxKey).contains(toBoxKey)) // the edge doesn't exist
			{
				throw new IllegalStateException(String.format("Cannot remove the edge from box '%s' to box '%s': they are not connected.",
						String.valueOf(fromBoxKey) + fromBox.getDisplayName(),
						String.valueOf(toBoxKey) + toBox.getDisplayName())
				);
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
