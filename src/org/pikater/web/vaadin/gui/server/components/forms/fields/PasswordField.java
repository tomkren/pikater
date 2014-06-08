package org.pikater.web.vaadin.gui.server.components.forms.fields;

public class PasswordField extends AbstractFormField
{
	private static final long serialVersionUID = -1242674623972921275L;
	
	public PasswordField(String value, boolean required, boolean readOnly)
	{
		this("Password:", value, required, readOnly);
	}
	
	public PasswordField(String caption, String value, boolean required, boolean readOnly)
	{
		super(caption, "Enter password", value, required, readOnly);
		
		// TODO: implement proper constraint eventually
		// addValidator(new StringLengthValidator("Five to twenty characters are required.", 5, 20, false));
		
		// TODO: try text change event to hide the password
	}
	
	@Override
	public void setValue(String newValue) throws com.vaadin.data.Property.ReadOnlyException
	{
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < newValue.length(); i++)
		{
			sb.append('*');
		}
		setValueBackup(newValue);
		super.setValue(sb.toString());
	}
	
	@Override
	public String getValue()
	{
		return getValueBackup();
	}
}
