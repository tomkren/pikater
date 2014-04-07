package org.pikater.web.experiment.validity;

import org.pikater.web.experiment.SchemaDataSource;

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
