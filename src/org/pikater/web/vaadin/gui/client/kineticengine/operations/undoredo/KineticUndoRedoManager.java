package org.pikater.web.vaadin.gui.client.kineticengine.operations.undoredo;

import java.util.Stack;

public class KineticUndoRedoManager
{
	private final Stack<BiDiOperation> undoStack; 
	private final Stack<BiDiOperation> redoStack;
	
	public KineticUndoRedoManager()
	{
		this.undoStack = new Stack<BiDiOperation>();
		this.redoStack = new Stack<BiDiOperation>();
	}
	
	public void clear()
	{
		undoStack.clear();
		redoStack.clear();
	}
	
	public void push(BiDiOperation operation)
	{
		undoStack.push(operation);
		redoStack.clear();
		System.out.println("Operation pushed: " + operation.toString());
	}
	
	public void undo()
	{
		if(!undoStack.isEmpty())
		{
			BiDiOperation op = undoStack.pop();
			redoStack.push(op);
			op.undo();
			System.out.println("Operation undid: " + op.toString());
		}
	}
	
	public void undoAndDiscard()
	{
		if(!undoStack.isEmpty())
		{
			undoStack.pop().undo();
		}
	}
	
	public void redo()
	{
		if(!redoStack.isEmpty())
		{
			BiDiOperation op = redoStack.pop();
			undoStack.push(op);
			op.redo();
			System.out.println("Operation redid: " + op.toString());
		}
	}
	
	public boolean existsUnsavedContent()
	{
		// TODO: what to do about this? it seems to always be true when it matters...
		
		return undoStack.size() > 0 || redoStack.size() > 0;
	}
	
	public String toString()
	{
		return "Undo: " + undoStack.size() + "; Redo: " + redoStack.size();
	}
}