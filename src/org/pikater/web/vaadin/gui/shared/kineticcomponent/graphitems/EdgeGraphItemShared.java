package org.pikater.web.vaadin.gui.shared.kineticcomponent.graphitems;

import org.pikater.web.vaadin.gui.client.kineticengine.experimentgraph.EdgeGraphItemClient;
import org.pikater.web.vaadin.gui.client.kineticengine.experimentgraph.EdgeGraphItemClient.EndPoint;

public class EdgeGraphItemShared
{
	public String fromBoxID;
	public String toBoxID;
	
	/** Keeps GWT and Vaadin happy */
	protected EdgeGraphItemShared()
	{
	}

	public EdgeGraphItemShared(EdgeGraphItemClient edge)
	{
		if(edge.areBothEndsDefined())
		{
			this.fromBoxID = edge.getEndPoint(EndPoint.FROM).getInfo().boxID;
			this.toBoxID = edge.getEndPoint(EndPoint.TO).getInfo().boxID;
		}
		else
		{
			throw new IllegalStateException("Edges were destroyed and apparently also "
					+ "unregistered from connected boxes. The connection has to be kept intact.");
		}
	}
	
	public static EdgeGraphItemShared[] fromArray(EdgeGraphItemClient[] edgeArray)
	{
		EdgeGraphItemShared[] result = new EdgeGraphItemShared[edgeArray.length];
		for(int i = 0; i < edgeArray.length; i++)
		{
			result[i] = new EdgeGraphItemShared(edgeArray[i]);
		}
		return result;
	}
}