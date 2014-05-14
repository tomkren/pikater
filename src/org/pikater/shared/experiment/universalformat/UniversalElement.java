package org.pikater.shared.experiment.universalformat;

public class UniversalElement
{
	private UniversalComputationDescription uModel;
	private UniversalOntology element;
	private UniversalGui gui;

	@SuppressWarnings("all")
	private UniversalElement()
	{
	}

	public UniversalElement(UniversalComputationDescription description)
	{
		this.uModel = description;
	}

	public UniversalOntology getElement()
	{
		return element;
	}
	
	public void setElement(UniversalOntology element) 
	{
		if (element == null)
		{
			throw new NullPointerException("Argument can not be null");
		}
		this.element = element;
		this.uModel.addElement(this); // TODO: bug if called multiple times?
	}

	public UniversalGui getGui()
	{
		return gui;
	}
	
	public void setGui(UniversalGui gui)
	{
		if (this.gui != null && gui == null)
		{
			throw new NullPointerException("UniversalGui can not be changed to null");
		}
		this.gui = gui;
	}
}
