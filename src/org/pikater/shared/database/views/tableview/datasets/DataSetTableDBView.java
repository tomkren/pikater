package org.pikater.shared.database.views.tableview.datasets;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.daos.DataSetDAO;
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
	 * <p>
	 * This enum is used for create Criteria API query in functions
	 * {@link DataSetDAO#getAll(int, int, ITableColumn, org.pikater.shared.database.views.base.SortOrder)} and 
	 * {@link DataSetDAO#getByOwner(JPAUser, int, int, ITableColumn, org.pikater.shared.database.views.base.SortOrder)}
	 * <p>
	 * If you want to change column names you can redefine function {@link Column#getDisplayName()}
	 */
	public enum Column implements ITableColumn
	{
		/*
		 * First the read-only properties.
		 */
		OWNER, // owner is expected to be declared first in the {@link #getColumns()} method
		CREATED,
		SIZE,
		DEFAULT_TASK_TYPE,
		NUMBER_OF_INSTANCES,
		DESCRIPTION,
		APPROVED,
		DOWNLOAD,
		DELETE;

		@Override
		public String getDisplayName()
		{
			switch(this)
			{
				case NUMBER_OF_INSTANCES:
					return "INSTANCES";
				case DEFAULT_TASK_TYPE:
					return "TASK_TYPE";
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
					
				case APPROVED:
					return DBViewValueType.BOOLEAN;
					
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
				return EnumSet.complementOf(EnumSet.of(Column.OWNER, Column.APPROVED));
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
		List<JPADataSetLO> allDatasets;
		int allDatasetCount=0;
		
		if(this.adminMode()){
			allDatasets = DAOs.dataSetDAO.getAllVisible(constraints.getOffset(),constraints.getMaxResults(),constraints.getSortColumn(),constraints.getSortOrder());
			allDatasetCount=DAOs.dataSetDAO.getAllVisibleCount();
		}else{
			allDatasets = DAOs.dataSetDAO.getByOwnerVisible(owner,constraints.getOffset(),constraints.getMaxResults(),constraints.getSortColumn(),constraints.getSortOrder());
			allDatasetCount=DAOs.dataSetDAO.getByOwnerVisibleCount(owner);
		}
		
		List<DataSetTableDBRow> rows = new ArrayList<DataSetTableDBRow>();
		
		for(JPADataSetLO dslo : allDatasets)
		{
			rows.add(new DataSetTableDBRow(dslo));
		}
		return new QueryResult(rows, allDatasetCount);
	}
}
