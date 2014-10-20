package org.pikater.shared.database.views.tableview.batches.experiments;

import java.util.ArrayList;
import java.util.List;

import org.pikater.shared.database.jpa.JPABatch;
import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.views.base.query.QueryConstraints;
import org.pikater.shared.database.views.base.query.QueryResult;

public class ExperimentWithModelTableDBView extends ExperimentTableDBView {

	public ExperimentWithModelTableDBView(JPAUser user, JPABatch batch) {
		super(user, batch);
	}

	@Override
	public QueryResult queryUninitializedRows(QueryConstraints constraints) {
		List<JPAExperiment> experiments = DAOs.EXPERIMENTDAO.getByBatchWithModel(batch, constraints.getOffset(), constraints.getMaxResults());
		int experimentCount = DAOs.EXPERIMENTDAO.getByBatchWithModelCount(batch);

		List<ExperimentTableDBRow> resultRows = new ArrayList<ExperimentTableDBRow>();
		for (JPAExperiment experiment : experiments) {
			resultRows.add(new ExperimentTableDBRow(experiment));
		}
		return new QueryResult(resultRows, experimentCount);
	}
}
