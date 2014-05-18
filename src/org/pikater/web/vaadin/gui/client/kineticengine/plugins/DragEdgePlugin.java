package org.pikater.web.vaadin.gui.client.kineticengine.plugins;

import net.edzard.kinetic.Rectangle.RectanglePoint;
import net.edzard.kinetic.event.EventType;
import net.edzard.kinetic.event.IEventListener;
import net.edzard.kinetic.event.KineticEvent;

import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTMisc;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine.EngineComponent;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.EdgePrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.ExperimentGraphItem;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.EdgePrototype.EndPoint;

@SuppressWarnings("deprecation")
public final class DragEdgePlugin implements IEnginePlugin
{
	public static String pluginID;
	
	/**
	 * The engine instance to work with.
	 */
	private final KineticEngine kineticEngine;
	
	/**
	 * The edge that is currently being dragged.
	 */
	private EdgePrototype draggedEdge;
	
	/**
	 * The dragged edge's endpoint that is being dragged.
	 */
	private EndPoint draggedEdgeEndpoint;
	
	/**
	 * The special event handlers/listeners to attach to graph items.
	 */
	private class DragMarkMouseDownListener extends PluginEventListener
	{
		private final EdgePrototype edgeBeingDragged;
		private final EndPoint draggedEdgeEndpoint;
		
		public DragMarkMouseDownListener(EdgePrototype edgeBeingDragged, EndPoint draggedEdgeEndpoint)
		{
			this.edgeBeingDragged = edgeBeingDragged;
			this.draggedEdgeEndpoint = draggedEdgeEndpoint;
		}

		@Override
		protected void handleInner(KineticEvent event)
		{
			edgeBeingDragged.edgeDrag_toBaseLine(
					edgeBeingDragged.getDragMark(draggedEdgeEndpoint).getAbsolutePointPosition(RectanglePoint.CENTER),
					edgeBeingDragged.getEndPoint(draggedEdgeEndpoint.getInverted())
			);
			setEdgeBeingDragged(edgeBeingDragged, draggedEdgeEndpoint);
			
			event.setProcessed();
			event.stopVerticalPropagation();
		}

		@Override
		protected String getListenerID()
		{
			return GWTMisc.getSimpleName(this.getClass());
		}
	}
	private class BoxMouseOverListener extends PluginEventListener
	{
		private final BoxPrototype parentBox;
		
		public BoxMouseOverListener(BoxPrototype parentBox)
		{
			this.parentBox = parentBox;
		}

		@Override
		protected void handleInner(KineticEvent event)
		{
			if(isAnEdgeBeingDragged() && !isStaticEndpointForThisDrag(parentBox))
			{
				parentBox.highlightAsNewEndpointCandidate();
				kineticEngine.draw(parentBox.getComponentToDraw());
				
				event.setProcessed();
				event.stopVerticalPropagation();
			}
		}

		@Override
		protected String getListenerID()
		{
			return GWTMisc.getSimpleName(this.getClass());
		}
	}
	private class BoxMouseOutListener extends PluginEventListener
	{
		private final BoxPrototype parentBox;
		
		public BoxMouseOutListener(BoxPrototype parentBox)
		{
			this.parentBox = parentBox;
		}

		@Override
		protected void handleInner(KineticEvent event)
		{
			if(isAnEdgeBeingDragged())
			{
				parentBox.cancelHighlightAsNewEndpointCandidate();
				kineticEngine.draw(parentBox.getComponentToDraw());
				
				event.setProcessed();
				event.stopVerticalPropagation();
			}
		}

		@Override
		protected String getListenerID()
		{
			return GWTMisc.getSimpleName(this.getClass());
		}
	}
	private class BoxMouseUpListener extends PluginEventListener
	{
		@Override
		protected void handleInner(KineticEvent event)
		{
			if(isAnEdgeBeingDragged())
			{
				// KineticJS bug: events can not be fired from named events, so a workaround: 
				fillRectangleEdgeDragMouseUpHandler.handle(null);
				event.setProcessed();
			}
			// event.stopPropagation(); // SERIOUSLY... don't set this up everytime. It can have devastating effects like preventing the Click event.
		}

		@Override
		protected String getListenerID()
		{
			return GWTMisc.getSimpleName(this.getClass());
		}
	};
	
