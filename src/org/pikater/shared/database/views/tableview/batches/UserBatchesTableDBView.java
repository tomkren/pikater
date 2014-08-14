package org.pikater.shared.database.views.tableview.batches;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.views.base.QueryConstraints;
import org.pikater.shared.database.views.base.QueryResult;
import org.pikater.shared.database.views.tableview.base.ITableColumn;

/**
 * A view displaying all experiments for the given user.
 */
public class UserBatchesTableDBView extends AbstractBatchTableDBView
{
	protected final JPAUser owner;
	
	/**  
	 * @param user the user whose batches to display
	 */
	public UserBatchesTableDBView(JPAUser user)
	{
		this.owner = user;
	}
	
	public JPAUser getOwner()
	{
		return owner;
	}	
	
	@Override
	public ITableColumn[] getColumns()
	{
		// everything except owner, which is specified
		return EnumSet.of(
				Column.STATUS,
				Column.FINISHED,
				Column.CREATED,
				Column.MAX_PRIORITY,
				Column.NAME,
				Column.NOTE,
				Column.ABORT,
				Column.RESULTS
		).toArray(new ITableColumn[0]);
	}
	
	@Override
	public ITableColumn getDefaultSortOrder()
	{
		return Column.STATUS;
	}
	
	@Override
	public QueryResult queryUninitializedRows(QueryConstraints constraints)
	{
		// TODO: NOW USES CONSTRAINTS GIVEN IN ARGUMENT BUT IT'S A SHALLOW AND INCORRECT IMPLEMENTATION - SHOULD BE NATIVE
		
		List<JPABatch> allBatches=DAOs.batchDAO.getByOwner(this.owner, constraints.getOffset(), constraints.getMaxResults(), constraints.getSortColumn(), constraints.getSortOrder());
		int userBatchCount=DAOs.batchDAO.getByOwnerCount(owner);
		List<BatchTableDBRow> rows = new ArrayList<BatchTableDBRow>();
		
		for(JPABatch batch : allBatches)
		{
			rows.add(new BatchTableDBRow(batch, false));
		}
		return new QueryResult(rows, userBatchCount);
	}
}
