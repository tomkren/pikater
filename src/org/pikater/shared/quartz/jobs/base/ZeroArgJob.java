package org.pikater.shared.quartz.jobs.base;

public abstract class ZeroArgJob extends AbstractJobWithArgs
{
	public ZeroArgJob()
	{
		super(0);
	}

	@Override
	public boolean argumentCorrect(int index, Object arg)
	{
		return true;
	}
}