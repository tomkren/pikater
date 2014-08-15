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

/**
 * A view displaying all saved (not scheduled) experiments for the given user.
 */
public class UserSavedBatchesTableDBView extends UserBatchesTableDBView
{
	/**  
	 * @param user the user whose batches to display
	 */
	public UserSavedBatchesTableDBView(JPAUser user)
	{
		super(user);
	}
	
	@Override
	public ITableColumn[] getColumns()
	{
		return EnumSet.of(
				Column.CREATED,
				Column.NAME,
				Column.NOTE
		).toArray(new ITableColumn[0]);
	}
	
	@Override
	public ITableColumn getDefaultSortOrder()
	{
		return Column.CREATED; // user will probably want to continue working on his last experiment
	}
	
	@Override
	public QueryResult queryUninitializedRows(QueryConstraints constraints)
	{
		// TODO: NOW USES CONSTRAINTS GIVEN IN ARGUMENT BUT IT'S A SHALLOW AND INCORRECT IMPLEMENTATION - SHOULD BE NATIVE
		
		// TODO: only display NOT SCHEDULED batches (Status.CREATED)
		
		List<JPABatch> allBatches=DAOs.batchDAO.getByOwnerAndStatus(this.owner, JPABatchStatus.CREATED, constraints.getOffset(), constraints.getMaxResults(), constraints.getSortColumn(), constraints.getSortOrder());
		int count = DAOs.batchDAO.getByOwnerAndStatusCount(this.owner, JPABatchStatus.CREATED);
		List<BatchTableDBRow> rows = new ArrayList<BatchTableDBRow>();
		
		for(JPABatch batch : allBatches)
		{
			rows.add(new BatchTableDBRow(batch, false));
		}
		return new QueryResult(rows, count);
	}
}