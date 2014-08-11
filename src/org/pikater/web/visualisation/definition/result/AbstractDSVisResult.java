package org.pikater.web.visualisation.definition.result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogResultHandler;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.ProgressDialog.IProgressDialogTaskResult;
import org.pikater.web.vaadin.gui.server.layouts.matrixlayout.IMatrixDataSource;
import org.pikater.web.visualisation.definition.SubresultIndexer;

public abstract class AbstractDSVisResult<I extends Object & Comparable<? super I>, R extends AbstractDSVisSubresult<I>> implements 
	IProgressDialogTaskResult, Iterable<R>
{
	/**
	 * Object providing progress callbacks.
	 */
	private final IProgressDialogResultHandler taskResult;
	
	/**
	 * Indexer for the generated images.
	 */
	private final SubresultIndexer<I, R> indexer;
	
	/**
	 * Collection of generated images, with some additional attached data.
	 */
	private final Collection<R> resultImageCollection;
	
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
		this.indexer = new SubresultIndexer<I, R>();
		this.resultImageCollection = new ArrayList<R>();
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
	}
	
	//------------------------------------------------------------------------------
	// PUBLIC INTERFACE
	
	/*
	 * Main.
	 */
	
	public int getImageWidth()
	{
		return imageWidth;
	}

	public int getImageHeight()
	{
		return imageHeight;
	}
	
	public IMatrixDataSource<I, R> toMatrixView()
	{
		return indexer;
	}
	
	@Override
	public Iterator<R> iterator()
	{
		return resultImageCollection.iterator();
	}
	
	/*
	 * OPAQUE PROGRESS ROUTINES FORWARDED TO THE PASSED TASK RESULT OBJECT
	 */
	
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
	
	//------------------------------------------------------------------------------
	// PROTECTED INTERFACE
	
	protected void registerSubresult(I leftIndex, I topIndex, R subresult)
	{
		indexer.registerSubresult(leftIndex, topIndex, subresult); // might throw an exception - let it be first
		resultImageCollection.add(subresult);
	}
}