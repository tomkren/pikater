package org.pikater.core.ontology.subtrees.agentInfo;

import org.pikater.core.ontology.subtrees.option.Option;

import jade.content.Concept;

import java.util.List;
import java.util.ArrayList;

public class AgentInfo implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5587052921037796722L;

	private String agentClass;
	private String ontologyClass;
	
	private String name;
	private String picture;
	private String description;

	private List<Option> options;
	
	private List<Slot> inputSlots;
	private List<Slot> outputSlots;

	public String getAgentClass() {
		return agentClass;
	}
	public void setAgentClass(String agentClass) {
		this.agentClass = agentClass;
	}

	public String getOntologyClass() {
		return ontologyClass;
	}
	public void setOntologyClass(String ontologyClass) {
		this.ontologyClass = ontologyClass;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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

	public List<Option> getOptions() {
		return options;
	}
	public void setOptions(List<Option> options) {
		this.options = options;
	}
	public void addOption(Option option) {
		if (this.options == null) {
			this.options = new ArrayList<Option>();
		}
		this.options.add(option);
	}

	public List<Slot> getInputSlots() {
		return inputSlots;
	}
	public void setInputSlots(List<Slot> inputSlots) {
		this.inputSlots = inputSlots;
	}
	public void addInputSlot(Slot inputSlot) {
		if (this.inputSlots == null) {
			this.inputSlots = new ArrayList<Slot>();
		}
		this.inputSlots.add(inputSlot);
	}

	public List<Slot> getOutputSlots() {
		return outputSlots;
	}
	public void setOutputSlots(List<Slot> outputSlots) {
		this.outputSlots = outputSlots;
	}
	public void addOutputSlot(Slot outputSlot) {
		if (this.outputSlots == null) {
			this.outputSlots = new ArrayList<Slot>();
		}
		this.outputSlots.add(outputSlot);
	}
	
}
