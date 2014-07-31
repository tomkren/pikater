package org.pikater.web.visualisation.definition.result;

import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogResultHandler;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogTaskResult;

public abstract class AbstractDatasetVisualizationResult implements IProgressDialogTaskResult
{
	/**
	 * Object providing progress callbacks.
	 */
	private final IProgressDialogResultHandler taskResult;

	public AbstractDatasetVisualizationResult(IProgressDialogResultHandler taskResult)
	{
		this.taskResult = taskResult;
	}
	
	//------------------------------------------------------------------------------
	// OPAQUE PROGRESS ROUTINES FORWARDED TO THE PASSED TASK RESULT OBJECT
	
	public void updateProgress(float percentage)
	{
		taskResult.updateProgress(percentage);
	}
	
	public void failed()
	{
		taskResult.failed();
	}
	
	public void finished()
	{
		taskResult.finished(this);
	}
}