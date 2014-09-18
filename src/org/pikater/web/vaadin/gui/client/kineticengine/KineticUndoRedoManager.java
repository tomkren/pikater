package org.pikater.web.vaadin.gui.client.kineticengine;

import java.util.LinkedList;

import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentWidget;
import org.pikater.web.vaadin.gui.client.kineticengine.operations.base.BiDiOperation;

public class KineticUndoRedoManager
{
	private final KineticComponentWidget widget;
	private final LinkedList<BiDiOperation> undoStack; 
	private final LinkedList<BiDiOperation> redoStack;
	
	public KineticUndoRedoManager(KineticComponentWidget widget)
	{
		this.widget = widget;
		this.undoStack = new LinkedList<BiDiOperation>();
		this.redoStack = new LinkedList<BiDiOperation>();
	}
	
	public void loadHistory(KineticUndoRedoManager history)
	{
		if(history != null)
		{
			if(!undoStack.isEmpty() || !redoStack.isEmpty())
			{
				throw new IllegalStateException("Can not load history if there is already an alternative newer version.");
			}
			else
			{
				this.undoStack.addAll(history.undoStack);
				this.redoStack.addAll(history.redoStack);
				
				// notifyExperimentModifiedIf(!undoStack.isEmpty());
			}
		}
	}
	
	public void clear()
	{
		undoStack.clear();
		redoStack.clear();
		
		// alterStateIf(undoStack.isEmpty());
	}
	
	public void push(BiDiOperation operation)
	{
		// alterStateIf(undoStack.isEmpty());
		
		undoStack.push(operation);
		redoStack.clear();
	}
	
	public void undo()
	{
		if(!undoStack.isEmpty())
		{
			BiDiOperation op = undoStack.pop();
			redoStack.push(op);
			op.undo();
			
			// alterStateIf(undoStack.isEmpty());
		}
	}
	
	public void undoAndDiscard()
	{
		if(!undoStack.isEmpty())
		{
			undoStack.pop().undo();
			
			// alterStateIf(undoStack.isEmpty());
		}
	}
	
	public void redo()
	{
		// alterStateIf(undoStack.isEmpty());
		
		if(!redoStack.isEmpty())
		{
			BiDiOperation op = redoStack.pop();
			undoStack.push(op);
			op.redo();
		}
	}
	
	//---------------------------------------------------------------
	// INTERFACE FOR SHARING THE "MODIFIED" STATUS WITH THE SERVER
	
	/*
	 * TODO: options editing makes this incomplete... we have to merge all changes
	 * to be registered either on the server or the client.
	 */
	
	@SuppressWarnings("unused")
	private void notifyExperimentModifiedIf(boolean condition)
	{
		if(condition)
		{
			widget.command_setExperimentModified(true);
		}
	}
	
	@SuppressWarnings("unused")
	private void notifyExperimentNotModifiedIf(boolean condition)
	{
		if(condition)
		{
			widget.command_setExperimentModified(false);
		}
	}
}