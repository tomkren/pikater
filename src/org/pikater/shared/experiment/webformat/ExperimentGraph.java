package org.pikater.shared.experiment.webformat;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExperimentGraph implements Serializable
{
	private static final long serialVersionUID = -1340889786809747684L;

	/**
	 * The 1:1 map containing all the boxes.
	 */
	public Map<String, BoxInfo> leafBoxes;
	
	/**
	 * Collection of oriented edges between boxes, sorted by the "from end point".
	 */
	public Map<String, Set<String>> edges;
	
	// ------------------------------------------------------------------
	// CONSTRUCTOR

	/** PUBLIC DEFAULT CONSTRUCTOR keeps Vaadin happy. */
	public ExperimentGraph()
	{
		this.leafBoxes = new HashMap<String, BoxInfo>();
		this.edges = new HashMap<String, Set<String>>();
	}
	
	// ------------------------------------------------------------------
	// PUBLIC INTERFACE
	
	public String addLeafBoxAndReturnID(BoxInfo info)
	{
		leafBoxes.put(info.boxID, info);
		return info.boxID;
	}
	
	public boolean edgesDefinedFor(String boxID)
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
	
	public void connect(String fromBoxKey, String toBoxKey)
	{
		interboxConnectionAction(fromBoxKey, toBoxKey, true);
	}
	
	public void disconnect(String fromBoxKey, String toBoxKey)
	{
		interboxConnectionAction(fromBoxKey, toBoxKey, false);
	}
	
	// ------------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private void interboxConnectionAction(String fromBoxKey, String toBoxKey, boolean connect)
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
				edges.put(fromBoxKey, new HashSet<String>());
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
