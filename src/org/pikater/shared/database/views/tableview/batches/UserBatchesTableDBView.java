package org.pikater.shared.database.views.tableview.batches;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.views.base.QueryConstraints;
import org.pikater.shared.database.views.base.QueryResult;
import org.pikater.shared.database.views.tableview.base.ITableColumn;

/**
 * A view displaying all experiments for the given user.
 */
public class UserBatchesTableDBView extends AllBatchesTableDBView
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
	public Set<ITableColumn> getAllColumns()
	{
		Set<ITableColumn> superResult = super.getAllColumns();
		superResult.remove(Column.OWNER);
		return superResult;
	}
	
	@Override
	public Set<ITableColumn> getDefaultColumns()
	{
		Set<ITableColumn> superResult = super.getDefaultColumns();
		superResult.remove(Column.OWNER);
		return superResult;
	}
	
	@Override
	public ITableColumn getDefaultSortOrder()
	{
		return Column.STATUS;
	}
	
	@Override
	public QueryResult queryUninitializedRows(QueryConstraints constraints)
	{
		List<JPABatch> resultBatches=DAOs.batchDAO.getByOwner(this.owner, constraints.getOffset(), constraints.getMaxResults(), constraints.getSortColumn(), constraints.getSortOrder());
		int allBatchesCount=DAOs.batchDAO.getByOwnerCount(owner);
		
		List<BatchTableDBRow> resultRows = new ArrayList<BatchTableDBRow>();
		for(JPABatch batch : resultBatches)
		{
			resultRows.add(new BatchTableDBRow(batch, false));
		}
		return new QueryResult(resultRows, allBatchesCount);
	}
}