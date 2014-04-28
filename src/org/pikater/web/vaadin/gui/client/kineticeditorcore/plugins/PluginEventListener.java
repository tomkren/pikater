package org.pikater.web.vaadin.gui.client.kineticeditorcore.plugins;

import net.edzard.kinetic.event.IEventListener;
import net.edzard.kinetic.event.KineticEvent;

import org.pikater.web.vaadin.gui.client.ClientVars;

public abstract class PluginEventListener implements IEventListener
{
	@Override
	public void handle(KineticEvent event)
	{
		if(!event.isProcessed())
		{
			handleInner(event);
			if(ClientVars.DEBUG_MODE)
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
		else if(ClientVars.DEBUG_MODE)
		{
			System.out.println(getListenerID() + " suppressed by " + event.getProcessedBy());
		}
	}
	
	protected abstract void handleInner(KineticEvent event);
	protected abstract String getListenerID();
}
