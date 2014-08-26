package org.pikater.shared.database.views.tableview.batches.experiments.results;

import java.util.ArrayList;
import java.util.List;

import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.views.base.query.QueryConstraints;
import org.pikater.shared.database.views.base.query.QueryResult;

public class ResultWithModelAgentNameTableDBView extends ResultTableDBView {

	protected JPAExperiment experiment;
	protected String agentName;
	
	public ResultWithModelAgentNameTableDBView(JPAExperiment experiment, String agentName){
		this.experiment=experiment;
		this.agentName=agentName;
	}
	
	@Override
	protected QueryResult queryUninitializedRows(QueryConstraints constraints) {
		List<JPAResult> results=DAOs.resultDAO.getByExperimentAndAgentNameWithModel(experiment, agentName, constraints.getOffset(), constraints.getMaxResults());
		int resultCount=DAOs.resultDAO.getByExperimentAndAgentNameWithModelCount(experiment, agentName);
		
		List<ResultTableDBRow> resultRows=new ArrayList<ResultTableDBRow>();
		for(JPAResult result : results){
			resultRows.add(new ResultTableDBRow(result));
		}
		
		return new QueryResult(resultRows, resultCount);
	}

}
