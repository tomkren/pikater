package org.pikater.web.vaadin.gui.client.kineticengine;

import org.pikater.shared.experiment.webformat.BoxInfo;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.EdgePrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.EdgePrototype.EndPoint;

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
	public BoxPrototype createBox(NodeRegisterType nrt, BoxInfo info)
	{
		BoxPrototype result = new BoxPrototype(kineticEngine, info);
		result.applyUserSettings();
		result.reloadVisualStyles(false);
		if(nrt == NodeRegisterType.AUTOMATIC)
		{
			kineticEngine.registerCreated(true, new BoxPrototype[] { result }, null);
		}
	    return result;
	}
	
	/**
	 * Creates a fully initialized edge, if registered automatically and both endpoints are not null (they can be).
	 */
	public EdgePrototype createEdge(NodeRegisterType nrt, BoxPrototype fromBox, BoxPrototype toBox)
	{
		EdgePrototype result = new EdgePrototype(kineticEngine);
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
			kineticEngine.registerCreated(true, null, new EdgePrototype[] { result });
		}
		return result;
	}
}
