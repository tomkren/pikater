package org.pikater.shared.database.views.base.values;

/**
 * Abstract parameterized read-only or editable value with a method to store
 * the changed value in database.
 */
public abstract class AbstractDBViewValue<T extends Object>
{
	private final DBViewValueType type;
	private T lastCommitedValue;
	private T currentValue;
	
	public AbstractDBViewValue(DBViewValueType type, T value)
	{
		this.type = type;
		this.currentValue = value;
		setLastCommitedValue();
	}
	
	/**
	 * Declares the type of this value so that the calling GUI code knows how to render it.
	 * @return
	 */
	public DBViewValueType getType()
	{
		return type;
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
			updateEntities(newValue);
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
			commitEntities();
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
	
	//-----------------------------------------------------------------
	// ABSTRACT INTERFACE
	
	/**
	 * Is this value read-only?
	 * @return
	 */
	public abstract boolean isReadOnly();
	
	/**
	 * Called when a new value is received. This method must do appropriate actions
	 * to update related entities but NOT commit them to database yet. To commit, 
	 * the {@link #commitEntities()} is used.
	 * @param newValue
	 */
	protected abstract void updateEntities(T newValue);
	
	/**
	 * Called to store changes made by {@link #updateEntities(Object)} to database.
	 */
	protected abstract void commitEntities();
}
