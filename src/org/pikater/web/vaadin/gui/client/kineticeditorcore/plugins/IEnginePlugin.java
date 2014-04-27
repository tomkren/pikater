package org.pikater.web.vaadin.gui.client.kineticeditorcore.plugins;

import org.pikater.web.vaadin.gui.client.kineticeditorcore.graphitems.ExperimentGraphItem;

public interface IEnginePlugin
{
	public String getPluginID();
	public String[] getItemsToAttachTo();
	public void attachEventListeners(ExperimentGraphItem graphItem);
}
