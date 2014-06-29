package org.pikater.shared.database.views.tableview.datasets;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.pikater.shared.database.jpa.JPADataSetLO;
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
public class DataSetTableDBView extends AbstractTableDBView
{
	private JPAUser owner;
	
	/**
	 * By default, admin mode (all datasets of all users) will be inspected. 
	 */
	public DataSetTableDBView()
	{
		this.owner = null;
	}
	
	/** 
	 * @param owner The user whose datasets to display. If null (admin mode), all datasets should
	 * be provided in the {@link #queryUninitializedRows(QueryConstraints constraints)} method instead.
	 */
	public void setDatasetOwner(JPAUser owner)
	{
		this.owner = owner;
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
		OWNER, // owner is expected to be declared first in the {@link #getColumns()} method
		CREATED,
		NUMBER_OF_INSTANCES,
		DEFAULT_TASK_TYPE,
		SIZE,
		DESCRIPTION,
		APPROVE,
		DOWNLOAD,
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
		public DBViewValueType getColumnType()
		{
			switch(this)
			{
				case OWNER:
				case CREATED:
				case NUMBER_OF_INSTANCES:
				case DEFAULT_TASK_TYPE:
				case SIZE:
				case DESCRIPTION:
					return DBViewValueType.STRING;
					
				case APPROVE:
				case DOWNLOAD:
				case DELETE:
					return DBViewValueType.NAMED_ACTION;
					
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
	public ITableColumn[] getColumns()
	{
		return (ITableColumn[]) Column.getColumns(adminMode()).toArray(new Column[0]);
	}
	
	@Override
	public ITableColumn getDefaultSortOrder()
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
