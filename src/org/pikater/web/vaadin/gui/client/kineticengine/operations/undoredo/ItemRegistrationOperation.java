package org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo;

import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.graph.BoxGraphItemClient;
import org.pikater.web.vaadin.gui.client.kineticengine.graph.EdgeGraphItemClient;
import org.pikater.web.vaadin.gui.client.kineticengine.modules.ItemRegistrationModule;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.base.BiDiOperation;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.graphitems.AbstractGraphItemShared.RegistrationOperation;

/**
 * Operation handling only the first item registration / deregistration. Item
 * removing/adding again is done in {@link DeleteSelectedBoxesOperation} which extends this class.
 */
public final class ItemRegistrationOperation extends BiDiOperation
{
	private final BoxGraphItemClient[] boxes;
	private final EdgeGraphItemClient[] edges;
	private final ItemRegistrationModule itemRegistrationModule;
	
	public ItemRegistrationOperation(KineticEngine kineticEngine, BoxGraphItemClient[] boxes, EdgeGraphItemClient[] edges)
	{
		super(kineticEngine);
		
		this.boxes = boxes == null ? new BoxGraphItemClient[0] : boxes;
		this.edges = edges == null ? new EdgeGraphItemClient[0] : edges;
		this.itemRegistrationModule = (ItemRegistrationModule) kineticEngine.getModule(ItemRegistrationModule.moduleID);
	}
	
	@Override
	public void firstExecution()
	{
		for(BoxGraphItemClient box : boxes)
		{
			kineticEngine.attachModuleHandlersTo(box);
		}
		for(EdgeGraphItemClient edge : edges)
		{
			kineticEngine.attachModuleHandlersTo(edge);
		}
		redo();
	}
	
	@Override
	public void undo()
	{
		itemRegistrationModule.doOperation(RegistrationOperation.UNREGISTER, false, edges);
		itemRegistrationModule.doOperation(RegistrationOperation.UNREGISTER, true, boxes); // automatically deselects
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