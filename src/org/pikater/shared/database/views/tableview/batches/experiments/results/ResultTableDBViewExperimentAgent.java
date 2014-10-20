package org.pikater.shared.database.views.tableview.batches.experiments.results;

import java.util.ArrayList;
import java.util.List;

import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.views.base.query.QueryConstraints;
import org.pikater.shared.database.views.base.query.QueryResult;

public class ResultTableDBViewExperimentAgent extends ResultTableDBViewMin {
	protected String agentName;

	public ResultTableDBViewExperimentAgent(JPAExperiment experiment, String agentName) {
		super(experiment);
		this.agentName = agentName;
	}

	@Override
	public QueryResult queryUninitializedRows(QueryConstraints constraints) {
		List<JPAResult> results = DAOs.RESULTDAO.getByExperimentAndAgentNameWithModel(getExperiment(), agentName, constraints.getOffset(), constraints.getMaxResults());
		int resultCount = DAOs.RESULTDAO.getByExperimentAndAgentNameWithModelCount(getExperiment(), agentName);

		List<ResultTableDBRow> resultRows = new ArrayList<ResultTableDBRow>();
		for (JPAResult result : results) {
			resultRows.add(new ResultTableDBRow(result));
		}
		return new QueryResult(resultRows, resultCount);
	}
}