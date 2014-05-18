package org.pikater.core.ontology.description;

import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;
import org.pikater.shared.experiment.universalformat.UniversalElement;


public abstract class AbstractDataProcessing implements IComputationElement {

	private static final long serialVersionUID = 8230702618515429166L;

	abstract UniversalElement exportUniversalElement(
			UniversalComputationDescription uModel);
	public static AbstractDataProcessing importUniversalElement() {
		return null;
	}

}
