package org.pikater.web.experiment_backup.validity;

import org.pikater.shared.experiment.webformat.ExperimentGraph;

public abstract class CommonState
{
	/**
	 * The original instance to check for validity.
	 */
	protected final ExperimentGraph source;
	
	public CommonState(ExperimentGraph source)
	{
		this.source = source;
	}

	public abstract void performCheck();
}
