package org.pikater.shared.experiment.webformat;

import java.io.Serializable;

public class BoxInfo implements Serializable
{
	private static final long serialVersionUID = 3875674558654733345L;
	
	/*
	 * General info.
	 */
	public String boxID;
	public String boxTypeName;
	public String displayName;
	
	/*
	 * Position.
	 */
	public int initialX;
	public int initialY;
	
	/*
	 * Picture info. 
	 */
	public String pictureURL;
    private String name;
    private String description;
    private String picture;
    private BoxType type;

    /** Keeps GWT and Vaadin happy */
	protected BoxInfo()
	{
	}

	/** 
	 * @param boxID
	 * @param boxTypeName
	 * @param displayName
	 * @param initialX
	 * @param initialY
	 * @param pictureURL
     */
	public BoxInfo(String boxID, String boxTypeName, String displayName, int initialX, int initialY, String pictureURL)
	{
		this.boxID = boxID;
		this.boxTypeName = boxTypeName;
		this.displayName = displayName;
		this.initialX = initialX;
		this.initialY = initialY;
		this.pictureURL = pictureURL;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public BoxType getType() {
        return type;
    }

    public void setType(BoxType type) {
        this.type = type;
    }
}