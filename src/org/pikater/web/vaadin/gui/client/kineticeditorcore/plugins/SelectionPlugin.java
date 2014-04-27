package org.pikater.web.vaadin.gui.client.kineticeditorcore.plugins;

import java.util.HashSet;
import java.util.Set;

import net.edzard.kinetic.event.EventType;
import net.edzard.kinetic.event.KineticEvent;

import org.pikater.web.vaadin.gui.client.ClientVars;
import org.pikater.web.vaadin.gui.client.GlobalInterface;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.KineticEngine.EngineComponent;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.graphitems.EdgePrototype;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.graphitems.ExperimentGraphItem;

@SuppressWarnings("deprecation")
public class SelectionPlugin implements IEnginePlugin
{
	public static String pluginID;
	
	/**
	 * The engine instance to work with.
	 */
	private final KineticEngine kineticEngine;
	
	/**
	 * A self-explanatory variable.
	 */
	private final Set<BoxPrototype> selectedBoxes;
	
	/** 
	 * Edges with exactly one of their ends (a box) selected.
	 */
	private final Set<EdgePrototype> edgesInBetween;
	
	/**
	 * The special event handlers/listeners to attach to graph items.
	 */
	private final class BoxClickListener extends PluginEventListener
 	{
		private final BoxPrototype parentBox;
		
 		public BoxClickListener(BoxPrototype parentBox)
		{
			this.parentBox = parentBox;
		}

		@Override
		protected void handleInner(KineticEvent event)
		{
			if(ClientVars.isShiftKeyDown())
 			{
 				// select or deselect this box only
	 			invertSelection(false, parentBox);
 			}
 			else
 			{
 				if(kineticEngine.getSelectionContainer().getChildren().size() == 1)
 				{
 					// NOTE: extracting the deselect method before the condition breaks consistency because of the "null" as "deselectAllBut" argument.
 					if(parentBox.isSelected()) // just this one box is selected
 					{
 						// deselect it
 						deselectAllBut(null, false);
 					}
 					else // some other box is selected
 					{
 						// deselect it first
 						deselectAllBut(null, false);
 						
 						// and then select this box
 						invertSelection(false, parentBox);
 					}
 				}
 				else
 				{
 					// first deselect everything
 					deselectAllBut(parentBox, false);
						
 					if(!parentBox.isSelected())
 					{
 						// select this box
 						invertSelection(false, parentBox);
 					}
 				}
 			}
 			kineticEngine.draw(EngineComponent.STAGE);
 			event.setProcessed();
 			event.stopVerticalPropagation();
		}

		@Override
		protected String getListenerID()
		{
			return GlobalInterface.getSimpleName(this.getClass());
		}
 	};
	
	/**
	 * Constructor.
	 * @param kineticEngine
	 */
	public SelectionPlugin(KineticEngine kineticEngine)
	{
		pluginID = GlobalInterface.getSimpleName(this.getClass());
		this.kineticEngine = kineticEngine;
		this.selectedBoxes = new HashSet<BoxPrototype>();
		this.edgesInBetween = new HashSet<EdgePrototype>();
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
		return new String[] { GlobalInterface.getSimpleName(BoxPrototype.class) };
	}

	@Override
	public void attachEventListeners(ExperimentGraphItem graphItem)
	{
		if(graphItem instanceof BoxPrototype)
		{
			BoxPrototype box = (BoxPrototype)graphItem;
			box.getMasterRectangle().addEventListener(EventType.Basic.CLICK.setName(pluginID), new BoxClickListener(box));
		}
		else
		{
			throw new IllegalStateException();
		}
	}
	
	// *****************************************************************************************************
	// PUBLIC INTERFACE
		
