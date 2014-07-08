package org.pikater.web.vaadin.gui.client.kineticengine.modules.base;

import org.pikater.web.vaadin.gui.client.kineticengine.experimentgraph.AbstractGraphItemClient;

public interface IEngineModule
{
	public String getModuleID();
	public String[] getItemsToAttachTo();
	public void attachEventListeners(AbstractGraphItemClient graphItem);
}
