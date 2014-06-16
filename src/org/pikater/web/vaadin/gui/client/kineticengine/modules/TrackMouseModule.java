package org.pikater.web.vaadin.gui.client.kineticengine.modules;

import net.edzard.kinetic.event.EventType;
import net.edzard.kinetic.event.KineticEvent;

import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTCursorManager;
import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTMisc;
import org.pikater.web.vaadin.gui.client.gwtmanagers.GWTCursorManager.MyCursor;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.BoxPrototype;
import org.pikater.web.vaadin.gui.client.kineticengine.graphitems.ExperimentGraphItem;
import org.pikater.web.vaadin.gui.client.kineticengine.modules.base.BoxListener;
import org.pikater.web.vaadin.gui.client.kineticengine.modules.base.IEngineModule;
import org.pikater.web.vaadin.gui.client.kineticengine.modules.base.ModuleEventListener;

public final class TrackMouseModule implements IEngineModule
{
	public static String moduleID;
	
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
	private class BoxMouseOverListener extends BoxListener
	{
		public BoxMouseOverListener(BoxPrototype parentBox)
		{
			super(parentBox);
		}

		@Override
		protected void handleInner(KineticEvent event)
		{
			setCurrentlyHoveredBox(parentBox);
			GWTCursorManager.setCursorType(kineticEngine.getContext().getStageDOMElement(), MyCursor.POINTER);
		}
	}
	private class BoxMouseOutHandler extends ModuleEventListener
	{
		@Override
		protected void handleInner(KineticEvent event)
		{
			unsetCurrentlyHoveredBox();
			GWTCursorManager.setCursorType(kineticEngine.getContext().getStageDOMElement(), MyCursor.AUTO);
		}
	};

	/**
	 * Constructor.
	 */
	public TrackMouseModule(KineticEngine kineticEngine)
	{
		moduleID = GWTMisc.getSimpleName(this.getClass());
		this.kineticEngine = kineticEngine;
		this.currentlyHoveredBox = null;
	}
	
	// **********************************************************************************************
	// INHERITED INTERFACE
	
	@Override
	public String getModuleID()
	{
		return moduleID;
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
			box.getMasterNode().addEventListener(new BoxMouseOverListener(box), EventType.Basic.MOUSEOVER.withName(moduleID));
			box.getMasterNode().addEventListener(new BoxMouseOutHandler(), EventType.Basic.MOUSEOUT.withName(moduleID));
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
