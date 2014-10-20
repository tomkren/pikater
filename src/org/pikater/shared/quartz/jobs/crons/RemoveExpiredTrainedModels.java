package org.pikater.shared.quartz.jobs.crons;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.quartz.jobs.base.ZeroArgJob;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

/**
 * Even though class is not used at all, it serves as a nice example
 * of how to define a cron in our application.
 * 
 * @author SkyCrawl
 */
@DisallowConcurrentExecution
public class RemoveExpiredTrainedModels extends ZeroArgJob {
	@Override
	public void buildJob(JobBuilder builder) {
		// builder.withIdentity("RemoveExpiredTrainedModels", "Jobs");
	}

	@Override
	public Trigger getJobTrigger() {
		return newTrigger()
		// .withIdentity("TestJobTrigger", "Triggers")
				.startNow().withSchedule(simpleSchedule().withIntervalInHours(24 * 7) // 1 week
						.repeatForever()).build();
	}

	@Override
	public void execute() throws JobExecutionException {
		//TODO: is this OK? Deleting models associated with experiments, but leaving the experiment statistic untouched
		//(apart from removing model ID)
		try {
			DAOs.MODELDAO.removeOldModels((short) 90);//remove models older than 90 days
		} catch (Exception e) {
			throw new JobExecutionException("Error while trying to delete old models.");
		}
	}
}