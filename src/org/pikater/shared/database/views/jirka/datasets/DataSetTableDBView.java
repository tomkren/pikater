package org.pikater.shared.database.views.jirka.datasets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.views.jirka.abstractview.AbstractTableDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.abstractview.QueryConstraints;
import org.pikater.shared.database.views.jirka.abstractview.QueryResult;

/**
 * A generic view for tables displaying dataset information.  
 */
public class DataSetTableDBView extends AbstractTableDBView
{
	private final JPAUser owner;
	
	/**  
	 * @param user The user whose datasets to display. If null (admin mode), all datasets should
	 * be provided in the {@link #queryUninitializedRows(QueryConstraints constraints)} method.
	 */
	public DataSetTableDBView(JPAUser user)
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
	public enum Column implements IColumn
	{
		/*
		 * First the read-only properties.
		 */
		OWNER, // owner is expected to be declared first in the {@link #getColumns()} method
		CREATED,
		NUMBER_OF_INSTANCES,
		DEFAULT_TASK_TYPE,
		SIZE,
		DESCRIPTION;

		@Override
		public String getDisplayName()
		{
			return this.name();
		}

		@Override
		public ColumnType getColumnType()
		{
			switch(this)
			{
				case OWNER:
				case CREATED:
				case NUMBER_OF_INSTANCES:
				case DEFAULT_TASK_TYPE:
				case SIZE:
				case DESCRIPTION:
					return ColumnType.STRING;
					
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
	}

	@Override
	public IColumn[] getColumns()
	{
		if(adminMode())
		{
			return Column.values();
		}
		else
		{
			IColumn[] allColumns = Column.values();
			return Arrays.copyOfRange(allColumns, 1, allColumns.length); // everything except owner, which is specified
		}
	}
	
	@Override
	public IColumn getDefaultSortOrder()
	{
		return adminMode() ? Column.OWNER : Column.CREATED;
	}
	
	@Override
	public QueryResult queryUninitializedRows(QueryConstraints constraints)
	{
		// TODO: NOW USES CONSTRAINTS GIVEN IN ARGUMENT BUT IT'S A SHALLOW AND INCORRECT IMPLEMENTATION - SHOULD BE NATIVE
		// TODO: different results should be returned depending on whether 'owner' field is specified
		
		List<JPADataSetLO> allDatasets = DAOs.dataSetDAO.getAll();
		List<DataSetTableDBRow> rows = new ArrayList<DataSetTableDBRow>();
		
		int endIndex = Math.min(constraints.getOffset() + constraints.getMaxResults(), allDatasets.size());
		for(JPADataSetLO dslo : allDatasets.subList(constraints.getOffset(), endIndex))
		{
			rows.add(new DataSetTableDBRow(dslo));
		}
		return new QueryResult(rows, allDatasets.size());
	}
}
