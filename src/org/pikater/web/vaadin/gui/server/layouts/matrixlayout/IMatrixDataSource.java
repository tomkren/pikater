package org.pikater.web.vaadin.gui.server.layouts.matrixlayout;

import java.util.Collection;

/**
 * Data provider for {@link MatrixLayout}.
 * 
 * @author SkyCrawl
 *
 * @param <I> The index type of the matrix.
 * @param <R> The item type of the matrix.
 */
public interface IMatrixDataSource<I extends Object, R extends Object>
{
	/*
	 * Generic arrays can not be created at runtime, so let's
	 * go with collections instead.
	 */
	
	/**
	 * Gets Y indexes.
	 * @return
	 */
	Collection<I> getLeftIndexSet();
	
	/**
	 * Gets X indexes.
	 * @return
	 */
	Collection<I> getTopIndexSet();
	
	/**
	 * Gets data source (not necessarily a component) for item at the given indexes.
	 * @param leftIndex
	 * @param topIndex
	 * @return
	 */
	R getElement(I leftIndex, I topIndex);
}