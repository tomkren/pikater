package org.pikater.web.experiment.box;

import java.util.Arrays;
import java.util.Collection;

import org.pikater.web.experiment.Box;
import org.pikater.web.experiment.BoxType;

public class WrapperBox extends AbstractBox
{
	private final Collection<AbstractBox> childBoxes;
	
	public WrapperBox(Box boxConfig, AbstractBox... childBoxes)
	{
		super(boxConfig.getName(), boxConfig.getDescription(), boxConfig.getPicture(), BoxType.WRAPPER); 
		
		this.childBoxes = Arrays.asList(childBoxes);
	}

	public Collection<AbstractBox> getChildBoxes()
	{
		return childBoxes;
	}
}
