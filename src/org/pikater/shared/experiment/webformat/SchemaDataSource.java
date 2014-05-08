package org.pikater.shared.experiment.webformat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.pikater.shared.experiment.webformat.box.AbstractBox;
import org.pikater.shared.experiment.webformat.box.LeafBox;
import org.pikater.shared.experiment.webformat.box.WrapperBox;
import org.pikater.shared.util.SimpleIDGenerator;

public class SchemaDataSource implements Serializable
{
	private static final long serialVersionUID = -1340889786809747684L;

	/**
	 * Used to generate unique Integer ID keys for all boxes. Generated IDs will be used as references in other variables
	 * to improve efficiency when serializing an instance of this class from server to client.
	 */
	private transient SimpleIDGenerator idGenerator;
	
	/**
	 * The 1:1 map containing all the boxes (wrapper boxes and leaf boxes).
	 */
	public Map<Integer, AbstractBox> allBoxes;
	
	/**
	 * Collection of oriented edges between boxes, sorted by the "from end point".
	 */
	public Map<Integer, Set<Integer>> edges;
	
	// ------------------------------------------------------------------
	// CONSTRUCTOR

	/**
	 * Default constructor keeps GWT and Vaadin happy.
	 */
	public SchemaDataSource()
	{
		this.idGenerator = new SimpleIDGenerator();
		this.allBoxes = new HashMap<Integer, AbstractBox>();
		this.edges = new HashMap<Integer, Set<Integer>>();
	}
	
	// ------------------------------------------------------------------
	// PUBLIC GETTERS
	
	public Collection<AbstractBox> getAllBoxes()
	{
		return allBoxes.values();
	}
	
	public Collection<Integer> getIDsOfLeafBoxes()
	{
		Collection<Integer> result = new ArrayList<Integer>();
		for(Integer boxKey : allBoxes.keySet())
		{
			if(isLeafBox(boxKey))
			{
				result.add(boxKey);
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
	
	public Integer addLeafBoxAndReturnID(BoxInfo info)
	{
		LeafBox newBox = new LeafBox(idGenerator.getAndIncrement(), info);
		allBoxes.put(newBox.id, newBox);
		return newBox.id;
	}
	
	public Integer addWrapperBoxAndReturnID(AbstractBox... childBoxes) 
	{
		// TODO: problems with overlapping of LeafBoxes?
		
		WrapperBox newBox = new WrapperBox(idGenerator.getAndIncrement(), childBoxes);
		allBoxes.put(newBox.id, newBox);
		return newBox.id;
	}
	
	public void connect(Integer fromBoxKey, Integer toBoxKey)
	{
		interboxConnectionAction(fromBoxKey, toBoxKey, true);
	}
	
	public void disconnect(Integer fromBoxKey, Integer toBoxKey)
	{
		interboxConnectionAction(fromBoxKey, toBoxKey, false);
	}
	
	// ------------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private boolean isLeafBox(Integer boxKey)
	{
		return allBoxes.containsKey(boxKey) && (allBoxes.get(boxKey) instanceof LeafBox);
	}
	
	private void interboxConnectionAction(Integer fromBoxKey, Integer toBoxKey, boolean connect)
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
		if(!isLeafBox(fromBoxKey) || !isLeafBox(toBoxKey))
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
}
