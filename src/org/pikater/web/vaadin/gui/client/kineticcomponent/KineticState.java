package org.pikater.web.vaadin.gui.client.kineticcomponent;

import org.pikater.web.vaadin.gui.client.kineticengine.IKineticEngineContext;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticEngine;
import org.pikater.web.vaadin.gui.client.kineticengine.KineticShapeCreator;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.base.KineticUndoRedoManager;
import org.pikater.web.vaadin.gui.shared.kineticcomponent.ClickMode;

import com.google.gwt.user.client.Element;

public class KineticState implements IKineticEngineContext
{
	private KineticComponentWidget parentWidget;
	
	/*
	 * All-purpose kinetic engine components.
	 */
	private final KineticEngine engine;
	private final KineticShapeCreator shapeCreator;
	private final KineticUndoRedoManager historyManager;
	
	public KineticState(KineticComponentWidget parentWidget)
	{
		this.engine = new KineticEngine();
		this.shapeCreator = new KineticShapeCreator(this.engine);
		this.historyManager = new KineticUndoRedoManager(parentWidget);
		
		setParentWidget(parentWidget); // requires engine to be set
	}
	
	public void setParentWidget(KineticComponentWidget parentWidget)
	{
		this.parentWidget = parentWidget;
		this.engine.setContext(parentWidget);
	}

	@Override
	public Element getStageDOMElement()
	{
		return parentWidget.getStageDOMElement();
	}
	
	@Override
	public KineticEngine getEngine()
	{
		return engine;
	}

	@Override
	public KineticShapeCreator getShapeCreator()
	{
		return shapeCreator;
	}

	@Override
	public KineticUndoRedoManager getHistoryManager()
	{
		return historyManager;
	}

	@Override
	public ClickMode getClickMode()
	{
		return parentWidget.getClickMode();
	}
}