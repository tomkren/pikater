package org.pikater.web.vaadin.gui.client.kineticeditorcore.operations.undoredo;

import net.edzard.kinetic.Node;
import net.edzard.kinetic.Vector2d;

import org.pikater.web.vaadin.gui.client.kineticeditorcore.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.KineticEngine.EngineComponent;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.graphitems.EdgePrototype;

public class MoveBoxesOperation extends BiDiOperation
{
	private final Node[] originalNodes;
	private final EdgePrototype[] originalEdgesInBetween;
	
	private Vector2d delta;
	
	public MoveBoxesOperation(KineticEngine kineticState, Node[] originalNodes, EdgePrototype[] originalEdgesInBetween)
	{
		super(kineticState);
		this.originalNodes = originalNodes;
		this.originalEdgesInBetween = originalEdgesInBetween;
		this.delta = null;
	}
	
	@Override
	public void firstExecution()
	{
		// Note: this is already after the drag itself
		
		delta = kineticEngine.getSelectionContainer().getPosition();
		setNewSelectionPositions();
		kineticEngine.getSelectionContainer().setPosition(Vector2d.origin);
	}
	
	@Override
	public void undo()
	{
		// restore original selection positions
		for(Node node : originalNodes)
		{
			node.setPosition(node.getX() - this.delta.x, node.getY() - this.delta.y);
		}
		
		// and finally, update the edges caught between selection and dynamic layer and draw changes
		updateEdgesInBetweenAndDraw();
	}

	@Override
	public void redo()
	{
		// first move the selection
		setNewSelectionPositions();
		
		// and then update the edges caught between selection and dynamic layer and draw changes
		updateEdgesInBetweenAndDraw();
	}
	
	@Override
	public String toString()
	{
		return "MoveBoxesOperation";
	}

	// **********************************************************************************
	// PRIVATE INTERFACE
	
	private void setNewSelectionPositions()
	{
		// propagate the drag operation changes to the selection items - update both boxes and edges
		for(Node node : originalNodes)
		{
			node.setPosition(node.getX() + this.delta.x, node.getY() + this.delta.y);
		}
	}
	
	private void updateEdgesInBetweenAndDraw()
	{
		// update the edges caught between selection and dynamic layer
		for(EdgePrototype edge : originalEdgesInBetween)
		{
			edge.updateEdge();
		}
		
		// and finally, draw changes
		kineticEngine.draw(EngineComponent.STAGE); // items may be deselected later when repeating the operation
	}
}
