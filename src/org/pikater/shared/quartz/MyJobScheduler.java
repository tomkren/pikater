package org.pikater.shared.quartz;

import static org.quartz.JobBuilder.newJob;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Properties;

import org.pikater.shared.PropertiesHandler;
import org.pikater.shared.logging.database.PikaterDBLogger;
import org.pikater.shared.quartz.jobs.base.AbstractJobWithArgs;
import org.pikater.shared.quartz.jobs.base.ZeroArgJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.UnableToInterruptJobException;
import org.quartz.impl.StdSchedulerFactory;

/**
 * A custom implementation of a job/task scheduler based on {@link Scheduler},
 * providing some convenience interface.
 * 
 * @author SkyCrawl 
 */
public class MyJobScheduler extends PropertiesHandler {
	/**
	 * Reference to the '.properties' file used as configuration
	 * for {@link #scheduler}.
	 */
	private final Properties quartzConf;

	/**
	 * The underlying scheduled used to schedule jobs.
	 */
	private Scheduler scheduler;

	/**
	 * By default, {@link StdSchedulerFactory} loads a ".properties" configuration
	 * file named "quartz.properties" from the 'current working directory'. If that fails,
	 * the "quartz.properties" file located (as a resource) in the "org.quartz"
	 * package is loaded and that is what we are providing.
	 * 
	 */
	public MyJobScheduler(String quartzConfAbsPath) {
		String quartzConfPath = quartzConfAbsPath;
		if (!quartzConfAbsPath.endsWith(System.getProperty("file.separator"))) {
			quartzConfPath = quartzConfAbsPath + System.getProperty("file.separator");
		}

		this.quartzConf = openProperties(new File(quartzConfPath + "quartz-configuration.properties"));
		this.scheduler = null;
	}

	//--------------------------------------------------------
	// SCHEDULER RELATED INTERFACE

	/**
	 * Start the scheduler. No jobs are defined after this call.
	 * 
	 * @throws SchedulerException
	 */
	public void start() throws SchedulerException {
		if (scheduler != null) {
			throw new IllegalStateException("Scheduler is already running. Use the 'shutdown' method instead.");
		} else {
			StdSchedulerFactory quartzFactory = new StdSchedulerFactory(quartzConf);
			scheduler = quartzFactory.getScheduler();
			scheduler.start();

			// scheduler.getMetaData(). // might be useful sometimes
		}
	}

	/**
	 * Shuts down the scheduler. All defined jobs are lost.
	 * 
	 * @throws SchedulerException
	 */
	public void shutdown() throws SchedulerException {
		if (scheduler != null) {
			scheduler.shutdown();
			scheduler = null;
		} else {
			throw new IllegalStateException("Scheduler has not been started. Use the 'start' method instead.");
		}
	}

	//--------------------------------------------------------
	// JOB SCHEDULLING/INTERRUPTING INTERFACE

	/**
	 * Defines a zero-argument job - builds it, creates it and schedules it.
	 * 
	 * @throws Exception
	 */
	public JobKey defineJob(Class<? extends ZeroArgJob> clazz) throws Exception {
		return defineJob(clazz, null);
	}

	/**
	 * Defines an arbitrary job - builds it, creates it and schedules it.
	 * 
	 * @throws Exception
	 */
	public JobKey defineJob(Class<? extends AbstractJobWithArgs> jobClass, Object[] args) throws Exception {
		if (scheduler == null) {
			throw new IllegalStateException("Scheduler has not been started. Use the 'start' method instead.");
		} else if (Modifier.isAbstract(jobClass.getModifiers())) {
			throw new IllegalArgumentException("Provided class is abstract - no instances can be created.");
		} else {
			// abstract job building
			JobBuilder jobBuilder = newJob(jobClass);
			JobDetail detail = jobBuilder.build();

			// pass arguments
			AbstractJobWithArgs helperJobInstance = jobClass.newInstance();
			helperJobInstance.buildJob(jobBuilder);
			setArguments(detail, helperJobInstance, args);

			// tell quartz to schedule the job using our trigger
			scheduler.scheduleJob(detail, helperJobInstance.getJobTrigger());

			// and return
			return detail.getKey();
		}
	}

	/**
	 * Interrupts the given job.
	 */
	public void interruptJob(JobKey key) {
		try {
			scheduler.interrupt(key);
		} catch (UnableToInterruptJobException e) {
			PikaterDBLogger.logThrowable("Could not interrupt job with key: " + key.toString(), e);
		}
	}

	//--------------------------------------------------------
	// OTHER INTERFACE

	/**
	 * Sets arguments to a job defined by the arguments. This method
	 * should be used prior to scheduling the job.
	 * 
	 * @throws Exception
	 */
	protected static void setArguments(JobDetail detail, AbstractJobWithArgs helperJobInstance, Object[] args) throws Exception {
		if (args.length != helperJobInstance.getNumberOfArguments()) {
			throw new IllegalArgumentException(String.format("This class requires exactly %d arguments. You provided %d.", helperJobInstance.getNumberOfArguments(), args.length));
		} else {
			for (int i = 0; i < args.length; i++) {
				if (helperJobInstance.argumentCorrect(args[i], i)) {
					detail.getJobDataMap().put(String.valueOf(i), args[i]);
				} else {
					throw new IllegalArgumentException(String.format("Argument %d not correct.", i));
				}
			}
		}
	}
}
