package org.pikater.core.ontology.subtrees.batchDescription;

import org.pikater.shared.experiment.universalformat.UniversalOntology;


public abstract class AbstractDataProcessing implements IComputationElement {

	private static final long serialVersionUID = 8230702618515429166L;

	private int id = -1;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int generateIDs(int lastUsedId) {
	
		if (this.getId() == -1) {
			this.setId(lastUsedId++);
		}
		return lastUsedId;
	}

	public UniversalOntology exportUniversalElement() {
		
		UniversalOntology ontologyInfo = new UniversalOntology();
		ontologyInfo.setId(getId());
		ontologyInfo.setType(getClass());
		ontologyInfo.setOptions(getUniversalOptions());
		ontologyInfo.setErrors(getUniversalErrors());
		//ontologyInfo.addInputSlots(getInputSlots(uModel));
		
		return ontologyInfo;
	}
	
	public static AbstractDataProcessing importUniversalElement() {
		return null;
	}

	
}
