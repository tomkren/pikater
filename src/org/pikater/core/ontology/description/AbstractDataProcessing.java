package org.pikater.core.ontology.description;

import org.pikater.shared.database.experiment.UniversalElementWrapper;

import jade.util.leap.ArrayList;

public abstract class AbstractDataProcessing implements IComputationElement {

//  public abstract ArrayList getInputSlots();
//	public abstract ArrayList getAllOptions();

	abstract UniversalElementWrapper exportUniversalElement();
	public static AbstractDataProcessing importUniversalElement() {
		return null;
	}

}
