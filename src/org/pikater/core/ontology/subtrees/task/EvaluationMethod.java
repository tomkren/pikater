package org.pikater.core.ontology.subtrees.task;


import jade.content.Concept;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.NewOption;

public class EvaluationMethod implements Concept {

	private static final long serialVersionUID = -9024769565945696142L;
	private String type;
	private List<NewOption> options;


	public void setOptions(List<NewOption> options) {
		this.options = options;
	}
	public List<NewOption> getOptions() {
		return options;
	}
	
    public void addOption(NewOption option) {
    	if (this.options == null) {
    		this.options = new ArrayList<NewOption>();
    	}
        this.options.add(option);
    }
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
