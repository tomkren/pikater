package org.pikater.web.unused.experiment.slots;

import java.util.Set;

import org.pikater.web.unused.experiment.resources.Resource;

public abstract class AbstractSlot
{
	public enum SlotContent
	{
		DATA,
		PARAMETER,
		ERROR
	}
	
	public final SlotContent content;
	public final Resource resource;
	
	public AbstractSlot(SlotContent content, Resource resource)
	{
		this.content = content;
		this.resource = resource;
	}
	
	// ------------------------------------------------------------------
	// PUBLIC INTERFACE
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result
				+ ((resource == null) ? 0 : resource.hashCode());
		return result;
	}
	
	/**
	 * Children of this class are used in Set collections in other code. As such, the equals method needs
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
		if(content == other.content)
		{
			// note that the equals method used in the following line is overriden and this method strictly requires that particular implementation
			return resource.equals(other.resource);
		}
		else
		{
			return false;
		}
	}
	
	// ------------------------------------------------------------------
	// ABSTRACT METHODS

	public abstract Set<AbstractSlot> getConnectedSlots();
	public abstract void addConnectedSlot(AbstractSlot slot);
	public abstract void removeConnectedSlot(AbstractSlot slot);
	public abstract boolean canAcceptMoreConnections();
}
