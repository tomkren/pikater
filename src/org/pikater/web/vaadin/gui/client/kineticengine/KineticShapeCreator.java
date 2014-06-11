package org.pikater.web.vaadin.gui.client.kineticengine;

import net.edzard.kinetic.Group;
import net.edzard.kinetic.Line;

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
		if(nrt == NodeRegisterType.AUTOMATIC)
		{
			kineticEngine.registerCreated(true, new BoxPrototype[] { result }, null);
		}
	    return result;
	}
	
	/**
	 * Creates a partially initialized edge. The following methods have to be called later to make it fully initialized: <br>
	 * <ul>
	 * <li>connectFromBox</li>
	 * <li>connectToBox</li>
	 * <li>updateEdge</li>
	 * </ul>
	 * In addition, the edge has to be registered, if not done so automatically by this method.
	 */
	public EdgePrototype createEdge(NodeRegisterType nrt, Group container, Line baseLine)
	{
		EdgePrototype result = new EdgePrototype(kineticEngine, container, baseLine);
		if(nrt == NodeRegisterType.AUTOMATIC)
		{
			kineticEngine.registerCreated(true, null, new EdgePrototype[] { result });
		}
		return result;
	}
	
	/**
	 * Creates a fully initialized edge, if registered automatically and both endpoints are not null (they can be).
	 */
	public EdgePrototype createEdge(NodeRegisterType nrt, BoxPrototype fromBox, BoxPrototype toBox)
	{
		EdgePrototype result = new EdgePrototype(kineticEngine);
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
