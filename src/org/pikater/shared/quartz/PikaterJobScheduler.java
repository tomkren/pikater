package org.pikater.shared.quartz;

import org.pikater.shared.logging.database.PikaterDBLogger;
import org.pikater.shared.quartz.jobs.base.ZeroArgJob;
import org.pikater.shared.quartz.jobs.crons.RemoveExpiredTrainedModels;
import org.pikater.shared.util.ReflectionUtils;
import org.quartz.SchedulerException;

public class PikaterJobScheduler
{
	private static MyJobScheduler staticScheduler = null;
	
	//-----------------------------------------------------------
	// SCHEDULER HANDLING
	
	/**
	 * Initialize and start the cron job scheduler. Your application will not terminate until you call the
	 * {@link #shutdown} method, because there will be active threads.
	 */
	public static synchronized void initStaticScheduler(String quartzConfAbsPath)
	{
		if(staticScheduler != null)
		{
			throw new IllegalStateException("Another instance of scheduler is running. Use the 'shutdown' method instead.");
		}
		else
		{
			PikaterJobScheduler.staticScheduler = new MyJobScheduler(quartzConfAbsPath);
			try
			{
				PikaterJobScheduler.staticScheduler.start();
			}
			catch (SchedulerException se)
			{
				throw new IllegalStateException("Could not initialize the application's static quartz scheduler.", se);
			}
		}
	}
	
	/**
	 * Shutdown the scheduler. 
	 */
	public static synchronized boolean shutdownStaticScheduler()
	{
		if(staticScheduler != null)
		{
			try
			{
				staticScheduler.shutdown();
	        }
			catch (SchedulerException se)
			{
				PikaterDBLogger.logThrowable("Could not shutdown the application's static quartz scheduler.", se);
				return false;
	        }
			finally
			{
				staticScheduler = null;
			}
		}
		return true;
	}
	
	//-----------------------------------------------------------
	// CONVENIENCE ROUTINES
	
	public static MyJobScheduler getJobScheduler()
	{
		if(staticScheduler == null)
		{
			throw new IllegalStateException("Scheduler has not been initialized. First call {@link #initStaticScheduler}.");
		}
		else
		{
			return staticScheduler;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void defineCronJobs()
	{
		for(Class<? extends Object> clazz : ReflectionUtils.getTypesFromPackage(RemoveExpiredTrainedModels.class.getPackage()))
		{
			if(!clazz.isInterface() && ZeroArgJob.class.isAssignableFrom(clazz))
			{
				try
				{
					staticScheduler.defineJob((Class<? extends ZeroArgJob>) clazz);
				}
				catch (Exception t)
				{
					PikaterDBLogger.logThrowable(String.format("Could not define the '%s' cron.", clazz.getName()), t);
				}
			}
			else
			{
				throw new IllegalArgumentException(String.format("The '%s' cron does not inherit from '%s'.", clazz.getName(), ZeroArgJob.class.getName()));
			}
		}
	}
}
