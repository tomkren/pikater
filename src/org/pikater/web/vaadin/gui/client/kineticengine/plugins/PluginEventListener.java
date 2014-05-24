package org.pikater.web.vaadin.gui.client.kineticengine.plugins;

import net.edzard.kinetic.event.IEventListener;
import net.edzard.kinetic.event.KineticEvent;

public abstract class PluginEventListener implements IEventListener
{
	@Override
	public void handle(KineticEvent event)
	{
		if(!event.isProcessed())
		{
			handleInner(event);
			/*
			if(JSNI_SharedConfig.isDebugModeActivated())
			{
				if(event.isProcessed())
				{
					event.setProcessed(getListenerID());
				}
				else
				{
					// TODO: method chain...
				}
			}
			*/
		}
	}
	
	protected abstract void handleInner(KineticEvent event);
	protected abstract String getListenerID();
}
