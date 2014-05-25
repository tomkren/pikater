package org.pikater.web.vaadin.gui.client.kineticengine;

import net.edzard.kinetic.Group;
import net.edzard.kinetic.Line;
import net.edzard.kinetic.Vector2d;

import org.pikater.shared.experiment.webformat.BoxInfo;
import org.pikater.shared.experiment.webformat.box.LeafBox;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.EdgePrototype;

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
	 * Creates a fully initialized box, if registered automatically. Only use this method when importing experiments serialized from the server.
	 */
	public BoxPrototype createBox(NodeRegisterType nrt, LeafBox origin)
	{
		return createBox(nrt, origin.boxInfo, new Vector2d(origin.guiInfo.x, origin.guiInfo.y));
	}
	
	/**
	 * Creates a fully initialized box, if registered automatically. Only use this method when creating boxes in the GUI.
	 */
	public BoxPrototype createBox(NodeRegisterType nrt, BoxInfo info, Vector2d position)
	{
		BoxPrototype result = new BoxPrototype(kineticEngine, GlobalEngineConfig.getBoxIDFromNumber(nextBoxID++), info, position);
		if(nrt == NodeRegisterType.AUTOMATIC)
		{
			kineticEngine.registerCreated(true, result);
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
			kineticEngine.registerCreated(true, result);
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
			kineticEngine.registerCreated(true, result);
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
