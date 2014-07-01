package org.pikater.core.ontology.subtrees.batchDescription;

import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;
import org.pikater.shared.experiment.universalformat.UniversalElement;


public abstract class AbstractDataProcessing implements IComputationElement {

	private static final long serialVersionUID = 8230702618515429166L;

	private int id = -1;
	
	abstract UniversalElement exportUniversalElement(
			UniversalComputationDescription uModel);
	public static AbstractDataProcessing importUniversalElement() {
		return null;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

}