	public void deselectAllBut(BoxPrototype exception, boolean drawOnFinish)
	{
		/*
		 * Notes:
		 * 1) No additional cleanups are needed. Everything is removed incrementally.
		 * 2) Don't draw anything - let the calling code handle this.
		 * 3) Can not use for-cycle because "selectionBoxes" variable gets updated in the underlying calls which generates a very sneaky and silent exception.
		 */
		
		// first remove the element not to be deselected
		if(exception != null)
		{
			selectedBoxes.remove(exception);
		}
		
		BoxPrototype box;
		boolean anythingDeselected = false;
		while(selectedBoxes.size() > 0)
		{
			box = selectedBoxes.iterator().next();
			invertSelection(false, box); // deselects edges also
			anythingDeselected = true;
		}
		
		// and finally return the element into the set
		if(exception != null)
		{
			selectedBoxes.add(exception);
		}
		
		if(anythingDeselected)
		{
			kineticEngine.draw(EngineComponent.STAGE);
		}
	}
	
	public void invertSelection(boolean drawChangesOnFinish, ExperimentGraphItem... graphItems)
	{
		for(ExperimentGraphItem graphItem : graphItems)
		{
			// do everything - reverse the selection state (programmatically and visually) and move the item to the correct container
			graphItem.invertSelection();
			
			// change the engine's inner data structures accordingly
			if(graphItem instanceof BoxPrototype)
			{
				if(graphItem.isSelected())
				{
					selectSingle((BoxPrototype)graphItem);
				}
				else
				{
					deselectSingle((BoxPrototype)graphItem);
				}
			}
		}
		if(drawChangesOnFinish)
		{
			kineticEngine.draw(EngineComponent.STAGE);
		}
	}
	
	public void onEdgeDragOperation(EdgePrototype edge, BoxPrototype originalEndPoint, BoxPrototype newEndPoint, BoxPrototype staticEndPoint)
	{
		// IMPORTANT: this code assumes that the endpoint swap has not been done yet
		if(originalEndPoint.isSelected() != newEndPoint.isSelected())
		{
			if(originalEndPoint.isSelected())
			{
				// the original endpoint IS selected and the new one is NOT, so:
				if(staticEndPoint.isSelected())
				{
					edge.invertSelection();
					edgesInBetween.add(edge);
				}
				else
				{
					edgesInBetween.remove(edge);
				}
			}
			else
			{
				// the original endpoint is NOT selected and the new one IS, so:
				if(staticEndPoint.isSelected())
				{
					edge.invertSelection();
					edgesInBetween.remove(edge);
				}
				else
				{
					edgesInBetween.add(edge);
				}
			}
		}
	}
	
	public void onEdgeCreateOperation(EdgePrototype edge)
	{
		assert(!edge.isSelected());
		switch (edge.getSelectedEndpointsCount())
		{
			case 1:
				edgesInBetween.add(edge);
				break;
			case 2:
				edge.invertSelection();
				break;
			default:
				break;
		}
	}
	
	public Set<EdgePrototype> getEdgesInBetween()
	{
		return edgesInBetween;
	}
	
	public Set<BoxPrototype> getSelectedBoxes()
	{
		return selectedBoxes;
	}
	
	// *****************************************************************************************************
	// PRIVATE INTERFACE
	
	private void selectSingle(BoxPrototype box)
	{
		selectedBoxes.add(box);
		for(EdgePrototype edge : box.connectedEdges)
		{
			// IMPORTANT: this code assumes that selection state of the item had already been changed before calling this method.
			if(edge.areBothEndsSelected())
			{
				invertSelection(false, edge);
				edgesInBetween.remove(edge);
			}
			else // 1 end is selected
			{
				edgesInBetween.add(edge);
			}
		}
	}
	
	private void deselectSingle(BoxPrototype box)
	{
		selectedBoxes.remove(box);
		for(EdgePrototype edge : box.connectedEdges)
		{
			// IMPORTANT: this code assumes that selection state of the item had already been changed before calling this method.
			// at least one of the edge ends must not be selected at this moment, so:
			if(edge.isExactlyOneEndSelected())
			{
				invertSelection(false, edge);
				edgesInBetween.add(edge);
			}
			else // neither end is selected
			{
				edgesInBetween.remove(edge);
			}
		}
	}
}
