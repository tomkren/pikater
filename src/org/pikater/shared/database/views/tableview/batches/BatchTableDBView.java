package org.pikater.shared.database.views.tableview.batches;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.views.base.QueryConstraints;
import org.pikater.shared.database.views.base.QueryResult;
import org.pikater.shared.database.views.base.values.DBViewValueType;
import org.pikater.shared.database.views.tableview.base.AbstractTableDBView;
import org.pikater.shared.database.views.tableview.base.ITableColumn;

/**
 * A generic view for tables displaying dataset information.  
 */
public class BatchTableDBView extends AbstractTableDBView
{
	private final JPAUser owner;
	
	/**  
	 * @param user The user whose batches to display. If null (admin mode), all datasets should
	 * be provided in the {@link #queryUninitializedRows(QueryConstraints constraints)} method.
	 */
	public BatchTableDBView(JPAUser user)
	{
		this.owner = user;
	}
	
	private boolean adminMode()
	{
		return this.owner == null;
	}
	
	/**
	 * Table headers will be presented in the order defined here, so
	 * make sure to order them right :). 
	 */
	public enum Column implements ITableColumn
	{
		/*
		 * First the read-only properties.
		 */
		FINISHED, // both users and admins should probably be most interested in the latest batches
		STATUS, // and whether they are yet finished or not
		PRIORITY,
		MAX_PRIORITY,
		CREATED,
		OWNER,
		NAME,
		NOTE; // this should be last because of potentially long text

		@Override
		public String getDisplayName()
		{
			return this.name();
		}

		@Override
		public DBViewValueType getColumnType()
		{
			switch(this)
			{
				case OWNER:
				case NAME:
				case NOTE:
				case CREATED:
				case FINISHED:
				case STATUS:
					return DBViewValueType.STRING;
					
				// TODO: these should be editable?
				case PRIORITY:
				case MAX_PRIORITY:
					return DBViewValueType.STRING;
					
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
	}

	@Override
	public ITableColumn[] getColumns()
	{
		if(adminMode())
		{
			return Column.values();
		}
		else
		{
			ITableColumn[] allColumns = Column.values();
			return Arrays.copyOfRange(allColumns, 1, allColumns.length); // everything except owner, which is specified
		}
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
		
		List<JPABatch> allBatches;
		if(this.adminMode()){
			allBatches=DAOs.batchDAO.getAll();
		}else{
			allBatches=DAOs.batchDAO.getByOwner(owner);
		}
		
		List<BatchTableDBRow> rows = new ArrayList<BatchTableDBRow>();
		
		int endIndex = Math.min(constraints.getOffset() + constraints.getMaxResults(), allBatches.size());
		for(JPABatch batch : allBatches.subList(constraints.getOffset(), endIndex))
		{
			rows.add(new BatchTableDBRow(batch));
		}
		return new QueryResult(rows, allBatches.size());
	}
}
