package org.pikater.shared.experiment.webformat.slots;

public class EditorSlot extends AbstractSlot
{
	private String data;
	
	public EditorSlot(String type, String description, String data)
	{
		super(type, description);
		this.data = data;
	}

	public String getData()
	{
		return data;
	}
	
	public void setData(String data)
	{
		this.data = data;
	}
}
