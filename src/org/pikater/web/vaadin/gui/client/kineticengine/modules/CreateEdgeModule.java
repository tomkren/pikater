package org.pikater.web.vaadin.gui.client.kineticengine.modules;

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
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.ExperimentGraphItem.VisualStyle;
import org.pikater.web.vaadin.gui.client.kineticengine.modules.base.BoxListener;
import org.pikater.web.vaadin.gui.client.kineticengine.modules.base.IEngineModule;
import org.pikater.web.vaadin.gui.shared.KineticComponentClickMode;

@SuppressWarnings("deprecation")
public final class CreateEdgeModule implements IEngineModule
{
	public static String moduleID;
	
	/**
	 * The engine instance to work with.
	 */
	private final KineticEngine kineticEngine;
	
	/**
	 * The class to handle all logic behind edge creating.
	 */
	private class CreateEdgeContext
	{
		/**
		 * The selection module instance to use.
		 */
		private final SelectionModule selectionModule;
		
		/**
		 * The box that starts this operation.
		 */
		private BoxPrototype fromEndPoint;
		
		/**
		 * The edge that is being created.
		 */
		private EdgePrototype newEdge;
		
		public CreateEdgeContext()
		{
			this.selectionModule = (SelectionModule) kineticEngine.getModule(SelectionModule.moduleID);
			reset();
		}
		
		// --------------------------------------------------------
		// OPERATION START/END ROUTINES
		
		public void startOperation(BoxPrototype box)
		{
			fromEndPoint = box;
			
			// create the new edge and convert it to baseline
			newEdge = kineticEngine.getContext().getShapeCreator().createEdge(NodeRegisterType.MANUAL, fromEndPoint, null);
			newEdge.edgeDrag_toBaseLine(kineticEngine.getMousePosition(), fromEndPoint);
		}
		
		/**
		 * This method has to call {@link finishOperation(BoxPrototype toEndPoint)} for consistency.
		 */
		public void stopOperation()
		{
			finishOperation(null);
		}
		
		public void finishOperation(BoxPrototype toEndPoint)
		{
			// IMPORTANT: don't violate the call order
			fromEndPoint.setVisualStyle(VisualStyle.NOT_HIGHLIGHTED);
			if(toEndPoint != null)
			{
				toEndPoint.setVisualStyle(VisualStyle.NOT_HIGHLIGHTED);
				if(fromEndPoint.isNotConnectedTo(toEndPoint))
				{
					newEdge.setEndpoint(EndPoint.TO, toEndPoint);
					newEdge.edgeDrag_toEdge(true); // switches the edge's internal state and updates it, doesn't draw anything

					selectionModule.onEdgeCreateOperation(newEdge);
				}
				else
				{
					kineticEngine.getContext().getHistoryManager().undoAndDiscard();
				}
			}
			else
			{
				kineticEngine.getContext().getHistoryManager().undoAndDiscard();
			}
			reset();
			
			kineticEngine.setFillRectangleHandlers(); // automatically removes these handlers before setting the originals
			kineticEngine.draw(EngineComponent.STAGE);
		}
		
		// --------------------------------------------------------
		// VARIOUS
		
		public BoxPrototype getFromEndPoint()
		{
			return fromEndPoint;
		}
		
		public EdgePrototype getNewEdge()
		{
			return newEdge;
		}
		
		public boolean isAnEdgeBeingCreated()
		{
			return getNewEdge() != null;
		}
		
		public void reset()
		{
			this.fromEndPoint = null;
			this.newEdge = null;
		}
	}
	
	/**
	 * The instance handling this operation's state.
	 */
	private final CreateEdgeContext edgeCreationContext;
	
	// **********************************************************************************************
	// ITEM LISTENERS
	
	/**
	 * The special event handlers/listeners to attach to graph items.
	 */
	private class BoxClickListener extends BoxListener
	{
		public BoxClickListener(BoxPrototype parentBox)
		{
			super(parentBox);
		}
		
