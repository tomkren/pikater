package org.pikater.core.ontology.subtrees.model;

import jade.content.AgentAction;

public class SaveModel implements AgentAction {
	private Model model=null;
	public SaveModel(){
		
	}
	
	public SaveModel(Model model){
		this.model=model;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}
	
	
}
