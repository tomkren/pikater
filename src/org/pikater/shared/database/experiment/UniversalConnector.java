package org.pikater.shared.database.experiment;

public class UniversalConnector {
	
    private UniversalElement universalDataProvider;
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

	public UniversalElement getUniversalDataProvider() {
		return universalDataProvider;
	}
	public void setUniversalDataProvider(UniversalElement universalDataProvider) {
		this.universalDataProvider = universalDataProvider;
	}

}
