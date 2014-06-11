package org.pikater.web.vaadin.gui.client.kineticengine.plugins;

import net.edzard.kinetic.event.EventType;
import net.edzard.kinetic.event.IEventListener;
import net.edzard.kinetic.event.KineticEvent;

import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTKeyboardManager;
import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTMisc;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine.EngineComponent;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticShapeCreator.NodeRegisterType;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.EdgePrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.ExperimentGraphItem;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.EdgePrototype.EndPoint;
import org.pikater.web.vaadin.gui.shared.KineticComponentClickMode;

@SuppressWarnings("deprecation")
public final class CreateEdgePlugin implements IEnginePlugin
{
	public static String pluginID;
	
	/**
	 * The engine instance to work with.
	 */
	private final KineticEngine kineticEngine;
	
	/**
	 * The box that starts this operation.
	 */
	private BoxPrototype fromEndPoint;
	
	/**
	 * The edge that is being created.
	 */
	private EdgePrototype newEdge;
	
	/**
	 * The special event handlers/listeners to attach to graph items.
	 */
	private class BoxClickListener extends PluginEventListener
	{
		private final BoxPrototype parentBox;
		
		public BoxClickListener(BoxPrototype parentBox)
		{
			this.parentBox = parentBox;
		}

		@Override
		protected void handleInner(KineticEvent event)
		{
			if(isAnEdgeBeingCreated()) // whichever clickmode is currently set, finish the operation if started
			{
				// KineticJS bug: events can not be fired from named events, so a workaround:
				fillRectangleEdgeDragMouseDownHandler.handle(event); // finish this create edge operation
				
				event.stopVerticalPropagation();
				event.setProcessed();
			}
			else if((kineticEngine.getContext().getClickMode() == KineticComponentClickMode.CONNECTION) 
					|| GWTKeyboardManager.isAltKeyDown()) // when it's either clear we want to connect or the right modifier key is down 
			{
				// start this create edge operation
				fromEndPoint = parentBox;
				fromEndPoint.highlightAsNewEndpointCandidate();
				newEdge = kineticEngine.getContext().getShapeCreator().createEdge(NodeRegisterType.MANUAL, fromEndPoint, null);
				kineticEngine.registerCreated(true, null, new EdgePrototype[] { newEdge });
				newEdge.edgeDrag_toBaseLine(kineticEngine.getMousePosition(), fromEndPoint);
				
				kineticEngine.removeFillRectangleHandlers();
				kineticEngine.getFillRectangle().addEventListener(fillRectangleEdgeDragMouseMoveHandler, EventType.Basic.MOUSEMOVE);
				kineticEngine.getFillRectangle().addEventListener(fillRectangleEdgeDragMouseDownHandler, EventType.Basic.MOUSEDOWN);
				
				event.stopVerticalPropagation();
				event.setProcessed();
			}
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
			if(isAnEdgeBeingCreated() && (parentBox != fromEndPoint))
			{
				parentBox.highlightAsNewEndpointCandidate();
				kineticEngine.draw(parentBox.getComponentToDraw());
				
				event.stopVerticalPropagation();
				event.setProcessed();
			}
		}

		@Override
		protected String getListenerID()
		{
			return GWTMisc.getSimpleName(this.getClass());
		}
	}
	
	/**
	 * The special handlers that will temporarily replace the engine's when an edge drag is triggered.
	 */
	private final IEventListener fillRectangleEdgeDragMouseMoveHandler = new IEventListener()
	{
		@Override
		public void handle(KineticEvent event)
		{
			newEdge.edgeDrag_updateBaseLine(kineticEngine.getMousePosition());
			kineticEngine.draw(newEdge.getComponentToDraw());
		}
	};
	private final IEventListener fillRectangleEdgeDragMouseDownHandler = new IEventListener()
	{
		@Override
		public void handle(KineticEvent event)
		{
			// IMPORTANT: don't violate the call order
			BoxPrototype newEndpoint = kineticEngine.getHoveredBox();
			if(newEndpoint != null)
			{
				newEndpoint.cancelHighlightAsNewEndpointCandidate();
				if((newEndpoint != fromEndPoint) && fromEndPoint.isNotConnectedTo(newEndpoint))
				{
					newEdge.setEndpoint(EndPoint.TO, newEndpoint);
					newEdge.edgeDrag_toEdge(true); // switches the edge's internal state and updates it, doesn't draw anything
					
					SelectionPlugin selectionPlugin = (SelectionPlugin) kineticEngine.getPlugin(SelectionPlugin.pluginID);
					selectionPlugin.onEdgeCreateOperation(newEdge);
				}
				else
				{
					kineticEngine.getContext().getUndoRedoManager().undoAndDiscard();
				}
			}
			else
			{
				kineticEngine.getContext().getUndoRedoManager().undoAndDiscard();
			}
			fromEndPoint.cancelHighlightAsNewEndpointCandidate();
			fromEndPoint = null;
			newEdge = null;
			kineticEngine.setFillRectangleHandlers(); // automatically removes these handlers before setting the originals
			kineticEngine.draw(EngineComponent.STAGE);
		}
	};

	/**
	 * Constructor.
	 * @param kineticEngine
	 */
	public CreateEdgePlugin(KineticEngine kineticEngine)
	{
		pluginID = GWTMisc.getSimpleName(this.getClass());
		this.kineticEngine = kineticEngine;
		this.fromEndPoint = null;
		this.newEdge = null;
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
		return new String[] { GWTMisc.getSimpleName(BoxPrototype.class) };
	}
	
	@Override
	public void attachEventListeners(ExperimentGraphItem graphItem)
	{
		if(graphItem instanceof BoxPrototype)
		{
			BoxPrototype box = (BoxPrototype) graphItem;
			box.getMasterRectangle().addEventListener(new BoxClickListener(box), EventType.Basic.CLICK.withName(pluginID));
			box.getMasterRectangle().addEventListener(new BoxMouseOverListener(box), EventType.Basic.MOUSEOVER.withName(pluginID));
		}
		else
		{
			throw new IllegalStateException();
		}
	}
	
	// **********************************************************************************************
	// PRIVATE INTERFACE
	
	private boolean isAnEdgeBeingCreated()
	{
		return newEdge != null;
	}
}
