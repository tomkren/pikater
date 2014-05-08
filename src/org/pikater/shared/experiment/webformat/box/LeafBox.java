package org.pikater.shared.experiment.webformat.box;

import org.pikater.shared.experiment.universalformat.UniversalGui;
import org.pikater.shared.experiment.webformat.BoxInfo;
import org.pikater.shared.experiment.webformat.BoxType;

public class LeafBox extends AbstractBox implements IAbstractBox
{
	private static final long serialVersionUID = 2147762129911609358L;
	
	// -----------------------------------------------------------
	// FIELDS NOT BEING DISPLAYED TO THE USER
	
	public BoxInfo boxInfo;
	
	// -----------------------------------------------------------
	// CONSTRUCTOR
	
	/**
	 * Default Ctor keeps GWT and Vaadin happy.
	 */
	protected LeafBox()
	{
	}
	
	public LeafBox(Integer id, UniversalGui guiInfo, BoxInfo boxInfo) 
	{
		super(id, guiInfo);
		this.boxInfo = boxInfo;
	}
	
	// -----------------------------------------------------------
	// FIELDS BEING DISPLAYED TO THE USER

	@Override
	public String getDisplayName()
	{
		return boxInfo.getName();
	}

	@Override
	public String getDescription()
	{
		return boxInfo.getDescription();
	}

	@Override
	public String getPicture()
	{
		return boxInfo.getPicture();
	}

	@Override
	public BoxType getType()
	{
		return boxInfo.getType();
	}
}
