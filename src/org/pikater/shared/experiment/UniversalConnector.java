package org.pikater.shared.experiment;

public class UniversalConnector
{
    private String inputDataIdentifier;
	private String outputDataIdentifier;
	private UniversalElement fromElement;
	
	public String getInputDataIdentifier()
	{
		return inputDataIdentifier;
	}
	public void setInputDataIdentifier(String inputDataIdentifier)
	{
		this.inputDataIdentifier = inputDataIdentifier;
	}
	public String getOutputDataIdentifier()
	{
		return outputDataIdentifier;
	}
	public void setOutputDataIdentifier(String outputDataIdentifier)
	{
		this.outputDataIdentifier = outputDataIdentifier;
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
		return (inputDataIdentifier != null) && (outputDataIdentifier != null); 
	}
	
	public void validate()
	{
		if(fromElement == null)
		{
			throw new IllegalStateException("From element is not defined.");
		}
		else if(((inputDataIdentifier != null) && (outputDataIdentifier == null)) ||
				((inputDataIdentifier == null) && (outputDataIdentifier != null)))
		{
			throw new IllegalStateException("One of the data types is not defined.");
		}
	}
}