	/**
	 * The special handlers that will temporarily replace the engine's when an edge drag is triggered.
	 */
	private final IEventListener fillRectangleEdgeDragMouseMoveHandler = new IEventListener()
	{
		@Override
		public void handle(KineticEvent event)
		{
			draggedEdge.edgeDrag_updateBaseLine(kineticEngine.getMousePosition());
			kineticEngine.draw(EngineComponent.LAYER_EDGES);
		}
	};
	private final IEventListener fillRectangleEdgeDragMouseUpHandler = new IEventListener()
	{
		@Override
		public void handle(KineticEvent event)
		{
			// IMPORTANT: don't violate the call order
			draggedEdge.edgeDrag_toEdge(false); // only switches the edge's internal state, doesn't update the edge or draws anything whatsoever
			BoxPrototype newEndpoint = kineticEngine.getHoveredBox();
			if(newEndpoint != null)
			{
				newEndpoint.cancelHighlightAsNewEndpointCandidate();
				BoxPrototype originalEndpoint = draggedEdge.getEndPoint(draggedEdgeEndpoint); 
				if(newEndpoint != originalEndpoint) // now we know for sure we are going to change the endpoint
				{
					// IMPORTANT: don't violate the call order
					SelectionPlugin selectionPlugin = (SelectionPlugin) kineticEngine.getPlugin(SelectionPlugin.pluginID);
					selectionPlugin.onEdgeDragOperation(draggedEdge, originalEndpoint, newEndpoint, draggedEdge.getEndPoint(draggedEdgeEndpoint.getInverted()));
					
					kineticEngine.swapEdgeEndpoint(); // changes the endpoint, updates the edge but doesn't draw
				}
			}
			resetEdgeDraggingVars();
			kineticEngine.draw(EngineComponent.STAGE);
		}
	};

	/**
	 * Constructor.
	 * @param kineticEngine
	 */
	public DragEdgePlugin(KineticEngine kineticEngine)
	{
		pluginID = GWTMisc.getSimpleName(this.getClass());
		this.kineticEngine = kineticEngine;
		this.draggedEdge = null;
	}
	
	// **********************************************************************************************
	// INHERITED INTERFACE
	
	@Override
	public String getPluginID()
	{
		return pluginID;
	}
	
	@Override
	public String[] getItemsToAttachTo()
	{
		return new String[] {
				GWTMisc.getSimpleName(EdgePrototype.class),
				GWTMisc.getSimpleName(BoxPrototype.class),
		};
	}
	
	@Override
	public void attachEventListeners(ExperimentGraphItem graphItem)
	{
		if(graphItem instanceof EdgePrototype)
		{
			EdgePrototype edge = (EdgePrototype)graphItem;
			setEdgeEventHandler(edge, EndPoint.FROM);
			setEdgeEventHandler(edge, EndPoint.TO);
		}
		else if(graphItem instanceof BoxPrototype)
		{
			BoxPrototype box = (BoxPrototype)graphItem;
			box.getMasterRectangle().addEventListener(EventType.Basic.MOUSEOVER.setName(pluginID), new BoxMouseOverListener(box));
			box.getMasterRectangle().addEventListener(EventType.Basic.MOUSEOUT.setName(pluginID), new BoxMouseOutListener(box));
			box.getMasterRectangle().addEventListener(EventType.Basic.MOUSEUP.setName(pluginID), new BoxMouseUpListener());
		}
		else
		{
			throw new IllegalStateException();
		}
	}
	
	// **********************************************************************************************
	// PUBLIC INTERFACE
	
	public boolean isAnEdgeBeingDragged()
	{
		return draggedEdge != null;
	}
	
	public EdgePrototype getDraggedEdge()
	{
		return draggedEdge;
	}
	
	public EndPoint getEndPointBeingChanged()
	{
		return draggedEdgeEndpoint;
	}
	
	// **********************************************************************************************
	// PRIVATE INTERFACE
	
	private void setEdgeEventHandler(EdgePrototype edge, EndPoint endPoint)
	{
		edge.getDragMark(endPoint).addEventListener(EventType.Basic.MOUSEDOWN.setName(pluginID), new DragMarkMouseDownListener(edge, endPoint));
	}
	
	private void setEdgeBeingDragged(EdgePrototype edge, EndPoint endPoint)
	{
		draggedEdge = edge;
		draggedEdgeEndpoint = endPoint;
		
		kineticEngine.removeFillRectangleHandlers();
		kineticEngine.getFillRectangle().addEventListener(EventType.Basic.MOUSEMOVE.toNativeEvent(), fillRectangleEdgeDragMouseMoveHandler);
		kineticEngine.getFillRectangle().addEventListener(EventType.Basic.MOUSEUP.toNativeEvent(), fillRectangleEdgeDragMouseUpHandler);
	}
	
	private boolean isStaticEndpointForThisDrag(BoxPrototype box)
	{
		return draggedEdge.getEndPoint(draggedEdgeEndpoint.getInverted()) == box;
	}
	
	private void resetEdgeDraggingVars()
	{
		draggedEdge = null;
		kineticEngine.setFillRectangleHandlers(); // automatically removes these handlers before setting the originals
	}
}
