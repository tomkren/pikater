package org.pikater.shared.database.views.tableview.base;

import java.util.Collection;
import java.util.HashSet;

import org.pikater.shared.database.views.base.QueryConstraints;
import org.pikater.shared.database.views.base.QueryResult;

/**
 * Database table view designed to work well with Vaadin's table {@link com.vaadin.data.Container}.
 * This view's interface has been reduced as much as possible so that only the essential
 * information/functionality is provided. Advanced functionality like sorting rows is out-sourced.
 * 
 * Also, this view is homogenous - it doesn't allow adding or removing table columns and all
 * values in a single column are required to be of the same type.
 */
public abstract class AbstractTableDBView
{
	/**
	 * Returns the enumeration of all columns this view defines.
	 * @see {@link org.pikater.shared.database.views.tableview.users.UsersTableDBView} - example
	 * @return
	 */
	public abstract ITableColumn[] getColumns();
	
	public Collection<ITableColumn> getSortableColumns()
	{
		HashSet<ITableColumn> result = new HashSet<ITableColumn>();
		for(ITableColumn column : getColumns())
		{
			if(column.getColumnType().isSortable())
			{
				result.add(column);
			}
		}
		return result;
	}
	
	/**
	 * Returns the column by which this view is sorted by default.
	 * @return
	 */
	public abstract ITableColumn getDefaultSortOrder();
	
	/**
	 * Returns a non-cached initialized collection of rows.
	 * @param sortOrder
	 * @return
	 */
	public QueryResult queryRows(QueryConstraints constraints)
	{
		QueryResult queryResult = queryUninitializedRows(constraints);
		if(queryResult.getConstrainedResults().size() > constraints.getMaxResults())
		{
			throw new IllegalStateException("Child view provided more results than it was asked for.");
		}
		for(AbstractTableRowDBView row : queryResult.getConstrainedResults())
		{
			row.init(this);
		}
		return queryResult;
	}
	
	/**
	 * Returns a non-cached (with the exception of a handful of special cases) collection
	 * of uninitialized rows as defined by the query given in the argument.
	 * Initialization of the rows will be performed automatically.
	 * @return
	 */
	protected abstract QueryResult queryUninitializedRows(QueryConstraints constraints);
}
