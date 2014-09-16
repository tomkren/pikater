package org.pikater.shared.logging.web;

import java.util.logging.Level;

import org.pikater.shared.logging.IPikaterLogger;

public class PikaterWebLogger
{
	private static IPikaterLogger logger = null;
	
	public static void setLogger(IPikaterLogger logger)
	{
		PikaterWebLogger.logger = logger;
	}
	
	public static IPikaterLogger getLogger()
	{
		if(logger == null)
		{
			throw new NullPointerException("Logger has not been set. Call the 'setLogger' method prior to calling this one.");
		}
		else
		{
			return logger;
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
