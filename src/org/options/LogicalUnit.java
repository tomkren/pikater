package org.options;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import com.thoughtworks.xstream.XStream;

import org.shared.parameters.*;
import org.shared.slots.*;


public class LogicalUnit {

	private boolean isBox = false;
	private String name = "NoName";
	private String agentName = "NoAgent";
	private String type = "NoType";
	private Class ontology = null;
	private String picture = "NoPicture";
	private String description = "NoDescription";

	private ArrayList<AbstractParameter> parameters = new ArrayList<AbstractParameter>();
	private ArrayList<AbstractSlot> inputSlots = new ArrayList<AbstractSlot>();
	private ArrayList<AbstractSlot> outputSlots = new ArrayList<AbstractSlot>();

	
	public boolean getIsBox() {
		return isBox;
	}
	public void setIsBox(boolean isBox) {
		this.isBox = isBox;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public Class getOntology() {
		return ontology;
	}
	public void setOntology(Class ontology) {
		this.ontology = ontology;
	}
	
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public ArrayList<AbstractParameter> getParameters() {
		return parameters;
	}
	public void setParameters(ArrayList<AbstractParameter> parameters) {
		this.parameters = parameters;
	}
	public void addParameter(AbstractParameter parameter) {
		this.parameters.add(parameter);
	}
	
	public ArrayList<AbstractSlot> getInputSlots() {
		return inputSlots;
	}
	public void setInputSlots(ArrayList<AbstractSlot> inputSlots) {
		this.inputSlots = inputSlots;
	}
	public void addInputSlots(AbstractSlot inputSlot) {
		this.inputSlots.add(inputSlot);
	}

	public ArrayList<AbstractSlot> getOutputSlots() {
		return outputSlots;
	}
	public void setOutputSlots(ArrayList<AbstractSlot> outputSlots) {
		this.outputSlots = outputSlots;
	}
	public void addOutputSlots(AbstractSlot outputSlot) {
		this.outputSlots.add(outputSlot);
	}

	public void exportXML() throws FileNotFoundException {

		System.out.println("Exporting: " + this.getClass().getSimpleName() + ".xml");
		
		XStream xstream = new XStream();
		xstream.alias("LogicalUnit", this.getClass());
		
		xstream.alias("EnumeratedValueParameter", org.shared.parameters.EnumeratedValueParameter.class);
		xstream.alias("RangedValueParameter", org.shared.parameters.RangedValueParameter.class);
		xstream.alias("ValueParameter", org.shared.parameters.ValueParameter.class);

		xstream.alias("Interval", org.shared.interval.Interval.class);

		xstream.alias("DataSlot", org.shared.slots.DataSlot.class);
		xstream.alias("ParameterSlot", org.shared.slots.ParameterSlot.class);
		xstream.alias("MethodSlot", org.shared.slots.MethodSlot.class);

		String xml = xstream.toXML(this);

		String fileName = 
				System.getProperty("user.dir") +
				System.getProperty("file.separator") + "src" +
				System.getProperty("file.separator") + "org" +
				System.getProperty("file.separator") + "options" +
				System.getProperty("file.separator") +
				this.getClass().getSimpleName() + ".xml";
				
		PrintWriter file = new PrintWriter(fileName);
		file.println(xml);
		file.close();
	}

	public static LogicalUnit importXML(File configFile) throws FileNotFoundException {

		System.out.println("Importing: " + configFile.getName());

		Scanner scanner = new Scanner(configFile);
		String content = scanner.useDelimiter("\\Z").next();
		scanner.close();
		
		XStream xstream = new XStream();
		xstream.alias("LogicalUnit", org.options.LogicalUnit.class);
		
		xstream.alias("EnumeratedValueParameter", org.shared.parameters.EnumeratedValueParameter.class);
		xstream.alias("RangedValueParameter", org.shared.parameters.RangedValueParameter.class);
		xstream.alias("ValueParameter", org.shared.parameters.ValueParameter.class);

		xstream.alias("Interval", org.shared.interval.Interval.class);

		xstream.alias("DataSlot", org.shared.slots.DataSlot.class);
		xstream.alias("ParameterSlot", org.shared.slots.ParameterSlot.class);
		xstream.alias("MethodSlot", org.shared.slots.MethodSlot.class);

		LogicalUnit unit = (LogicalUnit)xstream.fromXML(content);

		return unit;
	}
}
