package org.pikater.web.vaadin.gui.shared.box;

import java.util.Arrays;
import java.util.Collection;

import org.pikater.shared.experiment.box.BoxInfo.BoxType;

public class WrapperBox extends AbstractBox
{
	public final Collection<AbstractBox> childBoxes;
	
	public WrapperBox(AbstractBox... childBoxes)
	{
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
