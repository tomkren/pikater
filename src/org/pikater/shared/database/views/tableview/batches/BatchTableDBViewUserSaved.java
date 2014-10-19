package org.pikater.shared.database.views.tableview.batches;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.status.JPABatchStatus;
import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.query.QueryConstraints;
import org.pikater.shared.database.views.base.query.QueryResult;

/**
 * A view displaying all saved (not scheduled) experiments for the given user.
 */
public class BatchTableDBViewUserSaved extends BatchTableDBViewUser {
	/**  
	 * @param user the user whose batches to display
	 */
	public BatchTableDBViewUserSaved(JPAUser user) {
		super(user);
	}

	@Override
	public Set<ITableColumn> getAllColumns() {
		return new LinkedHashSet<ITableColumn>(EnumSet.of(Column.CREATED, Column.NAME, Column.NOTE));
	}

	@Override
	public Set<ITableColumn> getDefaultColumns() {
		return getAllColumns();
	}

	@Override
	public ITableColumn getDefaultSortOrder() {
		return Column.CREATED; // user will probably want to continue working on his last experiment
	}

	@Override
	public QueryResult queryUninitializedRows(QueryConstraints constraints) {
		List<JPABatch> resultBatches = DAOs.batchDAO.getByOwnerAndStatus(this.owner, JPABatchStatus.CREATED, constraints.getOffset(), constraints.getMaxResults(), constraints.getSortColumn(),
				constraints.getSortOrder());
		int allBatchesCount = DAOs.batchDAO.getByOwnerAndStatusCount(this.owner, JPABatchStatus.CREATED);

		List<BatchTableDBRow> resultRows = new ArrayList<BatchTableDBRow>();
		for (JPABatch batch : resultBatches) {
			resultRows.add(new BatchTableDBRow(batch, false));
		}
		return new QueryResult(resultRows, allBatchesCount);
	}
}