package org.pikater.web.vaadin.gui.client.kineticeditorcore.operations.undoredo;

import org.pikater.web.vaadin.gui.client.kineticeditorcore.KineticEngine;

public abstract class BiDiOperation
{
	protected final KineticEngine kineticEngine;
	
	public BiDiOperation(KineticEngine kineticEngine)
	{
		this.kineticEngine = kineticEngine;
	}
	
	public abstract void firstExecution();
	public abstract void undo();
	public abstract void redo();
	
	@Override
	public abstract String toString();
}
