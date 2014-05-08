package org.pikater.shared.experiment.webformat;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.pikater.shared.experiment.universalformat.UniversalGui;
import org.pikater.shared.experiment.webformat.box.LeafBox;
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
	 * The 1:1 map containing all the boxes.
	 */
	public Map<Integer, LeafBox> leafBoxes;
	
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
		this.leafBoxes = new HashMap<Integer, LeafBox>();
		this.edges = new HashMap<Integer, Set<Integer>>();
	}
	
	// ------------------------------------------------------------------
	// PUBLIC GETTERS
	
	// ------------------------------------------------------------------
	// OTHER PUBLIC INTERFACE
	
	public Integer addLeafBoxAndReturnID(UniversalGui guiInfo, BoxInfo info)
	{
		LeafBox newBox = new LeafBox(idGenerator.getAndIncrement(), guiInfo, info);
		leafBoxes.put(newBox.id, newBox);
		return newBox.id;
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
