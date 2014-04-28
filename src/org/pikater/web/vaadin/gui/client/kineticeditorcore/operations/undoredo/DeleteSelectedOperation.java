package org.pikater.web.vaadin.gui.client.kineticeditorcore.operations.undoredo;

import java.util.HashSet;
import java.util.Set;

import org.pikater.web.vaadin.gui.client.kineticeditorcore.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.KineticEngine.EngineComponent;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.graphitems.EdgePrototype;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.plugins.SelectionPlugin;

public class DeleteSelectedOperation extends BiDiOperation
{
	private final Set<BoxPrototype> originalSelectedBoxes;
	private final SelectionPlugin selectionPlugin;
	
	public DeleteSelectedOperation(KineticEngine kineticState)
	{
		super(kineticState);
		this.selectionPlugin = (SelectionPlugin) kineticState.getPlugin(SelectionPlugin.pluginID);
		this.originalSelectedBoxes = new HashSet<BoxPrototype>(this.selectionPlugin.getSelectedBoxes());
	}
	
	@Override
	public void firstExecution()
	{
		redo();
	}

	@Override
	public void undo()
	{
		// TODO: might as well save the current selection and load it after doing the "redo" operation...
		
		// restore the original state
		kineticEngine.allBoxes.addAll(originalSelectedBoxes);
		for(BoxPrototype box : originalSelectedBoxes)
		{
			for(EdgePrototype edge : box.connectedEdges)
			{
				if(!edge.getMasterNode().isRegistered())
				{
					edge.registerInKinetic();
				}
			}
			box.registerInKinetic();
			selectionPlugin.invertSelection(false, box);
		}
		
		// and finally, let the changes be drawn
		kineticEngine.draw(EngineComponent.STAGE); // dynamic layer needs to be drawn as well if it hasn't been drawn on selection
	}
	
	@Override
	public void redo()
	{
		// first deselect everything - might cause a bug with reexecuting if the selection changes
		selectionPlugin.deselectAllBut(null, false);
		
		// then remove the designated nodes
		for(BoxPrototype box : originalSelectedBoxes)
		{
			for(EdgePrototype edge : box.connectedEdges)
			{
				if(edge.getMasterNode().isRegistered())
				{
					edge.unregisterInKinetic();
				}
			}
			box.unregisterInKinetic();
		}
		kineticEngine.allBoxes.removeAll(originalSelectedBoxes);
		
		// and finally, let the changes be drawn
		kineticEngine.draw(EngineComponent.STAGE); // dynamic layer needs to be drawn as well if it hasn't been drawn on selection
	}

	@Override
	public String toString()
	{
		return "DeleteSelectedOperation";
	}
}
