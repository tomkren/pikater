package org.pikater.shared.database.views.tableview.base;

import java.util.HashMap;
import java.util.Map;

import org.pikater.shared.database.views.base.values.AbstractDBViewValue;

/**
 * This view represents a single row in the resulting table. Underlying
 * {@link AbstractDBViewValue} instance provides all information and
 * interface.
 */
public abstract class AbstractTableRowDBView
{
	private final Map<ITableColumn, AbstractDBViewValue<? extends Object>> columnToValueWrapper;
	private boolean initialized;
	
	public AbstractTableRowDBView()
	{
		this.columnToValueWrapper = new HashMap<ITableColumn, AbstractDBViewValue<? extends Object>>();
		this.initialized = false;
	}
	
	/**
	 * For internal use only. Initializes the row view and repeatedly calls
	 * {@link #initValueWrapper(ITableColumn column)}.
	 * @param dbView
	 */
	public void init(AbstractTableDBView dbView)
	{
		if(!initialized)
		{
			for(ITableColumn column : dbView.getColumns())
			{
				columnToValueWrapper.put(column, initValueWrapper(column));
			}
			initialized = true;
		}
		else
		{
			throw new IllegalStateException("This row has already been initialized.");
		}
	}
	
	/**
	 * Gets the value wrapper associated with this row and the provided column.
	 * @param column
	 * @return
	 */
	public AbstractDBViewValue<? extends Object> getValueWrapper(ITableColumn column)
	{
		if(initialized)
		{
			return columnToValueWrapper.get(column);
		}
		else
		{
			throw new IllegalStateException("This row has not been initialized.");
		}
	}
	
	/**
	 * Method to create an appropriate value wrapper for each of the table's columns,
	 * each associated with an appropriate value from this row.
	 * @param column
	 */
	public abstract AbstractDBViewValue<? extends Object> initValueWrapper(ITableColumn column);
	
	/**
	 * Stores all changes to this row to the database.
	 */
	public abstract void commitRow();
}
