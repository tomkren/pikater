package org.pikater.shared.database.views.jirka.datasets;

import java.util.ArrayList;
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
public class DataSetTableView extends AbstractTableDBView{
	/**
	 * Table headers will be presented in the order defined here, so
	 * make sure to order them right :). 
	 */
	public enum Column implements IColumn
	{
		/*
		 * First the read-only properties.
		 */
		DESCRIPTION,
		NUMBER_OF_INSTANCES,
		DEFAULT_TASK_TYPE,
		SIZE,
		CREATED,
		OWNER;

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
				case DESCRIPTION:
				case DEFAULT_TASK_TYPE:
				case CREATED:
				case OWNER:
					return ColumnType.STRING;
					
				case NUMBER_OF_INSTANCES:
				case SIZE:
					return ColumnType.STRING;
					
				default:
					throw new IllegalStateException("Unknown state: " + name());
			}
		}
	}

	@Override
	public IColumn[] getColumns()
	{
		return Column.values();
	}
	
	/** 
	 * Constructor.
	 * @param user The user to display datasets for. If null, all datasets (admin mode) should
	 * be returned.
	 */
	public DataSetTableView(JPAUser user)
	{
		// TODO:
	}

	@Override
	public QueryResult queryUninitializedRows(QueryConstraints constraints)
	{
		// TODO: NOW USES CONSTRAINTS GIVEN IN ARGUMENT BUT IT'S A SHALLOW AND INCORRECT IMPLEMENTATION - SHOULD BE NATIVE
		
		List<JPADataSetLO> allDatasets = DAOs.dataSetDAO.getAll();
		List<DataSetTableRow> rows = new ArrayList<DataSetTableRow>();
		
		int endIndex = Math.min(constraints.getOffset() + constraints.getMaxResults(), allDatasets.size());
		for(JPADataSetLO dslo : allDatasets.subList(constraints.getOffset(), endIndex))
		{
			rows.add(new DataSetTableRow(dslo));
		}
		return new QueryResult(rows, allDatasets.size());
	}
}
