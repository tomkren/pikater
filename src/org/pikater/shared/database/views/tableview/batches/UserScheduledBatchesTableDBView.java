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
import org.pikater.shared.database.views.base.QueryConstraints;
import org.pikater.shared.database.views.base.QueryResult;
import org.pikater.shared.database.views.tableview.base.ITableColumn;

/**
 * A view displaying all scheduled (whether waiting, started or finished) experiments for the given user.
 */
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
	public Set<ITableColumn> getAllColumns()
	{
		return new LinkedHashSet<ITableColumn>(EnumSet.of(
				Column.CREATED,
				Column.STATUS,
				Column.NAME,
				Column.NOTE
		));
	}
	
	@Override
	public Set<ITableColumn> getDefaultColumns()
	{
		return getAllColumns();
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
			rows.add(new BatchTableDBRow(batch, false));
		}
		return new QueryResult(rows, count);
	}
}