package pikater.utility.experiment.slots;

import pikater.utility.experiment.parameters.info.ParamInfo;

public class ParameterSlot extends AbstractSlot
{
	public final ParamInfo paramRef;
	
	public ParameterSlot(String description, ParamInfo paramRef)
	{
		super(description);
		this.paramRef = paramRef;
	}

	@Override
	public SlotContent getContent()
	{
		return SlotContent.PARAMETER;
	}

	@Override
	protected boolean canBeConnectedToSpecific(AbstractSlot other)
	{
		ParameterSlot otherSlot = (ParameterSlot) other;
		
		/**
		 * Note that the equals method used in the following line is overriden and this method strictly requires that particular implementation.
		 * For details, see:
		 * 
		 * @see ParamInfo
		 * @see AbstractSlot
		 */
		return paramRef.equals(otherSlot.paramRef);
	}
}
