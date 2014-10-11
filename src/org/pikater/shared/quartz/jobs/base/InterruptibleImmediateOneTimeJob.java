package org.pikater.shared.quartz.jobs.base;

import org.quartz.InterruptableJob;
import org.quartz.UnableToInterruptJobException;

/**
 * A {@link ImmediateOneTimeJob} that is also interruptible.
 * 
 * @author SkyCrawl
 */
public abstract class InterruptibleImmediateOneTimeJob extends ImmediateOneTimeJob implements InterruptableJob
{
	private boolean interrupted;
	
	public InterruptibleImmediateOneTimeJob(int numArgs)
	{
		super(numArgs);
		this.interrupted = false;
	}
	
	@Override
	public void interrupt() throws UnableToInterruptJobException
	{
		/*
		 * Quartz doesn't automatically interrupt the jobs' threads - we must do it ourselves.
		 */
		
		this.interrupted = true;
	}
	
	protected boolean isInterrupted()
	{
		return interrupted;
	}
}