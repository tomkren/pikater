package org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo;

import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.EdgePrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.EdgePrototype.EndPoint;
import org.pikater.web.vaadin.gui.client.kineticengine.modules.DragEdgeModule;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.base.BiDiOperation;

public class SwapEdgeEndPointOperation extends BiDiOperation
{
	private final EdgePrototype edge;
	private final EndPoint endPointType;
	private final BoxPrototype originalEndpoint;
	private final BoxPrototype newEndpoint;
	
	public SwapEdgeEndPointOperation(KineticEngine kineticEngine)
	{
		super(kineticEngine);
		
		DragEdgeModule edgeDragOperation = (DragEdgeModule) kineticEngine.getModule(DragEdgeModule.moduleID);
		this.edge = edgeDragOperation.getDraggedEdge();
		this.endPointType = edgeDragOperation.getEndPointBeingChanged();
		this.originalEndpoint = this.edge.getEndPoint(this.endPointType);
		this.newEndpoint = kineticEngine.getHoveredBox();
		
		if(this.newEndpoint == null)
		{
			throw new NullPointerException("Can not perform this operation because no hovered box was found. Did you"
					+ "somehow break the track mouse plugin's functions?");
		}
	}

	@Override
	public void firstExecution()
	{
		setEdgeEndPoint(newEndpoint); // same as Redo() but don't draw
	}

	@Override
	public void undo()
	{
		setEdgeEndPoint(originalEndpoint);
		drawChanges();
	}

	@Override
	public void redo()
	{
		setEdgeEndPoint(newEndpoint);
		drawChanges();
	}
	
	@Override
	public String toString()
	{
		return "SwapEdgeEndPointOperation";
	}
	
	// **********************************************************************************************
	// PRIVATE INTERFACE
	
	private void setEdgeEndPoint(BoxPrototype box)
	{
		edge.setEndpoint(endPointType, box);
		edge.updateEdge();
	}
	
	private void drawChanges()
	{
		kineticEngine.draw(edge.getComponentToDraw());
	}
}
