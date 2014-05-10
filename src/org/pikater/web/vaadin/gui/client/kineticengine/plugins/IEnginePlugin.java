package org.pikater.web.vaadin.gui.client.kineticengine.plugins;

import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.ExperimentGraphItem;

public interface IEnginePlugin
{
	public String getPluginID();
	public String[] getItemsToAttachTo();
	public void attachEventListeners(ExperimentGraphItem graphItem);
}
