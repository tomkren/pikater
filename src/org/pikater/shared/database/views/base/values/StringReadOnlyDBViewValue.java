package org.pikater.shared.database.views.base.values;

public class StringReadOnlyDBViewValue extends StringDBViewValue {
	public StringReadOnlyDBViewValue(String value) {
		super(value);
	}

	@Override
	public boolean isReadOnly() {
		return true;
	}

	@Override
	protected void updateEntities(String newValue) {
	}

	@Override
	protected void commitEntities() {
	}
}