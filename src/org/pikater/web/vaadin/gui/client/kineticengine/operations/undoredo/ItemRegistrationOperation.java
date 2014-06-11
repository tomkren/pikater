package org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo;

import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.EdgePrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.ItemRegistrationPlugin;
import org.pikater.web.vaadin.gui.client.kineticengine.plugins.ItemRegistrationPlugin.RegistrationOperation;

/**
 * Operation handling only the first item registration / deregistration. Item
 * removing/adding again is done in {@link DeleteSelectedOperation}. 
 */
public final class ItemRegistrationOperation extends BiDiOperation
{
	private final BoxPrototype[] boxes;
	private final EdgePrototype[] edges;
	private final ItemRegistrationPlugin itemRegistrationPlugin;
	
	public ItemRegistrationOperation(KineticEngine kineticState, BoxPrototype[] boxes, EdgePrototype[] edges)
	{
		super(kineticState);
		
		this.boxes = boxes == null ? new BoxPrototype[0] : boxes;
		this.edges = edges == null ? new EdgePrototype[0] : edges;
		this.itemRegistrationPlugin = (ItemRegistrationPlugin) kineticState.getPlugin(ItemRegistrationPlugin.pluginID);
	}
	
	@Override
	public void firstExecution()
	{
		for(BoxPrototype box : boxes)
		{
			kineticEngine.attachPluginHandlersTo(box);
		}
		for(EdgePrototype edge : edges)
		{
			kineticEngine.attachPluginHandlersTo(edge);
		}
		redo();
	}
	
	@Override
	public void undo()
	{
		itemRegistrationPlugin.doOperation(RegistrationOperation.UNREGISTER, false, boxes);
		itemRegistrationPlugin.doOperation(RegistrationOperation.UNREGISTER, true, edges);
	}

	@Override
	public void redo()
	{
		itemRegistrationPlugin.doOperation(RegistrationOperation.REGISTER, false, boxes);
		itemRegistrationPlugin.doOperation(RegistrationOperation.REGISTER, true, edges);
	}

	@Override
	public String toString()
	{
		return "ItemRegistrationOperation";
	}	
}
