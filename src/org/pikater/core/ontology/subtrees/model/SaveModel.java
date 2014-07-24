package org.pikater.core.ontology.subtrees.model;

import jade.content.AgentAction;

public class SaveModel implements AgentAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8323713195249482013L;
	
	private Model model;
	
	public SaveModel(){
		model = null;
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
