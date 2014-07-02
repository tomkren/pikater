package org.pikater.core.ontology.subtrees.batchDescription.export;

import org.pikater.core.ontology.subtrees.batchDescription.IComputationElement;

public class Slot {
	
	private IComputationElement abstractDataProcessing;
    private String inputDataType;
	private String outputDataType;
	
	
	public IComputationElement getAbstractDataProcessing() {
		return abstractDataProcessing;
	}
	public void setAbstractDataProcessing(IComputationElement abstractDataProcessing) {
		this.abstractDataProcessing = abstractDataProcessing;
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
