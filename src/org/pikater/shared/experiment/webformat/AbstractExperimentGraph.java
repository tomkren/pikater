package org.pikater.shared.experiment.webformat;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.pikater.shared.util.SimpleIDGenerator;

public class AbstractExperimentGraph<B extends IBoxInfo<Integer>> implements Serializable
{
	private static final long serialVersionUID = -1340889786809747684L;
	
	/**
	 * ID generator for boxes. 
	 */
	public SimpleIDGenerator boxIDGenerator;

	/**
	 * The 1:1 map containing all the boxes.
	 */
	public Map<Integer, B> leafBoxes;
	
	/**
	 * Collection of oriented edges between boxes, sorted by the "from end point".
	 */
	public Map<Integer, Set<Integer>> edges;
	
	// ------------------------------------------------------------------
	// CONSTRUCTOR

	/** PUBLIC DEFAULT CONSTRUCTOR keeps Vaadin happy. */
	public AbstractExperimentGraph()
	{
		this.boxIDGenerator = new SimpleIDGenerator();
		this.leafBoxes = new HashMap<Integer, B>();
		this.edges = new HashMap<Integer, Set<Integer>>();
	}
	
	// ------------------------------------------------------------------
	// PUBLIC INTERFACE
	
	public void clear()
	{
		leafBoxes.clear();
		edges.clear();
		boxIDGenerator.reset();
	}
	
	public boolean containsBox(Integer boxID)
	{
		return leafBoxes.containsKey(boxID);
	}
	
	public B getBox(Integer boxID)
	{
		return leafBoxes.get(boxID);
	}
	
	public B addBox(B box)
	{
		box.setID(boxIDGenerator.getAndIncrement());
		leafBoxes.put(box.getID(), box);
		return box;
	}
	
	public boolean edgesDefinedFor(Integer boxID)
	{
		return (edges.get(boxID) != null) && !edges.get(boxID).isEmpty(); 
	}
	
	/*
	public Integer addWrapperBoxAndReturnID(UniversalGui guiInfo, AbstractBox... childBoxes) 
	{
		// TODO: problems with overlapping of LeafBoxes?
		
		WrapperBox newBox = new WrapperBox(idGenerator.getAndIncrement(), guiInfo, childBoxes);
		allBoxes.put(newBox.id, newBox);
		return newBox.id;
	}
	*/
	
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
}