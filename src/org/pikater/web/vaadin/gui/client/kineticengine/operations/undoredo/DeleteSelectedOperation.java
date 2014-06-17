package org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo;

import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.modules.ItemRegistrationModule;
import org.pikater.web.vaadin.gui.client.kineticengine.modules.SelectionModule;
import org.pikater.web.vaadin.gui.client.kineticengine.modules.ItemRegistrationModule.RegistrationOperation;
import org.pikater.web.vaadin.gui.client.kineticengine.modules.SelectionModule.SelectionOperation;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.base.BiDiOperation;

public class DeleteSelectedOperation extends BiDiOperation
{
	private final SelectionModule selectionModule;
	private final ItemRegistrationModule itemRegistrationModule;
	private final BoxPrototype[] originalSelectedBoxes;
	
	public DeleteSelectedOperation(KineticEngine kineticState)
	{
		super(kineticState);
		this.selectionModule = (SelectionModule) kineticState.getModule(SelectionModule.moduleID);
		this.itemRegistrationModule = (ItemRegistrationModule) kineticState.getModule(ItemRegistrationModule.moduleID);
		this.originalSelectedBoxes = this.selectionModule.getSelectedBoxes();
	}
	
	@Override
	public void firstExecution()
	{
		redo();
	}

	@Override
	public void undo()
	{
		itemRegistrationModule.doOperation(RegistrationOperation.REGISTER, false, originalSelectedBoxes);
		selectionModule.doSelectionRelatedOperation(SelectionOperation.SELECTION, true, true, originalSelectedBoxes);
	}
	
	@Override
	public void redo()
	{
		itemRegistrationModule.doOperation(RegistrationOperation.UNREGISTER, true, originalSelectedBoxes); // automatically deselects
	}

	@Override
	public String toString()
	{
		return "DeleteSelectedOperation";
	}
}
