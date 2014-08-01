package org.pikater.web.visualisation.definition.result;

import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogResultHandler;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogTaskResult;

public abstract class AbstractDSVisResult implements IProgressDialogTaskResult
{
	/**
	 * Object providing progress callbacks.
	 */
	private final IProgressDialogResultHandler taskResult;
	
	/**
	 * The width of all images in the above collection.
	 */
	private final int imageWidth;
	
	/**
	 * The height of all images in the above collection.
	 */
	private final int imageHeight;

	public AbstractDSVisResult(IProgressDialogResultHandler taskResult, int imageWidth, int imageHeight)
	{
		this.taskResult = taskResult;
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
	}
	
	public int getImageWidth()
	{
		return imageWidth;
	}

	public int getImageHeight()
	{
		return imageHeight;
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