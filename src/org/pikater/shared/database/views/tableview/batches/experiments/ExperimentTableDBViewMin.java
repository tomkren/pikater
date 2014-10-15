package org.pikater.shared.database.views.tableview.batches.experiments;

import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.views.base.ITableColumn;

public class ExperimentTableDBViewMin extends ExperimentTableDBView {
	public ExperimentTableDBViewMin(JPAUser user, JPABatch batch) {
		super(user, batch);
	}

	@Override
	public Set<ITableColumn> getAllColumns() {
		return new LinkedHashSet<ITableColumn>(EnumSet.of(Column.STATUS, Column.STARTED, Column.FINISHED, Column.MODEL_STRATEGY));
	}

	@Override
	public Set<ITableColumn> getDefaultColumns() {
		return getAllColumns();
	}
}