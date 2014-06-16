package org.pikater.web.vaadin.gui.client.kineticengine.modules.base;

import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.ExperimentGraphItem;

public interface IEngineModule
{
	public String getModuleID();
	public String[] getItemsToAttachTo();
	public void attachEventListeners(ExperimentGraphItem graphItem);
}
