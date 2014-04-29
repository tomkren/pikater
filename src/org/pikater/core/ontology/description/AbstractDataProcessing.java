package org.pikater.core.ontology.description;

import org.pikater.shared.database.experiment.UniversalComputationDescription;
import org.pikater.shared.database.experiment.UniversalElement;


public abstract class AbstractDataProcessing implements IComputationElement {

	private static final long serialVersionUID = 8230702618515429166L;

//  public abstract ArrayList getInputSlots();
//	public abstract ArrayList getAllOptions();

	abstract UniversalElement exportUniversalElement(
			UniversalComputationDescription uModel);
	public static AbstractDataProcessing importUniversalElement() {
		return null;
	}

}
