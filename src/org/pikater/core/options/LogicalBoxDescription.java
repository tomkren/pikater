package org.pikater.core.options;

public class LogicalBoxDescription extends LogicalUnitDescription {

	private String name = null;
	private Class<? extends Object> agentClass = null;
	private String picture = null;
	private String description = null;

	public LogicalBoxDescription(String name, Class<? extends Object> ontology,
			String description) {

		this.name = name;
		super.ontology = ontology;
		this.description = description;
	}

	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}

	public Class<? extends Object> getAgentClass()
	{
		return agentClass;
	}
	public void setAgentName(Class<? extends Object> agentClass)
	{
		this.agentClass = agentClass;
	}

	public String getPicture()
	{
		return picture;
	}
	public void setPicture(String picture)
	{
		this.picture = picture;
	}

	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}

}
