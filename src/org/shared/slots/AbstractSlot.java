package org.shared.slots;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractSlot
{
	public enum SlotContent
	{
		DATA,
		PARAMETER,
		ERROR
	}
	
	public final String description;
	private final Collection<AbstractSlot> connectedSlots;
	
	public AbstractSlot(String description)
	{
		super();
		
		this.description = description;
		this.connectedSlots = new ArrayList<AbstractSlot>();
	}

	public void addConnectedSlot(AbstractSlot slot)
	{
		/**
		 * IMPORTANT: don't change the collection to Set lightly... only a single element could be contained in such case
		 * because of the equals method.
		 * @see AbstractSlot#equals(Object obj)
		 */
		if(!this.connectedSlots.contains(slot))
		{
			this.connectedSlots.add(slot);
		}
	}
	
	public void removeConnectedSlot(AbstractSlot slot)
	{
		this.connectedSlots.remove(slot);
	}
	
	public Collection<AbstractSlot> getConnectedSlot()
	{
		return this.connectedSlots;
	}
	
	public boolean isAmbiguous()
	{
		return this.connectedSlots.size() > 1;
	}
	
	/**
	 * Children of this class are used in Set collections in several classes. As such, the equals method needs
	 * to be overriden to denote whether the slots have identical meaning despite being different instances.
	 * 
	 * IMPORTANT:
	 * This particular implementation of the method is strictly required in the @LeafBox and @ParameterSlot classes.
	 * @see LeafBox#inputSlots
	 * @see LeafBox#outputSlots
	 * @see ParameterSlot
	 */
	@Override
	public boolean equals(Object obj)
	{
		// the first part is generated, as usual
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		
		// and the second part is checking the semantic meaning:
		AbstractSlot other = (AbstractSlot) obj;
		if(getContent() == other.getContent())
		{
			return this.canBeConnectedToSpecific(other);
		}
		else
		{
			return false;
		}
	}

	public abstract SlotContent getContent();
	protected abstract boolean canBeConnectedToSpecific(AbstractSlot other);
}
