package org.pikater.shared.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.pikater.shared.logging.database.PikaterDBLogger;

/**
 * This class is the first towards unified logging in the whole application.
 * See {@link PikaterDBLogger} for an example of how to create an application
 * wide static logger.
 * 
 * @author SkyCrawl
 */
public abstract class GeneralPikaterLogger {
	protected static IPikaterLogger createPikaterLogger(final Logger logger) {
		return new IPikaterLogger() {
			private final Logger LOGGER = logger;

			@Override
			public void logThrowable(String message, Throwable t) {
				LOGGER.log(Level.SEVERE, "exception occured: " + message + "\n" + throwableToStackTrace(t));
			}

			@Override
			public void log(Level logLevel, String message) {
				LOGGER.log(logLevel, message);
			}

			@Override
			public void log(Level logLevel, String source, String message) {
				LOGGER.logp(logLevel, source, null, message);
			}
		};
	}

	private static String throwableToStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		Throwable tt = t;
		while (tt != null) {
			tt.printStackTrace(pw); // not an error but a feature
			tt = tt.getCause();
			if (tt != null) {
				pw.print("caused by: ");
			}
		}
		return sw.toString();
	}
}