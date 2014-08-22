package org.pikater.shared.database.views.tableview.batches;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.status.JPABatchStatus;
import org.pikater.shared.database.views.base.QueryConstraints;
import org.pikater.shared.database.views.base.QueryResult;
import org.pikater.shared.database.views.tableview.base.ITableColumn;

/**
 * A view displaying all batches (admin mode).
 */
public class AllBatchesTableDBView extends AbstractBatchTableDBView
{
	@Override
	public Set<ITableColumn> getAllColumns()
	{
		return new LinkedHashSet<ITableColumn>(EnumSet.allOf(Column.class));
	}
	
	@Override
	public Set<ITableColumn> getDefaultColumns()
	{
		return new LinkedHashSet<ITableColumn>(EnumSet.of(
				Column.FINISHED,
				Column.STATUS,
				Column.CREATED,
				Column.OWNER,
				Column.NAME,
				Column.NOTE,
				Column.ABORT
		));
	}
	
	@Override
	public ITableColumn getDefaultSortOrder()
	{
		return Column.STATUS;
	}
	
	@Override
	public QueryResult queryUninitializedRows(QueryConstraints constraints)
	{
		List<JPABatch> resultBatches=DAOs.batchDAO.getAllExcludeByStatus(constraints.getOffset(), constraints.getMaxResults(),constraints.getSortColumn(),constraints.getSortOrder(),JPABatchStatus.CREATED);
		int allBatchesCount=DAOs.batchDAO.getAllExcludedStatusCount(JPABatchStatus.CREATED);
		
		List<BatchTableDBRow> resultRows = new ArrayList<BatchTableDBRow>();
		for(JPABatch batch : resultBatches)
		{
			resultRows.add(new BatchTableDBRow(batch, true));
		}
		return new QueryResult(resultRows, allBatchesCount);
	}
}