package org.pikater.web.experiment.box;

import java.util.Arrays;
import java.util.Collection;

import org.pikater.core.options.LogicalUnitDescription;
import org.pikater.shared.experiment.BoxType;

public class WrapperBox extends AbstractBox
{
	private final Collection<AbstractBox> childBoxes;
	
	public WrapperBox(LogicalUnitDescription boxConfig, AbstractBox... childBoxes)
	{
		super(boxConfig.getDisplayName(), boxConfig.getDescription(), boxConfig.getPicture(), BoxType.WRAPPER); 
		
		this.childBoxes = Arrays.asList(childBoxes);
	}

	public Collection<AbstractBox> getChildBoxes()
	{
		return childBoxes;
	}
}
