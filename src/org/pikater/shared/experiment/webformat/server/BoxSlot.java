package org.pikater.shared.experiment.webformat.server;

import org.pikater.core.ontology.subtrees.agentInfo.Slot;

public class BoxSlot
{
	public static enum SlotType
	{
		INPUT,
		OUTPUT;
		
		public SlotType getOther()
		{
			if(this == INPUT)
			{
				return OUTPUT;
			}
			else
			{
				return INPUT;
			}
		}
	}
	
	private final BoxInfoServer parentBox;
	private final Slot childSlot;
	private final SlotType childSlotsType;
	
	public BoxSlot(BoxInfoServer parentBox, Slot childSlot)
	{
		this.parentBox = parentBox;
		if(parentBox.getAssociatedAgent().getInputSlots().contains(childSlot))
		{
			this.childSlotsType = SlotType.INPUT;
		}
		else if(parentBox.getAssociatedAgent().getOutputSlots().contains(childSlot))
		{
			this.childSlotsType = SlotType.OUTPUT;
		}
		else
		{
			this.childSlotsType = null;
		}
		this.childSlot = childSlot;
	}

	public BoxInfoServer getParentBox()
	{
		return parentBox;
	}

	public Slot getChildSlot()
	{
		return childSlot;
	}

	public SlotType getChildSlotsType()
	{
		return childSlotsType;
	}
	
	public boolean isFromEndpoint()
	{
		return childSlotsType == SlotType.OUTPUT;
	}
	
	public boolean isValid()
	{
		if((childSlotsType == null) || (parentBox == null) || (childSlot == null))
		{
			return false;
		}
		// parent-child relationship between the box and slot is ensured by the constructor and slot type null check above
		return true;
	}
	
	//---------------------------------------------------------
	// COMPARISON INTERFACE - GENERATED WITH ECLIPSE
	
	/*
	 * NOT NEEDED AFTER ALL BUT KEPT FOR NEEDY TIMES
	 * 1) Equals comparisons were removed because they're unwanted and could
	 * potentially cause bugs. Instance comparison is sufficient for us 
	 * in this case.
	 * 2) Since we use validation as null checks before using instances
	 * of this class in collections, we don't have to do check again
	 * in here.
	 */
	
	/*
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BoxSlot other = (BoxSlot) obj;
		if ((childSlot != other.childSlot) ||  (childSlotsType != other.childSlotsType) || (parentBox != other.parentBox))
		{
			return false;
		}
		return true;
	}
	*/
}