		@Override
		protected void handleInner(KineticEvent event)
		{
			if(edgeCreationContext.isAnEdgeBeingCreated()) // whichever clickmode is currently set, finish the operation if started
			{
				/*
				 * Finish this create edge operation.
				 */
				if(parentBox != edgeCreationContext.getFromEndPoint())
				{
					edgeCreationContext.finishOperation(parentBox);
				}
				
				event.stopVerticalPropagation();
				event.setProcessed();
			}
			else if((kineticEngine.getContext().getClickMode() == KineticComponentClickMode.CONNECTION) 
					|| GWTKeyboardManager.isAltKeyDown()) // when it's either clear we want to connect or the right modifier key is down 
			{
				/*
				 * Start this create edge operation.
				 */
				
				edgeCreationContext.startOperation(parentBox);
				parentBox.setVisualStyle(VisualStyle.HIGHLIGHTED);
				
				// register the required handlers
				kineticEngine.removeFillRectangleHandlers();
				kineticEngine.getFillRectangle().addEventListener(fillRectangleEdgeDragMouseMoveHandler, EventType.Basic.MOUSEMOVE);
				kineticEngine.getFillRectangle().addEventListener(fillRectangleEdgeDragMouseDownHandler, EventType.Basic.MOUSEDOWN);
				
				// and display the new edge
				kineticEngine.registerCreated(true, null, new EdgePrototype[] { edgeCreationContext.getNewEdge() });
				
				event.stopVerticalPropagation();
				event.setProcessed();
			}
		}
	}
	private class BoxMouseEnterListener extends BoxListener
	{
		public BoxMouseEnterListener(BoxPrototype parentBox)
		{
			super(parentBox);
		}

		@Override
		protected void handleInner(KineticEvent event)
		{
			if(edgeCreationContext.isAnEdgeBeingCreated() && (parentBox != edgeCreationContext.getFromEndPoint()))
			{
				parentBox.setVisualStyle(VisualStyle.HIGHLIGHTED);
				kineticEngine.draw(parentBox.getComponentToDraw());
				
				event.stopVerticalPropagation();
				event.setProcessed();
			}
		}
	}
	private class BoxMouseLeaveListener extends BoxListener
	{
		public BoxMouseLeaveListener(BoxPrototype parentBox)
		{
			super(parentBox);
		}

		@Override
		protected void handleInner(KineticEvent event)
		{
			if(edgeCreationContext.isAnEdgeBeingCreated() && (parentBox != edgeCreationContext.getFromEndPoint()))
			{
				parentBox.setVisualStyle(VisualStyle.NOT_HIGHLIGHTED);
				kineticEngine.draw(parentBox.getComponentToDraw());
				
				event.stopVerticalPropagation();
				event.setProcessed();
			}
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
			edgeCreationContext.getNewEdge().edgeDrag_updateBaseLine(kineticEngine.getMousePosition());
			kineticEngine.draw(edgeCreationContext.getNewEdge().getComponentToDraw());
		}
	};
	private final IEventListener fillRectangleEdgeDragMouseDownHandler = new IEventListener()
	{
		@Override
		public void handle(KineticEvent event)
		{
			edgeCreationContext.stopOperation();
			
			event.stopVerticalPropagation();
			event.setProcessed();
		}
	};
	
	// **********************************************************************************************
	// CONSTRUCTOR

	public CreateEdgeModule(KineticEngine kineticEngine)
	{
		moduleID = GWTMisc.getSimpleName(this.getClass());
		this.kineticEngine = kineticEngine;
		this.edgeCreationContext = new CreateEdgeContext();
	}
	
	// **********************************************************************************************
	// INHERITED INTERFACE
	
	@Override
	public String getModuleID()
	{
		return moduleID;
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
			box.getMasterNode().addEventListener(new BoxClickListener(box), EventType.Basic.CLICK.withName(moduleID));
			box.getMasterNode().addEventListener(new BoxMouseEnterListener(box), EventType.Basic.MOUSEENTER.withName(moduleID));
			box.getMasterNode().addEventListener(new BoxMouseLeaveListener(box), EventType.Basic.MOUSELEAVE.withName(moduleID));
		}
		else
		{
			throw new IllegalStateException();
		}
	}
}