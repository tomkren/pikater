package org.pikater.web.vaadin.gui.client.kineticengine.plugins;

import net.edzard.kinetic.event.IEventListener;
import net.edzard.kinetic.event.KineticEvent;

import org.pikater.web.vaadin.gui.client.jsni.JSNI_SharedConfig;

public abstract class PluginEventListener implements IEventListener
{
	@Override
	public void handle(KineticEvent event)
	{
		if(!event.isProcessed())
		{
			handleInner(event);
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
		}
		else if(JSNI_SharedConfig.isDebugModeActivated())
		{
			System.out.println(getListenerID() + " suppressed by " + event.getProcessedBy());
		}
	}
	
	protected abstract void handleInner(KineticEvent event);
	protected abstract String getListenerID();
}
