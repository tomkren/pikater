package org.pikater.core.agents.system.computationDescriptionParser.items;

import org.pikater.core.agents.system.computationDescriptionParser.ItemOfGraph;

public class DatasetWrapper extends ItemOfGraph {
	
	private String fileURI;

	public DatasetWrapper(String fileURI) {
		this.fileURI = fileURI;
	}

	public String getFileURI() {
		return fileURI;
	}
	public void setFileURI(String fileURI) {
		this.fileURI = fileURI;
	}

}
