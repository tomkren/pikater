package org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo;

import java.util.Stack;

import org.pikater.web.vaadin.gui.client.kineticcomponent.KineticComponentWidget;

public class KineticUndoRedoManager
{
	private final KineticComponentWidget widget;
	private final Stack<BiDiOperation> undoStack; 
	private final Stack<BiDiOperation> redoStack;
	
	public KineticUndoRedoManager(KineticComponentWidget widget)
	{
		this.widget = widget;
		this.undoStack = new Stack<BiDiOperation>();
		this.redoStack = new Stack<BiDiOperation>();
	}
	
	public void clear()
	{
		alterStateIf(!undoStack.isEmpty());
		
		undoStack.clear();
		redoStack.clear();
	}
	
	public void push(BiDiOperation operation)
	{
		alterStateIf(undoStack.isEmpty());
		
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
			
			alterStateIf(undoStack.isEmpty());
		}
	}
	
	public void undoAndDiscard()
	{
		if(!undoStack.isEmpty())
		{
			undoStack.pop().undo();
			
			alterStateIf(undoStack.isEmpty());
		}
	}
	
	public void redo()
	{
		alterStateIf(undoStack.isEmpty());
		
		if(!redoStack.isEmpty())
		{
			BiDiOperation op = redoStack.pop();
			undoStack.push(op);
			op.redo();
		}
	}
	
	private void alterStateIf(boolean condition)
	{
		if(condition)
		{
			widget.command_setExperimentModified(!condition);
		}
	}
}