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
	
	public boolean isSubresultRegistered(I leftIndex, I topIndex)
	{
		return this.indexesToSubresult.containsKey(new Tuple<I, I>(leftIndex, topIndex));
	}
	
	public void registerSubresult(I leftIndex, I topIndex, R subresult)
	{
		if(!isSubresultRegistered(leftIndex, topIndex))
		{
			leftIndexSet.add(leftIndex);
			topIndexSet.add(topIndex);
			indexesToSubresult.put(new Tuple<I, I>(leftIndex, topIndex), subresult);
		}
		else
		{
			throw new IllegalArgumentException(String.format("Result for '%s' (left) and '%s' (top) was already registered.",
					leftIndex.toString(),
					topIndex.toString()
			));
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