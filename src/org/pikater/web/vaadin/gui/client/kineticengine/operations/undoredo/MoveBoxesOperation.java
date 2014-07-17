package org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo;

import net.edzard.kinetic.Node;
import net.edzard.kinetic.Vector2d;

import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine.EngineComponent;
import org.pikater.web.vaadin.gui.client.kineticengine.graph.EdgeGraphItemClient;
import org.pikater.web.vaadin.gui.client.kineticengine.modules.SelectionModule;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.base.BiDiOperation;

public class MoveBoxesOperation extends BiDiOperation
{
	private final Node[] originalNodes;
	private final EdgeGraphItemClient[] originalEdgesInBetween;
	
	private Vector2d delta;
	
	public MoveBoxesOperation(KineticEngine kineticState, Node[] originalNodes, EdgeGraphItemClient[] originalEdgesInBetween)
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
		SelectionModule selectionModule = (SelectionModule) kineticEngine.getModule(SelectionModule.moduleID);
		
		delta = selectionModule.getSelectionContainer().getPosition();
		setNewSelectionPositions();
		selectionModule.getSelectionContainer().setPosition(Vector2d.origin);
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
		for(EdgeGraphItemClient edge : originalEdgesInBetween)
		{
			edge.updateEdge();
		}
		
		// and finally, draw changes
		kineticEngine.draw(EngineComponent.STAGE); // items may be deselected later when repeating the operation
	}
}
