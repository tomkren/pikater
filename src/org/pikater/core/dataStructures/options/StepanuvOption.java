package org.pikater.core.dataStructures.options;

import org.pikater.core.dataStructures.options.types.AbstractOption;
import org.pikater.core.ontology.messages.Option;

import jade.content.Concept;


public class StepanuvOption {

	private String name;
	private String description;

	private AbstractOption option;

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public AbstractOption getOption() {
		return option;
	}
	public void setOption(AbstractOption option) {
		this.option = option;
	}
	
	public Option toOption() {
		return null;
	}
}
