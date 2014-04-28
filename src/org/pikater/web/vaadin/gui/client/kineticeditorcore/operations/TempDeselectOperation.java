package org.pikater.web.vaadin.gui.client.kineticeditorcore.operations;

import org.pikater.web.vaadin.gui.client.kineticeditorcore.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.plugins.SelectionPlugin;

import com.google.gwt.user.client.Command;

public class TempDeselectOperation extends TempConditionOperation
{
	private BoxPrototype[] originalSelectedBoxes;
	private SelectionPlugin selectionPlugin;
	
	public TempDeselectOperation(KineticEngine engine, Command cmd)
	{
		super(engine, cmd);
	}
	
	@Override
	protected void init()
	{
		selectionPlugin = (SelectionPlugin) engine.getPlugin(SelectionPlugin.pluginID); // don't put this into the constructor, NullPointerException will occur otherwise 
		originalSelectedBoxes = selectionPlugin.getSelectedBoxes().toArray(new BoxPrototype[0]);
	}

	@Override
	protected void createConditions()
	{
		selectionPlugin.deselectAllBut(null, false);
	}

	@Override
	protected void returnConditions()
	{
		selectionPlugin.invertSelection(false, originalSelectedBoxes);
	}
}