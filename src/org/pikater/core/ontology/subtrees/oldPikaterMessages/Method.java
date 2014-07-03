package org.pikater.core.ontology.subtrees.oldPikaterMessages;


import jade.content.Concept;

import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.NewOption;

public class Method implements Concept {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9024769565945696142L;
	private String name;
	private List<NewOption> options;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOptions(List<NewOption> options) {
		this.options = options;
	}

	public List<NewOption> getOptions() {
		return options;
	}
}
