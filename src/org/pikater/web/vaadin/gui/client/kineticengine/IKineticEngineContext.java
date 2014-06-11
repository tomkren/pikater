package org.pikater.web.vaadin.gui.client.kineticengine;

import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentServerRpc;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo.KineticUndoRedoManager;
import org.pikater.web.vaadin.gui.shared.KineticComponentClickMode;

import com.google.gwt.user.client.Element;

public interface IKineticEngineContext extends KineticComponentServerRpc
{
	Element getStageDOMElement();
	KineticShapeCreator getShapeCreator();
	KineticUndoRedoManager getUndoRedoManager();
	
	KineticComponentClickMode getClickMode();
	boolean openOptionsManagerOnSelectionChange();
}