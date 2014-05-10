package org.pikater.web.vaadin.gui.client.kineticeditorcore.plugins;

import net.edzard.kinetic.event.EventType;
import net.edzard.kinetic.event.KineticEvent;

import org.pikater.web.vaadin.gui.client.kineticeditorcore.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticeditorcore.graphitems.ExperimentGraphItem;
import org.pikater.web.vaadin.gui.client.managers.GWTCursorManager;
import org.pikater.web.vaadin.gui.client.managers.GWTMisc;
import org.pikater.web.vaadin.gui.client.managers.GWTCursorManager.MyCursor;

public final class TrackMousePlugin implements IEnginePlugin
{
	public static String pluginID;
	
	/**
	 * The engine instance to work with.
	 */
	private final KineticEngine kineticEngine;
	
	/**
	 * The box that the mouse is currently hovering on.
	 */
	private BoxPrototype currentlyHoveredBox;
	
	/**
	 * The special event handlers/listeners to attach to boxes.
	 */
	private class BoxMouseOverListener extends PluginEventListener
	{
		private final BoxPrototype parentBox;
		
		public BoxMouseOverListener(BoxPrototype parentBox)
		{
			this.parentBox = parentBox;
		}

		@Override
		protected void handleInner(KineticEvent event)
		{
			setCurrentlyHoveredBox(parentBox);
			GWTCursorManager.setCursorType(kineticEngine.getParentDOMElement(), MyCursor.POINTER);
		}

		@Override
		protected String getListenerID()
		{
			return GWTMisc.getSimpleName(this.getClass());
		}
	}
	private class BoxMouseOutHandler extends PluginEventListener
	{
		@Override
		protected void handleInner(KineticEvent event)
		{
			unsetCurrentlyHoveredBox();
			GWTCursorManager.setCursorType(kineticEngine.getParentDOMElement(), MyCursor.AUTO);
		}

		@Override
		protected String getListenerID()
		{
			return GWTMisc.getSimpleName(this.getClass());
		}
	};

	/**
	 * Constructor.
	 */
	public TrackMousePlugin(KineticEngine kineticEngine)
	{
		pluginID = GWTMisc.getSimpleName(this.getClass());
		this.kineticEngine = kineticEngine;
		this.currentlyHoveredBox = null;
	}
	
	// **********************************************************************************************
	// INHERITED INTERFACE
	
	@Override
	public String getPluginID()
	{
		return pluginID;
	}
	
	@Override
	public String[] getItemsToAttachTo()
	{
		return new String[] { GWTMisc.getSimpleName(BoxPrototype.class) };
	}

	@Override
	public void attachEventListeners(ExperimentGraphItem graphItem)
	{
		if(graphItem instanceof BoxPrototype)
		{
			BoxPrototype box = (BoxPrototype)graphItem;
			box.getMasterRectangle().addEventListener(EventType.Basic.MOUSEOVER.setName(pluginID), new BoxMouseOverListener(box));
			box.getMasterRectangle().addEventListener(EventType.Basic.MOUSEOUT.setName(pluginID), new BoxMouseOutHandler());
		}
		else
		{
			throw new IllegalStateException();
		}
	}
	
	// **********************************************************************************************
	// PUBLIC INTERFACE
	
	public boolean isBoxHovered()
	{
		return currentlyHoveredBox != null;
	}
	
	public BoxPrototype getCurrentlyHoveredBox()
	{
		return currentlyHoveredBox;
	}
	
	// **********************************************************************************************
	// PRIVATE INTERFACE

	private void setCurrentlyHoveredBox(BoxPrototype currentlyHoveredBox)
	{
		this.currentlyHoveredBox = currentlyHoveredBox;
	}
	
	private void unsetCurrentlyHoveredBox()
	{
		currentlyHoveredBox = null;
	}
}
