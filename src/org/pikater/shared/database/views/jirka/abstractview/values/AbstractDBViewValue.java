package org.pikater.shared.database.views.jirka.abstractview.values;

import org.pikater.shared.database.views.jirka.abstractview.AbstractTableDBView.ColumnType;

/**
 * Abstract parameterized read-only or editable value with a method to store
 * the changed value in database.
 */
public abstract class AbstractDBViewValue<T extends Object>
{
	private final ColumnType type;
	private T lastCommitedValue;
	private T currentValue;
	private final boolean readOnly;
	
	public AbstractDBViewValue(ColumnType type, T value, boolean readOnly)
	{
		this.type = type;
		this.currentValue = value;
		this.readOnly = readOnly;
		setLastCommitedValue();
	}
	
	/**
	 * Declares the type of this value so that the calling GUI code knows how to render it.
	 * @return
	 */
	public ColumnType getType()
	{
		return type;
	}
	
	/**
	 * Is this value read-only?
	 * @return
	 */
	public boolean isReadOnly()
	{
		return readOnly;
	}
	
	/**
	 * Gets the currently set value.
	 * @return
	 */
	public T getValue()
	{
		return currentValue;
	}

	/**
	 * Sets the given value.
	 * @param newValue
	 * @throws UnsupportedOperationException if this value is read-only
	 */
	public void setValue(T newValue) throws UnsupportedOperationException
	{
		if(isReadOnly())
		{
			throw new UnsupportedOperationException("This value is read-only.");
		}
		else
		{
			this.currentValue = newValue;
		}
	}
	
	/**
	 * Has this value been edited and not committed?
	 * @return
	 */
	public boolean isEdited()
	{
		return !getValue().equals(lastCommitedValue);
	}
	
	/**
	 * Stores the current value to database if updated.
	 */
	public void commit()
	{
		if(isEdited())
		{
			commitValue();
			setLastCommitedValue();
		}
	}
	
	/**
	 * Sets this value as committed.
	 */
	private void setLastCommitedValue()
	{
		lastCommitedValue = getValue();
	}
	
	/**
	 * The procedure that actually stores the changed value to database.
	 */
	protected abstract void commitValue();
}
