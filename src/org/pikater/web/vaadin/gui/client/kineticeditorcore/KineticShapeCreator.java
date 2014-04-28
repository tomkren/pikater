package org.pikater.web.vaadin.gui.client.kineticeditorcore;

import net.edzard.kinetic.Group;
import net.edzard.kinetic.Line;
import net.edzard.kinetic.Vector2d;

import org.pikater.web.vaadin.gui.client.kineticeditorcore.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.graphitems.EdgePrototype;

public class KineticShapeCreator
{
	/**
	 * Used to denote, whether the newly created node should be registered automatically or the calling code takes responsibility to do it.
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
	
	/**
	 * ID generation variables
	 */
	private int nextBoxID;

	public KineticShapeCreator(KineticEngine kineticStateWrapper)
	{
		this.kineticEngine = kineticStateWrapper;
		this.nextBoxID = 0;
	}
	
	/**
	 * Creates a fully initialized box, if registered automatically.
	 */
	public BoxPrototype createBox(NodeRegisterType nrt, String label, Vector2d position, Vector2d size)
	{
		BoxPrototype result = new BoxPrototype(kineticEngine, GlobalEngineConfig.getBoxIDFromNumber(nextBoxID++), label, position, size);
		if(nrt == NodeRegisterType.AUTOMATIC)
		{
			kineticEngine.registerCreated(result);
		}
	    return result;
	}
	
	/**
	 * Creates a partially initialized edge. The following methods have to be called later to make it fully initialized:
	 * 1) connectFromBox
	 * 2) connectToBox
	 * 3) updateEdge
	 * 
	 * In addition, the edge has to be registered, if not done so automatically by this method.
	 */
	public EdgePrototype createEdge(NodeRegisterType nrt, Group container, Line baseLine)
	{
		EdgePrototype result = new EdgePrototype(kineticEngine, container, baseLine);
		if(nrt == NodeRegisterType.AUTOMATIC)
		{
			kineticEngine.registerCreated(result);
		}
		return result;
	}
	
	/**
	 * Creates a fully initialized edge, if registered automatically and both endpoints are not null (they can be).
	 */
	public EdgePrototype createEdge(NodeRegisterType nrt, BoxPrototype fromBox, BoxPrototype toBox)
	{
		EdgePrototype result = new EdgePrototype(kineticEngine);
		result.connectFromBox(fromBox);
		result.connectToBox(toBox);
		if((fromBox != null) && (toBox != null))
		{
			result.updateEdge();
		}
		if(nrt == NodeRegisterType.AUTOMATIC)
		{
			kineticEngine.registerCreated(result);
		}
		return result;
	}
	
	/**
	 * Should be called before deserializing a stage.
	 */
	public void reset()
	{
		this.nextBoxID = 0;
	}
}
