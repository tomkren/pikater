package org.pikater.web.experiment.box;

import java.io.IOException;

import org.pikater.shared.XStreamHelper;

public abstract class AbstractBox
{
	// -----------------------------------------------------------
	// EDITOR RELATED FIELDS SUITABLE TO BE DISPLAYED TO THE USER
	
	/**
	 * The name of the box displayed within its container rectangle in the editor.
	 */
	public final String displayName;
	
	/**
	 * The box's container rectangle's background image.
	 */
	public final String picture;
	
	/**
	 * The description to display to the user when asking for details. 
	 */
	public final String description;
	
	// -----------------------------------------------------------
	// CONSTRUCTOR
	
	public AbstractBox(String displayName, String picture, String description)
	{
		this.displayName = displayName;
		this.picture = picture;
		this.description = description;
	}
	
	public static <T extends AbstractBox> T deserializeFromPath(String path) throws IOException
	{
		return XStreamHelper.deserializeFromPath(path);
	}
	
	public static <T extends AbstractBox> T deserializeFromXML(String xml)
	{
		return XStreamHelper.deserializeFromXML(xml);
	}

	public abstract boolean isLeaf();
}
