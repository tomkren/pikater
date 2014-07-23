package org.pikater.shared.quartz.jobs.base;

import org.quartz.InterruptableJob;

public abstract class InterruptibleOneTimeJob extends ImmediateOneTimeJob implements InterruptableJob{

	public InterruptibleOneTimeJob(int numArgs) {
		super(numArgs);
	}

}
