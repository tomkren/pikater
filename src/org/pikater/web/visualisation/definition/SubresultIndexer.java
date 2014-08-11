package org.pikater.web.visualisation.definition;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.pikater.shared.util.Tuple;
import org.pikater.shared.util.collections.CustomOrderSet;
import org.pikater.web.vaadin.gui.server.layouts.matrixlayout.IMatrixDataSource;
import org.pikater.web.visualisation.definition.result.AbstractDSVisSubresult;

public class SubresultIndexer<I extends Object & Comparable<? super I>, R extends AbstractDSVisSubresult<I>> implements
	IMatrixDataSource<I, R>
{
	private final CustomOrderSet<I> leftIndexSet;
	private final CustomOrderSet<I> topIndexSet;
	private final Map<Tuple<I, I>, R> indexesToSubresult;
	
	public SubresultIndexer()
	{
		this.leftIndexSet = new CustomOrderSet<I>();
		this.topIndexSet = new CustomOrderSet<I>();
		this.indexesToSubresult = new HashMap<Tuple<I, I>, R>();
	}
	
	public void registerSubresult(I leftIndex, I topIndex, R subresult)
	{
		if(this.leftIndexSet.add(leftIndex) || this.topIndexSet.add(topIndex))
		{
			indexesToSubresult.put(new Tuple<I, I>(leftIndex, topIndex), subresult);
		}
		else
		{
			throw new IllegalArgumentException("That result was already registered.");
		}
	}

	@Override
	public Collection<I> getLeftIndexSet()
	{
		return leftIndexSet;
	}

	@Override
	public Collection<I> getTopIndexSet()
	{
		return topIndexSet;
	}

	@Override
	public R getElement(I leftIndex, I topIndex)
	{
		return indexesToSubresult.get(new Tuple<I, I>(leftIndex, topIndex));
	}
}