package org.pikater.shared.database.views.tableview.batches;

import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.views.base.ITableColumn;

public class BatchTableDBViewUserMin extends BatchTableDBViewUser {
	public BatchTableDBViewUserMin(JPAUser user) {
		super(user);
	}

	@Override
	public Set<ITableColumn> getAllColumns() {
		return new LinkedHashSet<ITableColumn>(EnumSet.of(Column.FINISHED, Column.STATUS, Column.CREATED, Column.NAME, Column.NOTE));
	}

	@Override
	public Set<ITableColumn> getDefaultColumns() {
		return getAllColumns();
	}
}