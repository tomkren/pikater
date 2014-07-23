package org.pikater.shared.database.views.tableview.batches;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.views.base.QueryConstraints;
import org.pikater.shared.database.views.base.QueryResult;
import org.pikater.shared.database.views.tableview.base.ITableColumn;

/**
 * A view for scheduled (whether waiting, started or finished) experiments.
 */
public class AllBatchesTableDBView extends AbstractBatchTableDBView
{
	@Override
	public ITableColumn[] getColumns()
	{
		return EnumSet.of(
				Column.FINISHED,
				Column.STATUS,
				Column.PRIORITY,
				Column.MAX_PRIORITY,
				Column.CREATED,
				Column.OWNER,
				Column.NAME,
				Column.NOTE
		).toArray(new ITableColumn[0]);
	}
	
	@Override
	public ITableColumn getDefaultSortOrder()
	{
		return Column.FINISHED;
	}
	
	@Override
	public QueryResult queryUninitializedRows(QueryConstraints constraints)
	{
		// TODO: NOW USES CONSTRAINTS GIVEN IN ARGUMENT BUT IT'S A SHALLOW AND INCORRECT IMPLEMENTATION - SHOULD BE NATIVE
		
		// TODO: only display SCHEDULED batches (don't include Status.CREATED)
		
		List<JPABatch> allBatches=DAOs.batchDAO.getAll();
		List<BatchTableDBRow> rows = new ArrayList<BatchTableDBRow>();
		
		int endIndex = Math.min(constraints.getOffset() + constraints.getMaxResults(), allBatches.size());
		for(JPABatch batch : allBatches.subList(constraints.getOffset(), endIndex))
		{
			rows.add(new BatchTableDBRow(batch));
		}
		return new QueryResult(rows, allBatches.size());
	}
}
