package org.pikater.shared.logging.web;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.pikater.shared.logging.GeneralPikaterLogger;
import org.pikater.shared.logging.IPikaterLogger;

public class PikaterWebLogger extends GeneralPikaterLogger
{
	private static IPikaterLogger innerLogger;
	static
	{
		innerLogger = new IPikaterLogger()
		{
			private final Logger innerLogger = Logger.getLogger("log4j");
			
			@Override
			public void logThrowable(String problemDescription, Throwable t)
			{
				innerLogger.log(Level.SEVERE, "exception occured: " + problemDescription + "\n" + throwableToStackTrace(t));
			}
			
			@Override
			public void log(Level logLevel, String message)
			{
				innerLogger.log(logLevel, message);
			}
		};
	}
	
	public static IPikaterLogger getLogger()
	{
		if(innerLogger == null)
		{
			throw new NullPointerException("Logger has not been set. Call the 'setLogger' method prior to calling this one.");
		}
		else
		{
			return innerLogger;
		}
	}
	
	public static void logThrowable(String problemDescription, Throwable t)
	{
		getLogger().logThrowable(problemDescription, t);
	}

	public static void log(Level logLevel, String message)
	{
		getLogger().log(logLevel, message);
	}
}
