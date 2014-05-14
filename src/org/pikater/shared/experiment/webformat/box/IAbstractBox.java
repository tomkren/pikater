package org.pikater.shared.experiment.webformat.box;

import org.pikater.shared.experiment.webformat.BoxType;

public interface IAbstractBox
{
	/**
	 * Gets the name of the box displayed within its container rectangle in the editor.
	 */
	public String getDisplayName();
	
	/**
	 * Gets the description to display to the user when asking for details. 
	 */
	public String getDescription();
	
	/**
	 * Gets the box's container rectangle's background image.
	 */
	public String getPicture();
	
	/**
	 * Gets the type of this box. 
	 */
	public BoxType getType();
}
