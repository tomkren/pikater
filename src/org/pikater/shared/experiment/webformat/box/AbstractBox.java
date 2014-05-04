package org.pikater.shared.experiment.webformat.box;

import org.pikater.shared.experiment.webformat.BoxInfo.BoxType;

public abstract class AbstractBox
{
	/**
	 * Gets the name of the box displayed within its container rectangle in the editor.
	 */
	public abstract String getDisplayName();
	
	/**
	 * Gets the description to display to the user when asking for details. 
	 */
	public abstract String getDescription();
	
	/**
	 * Gets the box's container rectangle's background image.
	 */
	public abstract String getPicture();
	
	/**
	 * Gets the type of this box. 
	 */
	public abstract BoxType getType();
	
	public boolean isLeaf()
	{
		return !isWrapper();
	}
	
	public boolean isWrapper()
	{
		return getType() == BoxType.WRAPPER;
	}
}
