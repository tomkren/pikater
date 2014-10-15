package org.pikater.shared.quartz.jobs.base;

import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.Trigger;

/**
 * A job that should start immediately (just after being scheduled)
 * and should only execute once.
 * 
 * @author SkyCrawl
 */
public abstract class ImmediateOneTimeJob extends AbstractJobWithArgs {
	public ImmediateOneTimeJob(int numArgs) {
		super(numArgs);
	}

	@Override
	public Trigger getJobTrigger() {
		return newTrigger().startNow().build();
	}
}