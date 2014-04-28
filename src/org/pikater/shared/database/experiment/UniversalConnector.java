package org.pikater.shared.database.experiment;

public class UniversalConnector {
	
    private UniversalElementWrapper universalDataProvider;
    private String inputDataType;
	private String outputDataType;

    public String getInputDataType() {
		return inputDataType;
	}
	public void setInputDataType(String inputDataType) {
		this.inputDataType = inputDataType;
	}

	public String getOutputDataType() {
		return outputDataType;
	}
	public void setOutputDataType(String dataType) {
		this.outputDataType = dataType;
	}

	public UniversalElementWrapper getUniversalDataProvider() {
		return universalDataProvider;
	}
	public void setUniversalDataProvider(UniversalElementWrapper universalDataProvider) {
		this.universalDataProvider = universalDataProvider;
	}

}
