package org.pikater.shared.quartz.jobs.base;

/**
 * Basic job without arguments.
 * 
 * @author SkyCrawl
 */
public abstract class ZeroArgJob extends AbstractJobWithArgs
{
	public ZeroArgJob()
	{
		super(0);
	}

	@Override
	public boolean argumentCorrect(Object argument, int argIndex)
	{
		return true;
	}
}