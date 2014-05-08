package org.pikater.shared.experiment.webformat.box;

import java.io.Serializable;

import org.pikater.shared.experiment.universalformat.UniversalGui;

/**
 * This class represents a generic "box" for the GUI. It is not meant to be used directly and it would be marked "abstract"
 * if the serialization mechanism of Vaadin supported it.
 */
public class AbstractBox implements Serializable
{
	private static final long serialVersionUID = 5718299819727491796L;
	
	public Integer id;
	public UniversalGui guiInfo;
	
	/** Default Ctor keeps GWT happy */
	protected AbstractBox()
	{
	}
	
	public AbstractBox(Integer id, UniversalGui guiInfo)
	{
		this.id = id;
		this.guiInfo = guiInfo;
	}
}
