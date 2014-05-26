package org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo;

import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine.EngineComponent;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.EdgePrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.ExperimentGraphItem;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.SelectionPlugin;

/**
 * Operation handling only the first item registration / deregistration. Item
 * removing/adding again is done in {@link DeleteSelectedOperation}. 
 */
public final class ItemRegistrationOperation extends BiDiOperation
{
	private final ExperimentGraphItem[] graphItems;
	private final SelectionPlugin selectionPlugin;
	
	public ItemRegistrationOperation(KineticEngine kineticState, ExperimentGraphItem[] graphItems)
	{
		super(kineticState);
		this.graphItems = graphItems;
		this.selectionPlugin = (SelectionPlugin) kineticState.getPlugin(SelectionPlugin.pluginID);
	}
	
	@Override
	public void firstExecution()
	{
		for(ExperimentGraphItem graphItem : graphItems)
		{
			kineticEngine.attachPluginHandlersTo(graphItem);
			if(graphItem instanceof BoxPrototype)
			{
				kineticEngine.allBoxes.add((BoxPrototype)graphItem);
			}
			/*
			 * IMPORTANT NOTE: edges are not registered here, because not both endpoints are specified when
			 * creating them. It's the responsibility of edge creation code to register the edge in its
			 * endpoints.
			 */
			graphItem.registerInKinetic();
		}
		kineticEngine.draw(EngineComponent.STAGE);
	}
	
	@Override
	public void undo()
	{
		for(ExperimentGraphItem graphItem : graphItems)
		{
			if(graphItem.isSelected())
			{
				selectionPlugin.invertSelection(false, graphItem);
			}
			if(graphItem instanceof BoxPrototype)
			{
				kineticEngine.allBoxes.remove((BoxPrototype)graphItem);
			}
			else if(graphItem instanceof EdgePrototype)
			{
				((EdgePrototype) graphItem).unregisterEdgeInEndpoints();
			}
			else
			{
				throw new IllegalStateException("Unknown graph item type encountered.");
			}
			graphItem.unregisterInKinetic();
		}
		kineticEngine.draw(EngineComponent.STAGE); // some items may have been deselected + both edges and boxes can be unregistered
	}

	@Override
	public void redo()
	{
		for(ExperimentGraphItem graphItem : graphItems)
		{
			// undo method ensures that items are not selected, so only do the following:
			if(graphItem instanceof BoxPrototype)
			{
				kineticEngine.allBoxes.add((BoxPrototype)graphItem);
			}
			else if(graphItem instanceof EdgePrototype)
			{
				((EdgePrototype) graphItem).registerEdgeInEndpoints();
			}
			else
			{
				throw new IllegalStateException("Unknown graph item type encountered.");
			}
			graphItem.registerInKinetic();
		}
		kineticEngine.draw(EngineComponent.STAGE);
	}

	@Override
	public String toString()
	{
		return "ItemRegistrationOperation";
	}	
}
