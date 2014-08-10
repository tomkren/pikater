package org.pikater.shared.experiment.webformat.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pikater.core.ontology.subtrees.agentInfo.Slot;

/**
 * Maps all slots (input/output; all boxes) to connected slots, wrapped in a special
 * class.
 */
public class SlotConnections
{
	/**
	 * Context for this class providing a method to determine whether
	 * a slot connection is currently valid (whether an edge exists
	 * between the endpoint boxes).
	 */
	private final ISlotConnectionsContext<Integer> context;
	
	/**
	 * A special field that lets us avoid indexing connections by
	 * {@link BoxSlot} and specifying additional arguments (beside
	 * {@link Slot slots}) to methods of this class. Let's keep usage
	 * of this class simple :).</br>
	 * This field requires the same condition as {@link #connectionMap}. 
	 */
	private final Map<Slot, BoxSlot> slotToBoxSlot;
	
	/**
	 * Field storing all the slot connections.</br>
	 * There are several very important things about this:
	 * <ul>
	 * <li> {@link Slot} is required NOT to have hashCode and equals
	 * methods defined, otherwise the Map will not compare keys by instance.
	 * <li> {@link BoxSlot} should not be used as key in this collection,
	 * because equals & hashCode methods would be required for it, which in
	 * turn require the same methods on subtypes. And so on, so forth.
	 * </ul>  
	 */
	private final Map<Slot, Set<BoxSlot>> connectionMap;

	/**
	 * Ctor.
	 * @param context
	 */
	public SlotConnections(ISlotConnectionsContext<Integer> context)
	{
		this.context = context;
		this.slotToBoxSlot = new HashMap<Slot, BoxSlot>();
		this.connectionMap = new HashMap<Slot, Set<BoxSlot>>();
	}
	
	/**
	 * Connects the given 2 slots, if possible. Throws exceptions otherwise.</br>
	 * No need to distinguish between input/output endpoints.
	 * @param endPoint1
	 * @param endPoint2
	 */
	public void connect(BoxSlot endPoint1, BoxSlot endPoint2)
	{
		if(!endPoint1.isValid() || !endPoint2.isValid())
		{
			throw new IllegalStateException("One of the 2 arguments was not valid.");
		}
		else if(endPoint1.getChildSlotsType() == endPoint2.getChildSlotsType())
		{
			throw new IllegalStateException("Can not make a connection between INPUT-INPUT or OUTPUT-OUTPUT slots.");
		}
		else if(!endPoint1.getChildSlot().isCompatibleWith(endPoint2.getChildSlot()))
		{
			throw new IllegalStateException("Slots were not compatible.");
		}
		else
		{
			oneWayConnect(endPoint1, endPoint2);
			oneWayConnect(endPoint2, endPoint1);
		}
	}
	
	/**
	 * Disconnects the given slots, if they were previously connected.
	 * Throws exceptions otherwise.</br>
	 * Input/output slots are not distinguished in this method. Both can be passed.
	 * @param slot 
	 */
	public void disconnect(Slot slot1, Slot slot2) 
	{
		if(!connectionExistsFor(slot1))
		{
			throw new IllegalStateException("No connections exist for the given slot.");
		}
		else if(!connectionMap.get(slot1).remove(slotToBoxSlot.get(slot2)))
		{
			throw new IllegalStateException("The slots are not connected. Can not disconnect.");
		}
		else if(!connectionMap.get(slot2).remove(slotToBoxSlot.get(slot1))) 
		{
			throw new IllegalStateException("Slot connection was only defined in one way. There is a bug in this class.");
		}
	}
	
	public List<BoxSlot> getConnectedAndValidEndpointsForSlot(Slot slot)
	{
		List<BoxSlot> result = new ArrayList<BoxSlot>();
		if(connectionExistsFor(slot))
		{
			for(BoxSlot endPoint : connectionMap.get(slot))
			{
				Integer fromBoxID = null;
				Integer toBoxID = null;
				if(endPoint.isFromEndpoint())
				{
					fromBoxID = endPoint.getParentBox().getID();
					toBoxID = slotToBoxSlot.get(slot).getParentBox().getID();
				}
				else
				{
					toBoxID = slotToBoxSlot.get(slot).getParentBox().getID();
					toBoxID = endPoint.getParentBox().getID();
				}
				if(context.edgeExistsBetween(fromBoxID, toBoxID))
				{
					result.add(endPoint);
				}
			}
		}
		return result;
	}
	
	public boolean isSlotConnectedToAValidEndpoint(Slot slot)
	{
		return !getConnectedAndValidEndpointsForSlot(slot).isEmpty();
	}
	
	//-------------------------------------------
	// PRIVATE INTERFACE
	
	/**
	 * IMPORTANT: doesn't distinguish between valid and invalid connections. 
	 * @param slot
	 * @return
	 */
	private boolean connectionExistsFor(Slot slot)
	{
		if(connectionMap.containsKey(slot))
		{
			Set<BoxSlot> connections = connectionMap.get(slot);
			return (connections != null) && !connections.isEmpty();
		}
		else
		{
			return false;
		}
	}
	
	private void oneWayConnect(BoxSlot from, BoxSlot to)
	{
		/*
		 * Create connections for slot if not yet defined.
		 */
		if(!connectionExistsFor(from.getChildSlot()))
		{
			connectionMap.put(from.getChildSlot(), new HashSet<BoxSlot>());
		}
		
		// connect the slots in the given order
		if(!connectionMap.get(from.getChildSlot()).add(to))
		{
			/*
			 * A simple "safety" check (not really necessary to make this work but it could identify
			 * a potential bug).
			 */
			throw new IllegalStateException("These endpoints are already connected.");
		}
		slotToBoxSlot.put(from.getChildSlot(), from);
	}
}