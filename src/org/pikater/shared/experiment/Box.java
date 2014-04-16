package org.pikater.shared.experiment;

import java.util.ArrayList;

import org.pikater.core.ontology.options.StepanuvOption;
import org.pikater.shared.experiment.slots.AbstractSlot;

/**
 * An intermediate box format used in the universal computation model.
 */
public class Box {

	private Class<? extends Object> ontology = null;
	private Class<? extends Object> agentClass = null;
	
	private String name = null;
	private BoxType type = null;
	private String picture = null;
	private String description = null;
	
	private ArrayList<StepanuvOption> options = new ArrayList<StepanuvOption>();
	private ArrayList<AbstractSlot> inputSlots = new ArrayList<AbstractSlot>();
	private ArrayList<AbstractSlot> outputSlots = new ArrayList<AbstractSlot>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Class<? extends Object> getAgentClass() {
		return agentClass;
	}
	public void setAgentClass(Class<? extends Object> agentClass) {
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

	public Class<? extends Object> getOntology() {
		return ontology;
	}
	public void setOntology(Class<? extends Object> ontology) {
		this.ontology = ontology;
	}

	public BoxType getType() {
		return type;
	}
	public void setType(BoxType type) {
		this.type = type;
	}
	
	public ArrayList<StepanuvOption> getParameters() {
		return options;
	}
	public void setParameters(ArrayList<StepanuvOption> parameters) {
		this.options = parameters;
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
