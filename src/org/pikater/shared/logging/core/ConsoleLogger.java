package org.pikater.shared.logging.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.pikater.shared.logging.GeneralPikaterLogger;
import org.pikater.shared.logging.IPikaterLogger;

/**
 * Primary logger for core system. This logger could be easily "extended"
 * to send logged information to the web application, where it would be
 * logged centrally. Either define your own servlets/RPC or use (for example)
 * log4j which should provide such feature.
 * 
 * @author SkyCrawl
 */
public class ConsoleLogger extends GeneralPikaterLogger {
	private static final IPikaterLogger innerLogger = createPikaterLogger(Logger.getAnonymousLogger());

	public static IPikaterLogger getLogger() {
		return innerLogger;
	}

	public static void logThrowable(String message, Throwable t) {
		getLogger().logThrowable(message, t);
	}

	public static void log(Level logLevel, String message) {
		getLogger().log(logLevel, message);
	}

	public static void log(Level logLevel, String source, String message) {
		getLogger().log(logLevel, source, message);
	}
}