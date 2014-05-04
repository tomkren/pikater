package org.pikater.shared.experiment.webformat.box;

import org.pikater.shared.experiment.webformat.BoxInfo;
import org.pikater.shared.experiment.webformat.BoxInfo.BoxType;

public class LeafBox extends AbstractBox
{
	// -----------------------------------------------------------
	// FIELDS NOT BEING DISPLAYED TO THE USER
	
	private final BoxInfo info;
	
	// -----------------------------------------------------------
	// CONSTRUCTOR
	
	public LeafBox(BoxInfo info)
	{
		this.info = info;
	}
	
	// -----------------------------------------------------------
	// FIELDS BEING DISPLAYED TO THE USER

	@Override
	public String getDisplayName()
	{
		return info.name;
	}

	@Override
	public String getDescription()
	{
		return info.description;
	}

	@Override
	public String getPicture()
	{
		return info.picture;
	}

	@Override
	public BoxType getType()
	{
		return info.type;
	}
}
