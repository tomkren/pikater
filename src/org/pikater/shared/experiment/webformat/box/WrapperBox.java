package org.pikater.shared.experiment.webformat.box;

import java.util.Arrays;
import java.util.Collection;

import org.pikater.shared.experiment.webformat.BoxType;

public class WrapperBox extends AbstractBox implements IAbstractBox
{
	private static final long serialVersionUID = -1565787757937373570L;
	
	public final Collection<AbstractBox> childBoxes;
	
	public WrapperBox(Integer id, AbstractBox... childBoxes)
	{
		super(id);
		this.childBoxes = Arrays.asList(childBoxes); 
	}

	@Override
	public String getDisplayName()
	{
		return "Wrapper";
	}

	@Override
	public String getDescription()
	{
		return null;
	}

	@Override
	public String getPicture()
	{
		return ""; // TODO:
	}

	@Override
	public BoxType getType()
	{
		return BoxType.WRAPPER;
	}
}
