package org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo;

import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.ItemRegistrationPlugin;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.ItemRegistrationPlugin.RegistrationOperation;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.SelectionPlugin;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.SelectionPlugin.SelectionOperation;

public class DeleteSelectedOperation extends BiDiOperation
{
	private final SelectionPlugin selectionPlugin;
	private final ItemRegistrationPlugin itemRegistrationPlugin;
	private final BoxPrototype[] originalSelectedBoxes;
	
	public DeleteSelectedOperation(KineticEngine kineticState)
	{
		super(kineticState);
		this.selectionPlugin = (SelectionPlugin) kineticState.getPlugin(SelectionPlugin.pluginID);
		this.itemRegistrationPlugin = (ItemRegistrationPlugin) kineticState.getPlugin(ItemRegistrationPlugin.pluginID);
		this.originalSelectedBoxes = this.selectionPlugin.getSelectedBoxes();
	}
	
	@Override
	public void firstExecution()
	{
		redo();
	}

	@Override
	public void undo()
	{
		itemRegistrationPlugin.doOperation(RegistrationOperation.REGISTER, false, originalSelectedBoxes);
		selectionPlugin.doSelectionRelatedOperation(SelectionOperation.SELECTION, true, true, originalSelectedBoxes);
	}
	
	@Override
	public void redo()
	{
		itemRegistrationPlugin.doOperation(RegistrationOperation.UNREGISTER, true, originalSelectedBoxes); // automatically deselects
	}

	@Override
	public String toString()
	{
		return "DeleteSelectedOperation";
	}
}
