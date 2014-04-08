package org.pikater.core.options;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import org.pikater.shared.experiment.BoxType;
import org.pikater.shared.experiment.parameters.AbstractParameter;
import org.pikater.shared.experiment.parameters.EnumeratedValueParameter;
import org.pikater.shared.experiment.parameters.RangedValueParameter;
import org.pikater.shared.experiment.parameters.ValueParameter;
import org.pikater.shared.experiment.slots.AbstractSlot;
import org.pikater.shared.util.Interval;

import com.thoughtworks.xstream.XStream;

public class LogicalUnit
{
	private boolean isBox = false;
	private String displayName = "NoName";
	private String agentName = "NoAgent";
	private BoxType type = null;
	private Class ontology = null;
	private String picture = "NoPicture";
	private String description = "NoDescription";

	private ArrayList<AbstractParameter> parameters = new ArrayList<AbstractParameter>();
	private ArrayList<AbstractSlot> inputSlots = new ArrayList<AbstractSlot>();
	private ArrayList<AbstractSlot> outputSlots = new ArrayList<AbstractSlot>();

	public boolean getIsBox()
	{
		return isBox;
	}

	public void setIsBox(boolean isBox)
	{
		this.isBox = isBox;
	}

	public String getDisplayName()
	{
		return displayName;
	}

	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}

	public String getAgentName()
	{
		return agentName;
	}

	public void setAgentName(String agentName)
	{
		this.agentName = agentName;
	}

	public BoxType getType()
	{
		return type;
	}

	public void setType(BoxType type)
	{
		this.type = type;
	}

	public Class getOntology()
	{
		return ontology;
	}

	public void setOntology(Class ontology)
	{
		this.ontology = ontology;
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

	public ArrayList<AbstractParameter> getParameters()
	{
		return parameters;
	}

	public void addParameter(AbstractParameter parameter)
	{
		this.parameters.add(parameter);
	}

	public ArrayList<AbstractSlot> getInputSlots()
	{
		return inputSlots;
	}

	public void setInputSlots(ArrayList<AbstractSlot> inputSlots)
	{
		this.inputSlots = inputSlots;
	}

	public void addInputSlot(AbstractSlot inputSlot)
	{
		this.inputSlots.add(inputSlot);
	}

	public ArrayList<AbstractSlot> getOutputSlots()
	{
		return outputSlots;
	}

	public void setOutputSlots(ArrayList<AbstractSlot> outputSlots)
	{
		this.outputSlots = outputSlots;
	}

	public void addOutputSlot(AbstractSlot outputSlot)
	{
		this.outputSlots.add(outputSlot);
	}

	public void exportXML() throws FileNotFoundException
	{

		System.out.println("Exporting: " + this.getClass().getSimpleName() + ".xml");

		XStream xstream = new XStream();
		xstream.alias("LogicalUnit", this.getClass());

		xstream.alias("EnumeratedValueParameter", EnumeratedValueParameter.class);
		xstream.alias("RangedValueParameter", RangedValueParameter.class);
		xstream.alias("ValueParameter", ValueParameter.class);

		xstream.alias("Interval", Interval.class);

		// TODO:
		// xstream.alias("DataSlot", DataSlot.class);
		// xstream.alias("ParameterSlot", ParameterSlot.class);
		// xstream.alias("MethodSlot", MethodSlot.class);

		String xml = xstream.toXML(this);

		String fileName = System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "src"
				+ System.getProperty("file.separator") + "org"
				+ System.getProperty("file.separator") + "options"
				+ System.getProperty("file.separator")
				+ this.getClass().getSimpleName() + ".xml";

		PrintWriter file = new PrintWriter(fileName);
		file.println(xml);
		file.close();
	}

	public static LogicalUnit importXML(File configFile) throws FileNotFoundException
	{
		System.out.println("Importing: " + configFile.getName());

		Scanner scanner = new Scanner(configFile);
		String content = scanner.useDelimiter("\\Z").next();
		scanner.close();

		XStream xstream = new XStream();
		xstream.alias("LogicalUnit", LogicalUnit.class);

		xstream.alias("EnumeratedValueParameter", EnumeratedValueParameter.class);
		xstream.alias("RangedValueParameter", RangedValueParameter.class);
		xstream.alias("ValueParameter", ValueParameter.class);

		xstream.alias("Interval", Interval.class);

		// TODO:
		// xstream.alias("DataSlot", DataSlot.class);
		// xstream.alias("ParameterSlot", ParameterSlot.class);
		// xstream.alias("MethodSlot", MethodSlot.class);

		LogicalUnit unit = (LogicalUnit) xstream.fromXML(content);

		return unit;
	}
}
