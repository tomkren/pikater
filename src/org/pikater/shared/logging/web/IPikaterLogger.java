package org.pikater.shared.logging.web;

import java.util.logging.Level;

public interface IPikaterLogger
{
	void logThrowable(String problemDescription, Throwable t);
	void log(Level logLevel, String message);
}