package org.pikater.core.options;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.options.StepanuvOption;
import org.pikater.core.dataStructures.options.types.AbstractOption;
import org.pikater.core.dataStructures.options.types.OptionInterval;
import org.pikater.core.dataStructures.options.types.OptionList;
import org.pikater.core.dataStructures.options.types.OptionValue;
import org.pikater.shared.experiment.slots.AbstractSlot;

import com.thoughtworks.xstream.XStream;

public class LogicalUnitDescription
{
	// TODO: use AppHelper instead?
	public static String filePath = System.getProperty("user.dir")
			+ System.getProperty("file.separator") + "src"
			+ System.getProperty("file.separator") + "org"
			+ System.getProperty("file.separator") + "pikater"
			+ System.getProperty("file.separator") + "core"
			+ System.getProperty("file.separator") + "options"
			+ System.getProperty("file.separator");
	
	protected Class ontology = null;
	
	private ArrayList<OptionDefault> options = new ArrayList<OptionDefault>();
	private ArrayList<AbstractSlot> inputSlots = new ArrayList<AbstractSlot>();
	private ArrayList<AbstractSlot> outputSlots = new ArrayList<AbstractSlot>();

	public boolean getIsBox()
	{
		return this instanceof LogicalBoxDescription;
	}
	
	public Class getOntology()
	{
		return ontology;
	}

	public void setOntology(Class ontology)
	{
		this.ontology = ontology;
	}

	public ArrayList<OptionDefault> getParameters()
	{
		return options;
	}

	public void addParameter(OptionDefault parameter)
	{
		this.options.add(parameter);
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
		// TODO: initialize XStream variable just once, with automatically processed aliases?
		// TODO: see OptionXMLSerializer

		System.out.println("Exporting: " + this.getClass().getSimpleName() + ".xml");

		XStream xstream = new XStream();
		if (this instanceof LogicalBoxDescription) {
			xstream.alias("Box", this.getClass());
		} else {
			xstream.alias("LogicalUnit", this.getClass());
		}

		xstream.alias("AbstractOption", AbstractOption.class);
		xstream.alias("OptionInterval", OptionInterval.class);
		xstream.alias("OptionList", OptionList.class);
		xstream.alias("OptionValue", OptionValue.class);
		
		xstream.alias("OptionDefault", OptionDefault.class);
		xstream.alias("Option", StepanuvOption.class);

		xstream.aliasSystemAttribute("type", "class");
		
		// TODO:
		// xstream.alias("DataSlot", DataSlot.class);
		// xstream.alias("ParameterSlot", ParameterSlot.class);
		// xstream.alias("MethodSlot", MethodSlot.class);

		String xml = xstream.toXML(this);

		String fileName = filePath
				+ this.getClass().getSimpleName() + ".xml";

		PrintWriter file = new PrintWriter(fileName);
		file.println(xml);
		file.close();
	}

	public static LogicalUnitDescription importXML(File configFile) throws FileNotFoundException
	{
		// TODO: initialize XStream variable just once, with automatically processed aliases?
		// TODO: see OptionXMLSerializer
		
		System.out.println("Importing: " + configFile.getName());

		Scanner scanner = new Scanner(configFile);
		String content = scanner.useDelimiter("\\Z").next();
		scanner.close();

		XStream xstream = new XStream();
		xstream.alias("LogicalUnit", LogicalUnitDescription.class);
		xstream.alias("Box", LogicalBoxDescription.class);

		xstream.alias("AbstractOption", AbstractOption.class);
		xstream.alias("OptionInterval", OptionInterval.class);
		xstream.alias("OptionList", OptionList.class);
		xstream.alias("OptionValue", OptionValue.class);
		
		xstream.alias("OptionDefault", OptionDefault.class);
		xstream.alias("Option", StepanuvOption.class);

		xstream.aliasSystemAttribute("type", "class");
		
		// TODO:
		// xstream.alias("DataSlot", DataSlot.class);
		// xstream.alias("ParameterSlot", ParameterSlot.class);
		// xstream.alias("MethodSlot", MethodSlot.class);

		LogicalUnitDescription unit = (LogicalUnitDescription) xstream.fromXML(content);

		return unit;
	}
}
