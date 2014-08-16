package org.pikater.web.visualisation.exception;

public class MetadataNotPresentException extends VisualisationException {
	private static final long serialVersionUID = 2836255324545952145L;
	private String datasetName=null;
	
	public MetadataNotPresentException(String datasetName){
		this.datasetName=datasetName;
	}
	
	public MetadataNotPresentException(){}
	
	@Override
	public String getMessage() {
		return (datasetName==null)?
				   super.getMessage() : 
			       ("Dataset name : "+this.datasetName+"\n"+super.getMessage());
	}
	
}
