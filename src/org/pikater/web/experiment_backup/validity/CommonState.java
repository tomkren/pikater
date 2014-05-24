package org.pikater.web.experiment_backup.validity;

import org.pikater.shared.experiment.webformat.Experiment;

public abstract class CommonState
{
	/**
	 * The original instance to check for validity.
	 */
	protected final Experiment source;
	
	public CommonState(Experiment source)
	{
		this.source = source;
	}

	public abstract void performCheck();
}
