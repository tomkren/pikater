package org.pikater.shared.database.views.jirka.datasets;

import java.util.ArrayList;
import java.util.Collection;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.views.jirka.abstractview.AbstractTableDBView;
import org.pikater.shared.database.views.jirka.abstractview.AbstractTableRowDBView;
import org.pikater.shared.database.views.jirka.abstractview.IColumn;
import org.pikater.shared.database.views.jirka.abstractview.QueryConstraints;

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
		NUMBER_OF_INSANCES,
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
					
				case NUMBER_OF_INSANCES:
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

	@Override
	public Collection<? extends AbstractTableRowDBView> getUninitializedRows(QueryConstraints constraints)
	{
		// IMPORTANT: as stated in Javadoc, the result collection should not be internally cached, so:
		
		// TODO: use constraints
		
		Collection<DataSetTableRow> rows = new ArrayList<DataSetTableRow>();
		for(JPADataSetLO dslo : DAOs.dataSetDAO.getAll())
		{
			rows.add(new DataSetTableRow(dslo));
		}
		return rows;
	}
}
