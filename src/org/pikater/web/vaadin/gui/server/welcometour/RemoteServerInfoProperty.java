package org.pikater.web.vaadin.gui.server.welcometour;

import org.pikater.shared.RemoteServerInfo;
import org.pikater.shared.RemoteServerInfo.FieldType;

import com.vaadin.data.Property;

public class RemoteServerInfoProperty implements Property<String>
{
	private static final long serialVersionUID = -5592575393926531075L;
	
	private final RemoteServerInfo server;
	private final FieldType field;

	public RemoteServerInfoProperty(RemoteServerInfo server, FieldType field)
	{
		this.server = server;
		this.field = field;
	}

	@Override
	public String getValue()
	{
		return server.getField(field);
	}

	@Override
	public void setValue(String newValue) throws com.vaadin.data.Property.ReadOnlyException
	{
		server.setField(field, newValue);
	}

	@Override
	public Class<? extends String> getType()
	{
		return String.class;
	}

	@Override
	public boolean isReadOnly()
	{
		return false;
	}

	// ------------------------------------------------------------------------------------------------
	// OPTIONAL:

	@Override
	public void setReadOnly(boolean newStatus)
	{
	}
}
