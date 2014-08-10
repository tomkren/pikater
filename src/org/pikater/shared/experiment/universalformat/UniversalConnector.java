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
	
	public boolean isFullySpecified()
	{
		return (inputDataType != null) && (outputDataType != null); 
	}
	
	public void validate()
	{
		if(fromElement == null)
		{
			throw new IllegalStateException("From element is not defined.");
		}
		else if(((inputDataType != null) && (outputDataType == null)) ||
				((inputDataType == null) && (outputDataType != null)))
		{
			throw new IllegalStateException("One of the data types is not defined.");
		}
	}
}