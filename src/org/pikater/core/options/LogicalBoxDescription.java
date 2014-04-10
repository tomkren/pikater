package org.pikater.core.options;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.pikater.shared.experiment.parameters.EnumeratedValueParameter;
import org.pikater.shared.experiment.parameters.RangedValueParameter;
import org.pikater.shared.experiment.parameters.ValueParameter;
import org.pikater.shared.util.Interval;

import com.thoughtworks.xstream.XStream;

import jade.core.Agent;


public class LogicalBoxDescription extends LogicalUnitDescription {

	private static String NONAME = "NoName";
	public static Class NOAGENTCLASS = null;
	public static String NOPICTURE = "NoPicture";
	private static String NODESCRIPTION = "NoDescription";

	private String name = NONAME;
	private Class agentClass = NOAGENTCLASS;
	private String picture = NOPICTURE;
	private String description = NODESCRIPTION;


	public LogicalBoxDescription(String name, Class ontology,
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

	public Class getagentClass()
	{
		return agentClass;
	}
	public void setAgentName(Class agentClass)
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
