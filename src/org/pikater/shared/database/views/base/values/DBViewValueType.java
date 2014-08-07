package org.pikater.shared.database.views.base.values;

/**
 * Enumeration defining supported types of values. GUI tables using a child of this view
 * are guaranteed to recognize these types and to be capable of working with them in a 
 * generic fashion.
 */
public enum DBViewValueType
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
	BOOLEAN,
	
	/**
	 * Corresponds to a button. If using this column type, you should only name the action and provide
	 * an execute callback. 
	 */
	NAMED_ACTION;
	
	public boolean isSortable()
	{
		return (this == STRING) || (this == BOOLEAN); 
	}
}