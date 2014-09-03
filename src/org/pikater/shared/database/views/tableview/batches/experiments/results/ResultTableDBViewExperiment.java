package org.pikater.shared.database.views.tableview.batches.experiments.results;

import java.util.ArrayList;
import java.util.List;

import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAResult;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.views.base.query.QueryConstraints;
import org.pikater.shared.database.views.base.query.QueryResult;

/**
 * A generic view for tables displaying result errors and statistic.  
 */
public class ResultTableDBViewExperiment extends ResultTableDBView
{
	private final JPAUser owner;
	private final JPAExperiment experiment;
	
	/**  
	 * @param user The user whose experiment results to display. If null (admin mode), all datasets should
	 * be provided in the {@link #queryUninitializedRows(QueryConstraints constraints)} method.
	 * The owner of the batch of experiment is compared to the given user.
	 * @param experiment The experiment for which the results are listed
	 */
	public ResultTableDBViewExperiment(JPAExperiment experiment)
	{
		this.owner = experiment.getBatch().getOwner();
		this.experiment = experiment;
	}
	
	public JPAExperiment getExperiment()
	{
		return experiment;
	}
	
	@Override
	public QueryResult queryUninitializedRows(QueryConstraints constraints)
	{
		List<JPAResult> allResults;
		if(owner==null)
		{
			allResults=experiment.getResults();
		}
		else
		{
			if(owner.getId() == experiment.getBatch().getOwner().getId())
			{
				allResults=experiment.getResults();
			}
			else
			{
				allResults=new ArrayList<JPAResult>();
			}
		}
		
		int endIndex = Math.min(constraints.getOffset() + constraints.getMaxResults(), allResults.size());
		List<ResultTableDBRow> resultRows = new ArrayList<ResultTableDBRow>();
		for(JPAResult result : allResults.subList(constraints.getOffset(), endIndex))
		{
			resultRows.add(new ResultTableDBRow(result));
		}
		return new QueryResult(resultRows, allResults.size());
	}
}