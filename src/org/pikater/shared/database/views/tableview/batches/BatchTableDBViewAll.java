package org.pikater.shared.database.views.tableview.batches;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.status.JPABatchStatus;
import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.query.QueryConstraints;
import org.pikater.shared.database.views.base.query.QueryResult;

/**
 * A view displaying all batches (admin mode).
 */
public class BatchTableDBViewAll extends BatchTableDBView {
	@Override
	public Set<ITableColumn> getAllColumns() {
		LinkedHashSet<ITableColumn> result = new LinkedHashSet<ITableColumn>(EnumSet.allOf(Column.class));
		result.remove(Column.USER_PRIORITY);
		return result;
	}

	@Override
	public Set<ITableColumn> getDefaultColumns() {
		return new LinkedHashSet<ITableColumn>(EnumSet.of(Column.STATUS, Column.CREATED, Column.TOTAL_PRIORITY, Column.OWNER, Column.NAME, Column.NOTE, Column.ABORT));
	}

	@Override
	public ITableColumn getDefaultSortOrder() {
		return Column.STATUS;
	}

	@Override
	public QueryResult queryUninitializedRows(QueryConstraints constraints) {
		List<JPABatch> resultBatches = DAOs.batchDAO.getAllExcludeByStatus(constraints.getOffset(), constraints.getMaxResults(), constraints.getSortColumn(), constraints.getSortOrder(),
				JPABatchStatus.CREATED);
		int allBatchesCount = DAOs.batchDAO.getAllExcludeByStatusCount(JPABatchStatus.CREATED);

		List<BatchTableDBRow> resultRows = new ArrayList<BatchTableDBRow>();
		for (JPABatch batch : resultBatches) {
			resultRows.add(new BatchTableDBRow(batch, true));
		}
		return new QueryResult(resultRows, allBatchesCount);
	}
}