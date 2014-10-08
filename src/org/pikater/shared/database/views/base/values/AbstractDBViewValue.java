package org.pikater.shared.database.views.base.values;

import org.pikater.shared.database.views.tableview.AbstractTableRowDBView;

/**
 * Abstract parameterized read-only or editable value with a method to store
 * the changed value in database.
 */
public abstract class AbstractDBViewValue<T extends Object>
{
	private final DBViewValueType type;
	private T lastCommitedValue;
	private T currentValue;
	private IOnValueCommitted onCommitCallback;
	
	public AbstractDBViewValue(DBViewValueType type, T value)
	{
		this.type = type;
		this.currentValue = value;
		this.onCommitCallback = null;
		setLastCommitedValue();
	}
	
	/**
	 * Declares the type of this value so that the calling GUI code knows how to render it.
	 */
	public DBViewValueType getType()
	{
		return type;
	}
	
	/**
	 * Gets the currently set value.
	 */
	public T getValue()
	{
		return currentValue;
	}

	/**
	 * Sets the given value.
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
	 */
	public boolean isEdited()
	{
		return !getValue().equals(lastCommitedValue);
	}
	
	/**
	 * Sets a custom action to be called when this value is committed to database.
	 */
	public void setOnCommitCallback(IOnValueCommitted onCommitCallback)
	{
		this.onCommitCallback = onCommitCallback;
	}
	
	/**
	 * Stores the current value to database if updated.
	 */
	public void commit(AbstractTableRowDBView row)
	{
		if(isEdited())
		{
			commitEntities();
			onCommit(row);
		}
	}
	
	/**
	 * Method defining action to be taken when this value is committed to database.</br>
	 * Use only if you know what you're doing.
	 */
	public void onCommit(AbstractTableRowDBView row)
	{
		setLastCommitedValue();
		if(onCommitCallback != null)
		{
			onCommitCallback.onCommitted(row, this);
		}
	}
	
	//-----------------------------------------------------------------
	// PRIVATE & PROTECTED INTERFACE
	
	/**
	 * Sets this value as committed.
	 */
	protected void setLastCommitedValue()
	{
		lastCommitedValue = getValue();
	}
	
	//-----------------------------------------------------------------
	// SPECIAL TYPES
	
	public static interface IOnValueCommitted
	{
		/**
		 * Called when a value is edited and committed to database.
		 */
		void onCommitted(AbstractTableRowDBView row, AbstractDBViewValue<?> value);
	}
	
	//-----------------------------------------------------------------
	// ABSTRACT INTERFACE
	
	/**
	 * Is this value read-only?
	 */
	public abstract boolean isReadOnly();
	
	/**
	 * Called when a new value is received. This method must do appropriate actions
	 * to update related entities but NOT commit them to database yet. To commit, 
	 * the {@link #commitEntities()} is used.
	 */
	protected abstract void updateEntities(T newValue);
	
	/**
	 * Called to store changes made by {@link #updateEntities(Object)} to database.
	 */
	protected abstract void commitEntities();
}
