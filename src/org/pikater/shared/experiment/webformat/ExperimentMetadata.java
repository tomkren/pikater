package org.pikater.shared.experiment.webformat;

import java.io.Serializable;

public class ExperimentMetadata implements Serializable
{
	private static final long serialVersionUID = -4774032604758675774L;
	
	public String name;
	
	/**
	 * Default constructor keeps GWT and Vaadin happy.
	 */
	protected ExperimentMetadata()
	{
	}

	public ExperimentMetadata(String name)
	{
		this.name = name;
	}
}
