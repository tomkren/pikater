package org.pikater.shared.quartz;

import static org.quartz.JobBuilder.newJob;

import java.io.File;
import java.util.Properties;

import org.pikater.shared.PropertiesHandler;
import org.pikater.shared.logging.PikaterLogger;
import org.quartz.JobBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

public class PikaterJobScheduler extends PropertiesHandler
{
	private static Scheduler scheduler = null;
	
	/**
	 * Initialize and start the cron job scheduler. Your application will not terminate until you call the
	 * {@link #shutdown} method, because there will be active threads.
	 */
	public static boolean init(String quartzConfAbsPath)
	{
		if(scheduler != null)
		{
			throw new IllegalStateException("Another instance of scheduler is running. Use the 'shutdown' method instead.");
		}
		else
		{
			try
			{
				/*
				 * By default, StdSchedulerFactory load a properties file named "quartz.properties" from the 'current working
				 * directory'. If that fails, then the "quartz.properties" file located (as a resource) in the org/quartz
				 * package is loaded.
				 * 
				 * We will provide our own configuration:
				 */
				
				String quartzConfPath = quartzConfAbsPath;
				if(!quartzConfAbsPath.endsWith(System.getProperty("file.separator")))
				{
					quartzConfPath = quartzConfAbsPath + System.getProperty("file.separator");
				}
				Properties quartzConf = openProperties(new File(quartzConfPath + "quartz-configuration.properties"));

				StdSchedulerFactory quartzFactory = new StdSchedulerFactory(quartzConf);
				scheduler = quartzFactory.getScheduler();

				//scheduler.getMetaData().
				
				// ****************************************************************
				/*
				 * THIS IS WHERE YOU TELL WHICH JOBS TO SCHEDULE.
				 */
				
				defineCronJob(TestJob.class);
				
				// ****************************************************************
				
				scheduler.start();
				return true;
			}
			catch (SchedulerException se)
			{
				PikaterLogger.logThrowable("Could not initialized the application's cron job scheduler.", se);
				return false;
			}
		}
	}
	
	/**
	 * Shutdown the scheduler. 
	 */
	public static boolean shutdown()
	{
		if(scheduler != null)
		{
			try
			{
				scheduler.shutdown();
				return true;
	        }
			catch (SchedulerException se)
			{
				PikaterLogger.logThrowable("Could not shutdown the application's cron job scheduler.", se);
				return false;
	        }
			finally
			{
				scheduler = null;
			}
		}
		else
		{
			throw new NullPointerException("Can not destroy a scheduler that has not been initialized. Call the 'init' method prior to this one.");
		}
	}
	
	private static void defineCronJob(Class<? extends IPikaterJob> jobClass)
	{
		JobBuilder jobBuilder = newJob(jobClass);
		IPikaterJob helperJobInstance;
		try
		{
			helperJobInstance = jobClass.newInstance();
			helperJobInstance.buildJob(jobBuilder);
			scheduler.scheduleJob(jobBuilder.build(), helperJobInstance.getJobTrigger()); // tell quartz to schedule the job using our trigger
		}
		catch (InstantiationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SchedulerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
