package org.pikater.shared.experiment;

public class UniversalElement
{
	private UniversalOntology ontologyInfo;
	private UniversalGui guiInfo;
	
	public UniversalElement()
	{
		this.ontologyInfo = new UniversalOntology();
		this.guiInfo = new UniversalGui();
	}

	public UniversalOntology getOntologyInfo()
	{
		return ontologyInfo;
	}
	public void setOntologyInfo(UniversalOntology ontologyInfo)
	{
		this.ontologyInfo = ontologyInfo;
	}
	public UniversalGui getGuiInfo()
	{
		return guiInfo;
	}
	public void setGuiInfo(UniversalGui guiInfo)
	{
		this.guiInfo = guiInfo;
	}
	
	public boolean isOntologyDefined()
	{
		return this.ontologyInfo != null;
	}
}