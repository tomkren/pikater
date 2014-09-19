package org.pikater.core.ontology.subtrees.management;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;

import jade.content.Concept;


public class Agent implements Concept, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6257129995443147585L;
	
	private String name;
	private String type;
	private Integer model; // null = new model
	private List<NewOption> options;
	private byte[] object;
	
	public Agent(){
		this.options=new ArrayList<NewOption>();
	}

	// Methods required to use this class to represent the OPTIONS role
	public List<NewOption> getOptions() {
		return options;
	}
	public void setOptions(List<NewOption> options) {
		this.options = options;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public byte[] getObject() {
		return object;
	}
	public void setObject(byte[] object) {
		this.object = object;
	}
	
	public boolean containsOptionByName(String optionName) {
		NewOptions option = new NewOptions(getOptions());
		return option.containsOptionWithName(optionName);		
	}
	public NewOption getOptionByName(String name) {
		NewOptions option = new NewOptions(getOptions());
		return option.fetchOptionByName(name);
	}

	public Object clone() throws CloneNotSupportedException {
		Agent agent = (Agent) super.clone();
		agent.setName(name);
		agent.setObject(object);
		agent.setOptions(options);
		agent.setType(type);
		agent.setModel(model);
		return agent;
	}

	public Integer getModel() {
		return model;
	}

	public void setModel(Integer model) {
		this.model = model;
	}

}
