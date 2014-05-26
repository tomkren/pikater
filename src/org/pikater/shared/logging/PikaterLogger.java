package org.pikater.shared.logging;

import java.util.logging.Level;

public class PikaterLogger
{
	private static IPikaterLogger logger = null;
	
	public static void setLogger(IPikaterLogger logger)
	{
		PikaterLogger.logger = logger;
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
