package org.pikater.core.ontology.subtrees.openml;

import jade.content.Concept;
import jade.util.leap.ArrayList;
import jade.util.leap.List;

public class Datasets  implements Concept {

	private static final long serialVersionUID = -138002703068473729L;
	
	private List datasets;
	
	public Datasets() {
		this.datasets = new ArrayList();
	}

	public List getDatasets() {
		return datasets;
	}

	public void setDatasets(List datasets) {
		this.datasets = datasets;
	}
}
