package org.pikater.web.vaadin.gui.shared.kineticcomponent.graphitems;

import org.pikater.web.vaadin.gui.client.kineticengine.experimentgraph.BoxGraphItemClient;

public class BoxGraphItemShared
{
	public String boxID;
	
	/** Keeps GWT and Vaadin happy */
	protected BoxGraphItemShared()
	{
	}

	public BoxGraphItemShared(BoxGraphItemClient box)
	{
		this.boxID = box.getInfo().boxID;
	}
	
	public static BoxGraphItemShared[] fromArray(BoxGraphItemClient[] boxArray)
	{
		BoxGraphItemShared[] result = new BoxGraphItemShared[boxArray.length];
		for(int i = 0; i < boxArray.length; i++)
		{
			result[i] = new BoxGraphItemShared(boxArray[i]);
		}
		return result;
	}
}