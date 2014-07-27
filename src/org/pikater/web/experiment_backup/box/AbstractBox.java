package org.pikater.web.experiment_backup.box;

import org.pikater.shared.experiment.webformat.BoxType;

public abstract class AbstractBox
{
	// -----------------------------------------------------------
	// EDITOR RELATED FIELDS SUITABLE TO BE DISPLAYED TO THE USER
	
	/**
	 * The name of the box displayed within its container rectangle in the editor.
	 */
	public final String displayName;
	
	/**
	 * The description to display to the user when asking for details. 
	 */
	public final String description;
	
	/**
	 * The box's container rectangle's background image.
	 */
	public final String picture;
	
	/**
	 * The type of this box. 
	 */
	public final BoxType type;
	
	// -----------------------------------------------------------
	// CONSTRUCTOR
	
	public AbstractBox(String displayName, String description, String picture, BoxType type)
	{
		this.displayName = displayName;
		this.description = description;
		this.picture = picture;
		this.type = type;
	}
	
	public boolean isLeaf()
	{
		return this.type != BoxType.TRIBOX;
	}
}
