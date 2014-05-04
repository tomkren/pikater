package org.pikater.web.experiment_backup.box;

import java.util.Arrays;
import java.util.Collection;

import org.pikater.shared.experiment.webformat.BoxInfo;
import org.pikater.shared.experiment.webformat.BoxInfo.BoxType;

public class WrapperBox extends AbstractBox
{
	private final Collection<AbstractBox> childBoxes;
	
	public WrapperBox(BoxInfo boxConfig, AbstractBox... childBoxes)
	{
		super(boxConfig.getName(), boxConfig.getDescription(), boxConfig.getPicture(), BoxType.WRAPPER); 
		
		this.childBoxes = Arrays.asList(childBoxes);
	}

	public Collection<AbstractBox> getChildBoxes()
	{
		return childBoxes;
	}
}
