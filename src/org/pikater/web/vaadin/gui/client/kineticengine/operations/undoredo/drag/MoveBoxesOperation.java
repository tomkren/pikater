package org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo.drag;

import net.edzard.kinetic.Node;
import net.edzard.kinetic.Vector2d;

import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine.EngineComponent;
import org.pikater.web.vaadin.gui.client.kineticengine.graph.EdgeGraphItemClient;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.base.BiDiOperation;

/** 
 * Operation to move some graph items as a group and update related edges. This operation
 * is selection independent, it doesn't deselect or select anything.
 */
public class MoveBoxesOperation extends BiDiOperation implements IBoxDragEndContext
{
	// main operation related variables 
	private final IBoxDragContext context;
	private final Node[] allMovedNodes; // boxes and edges
	private final EdgeGraphItemClient[] edgesInBetween;
	
	// positions to keep track of
	private final Vector2d originalPosition;
	private final Vector2d newPosition;
	private final Vector2d delta;
	
	public MoveBoxesOperation(IBoxDragContext context, Node[] allMovedNodes, EdgeGraphItemClient[] edgesInBetween, Vector2d originalPosition,
			Vector2d newPosition)
	{
		super(context.getEngine());
		
		this.context = context;
		this.allMovedNodes = allMovedNodes;
		this.edgesInBetween = edgesInBetween;
		
		this.originalPosition = originalPosition;
		this.newPosition = newPosition;
		this.delta = new Vector2d(newPosition).sub(originalPosition);
	}
	
	// **********************************************************************************
	// INHERITED OPERATION INTERFACE

	/**
	 * NOTE: this method is executed AFTER the drag itself.
	 */
	@Override
	public void firstExecution()
	{
		// switch from drag mode back to normal mode (but don't recompute the edges yet)
		for(EdgeGraphItemClient edge : edgesInBetween)
		{
			edge.endPointDrag_toEdge();
		}
				
		redo();
	}
	
	@Override
	public void undo()
	{
		// first restore original positions
		context.setOriginalPositions(allMovedNodes, this);
		
		// and finally, update the edges caught between selection and dynamic layer and draw changes
		recomputeEdgesInBetweenAndDraw();
	}

	@Override
	public void redo()
	{
		// first move the nodes
		context.setNewPositions(allMovedNodes, this);
		
		// and then update the edges caught between selection and dynamic layer and draw changes
		recomputeEdgesInBetweenAndDraw();
	}
	
	// **********************************************************************************
	// INHERITED BOX DRAG CONTEXT INTERFACE
	
	@Override
	public Vector2d getOriginalPosition()
	{
		return originalPosition;
	}

	@Override
	public Vector2d getNewPosition()
	{
		return newPosition;
	}

	@Override
	public Vector2d getDelta()
	{
		return delta;
	}
	
	@Override
	public String toString()
	{
		return "MoveBoxesOperation";
	}
	
	// **********************************************************************************
	// PRIVATE INTERFACE

	private void recomputeEdgesInBetweenAndDraw()
	{
		// update the edges caught between selection and dynamic layer
		for(EdgeGraphItemClient edge : edgesInBetween)
		{
			edge.updateEdge();
		}

		// and finally, draw changes
		kineticEngine.draw(EngineComponent.STAGE); // items may be deselected later when repeating the operation
	}
}
