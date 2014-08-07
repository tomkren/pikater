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
	private final boolean notifyServer;
	
	public ItemRegistrationOperation(KineticEngine kineticEngine, BoxGraphItemClient[] boxes, EdgeGraphItemClient[] edges, boolean notifyServer)
	{
		super(kineticEngine);
		
		this.boxes = boxes == null ? new BoxGraphItemClient[0] : boxes;
		this.edges = edges == null ? new EdgeGraphItemClient[0] : edges;
		this.itemRegistrationModule = (ItemRegistrationModule) kineticEngine.getModule(ItemRegistrationModule.moduleID);
		this.notifyServer = notifyServer;
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
		itemRegistrationModule.doOperation(RegistrationOperation.UNREGISTER, false, notifyServer, edges);
		itemRegistrationModule.doOperation(RegistrationOperation.UNREGISTER, true, notifyServer, boxes); // automatically deselects
	}

	@Override
	public void redo()
	{
		itemRegistrationModule.doOperation(RegistrationOperation.REGISTER, false, notifyServer, boxes);
		itemRegistrationModule.doOperation(RegistrationOperation.REGISTER, true, notifyServer, edges);
	}

	@Override
	public String toString()
	{
		return "ItemRegistrationOperation";
	}	
}