package org.pikater.web.vaadin.gui.shared.slots;

import java.util.Set;

import org.pikater.shared.util.InstanceSet;

/**
 * Main slot type representing one to many connections.
 */
public class ArraySlot extends AbstractSlot
{
	/**
	 * It is important to use instance set here. AbstractSlot overrides the equals method and all slots
	 * added to this field are expected/required to be equal (in the sense of "slot1.equals(slot2)").
	 */
	private final InstanceSet<AbstractSlot> connectedSlots;
	
	private final int capacity;
	
	protected ArraySlot(SlotContent content, int capacity)
	{
		super(content);
		
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
}