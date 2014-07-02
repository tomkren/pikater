package org.pikater.core.ontology.subtrees.batchDescription.export;

import org.pikater.core.ontology.subtrees.batchDescription.DataProcessing;

public class Slot {
	
	private DataProcessing dataProcessing;
    private String inputDataType;
	private String outputDataType;
	
	
	public DataProcessing getDataProcessing() {
		return dataProcessing;
	}
	public void setDataProcessing(DataProcessing dataProcessing) {
		this.dataProcessing = dataProcessing;
	}
	public String getInputDataType() {
		return inputDataType;
	}
	public void setInputDataType(String inputDataType) {
		this.inputDataType = inputDataType;
	}
	public String getOutputDataType() {
		return outputDataType;
	}
	public void setOutputDataType(String outputDataType) {
		this.outputDataType = outputDataType;
	}
	
}
