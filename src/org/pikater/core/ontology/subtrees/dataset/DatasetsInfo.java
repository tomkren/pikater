package org.pikater.core.ontology.subtrees.dataset;

import jade.content.Concept;

import java.util.ArrayList;
import java.util.List;

public class DatasetsInfo implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = -319009385873427155L;
	
	private List<DatasetInfo> datasets = new ArrayList<DatasetInfo>();

	
	public List<DatasetInfo> getDatasets() {
		return datasets;
	}
	public void setDatasets(List<DatasetInfo> datasets) {
		this.datasets = datasets;
	}
	public void addDatasets(DatasetInfo dataset) {
		this.datasets.add(dataset);
	}	
}
