package org.shared.slots;


public class ParameterSlot extends AbstractSlot
{	
	public ParameterSlot(String description)
	{
		super(description);
	}

	@Override
	public SlotContent getContent()
	{
		return SlotContent.PARAMETER;
	}

	@Override
	protected boolean canBeConnectedToSpecific(AbstractSlot other)
	{
		if (other instanceof ParameterSlot) {
			return true;
		}

		return false;
	}
}
