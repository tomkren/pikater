package org.pikater.shared.logging.core;

import java.util.logging.Level;

import org.pikater.core.agents.PikaterAgent;

public class AgentLogger
{
	private final String source;
	
	public AgentLogger(PikaterAgent agent)
	{
		super();
		
		this.source = agent.getLocalName();
	}
	
	public void log(Level logLevel, String message)
	{
		ConsoleLogger.log(logLevel, source, message);
	}

	public void logThrowable(String message, Throwable t)
	{
		ConsoleLogger.logThrowable(message, t);
	}
		
	/*
	 * OBSOLETE CODE
	 */
	
	// private static final String DEFAULT_LOGGER_BEAN = "logger";
	// private static final String LOGGER_BEAN_ARG = "logger";
	// private static final String VERBOSITY_ARG = "verbosity";
	// private final Verbosity verbosity;
	
	@SuppressWarnings("unused")
	private enum Severity
	{
	    Minimal,
	    Normal,
	    Critical
	}
	
	@SuppressWarnings("unused")
	private enum Verbosity
	{
	    NO_OUTPUT,
	    MINIMAL,
	    NORMAL,
	    DETAILED
	}
	
	/*
	String loggerBean = agent.containsArgument(LOGGER_BEAN_ARG) ? agent.getArgumentValue(LOGGER_BEAN_ARG) : DEFAULT_LOGGER_BEAN;
	logger = (Logger) CoreConfiguration.APPLICATION_CONTEXT.getBean(loggerBean);
	if(logger == null)
	{
		throw new RuntimeException("Failed to initialize logger bean");
	}
	
	Verbosity verbosity = Verbosity.NORMAL;
	if(agent.containsArgument(VERBOSITY_ARG))
	{
		String verbosityValue = agent.getArgumentValue(VERBOSITY_ARG);
		switch (verbosityValue)
		{
			case "0":
				verbosity = Verbosity.NO_OUTPUT;
				break;
			case "1":
				verbosity = Verbosity.MINIMAL;
				break;
			case "2":
				verbosity = Verbosity.NORMAL;
				break;
			case "3":
				verbosity = Verbosity.DETAILED;
				break;
		}
	}
	this.verbosity = verbosity;
	*/
}