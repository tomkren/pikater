package org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo;

import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.EdgePrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.EdgePrototype.EndPoint;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.DragEdgePlugin;

public class SwapEdgeEndPointOperation extends BiDiOperation
{
	private final EdgePrototype edge;
	private final EndPoint endPointType;
	private final BoxPrototype originalEndpoint;
	private final BoxPrototype newEndpoint;
	
	public SwapEdgeEndPointOperation(KineticEngine kineticEngine)
	{
		super(kineticEngine);
		DragEdgePlugin edgeDragOperation = (DragEdgePlugin) kineticEngine.getPlugin(DragEdgePlugin.pluginID);
		this.edge = edgeDragOperation.getDraggedEdge();
		this.endPointType = edgeDragOperation.getEndPointBeingChanged();
		this.originalEndpoint = this.edge.getEndPoint(this.endPointType);
		this.newEndpoint = kineticEngine.getHoveredBox();
		assert(this.newEndpoint != null);
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
		switch (endPointType)
		{
			case FROM:
				edge.connectFromBox(box);
				break;
			case TO:
				edge.connectToBox(box);
				break;
			default:
				throw new IllegalStateException();
		}
		edge.updateEdge();
	}
	
	private void drawChanges()
	{
		kineticEngine.draw(edge.getComponentToDraw());
	}
}
