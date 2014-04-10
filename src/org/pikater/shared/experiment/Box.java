package org.pikater.shared.experiment;

import java.util.ArrayList;

import org.pikater.shared.experiment.parameters.AbstractParameter;
import org.pikater.shared.experiment.slots.AbstractSlot;

public class Box {

	private String name = "";
	private Class agentClass = null;
	private String picture = "";
	private String description = "";
	
	private Class ontology = null;
	private BoxType type;
	
	private ArrayList<AbstractParameter> parameters = new ArrayList<AbstractParameter>();
	private ArrayList<AbstractSlot> inputSlots = new ArrayList<AbstractSlot>();
	private ArrayList<AbstractSlot> outputSlots = new ArrayList<AbstractSlot>();


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Class getAgentClass() {
		return agentClass;
	}
	public void setAgentClass(Class agentClass) {
		this.agentClass = agentClass;
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

	public Class getOntology() {
		return ontology;
	}
	public void setOntology(Class ontology) {
		this.ontology = ontology;
	}

	public BoxType getType() {
		return type;
	}
	public void setType(BoxType type) {
		this.type = type;
	}
	
	public ArrayList<AbstractParameter> getParameters() {
		return parameters;
	}
	public void setParameters(ArrayList<AbstractParameter> parameters) {
		this.parameters = parameters;
	}

	public ArrayList<AbstractSlot> getInputSlots() {
		return inputSlots;
	}
	public void setInputSlots(ArrayList<AbstractSlot> inputSlots) {
		this.inputSlots = inputSlots;
	}

	public ArrayList<AbstractSlot> getOutputSlots() {
		return outputSlots;
	}
	public void setOutputSlots(ArrayList<AbstractSlot> outputSlots) {
		this.outputSlots = outputSlots;
	}

}
