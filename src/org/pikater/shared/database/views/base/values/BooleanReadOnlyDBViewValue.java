package org.pikater.shared.database.views.base.values;

/**
 * True of false value, not editable.
 */
public class BooleanReadOnlyDBViewValue extends BooleanDBViewValue {
	public BooleanReadOnlyDBViewValue(Boolean value) {
		super(value);
	}

	@Override
	public boolean isReadOnly() {
		return true;
	}

	@Override
	protected void updateEntities(Boolean newValue) {
	}

	@Override
	protected void commitEntities() {
	}
}