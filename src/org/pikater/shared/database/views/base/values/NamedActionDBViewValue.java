package org.pikater.shared.database.views.base.values;

/**
 * Corresponds to a button. You have an action that is named and can be arbitrarily called.
 */
public abstract class NamedActionDBViewValue extends AbstractDBViewValue<String> {
	private boolean executedAndNotCommitted;

	/** 
	 * @param value the name of the action 
	 */
	public NamedActionDBViewValue(String value) {
		super(DBViewValueType.NAMED_ACTION, value);
		setLastCommitedValue();
	}

	@Override
	public boolean isReadOnly() {
		return true; // read-only since the name of the action is constant
	}

	/**
	 * Replaced by {@link #updateEntities()}.
	 */
	@Deprecated()
	@Override
	protected void updateEntities(String newValue) {
		throw new UnsupportedOperationException("This method is obsolete for this type. See the Javadoc.");
	}

	@Override
	public boolean isEdited() {
		return executedAndNotCommitted;
	}

	@Override
	public void setLastCommitedValue() {
		this.executedAndNotCommitted = false;
	}

	public void executeAction() {
		updateEntities();
		executedAndNotCommitted = true;
	}

	/** 
	 * @return whether this action is enabled for the given row
	 */
	public abstract boolean isEnabled();

	/**
	 * Execute this action and appropriately update all related entities. However, do
	 * not store anything to database yet - {@link #commitEntities()} is responsible for that. 
	 */
	protected abstract void updateEntities();
}