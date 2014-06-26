package org.pikater.shared.database.views.base.values;

/**
 * Corresponds to a button. You have an action that is named and can be arbitrarily called.
 */
public abstract class NamedActionDBViewValue extends AbstractDBViewValue<String>
{
	/** 
	 * @param value the name of the action 
	 */
	public NamedActionDBViewValue(String value)
	{
		super(DBViewValueType.NAMED_ACTION, value);
	}
	
	@Override
	public boolean isReadOnly()
	{
		return true; // read-only since the name of the action is constant
	}
	
	/**
	 * Should not be used since this value is read only and the original
	 * implementation of this method has no effect then.
	 * Replaced by {@link #actionExecuted}.
	 */
	@Deprecated()
	@Override
	public void commit()
	{
		throw new UnsupportedOperationException("This method is obsolete for this type. See the Javadoc.");
	}
	
	/**
	 * Called as a replacement for {@link #commit} and {@link @setValue}.
	 * @param commit whether changes made by this action should also be committed to DB right away
	 */
	public void actionExecuted(boolean commit)
	{
		updateEntities();
		if(commit)
		{
			commitEntities();
		}
	}
	
	/**
	 * Replaced by {@link #updateEntities()}.
	 */
	@Deprecated()
	@Override
	protected void updateEntities(String newValue)
	{
		throw new UnsupportedOperationException("This method is obsolete for this type. See the Javadoc.");
	}
	
	/** 
	 * @return whether this action is enabled for the given row
	 */
	public abstract boolean isEnabled();
	
	/**
	 * Execute this action and appropriately update all related entities. However, do
	 * not store anything to database yet - {@link #commitChanges} is responsible for that. 
	 */
	protected abstract void updateEntities();
}