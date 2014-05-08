package org.pikater.shared.experiment.webformat.slots;

public class EditorSlot extends AbstractSlot
{
	private static final long serialVersionUID = -2131306420223085727L;
	
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
