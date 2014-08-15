package org.pikater.web.vaadin.gui.client.kineticengine.modules.base;

import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTMisc;

import net.edzard.kinetic.event.IEventListener;
import net.edzard.kinetic.event.KineticEvent;

public abstract class ModuleEventListener implements IEventListener
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
					// TODO
				}
			}
			*/
		}
	}
	
	protected abstract void handleInner(KineticEvent event);
	
	protected static String getListenerID(ModuleEventListener listener)
	{
		return GWTMisc.getSimpleName(listener.getClass());
	}
}
