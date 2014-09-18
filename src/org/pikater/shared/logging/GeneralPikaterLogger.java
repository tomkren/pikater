package org.pikater.shared.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class GeneralPikaterLogger
{
	protected static IPikaterLogger createPikaterLogger(final Logger logger)
	{
		return new IPikaterLogger()
		{
			@Override
			public void logThrowable(String message, Throwable t)
			{
				logger.log(Level.SEVERE, "exception occured: " + message + "\n" + throwableToStackTrace(t));
			}

			@Override
			public void log(Level logLevel, String message)
			{
				logger.log(logLevel, message);
			}

			@Override
			public void log(Level logLevel, String source, String message)
			{
				logger.logp(logLevel, source, null, message);
		    }
		};
	}
	
	private static String throwableToStackTrace(Throwable t)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		Throwable tt = t;
		while (tt != null)
		{
			tt.printStackTrace(pw); // not an error but a feature
			tt = tt.getCause();
			if(tt != null)
			{
				pw.print("caused by: ");
			}
		}
		return sw.toString();
	}
}