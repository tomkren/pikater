package org.pikater.shared.experiment.universalformat;

public class UniversalConnector
{
    private String inputDataType;
	private String outputDataType;
	private UniversalElement fromElement;
	
	public String getInputDataType()
	{
		return inputDataType;
	}
	public void setInputDataType(String inputDataType)
	{
		this.inputDataType = inputDataType;
	}
	
	public String getOutputDataType()
	{
		return outputDataType;
	}
	public void setOutputDataType(String outputDataType)
	{
		this.outputDataType = outputDataType;
	}
	
	public UniversalElement getFromElement()
	{
		return fromElement;
	}
	public void setFromElement(UniversalElement fromElement)
	{
		this.fromElement = fromElement;
	}
}