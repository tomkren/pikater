package org.pikater.web.experiment_backup.validity;

import org.pikater.shared.experiment.webformat.SchemaDataSource;

public abstract class CommonState
{
	/**
	 * The original instance to check for validity.
	 */
	protected final SchemaDataSource source;
	
	public CommonState(SchemaDataSource source)
	{
		this.source = source;
	}

	public abstract void performCheck();
}
