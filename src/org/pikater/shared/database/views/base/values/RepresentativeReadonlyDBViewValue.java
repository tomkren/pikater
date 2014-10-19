package org.pikater.shared.database.views.base.values;

import java.util.Set;

/**
 * A single value chosen from a set of values, NOT editable.
 */
public class RepresentativeReadonlyDBViewValue extends RepresentativeDBViewValue {
	public RepresentativeReadonlyDBViewValue(Set<String> values, String selectedValue) {
		super(values, selectedValue);
	}

	@Override
	public boolean isReadOnly() {
		return true;
	}

	@Override
	protected void commitEntities() {
	}

	@Override
	protected void updateEntities(String newValue) {
	}
}