package org.pikater.web;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppLogger
{
	private static final Logger logger = Logger.getAnonymousLogger();
			
	public static Logger getLogger()
	{
		return logger;
	}
	
	public static void logThrowable(String problemDescription, Throwable t)
	{
		String header = "exception occured: " + problemDescription + "\n";
		String stackTrace = printStackTrace(t);
		logger.log(Level.SEVERE, header + stackTrace);
	}
	
	public static void logErrorWithoutException(String fileAndMethod, String problemDescription)
	{
		logger.log(Level.SEVERE, fileAndMethod + "' :\n" + problemDescription);
	}
	
	public static void log(Level logLevel, String message)
	{
		logger.log(logLevel, message);
	}
	
	private static String printStackTrace(Throwable t)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		Throwable tt = t;
		while (tt != null)
		{
			tt.printStackTrace(pw);
			tt = tt.getCause();
			if(tt != null)
			{
				pw.print("caused by: ");
			}
		}
		return sw.toString();
	}
}
