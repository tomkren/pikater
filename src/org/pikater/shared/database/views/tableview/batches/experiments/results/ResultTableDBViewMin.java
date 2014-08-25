package org.pikater.shared.database.views.tableview.batches.experiments.results;

import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.pikater.shared.database.jpa.JPAExperiment;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.views.base.ITableColumn;

public class ResultTableDBViewMin extends ResultTableDBViewExperiment 
{
	public ResultTableDBViewMin(JPAUser user, JPAExperiment experiment)
	{
		super(user, experiment);
	}

	@Override
	public Set<ITableColumn> getAllColumns()
	{
		return new LinkedHashSet<ITableColumn>(EnumSet.of(
				Column.AGENT_NAME,
				Column.NOTE,
				Column.ERROR_RATE,
				Column.KAPPA,
				Column.MEAN_ABS_ERR,
				Column.REL_ABS_ERR,
				Column.ROOT_MEAN_SQR_ERR,
				Column.ROOT_REL_SQR_ERR
		));
	}
	
	@Override
	public Set<ITableColumn> getDefaultColumns()
	{
		return getAllColumns();
	}
}