package org.pikater.shared.database.views.tableview.test;

import java.util.HashSet;
import java.util.Set;

import org.pikater.shared.database.views.base.ITableColumn;
import org.pikater.shared.database.views.base.values.AbstractDBViewValue;
import org.pikater.shared.database.views.base.values.BooleanDBViewValue;
import org.pikater.shared.database.views.base.values.NamedActionDBViewValue;
import org.pikater.shared.database.views.base.values.RepresentativeDBViewValue;
import org.pikater.shared.database.views.base.values.StringReadOnlyDBViewValue;
import org.pikater.shared.database.views.tableview.AbstractTableRowDBView;
import org.pikater.shared.database.views.tableview.test.TestTableDBView.Column;

public class TestTableDBRow extends AbstractTableRowDBView {
	private static final String mockupValue = "value";

	private final int rowNr;
	private final Set<String> mockupSet;

	public TestTableDBRow(int rowNr) {
		this.rowNr = rowNr;
		this.mockupSet = new HashSet<String>();
		this.mockupSet.add(mockupValue);
	}

	@Override
	public AbstractDBViewValue<? extends Object> initValueWrapper(
			ITableColumn column) {
		
		Column specificColumn = (Column) column;
		switch (specificColumn) {
		case COLUMN1:
			return new StringReadOnlyDBViewValue(String.format("%s_%d",
					column.getDisplayName(), rowNr));
		case COLUMN2:
			return new BooleanDBViewValue(true) {
				@Override
				protected void updateEntities(Boolean newValue) {
				}

				@Override
				protected void commitEntities() {
				}
			};
		case COLUMN3:
			return new RepresentativeDBViewValue(mockupSet, mockupValue) {
				@Override
				protected void updateEntities(String newValue) {
				}

				@Override
				protected void commitEntities() {
				}
			};
		case COLUMN4:
			return new NamedActionDBViewValue("Action") {
				@Override
				public boolean isEnabled() {
					return true;
				}

				@Override
				protected void updateEntities() {
				}

				@Override
				protected void commitEntities() {
				}
			};
		default:
			throw new IllegalStateException("Unknown state: "
					+ specificColumn.name());
		}
	}

	@Override
	protected void commitRow() {
	}
}