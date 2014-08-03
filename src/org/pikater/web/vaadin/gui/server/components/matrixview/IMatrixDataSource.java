package org.pikater.web.vaadin.gui.server.components.matrixview;

import java.util.Collection;

public interface IMatrixDataSource<I extends Object, R extends Object>
{
	/*
	 * Generic arrays can not be created at runtime, so let's
	 * go with collections instead.
	 */
	
	Collection<I> getLeftIndexSet();
	Collection<I> getTopIndexSet();
	R getElement(I leftIndex, I topIndex);
}