package org.pikater.web.vaadin.gui.shared.box;

import org.pikater.shared.experiment.box.BoxInfo;
import org.pikater.shared.experiment.box.BoxInfo.BoxType;

public class LeafBox extends AbstractBox
{
	// -----------------------------------------------------------
	// INTERNAL FIELDS NOT BEING DISPLAYED TO THE USER
	
	private final BoxInfo info;
	
	// -----------------------------------------------------------
	// CONSTRUCTOR
	
	public LeafBox(BoxInfo info)
	{
		this.info = info;
	}

	@Override
	public String getDisplayName()
	{
		return info.getName();
	}

	@Override
	public String getDescription()
	{
		return info.getDescription();
	}

	@Override
	public String getPicture()
	{
		return info.getPicture();
	}

	@Override
	public BoxType getType()
	{
		return info.getType();
	}
}
