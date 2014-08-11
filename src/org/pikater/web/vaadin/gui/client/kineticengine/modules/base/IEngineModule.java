package org.pikater.web.vaadin.gui.client.kineticengine.modules.base;

import org.pikater.web.vaadin.gui.client.kineticengine.graph.AbstractGraphItemClient;

public interface IEngineModule
{
	public String getModuleID();
	public void createModuleCrossReferences();
	public String[] getGraphItemTypesToAttachHandlersTo();
	public void attachHandlers(AbstractGraphItemClient<?> graphItem);
}
