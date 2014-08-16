package org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo.drag;

import net.edzard.kinetic.Vector2d;
import net.edzard.kinetic.event.IEventListener;
import net.edzard.kinetic.event.KineticEvent;

import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTCursorManager;
import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTCursorManager.MyCursor;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine.EngineComponent;
import org.pikater.web.vaadin.gui.client.kineticengine.graph.BoxGraphItemClient;
import org.pikater.web.vaadin.gui.client.kineticengine.graph.EdgeGraphItemClient;

@SuppressWarnings("deprecation")
public class BoxDragListenerProvider
{
	private final IBoxDragContext context;
	private Vector2d originalPosition;

	public BoxDragListenerProvider(IBoxDragContext context)
	{
		this.context = context;
	}
	
	//-----------------------------------------------------------
	// EVENT LISTENERS
	
	private BoxGraphItemClient boxBeingMoved;
	
	public IEventListener getDragStartListener()
	{
		return new IEventListener()
		{
			@Override
			public void handle(KineticEvent event)
			{
				GWTCursorManager.setCursorType(context.getEngine().getContext().getStageDOMElement(), MyCursor.MOVE);
				
				// first and foremost, remember & compute some basic information
				originalPosition = context.getCurrentPosition();
				boxBeingMoved = (context.getBoxesBeingMoved() != null) || (context.getBoxesBeingMoved().size() == 1) ?
						context.getBoxesBeingMoved().iterator().next() : null;
				
				/*
				 * Switch from normal mode to drag mode - turn all edges in between into dashed
				 * lines of a special color that connect the centers of their endpoint boxes.
				 */
				
				for(EdgeGraphItemClient edge : context.getEdgesInBetween())
				{
					edge.endPointDrag_toBaseLine(boxBeingMoved != null ? boxBeingMoved : edge.getSelectedEndpoint());
				}
				
				// draw and finish
				context.getEngine().draw(EngineComponent.LAYER_EDGES);
				event.stopVerticalPropagation();
			}
		};
	}
	
	public IEventListener getDragMoveListener()
	{
		return new IEventListener()
		{
			@Override
			public void handle(KineticEvent event)
			{
				// update the dashed edges
				for(EdgeGraphItemClient edge : context.getEdgesInBetween())
				{
					edge.endPointDrag_updateBaseLine(boxBeingMoved != null ? boxBeingMoved : edge.getSelectedEndpoint());
				}
				
				// draw and finish
				context.getEngine().draw(EngineComponent.LAYER_EDGES);
				event.stopVerticalPropagation();
			}
		};
	}
	
	public IEventListener getDragEndListener()
	{
		return new IEventListener()
		{
			/**
			 * End of a drag operation.
			 * Undo the effects of drag start and propagate the drag changes to selection items.
			 */
			@Override
			public void handle(KineticEvent event)
			{
				GWTCursorManager.rollBackCursor(context.getEngine().getContext().getStageDOMElement());
				
				// propagate the new position to selected items and switch edges in between back to normal mode
				context.getEngine().pushNewOperation(new MoveBoxesOperation(
						context,
						originalPosition,
						context.getCurrentPosition()
				));
				
				// finish
				event.stopVerticalPropagation();
			}
		};
	}
}