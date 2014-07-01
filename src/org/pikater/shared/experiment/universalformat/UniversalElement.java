package org.pikater.shared.experiment.universalformat;

public class UniversalElement
{
	private UniversalOntology ontologyInfo;
	private UniversalGui guiInfo;
	
	public UniversalOntology getOntologyInfo()
	{
		return ontologyInfo;
	}
	
	public boolean isOntologyDefined()
	{
		return ontologyInfo != null;
	}
	
	public void setOntologyInfo(UniversalOntology ontologyInfo) 
	{
		if (ontologyInfo == null)
		{
			throw new NullPointerException("Argument can't be null.");
		}
		this.ontologyInfo = ontologyInfo;
	}

	public UniversalGui getGUIInfo()
	{
		return guiInfo;
	}
	
	public void setGUIInfo(UniversalGui guiInfo)
	{
		if(guiInfo == null)
		{
			throw new NullPointerException("Argument can't be null.");
		}
		this.guiInfo = guiInfo;
	}
}
