package org.pikater.core.ontology.subtrees.management;

import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.NewOptionList;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;

import jade.content.Concept;


public class Agent implements Concept, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6257129995443147585L;
	
	private String name;
	private String type;
	private List<NewOption> options;
	private byte[] object;

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
		NewOptionList option = new NewOptionList(getOptions());
		return option.containsOptionWithName(optionName);		
	}
	public NewOption getOptionByName(String name) {
		NewOptionList option = new NewOptionList(getOptions());
		return option.getOptionByName(name);
	}

	public Object clone() {

		Agent agent = new Agent();
		agent.setName(name);
		agent.setObject(object);
		agent.setOptions(options);
		agent.setType(type);

		return agent;
	}

}