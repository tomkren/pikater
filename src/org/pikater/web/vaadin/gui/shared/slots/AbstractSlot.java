package org.pikater.web.vaadin.gui.shared.slots;

import java.util.Set;

public abstract class AbstractSlot
{
	public enum SlotContent
	{
		DATA,
		PARAMETER,
		ERROR
	};
	
	public final SlotContent content;
	
	public AbstractSlot(SlotContent content)
	{
		this.content = content;
	}
	
	// ------------------------------------------------------------------
	// PUBLIC INTERFACE
	
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
		return content == other.content;
	}
	
	// ------------------------------------------------------------------
	// ABSTRACT METHODS

	public abstract Set<AbstractSlot> getConnectedSlots();
	public abstract void addConnectedSlot(AbstractSlot slot);
	public abstract void removeConnectedSlot(AbstractSlot slot);
	public abstract boolean canAcceptMoreConnections();
}
