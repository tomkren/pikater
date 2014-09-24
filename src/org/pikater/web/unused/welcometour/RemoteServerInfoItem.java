package org.pikater.web.unused.welcometour;

import java.util.HashMap;
import java.util.Map;

import org.pikater.shared.RemoteServerInfo;
import org.pikater.shared.RemoteServerInfo.FieldType;
import org.pikater.shared.TopologyModel.ServerType;
import org.pikater.web.vaadin.gui.server.components.RevealablePasswordField;

import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.CheckBox;

public class RemoteServerInfoItem
{
	// header definitions
	public enum Header
	{
		HOSTNAME,
		USERNAME,
		PASSWORD,
		DIRECTORY,
		TOPOLOGYNAME,
		SERVERTYPE,
		INCLUDE,
		STATUS;
		
		@Override
		public String toString()
		{
			switch (this)
			{
				case HOSTNAME:
				case USERNAME:
				case PASSWORD:
				case DIRECTORY:
				case SERVERTYPE:
				case STATUS:
				case TOPOLOGYNAME:
					return this.name();
				case INCLUDE:
					return "CONNECT";
				default:
					throw new IllegalStateException();
			}
		}
		
		public boolean supportsBatchSet()
		{
			switch (this)
			{
				case HOSTNAME:
				case USERNAME:
				case PASSWORD:
					return true;
				default:
					return false;
			}
		}
	}
	
	// values
	public static final String connectionStatus_default = "Pending";
	public static final String connectionStatus_canConnect = "Can connect";
	public static final String connectionStatus_canNotConnect = "Can NOT connect";
	public static final String connectionStatus_launched = "Launched";
	public static final String connectionStatus_error = "Error";
	
	public final RemoteServerInfo underlyingInfoInstance;
	private final Map<Header, Property<? extends Object>> data;
	
	public RemoteServerInfoItem(String filename, ServerType type, RemoteServerInfo server)
	{
		this.underlyingInfoInstance = server;
		
		this.data = new HashMap<Header, Property<? extends Object>>();
		this.data.put(Header.HOSTNAME, new RemoteServerInfoProperty(server, FieldType.HOSTNAME));
		this.data.put(Header.USERNAME, new RemoteServerInfoProperty(server, FieldType.USERNAME));
		this.data.put(Header.PASSWORD, new ObjectProperty<RevealablePasswordField>(new RevealablePasswordField(
				new RemoteServerInfoProperty(server, FieldType.PASSWORD), false)));
		this.data.put(Header.DIRECTORY, new RemoteServerInfoProperty(server, FieldType.DIRECTORY));
		this.data.put(Header.SERVERTYPE, new ObjectProperty<String>(type.name(), String.class, true)); // read-only
		this.data.put(Header.TOPOLOGYNAME, new ObjectProperty<String>(filename, String.class, true)); // read-only
		this.data.put(Header.INCLUDE, new ObjectProperty<CheckBox>(new CheckBox("", new ObjectProperty<Boolean>(true))));
		this.data.put(Header.STATUS, new ObjectProperty<String>(connectionStatus_default, String.class));
	}
	
	// -----------------------------------------------------------------
	// GENERAL VAADIN.UTIL.DATA.ITEM INTERFACE
	
	public static Class<?> getType(Object propertyId)
	{
		switch ((Header) propertyId)
		{
			case PASSWORD:
				return AbstractTextField.class;
			case INCLUDE:
				return CheckBox.class;
			default:
				return String.class;	
		}
	}
	
	public Property<? extends Object> getProperty(Object propertyId)
	{
		switch ((Header) propertyId)
		{
			case PASSWORD:
				return getPasswordField().getComponent();
			default:
				return data.get(propertyId);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Object> void setValueForProperty(Object propertyId, T newValue)
	{
		Property<T> prop = null;
		switch ((Header) propertyId)
		{
			case PASSWORD:
				prop = (Property<T>) getPasswordField().getProperty();
				break;
			case INCLUDE:
				prop = (Property<T>) getIncludeCheckBox().getPropertyDataSource();
				break;
			default:
				prop = (Property<T>) data.get(propertyId);
				break;
		}
		prop.setValue(newValue);
	}
	
	// -----------------------------------------------------------------
	// PUBLIC INTERFACE / CONVENIENCE METHODS
	
	public String getTopologyName()
	{
		return (String) getProperty(Header.TOPOLOGYNAME).getValue();
	}
	
	public void setDisplayPasswordsInPlainText(boolean enabled)
	{
		getPasswordField().setPlainText(enabled);
	}
	
	public boolean isIncluded()
	{
		return getIncludeCheckBox().getValue();
	}
	
	public boolean isConnected()
	{
		String connectionStatus = (String) getProperty(Header.STATUS).getValue(); 
		return connectionStatus == connectionStatus_canConnect;
	}
	
	public boolean isLaunched()
	{
		String connectionStatus = (String) getProperty(Header.STATUS).getValue(); 
		return connectionStatus == connectionStatus_launched;
	}
	
	public ServerType getServerType()
	{
		return ServerType.valueOf((String) getProperty(Header.SERVERTYPE).getValue());
	}
	
	public boolean isHostnameUsernameOrPasswordMissing()
	{
		return underlyingInfoInstance.isHostnameUsernameOrPasswordMissing();
	}
	
	// -----------------------------------------------------------------
	// PRIVATE INTERFACE
	
	private RevealablePasswordField getPasswordField()
	{
		return (RevealablePasswordField) data.get(Header.PASSWORD).getValue();
	}
	
	private CheckBox getIncludeCheckBox()
	{
		return (CheckBox) data.get(Header.INCLUDE).getValue();
	}
}
