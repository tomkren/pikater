package org.pikater.shared.experiment.slots;

import java.util.Set;

import org.pikater.shared.experiment.resources.DataResource;
import org.pikater.shared.experiment.resources.ParamResource;
import org.pikater.shared.experiment.resources.Resource;
import org.pikater.shared.util.InstanceSet;

/**
 * Main slot type representing one to many connections.
 * Each instance creating static method provides a description field to programmatically assign semantic meaning to slots but
 * these descriptions are actually never used since they're not needed by the model itself.
 */
public class ArraySlot extends AbstractSlot
{
	/**
	 * It is important to use instance set here. AbstractSlot overrides the equals method and all slots
	 * added to this field are expected/required to be equal (in the sense of "slot1.equals(slot2)").
	 */
	private final InstanceSet<AbstractSlot> connectedSlots;
	
	private final int capacity;
	
	protected ArraySlot(SlotContent content, int capacity, Resource resource)
	{
		super(content, resource);
		
		this.connectedSlots = new InstanceSet<AbstractSlot>();
		this.capacity = capacity;
	}
	
	// -----------------------------------------------------------------
	// PUBLIC INTERFACE

	@Override
	public Set<AbstractSlot> getConnectedSlots()
	{
		return connectedSlots;
	}

	@Override
	public void addConnectedSlot(AbstractSlot slot)
	{
		if(canAcceptMoreConnections())
		{
			if(!connectedSlots.add(slot))
			{
				throw new IllegalStateException("The provided slot was already connected.");
			}
		}
		else
		{
			throw new IllegalStateException("Capacity full.");
		}
	}

	@Override
	public void removeConnectedSlot(AbstractSlot slot)
	{
		if(connectedSlots.isEmpty() || !connectedSlots.remove(slot))
		{
			throw new IllegalStateException("The provided slot was not previously connected.");
		}
	}

	@Override
	public boolean canAcceptMoreConnections()
	{
		return connectedSlots.size() < capacity; 
	}
	
	// -----------------------------------------------------------------
	// INSTANCE CREATING METHODS FOR PUBLIC USE
	
	public static ArraySlot getDataInputSlot(DataResource resource, int capacity, String description)
	{
		return new ArraySlot(SlotContent.DATA, capacity, resource);
	}
	
	public static ArraySlot getDataOutputSlot(DataResource resource, String description)
	{
		return new ArraySlot(SlotContent.DATA, Integer.MAX_VALUE, resource);
	}
	
	public static ArraySlot getParamInputSlot(ParamResource resource, int capacity, String description)
	{
		return new ArraySlot(SlotContent.PARAMETER, capacity, resource);
	}
	
	public static ArraySlot getParamOutputSlot(ParamResource resource, String description)
	{
		return new ArraySlot(SlotContent.PARAMETER, Integer.MAX_VALUE, resource);
	}
	
	public static ArraySlot getErrorInputSlot(Resource resource, int capacity, String description)
	{
		return new ArraySlot(SlotContent.ERROR, capacity, resource);
	}
	
	public static ArraySlot getErrorOutputSlot(Resource resource, String description)
	{
		return new ArraySlot(SlotContent.ERROR, Integer.MAX_VALUE, resource);
	}
}