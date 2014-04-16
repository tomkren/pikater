package org.pikater.core.ontology.options;

import org.pikater.core.ontology.options.types.AbstractOption;

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
}
