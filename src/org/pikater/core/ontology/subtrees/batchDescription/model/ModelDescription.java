package org.pikater.core.ontology.subtrees.batchDescription.model;


public class ModelDescription implements IModelDescription {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6265870438825648284L;

	private int modelID;

	public ModelDescription() {}
	
	public ModelDescription(int modelID) {
		this.modelID = modelID;
	}
	public int getModelID() {
		return modelID;
	}

	public void setModelID(int modelID) {
		this.modelID = modelID;
	}
	
}
