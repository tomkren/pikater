package org.pikater.shared.quartz.jobs.base;

import org.quartz.InterruptableJob;

public abstract class InterruptibleImmediateOneTimeJob extends ImmediateOneTimeJob implements InterruptableJob{

	public InterruptibleImmediateOneTimeJob(int numArgs) {
		super(numArgs);
	}

}
