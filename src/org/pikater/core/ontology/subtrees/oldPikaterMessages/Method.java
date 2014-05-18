package org.pikater.core.ontology.subtrees.oldPikaterMessages;

import org.pikater.core.ontology.subtrees.option.Option;

import jade.content.Concept;
import java.util.List;

public class Method implements Concept {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9024769565945696142L;
	private String name;
	private List<Option> options;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	public List<Option> getOptions() {
		return options;
	}
}
