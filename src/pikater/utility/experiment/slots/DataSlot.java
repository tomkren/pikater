package pikater.utility.experiment.slots;

public class DataSlot extends AbstractSlot
{
	public DataSlot(String description)
	{
		super(description);
		// TODO Auto-generated constructor stub
	}

	@Override
	public SlotContent getContent()
	{
		return SlotContent.DATA;
	}

	@Override
	protected boolean canBeConnectedToSpecific(AbstractSlot other)
	{
		// TODO Auto-generated method stub
		return true;
	}
}
