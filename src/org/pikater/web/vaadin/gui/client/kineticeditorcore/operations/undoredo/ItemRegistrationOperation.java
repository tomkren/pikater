package org.pikater.web.vaadin.gui.client.kineticeditorcore.operations.undoredo;

import org.pikater.web.vaadin.gui.client.kineticeditorcore.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.KineticEngine.EngineComponent;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.graphitems.ExperimentGraphItem;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.plugins.SelectionPlugin;

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
		}
		redo();
	}
	
	@Override
	public void undo()
	{
		for(ExperimentGraphItem graphItem : graphItems)
		{
			if(graphItem instanceof BoxPrototype)
			{
				if(graphItem.isSelected())
				{
					selectionPlugin.invertSelection(false, graphItem);
				}
				kineticEngine.allBoxes.remove((BoxPrototype)graphItem);
			}
			graphItem.unregisterInKinetic();
		}
		kineticEngine.draw(EngineComponent.STAGE); // some items may be selected
	}

	@Override
	public void redo()
	{
		for(ExperimentGraphItem graphItem : graphItems)
		{
			if(graphItem instanceof BoxPrototype)
			{
				kineticEngine.allBoxes.add((BoxPrototype)graphItem);
			}
			graphItem.registerInKinetic();
		}
		kineticEngine.draw(EngineComponent.STAGE); // some items may be selected from before "undo"
	}

	@Override
	public String toString()
	{
		return "ItemRegistrationOperation";
	}	
}
