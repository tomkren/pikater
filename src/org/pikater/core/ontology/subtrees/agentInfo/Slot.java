package org.pikater.core.ontology.subtrees.agentInfo;

import org.pikater.core.ontology.subtrees.agentInfo.slotTypes.SlotTypes;
import org.pikater.shared.util.ICloneable;

import jade.content.Concept;

public class Slot implements Concept, ICloneable
{
	private static final long serialVersionUID = -1146617082338754196L;

	private String dataType;
	private String slotType;
	private String description;
	
	public Slot()
	{
	}

	public String getDataType() 
	{
		return dataType;
	}
	public void setDataType(String dataType)
	{
		this.dataType = dataType;
	}
	public String getSlotType()
	{
		return slotType;
	}
	public void setSlotType(String slotType)
	{
		this.slotType = slotType;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getDescription()
	{
		return this.description;
	}
	
	@Override
	public Slot clone()
	{
		Slot result;
		try
		{
			result = (Slot) super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new RuntimeException(e);
		}
		result.setDataType(dataType);
		result.setSlotType(slotType);
		result.setDescription(description);
		return result;
	}
	
	/*
	 * Some convenience interface.
	 */
	public boolean isCompatibleWith(Slot otherSlot)
	{
		return true; // TODO: a hack around...
		// return slotType.equals(otherSlot.slotType) when the experiment data streams are a bit more "standardized"
	}
	public boolean isErrorSlot()
	{
		return slotType.equals(SlotTypes.ERROR);
	}
}