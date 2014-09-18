package org.pikater.shared.logging;

import java.util.logging.Level;

public interface IPikaterLogger
{
    void log(Level logLevel, String source, String message);
	void logThrowable(String message, Throwable t);
	void log(Level logLevel, String message);
}