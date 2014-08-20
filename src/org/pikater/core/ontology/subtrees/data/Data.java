package org.pikater.core.ontology.subtrees.data;

import org.pikater.core.ontology.subtrees.metadata.Metadata;

import jade.content.Concept;

public class Data implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = 823834120042933635L;
	
	private String internalFileName;
	private String externalFileName;
	private String dataType;
	
	private Metadata metadata;

	public Data() {}
	
	public Data(String externalFileName, String internalFileName, 
			String dataType) {
		this.internalFileName = internalFileName;
		this.externalFileName = externalFileName;
		this.dataType = dataType;
	}

	public String getInternalFileName() {
		return internalFileName;
	}
	public void setInternalFileName(String internalFileName) {
		this.internalFileName = internalFileName;
	}

	public String getExternalFileName() {
		return externalFileName;
	}
	public void setExternalFileName(String externalFileName) {
		this.externalFileName = externalFileName;
	}

	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public Metadata getMetadata() {
		return metadata;
	}
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	
}
