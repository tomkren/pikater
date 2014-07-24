package org.pikater.shared.database.views.tableview.batches;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.status.JPABatchStatus;
import org.pikater.shared.database.views.base.QueryConstraints;
import org.pikater.shared.database.views.base.QueryResult;
import org.pikater.shared.database.views.tableview.base.ITableColumn;

public class UserScheduledBatchesTableDBView extends UserBatchesTableDBView
{
	/**  
	 * @param user the user whose batches to display
	 */
	public UserScheduledBatchesTableDBView(JPAUser user)
	{
		super(user);
	}
	
	@Override
	public ITableColumn[] getColumns()
	{
		return EnumSet.of(
				Column.CREATED,
				Column.NAME,
				Column.NOTE,
				Column.STATUS
		).toArray(new ITableColumn[0]);
	}
	
	@Override
	public ITableColumn getDefaultSortOrder()
	{
		return Column.CREATED; // user will probably want to continue working on his last experiments
	}
	
	@Override
	public QueryResult queryUninitializedRows(QueryConstraints constraints)
	{
		// TODO: NOW USES CONSTRAINTS GIVEN IN ARGUMENT BUT IT'S A SHALLOW AND INCORRECT IMPLEMENTATION - SHOULD BE NATIVE
		
		// TODO: only display SCHEDULED batches (don't include Status.CREATED)
		
		List<JPABatch> allBatches=DAOs.batchDAO.getByOwnerAndNotStatus(this.owner, JPABatchStatus.CREATED, constraints.getOffset(), constraints.getMaxResults(), constraints.getSortColumn(), constraints.getSortOrder());
		int count = DAOs.batchDAO.getByOwnerAndNotStatusCount(this.owner, JPABatchStatus.CREATED);
		List<BatchTableDBRow> rows = new ArrayList<BatchTableDBRow>();
		
		for(JPABatch batch : allBatches)
		{
			rows.add(new BatchTableDBRow(batch));
		}
		return new QueryResult(rows, count);
	}
}