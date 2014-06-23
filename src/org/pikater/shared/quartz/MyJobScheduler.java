package org.pikater.shared.quartz;

import static org.quartz.JobBuilder.newJob;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Properties;

import org.pikater.shared.PropertiesHandler;
import org.pikater.shared.quartz.jobs.base.AbstractJobWithArgs;
import org.pikater.shared.quartz.jobs.base.ZeroArgJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public class MyJobScheduler extends PropertiesHandler
{
	private final Properties quartzConf;
	private Scheduler scheduler;
	
	/**
	 * By default, {@link StdSchedulerFactory} loads a properties file named "quartz.properties" from the 'current working
	 * directory'. If that fails, then the "quartz.properties" file located (as a resource) in the org/quartz
	 * package is loaded.</br></br>
	 * 
	 * We will provide our own configuration. 
	 * @param quartzConfAbsPath
	 */
	public MyJobScheduler(String quartzConfAbsPath)
	{
		String quartzConfPath = quartzConfAbsPath;
		if(!quartzConfAbsPath.endsWith(System.getProperty("file.separator")))
		{
			quartzConfPath = quartzConfAbsPath + System.getProperty("file.separator");
		}
		
		this.quartzConf = openProperties(new File(quartzConfPath + "quartz-configuration.properties"));
		this.scheduler = null;
	}
	
	/**
	 * Start the scheduler. No jobs will be defined after this call.
	 * @throws SchedulerException
	 */
	public void start() throws SchedulerException
	{
		if(scheduler != null)
		{
			throw new IllegalStateException("Scheduler is already running. Use the 'shutdown' method instead.");
		}
		else
		{
			StdSchedulerFactory quartzFactory = new StdSchedulerFactory(quartzConf);
			scheduler = quartzFactory.getScheduler();
			scheduler.start();
			
			// scheduler.getMetaData(). // might be useful sometimes
		}
	}
	
	/**
	 * Shuts down the scheduler. All defined jobs are lost.
	 * @throws SchedulerException
	 */
	public void shutdown() throws SchedulerException
	{
		if(scheduler != null)
		{
			scheduler.shutdown();
			scheduler = null;
		}
		else
		{
			throw new IllegalStateException("Scheduler has not been started. Use the 'start' method instead.");
		}
	}
	
	//--------------------------------------------------------
	// JOB SCHEDULLING INTERFACE
	
	/**
	 * Define a zero-argument job - build it, create it and schedule it.
	 * @param clazz
	 * @throws Throwable
	 */
	public void defineJob(Class<? extends ZeroArgJob> clazz) throws Throwable
	{
		defineJob(clazz, null);
	}

	/**
	 * Define an arbitrary job - build it, create it and schedule it.
	 * @param jobClass
	 */
	public void defineJob(Class<? extends AbstractJobWithArgs> jobClass, Object[] args) throws Throwable
	{
		if(scheduler == null)
		{
			throw new IllegalStateException("Scheduler has not been started. Use the 'start' method instead.");
		}
		else if(Modifier.isAbstract(jobClass.getModifiers()))
		{
			throw new IllegalArgumentException("Provided class is abstract - no instances can be created.");
		}
		else
		{
			JobBuilder jobBuilder = newJob(jobClass);
			JobDetail detail = jobBuilder.build();
			AbstractJobWithArgs helperJobInstance = jobClass.newInstance();
			helperJobInstance.buildJob(jobBuilder);
			setArguments(detail, helperJobInstance, args);
			
			scheduler.scheduleJob(detail, helperJobInstance.getJobTrigger()); // tell quartz to schedule the job using our trigger
		}
	}
	
	//--------------------------------------------------------
	// OTHER INTERFACE
	
	protected static void setArguments(JobDetail detail, AbstractJobWithArgs helperJobInstance, Object[] args)  
	{
		if(args.length != helperJobInstance.getNumberOfArguments())
		{
			throw new IllegalArgumentException(String.format("This class requires exactly %d arguments. You provided %d.", 
					helperJobInstance.getNumberOfArguments(), args.length));
		}
		else
		{
			for(int i = 0; i < args.length; i++)
			{
				if(helperJobInstance.argumentCorrect(i, args[i]))
				{
					detail.getJobDataMap().put(String.valueOf(i), args[i]);
				}
				else
				{
					throw new IllegalArgumentException(String.format("Argument %d not correct.", i));
				}
			}
		}
	}
}