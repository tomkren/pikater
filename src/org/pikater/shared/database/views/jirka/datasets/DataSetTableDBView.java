package org.pikater.shared.database.views.jirka.datasets;

import java.util.ArrayList;
import java.util.EnumSet;
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
		DESCRIPTION,
		APPROVE,
		DELETE;

		@Override
		public String getDisplayName()
		{
			switch(this)
			{
				case NUMBER_OF_INSTANCES:
					return "INSTANCES";
				default:
					return this.name();	
			}
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
					
				case APPROVE:
				case DELETE:
					return ColumnType.NAMED_ACTION;
					
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
		
		public static EnumSet<Column> getColumns(boolean adminMode)
		{
			if(adminMode)
			{
				return EnumSet.allOf(Column.class);
			}
			else
			{
				return EnumSet.complementOf(EnumSet.of(Column.OWNER, Column.APPROVE));
			}
		}
	}

	@Override
	public IColumn[] getColumns()
	{
		return (IColumn[]) Column.getColumns(adminMode()).toArray(new Column[0]);
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
		
		List<JPADataSetLO> allDatasets;
		
		if(this.adminMode()){
			allDatasets = DAOs.dataSetDAO.getAll();
		}else{
			allDatasets = DAOs.dataSetDAO.getByOwner(owner);
		}
		
		List<DataSetTableDBRow> rows = new ArrayList<DataSetTableDBRow>();
		
		int endIndex = Math.min(constraints.getOffset() + constraints.getMaxResults(), allDatasets.size());
		for(JPADataSetLO dslo : allDatasets.subList(constraints.getOffset(), endIndex))
		{
			rows.add(new DataSetTableDBRow(dslo));
		}
		return new QueryResult(rows, allDatasets.size());
	}
}
