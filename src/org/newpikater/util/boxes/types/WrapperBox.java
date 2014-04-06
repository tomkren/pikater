package org.newpikater.util.boxes.types;

import java.util.Arrays;
import java.util.Collection;

public class WrapperBox extends AbstractBox
{
	private final Collection<AbstractBox> childBoxes;
	
	public WrapperBox(String displayName, String picture, String description, AbstractBox... childBoxes)
	{
		super(displayName, picture, description);
		
		this.childBoxes = Arrays.asList(childBoxes);
	}

	@Override
	public boolean isLeaf()
	{
		return false;
	}

	public Collection<AbstractBox> getChildBoxes()
	{
		return childBoxes;
	}
}
