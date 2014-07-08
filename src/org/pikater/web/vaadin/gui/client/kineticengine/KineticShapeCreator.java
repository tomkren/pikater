package org.pikater.web.vaadin.gui.client.kineticengine;

import org.pikater.shared.experiment.webformat.BoxInfo;
import org.pikater.web.vaadin.gui.client.kineticengine.experimentgraph.BoxGraphItemClient;
import org.pikater.web.vaadin.gui.client.kineticengine.experimentgraph.EdgeGraphItemClient;
import org.pikater.web.vaadin.gui.client.kineticengine.experimentgraph.EdgeGraphItemClient.EndPoint;

public class KineticShapeCreator
{
	/**
	 * Denotes whether a newly created node should be added to Kinetic automatically or the calling code takes responsibility to do it.
	 * Always use AUTOMATIC unless we add many nodes at once - then it is more efficient to call batch creation callback on kinetic state.  
	 */
	public static enum NodeRegisterType
	{
		AUTOMATIC,
		MANUAL
	};
	
	/**
	 * The main structure of kinetic's own variables. 
	 */
	private final KineticEngine kineticEngine;
	
	public KineticShapeCreator(KineticEngine kineticEngine)
	{
		this.kineticEngine = kineticEngine;
	}
	
	/**
	 * Creates a fully initialized box, if registered automatically. Only use this method when creating boxes in the GUI.
	 */
	public BoxGraphItemClient createBox(NodeRegisterType nrt, BoxInfo info)
	{
		BoxGraphItemClient result = new BoxGraphItemClient(kineticEngine, info);
		result.applyUserSettings();
		result.reloadVisualStyles(false);
		if(nrt == NodeRegisterType.AUTOMATIC)
		{
			kineticEngine.registerCreated(true, new BoxGraphItemClient[] { result }, null);
		}
	    return result;
	}
	
	/**
	 * Creates a fully initialized edge, if registered automatically and both endpoints are not null (they can be).
	 */
	public EdgeGraphItemClient createEdge(NodeRegisterType nrt, BoxGraphItemClient fromBox, BoxGraphItemClient toBox)
	{
		EdgeGraphItemClient result = new EdgeGraphItemClient(kineticEngine);
		result.applyUserSettings();
		result.reloadVisualStyles(false);
		result.setEndpoint(EndPoint.FROM, fromBox);
		result.setEndpoint(EndPoint.TO, toBox);
		if((fromBox != null) && (toBox != null))
		{
			result.updateEdge();
		}
		if(nrt == NodeRegisterType.AUTOMATIC)
		{
			kineticEngine.registerCreated(true, null, new EdgeGraphItemClient[] { result });
		}
		return result;
	}
}
