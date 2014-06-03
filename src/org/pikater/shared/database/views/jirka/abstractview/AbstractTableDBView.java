package org.pikater.shared.database.views.jirka.abstractview;

import java.util.Collection;

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
	 * Enumeration defining supported types of values. GUI tables using a child of this view
	 * are guaranteed to recognize these types and to be capable of working with them in a 
	 * generic fashion.
	 */
	public enum ColumnType
	{
		/**
		 * A single string value, read-only or editable.
		 * Make sure the {@link org.pikater.shared.database.views.jirka.abstractview.values.StringDBViewValue} type's
		 * generic parameter matches the one provided in the {@link #getResultJavaType()} method.
		 */
		STRING,
		
		/**
		 * A single value chosen from a set of values. Read-only or editable.
		 * Make sure the {@link org.pikater.shared.database.views.jirka.abstractview.values.RepresentativeDBViewValue} type's
		 * generic parameter matches the one provided in the {@link #getResultJavaType()} method.
		 */
		REPRESENTATIVE,
		
		/**
		 * True of false. Read-only or editable.
		 * Make sure the {@link org.pikater.shared.database.views.jirka.abstractview.values.BooleanDBViewValue} type's
		 * generic parameter matches the one provided in the {@link #getResultJavaType()} method.
		 */
		BOOLEAN;
		
		public Class<? extends Object> getResultJavaType()
		{
			switch(this)
			{
				case BOOLEAN:
					return Boolean.class;
				default:
					return String.class;
			}
		}
	}
	
	/**
	 * Returns the enumeration of all columns this view defines.
	 * @see {@link org.pikater.shared.database.views.jirka.users.UsersTableView} - example
	 * @return
	 */
	public abstract IColumn[] getColumns();
	
	/**
	 * Returns a non-cached initialized collection of rows.
	 * @param sortOrder
	 * @return
	 */
	public Collection<? extends AbstractTableRowDBView> getRows(QueryConstraints constraints)
	{
		@SuppressWarnings("unchecked")
		Collection<AbstractTableRowDBView> result = (Collection<AbstractTableRowDBView>) getUninitializedRows(constraints);
		for(AbstractTableRowDBView row : result)
		{
			row.init(this);
		}
		return result;
	}
	
	/**
	 * Returns a non-cached collection of uninitialized rows. Initialization
	 * will be performed in the calling code.
	 * @return
	 */
	protected abstract Collection<? extends AbstractTableRowDBView> getUninitializedRows(QueryConstraints constraints);
}
