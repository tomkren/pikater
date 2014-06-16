package org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo;

import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.EdgePrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.modules.ItemRegistrationModule;
import org.pikater.web.vaadin.gui.client.kineticengine.modules.ItemRegistrationModule.RegistrationOperation;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.base.BiDiOperation;

/**
 * Operation handling only the first item registration / deregistration. Item
 * removing/adding again is done in {@link DeleteSelectedOperation}. 
 */
public final class ItemRegistrationOperation extends BiDiOperation
{
	private final BoxPrototype[] boxes;
	private final EdgePrototype[] edges;
	private final ItemRegistrationModule itemRegistrationModule;
	
	public ItemRegistrationOperation(KineticEngine kineticState, BoxPrototype[] boxes, EdgePrototype[] edges)
	{
		super(kineticState);
		
		this.boxes = boxes == null ? new BoxPrototype[0] : boxes;
		this.edges = edges == null ? new EdgePrototype[0] : edges;
		this.itemRegistrationModule = (ItemRegistrationModule) kineticState.getModule(ItemRegistrationModule.moduleID);
	}
	
	@Override
	public void firstExecution()
	{
		for(BoxPrototype box : boxes)
		{
			kineticEngine.attachModuleHandlersTo(box);
		}
		for(EdgePrototype edge : edges)
		{
			kineticEngine.attachModuleHandlersTo(edge);
		}
		redo();
	}
	
	@Override
	public void undo()
	{
		itemRegistrationModule.doOperation(RegistrationOperation.UNREGISTER, false, boxes);
		itemRegistrationModule.doOperation(RegistrationOperation.UNREGISTER, true, edges);
	}

	@Override
	public void redo()
	{
		itemRegistrationModule.doOperation(RegistrationOperation.REGISTER, false, boxes);
		itemRegistrationModule.doOperation(RegistrationOperation.REGISTER, true, edges);
	}

	@Override
	public String toString()
	{
		return "ItemRegistrationOperation";
	}	
}
