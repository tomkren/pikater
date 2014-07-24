package org.pikater.shared.database.views.tableview.batches;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.status.JPABatchStatus;
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
		// TODO: only display SCHEDULED batches (don't include Status.CREATED)
		List<JPABatch> allBatches=DAOs.batchDAO.getAllExcludeByStatus(constraints.getOffset(), constraints.getMaxResults(),constraints.getSortColumn(),constraints.getSortOrder(),JPABatchStatus.CREATED);
		int allBatchesCount=DAOs.batchDAO.getAllExcludedStatusCount(JPABatchStatus.CREATED);
		List<BatchTableDBRow> rows = new ArrayList<BatchTableDBRow>();
		
		for(JPABatch batch : allBatches)
		{
			rows.add(new BatchTableDBRow(batch));
		}
		return new QueryResult(rows, allBatchesCount);
	}
}
