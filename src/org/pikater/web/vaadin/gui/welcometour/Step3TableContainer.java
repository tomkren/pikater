package org.pikater.web.vaadin.gui.welcometour;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.pikater.web.vaadin.gui.welcometour.RemoteServerInfoItem.Header;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

@SuppressWarnings("serial")
public class Step3TableContainer implements Container
{
	private static final Collection<Header> headers = new ArrayList<Header>()
	{
		{
			add(Header.SERVERTYPE); // read-only
			add(Header.HOSTNAME);
			add(Header.USERNAME);
			add(Header.INCLUDE);
			add(Header.STATUS); // read-only
		}
	};
	
	private final Map<Integer, RemoteServerInfoItemInner> remoteServers;
	
	public Step3TableContainer(Collection<RemoteServerInfoItem> wrappedModels)
	{
		/*
		 * IMPORTANT: note that only inner information (username, ...) will be updated when setting up the application. Not the
		 * list of remote masters and slaves - it is final, so we can safely construct items in the constructor and don't
		 * mind whether some of them are added/removed later.
		 */
		
		this.remoteServers = new HashMap<Integer, RemoteServerInfoItemInner>();
		int id = 1;
		for(RemoteServerInfoItem wrappedModel : wrappedModels)
		{
			this.remoteServers.put(id, new RemoteServerInfoItemInner(wrappedModel));
			id++;
		}
	}
	
	public RemoteServerInfoItem getInfoInstanceByID(Object id)
	{
		return getItem(id).serverInfoProperties;
	}
	
	@Override
	public Collection<?> getContainerPropertyIds()
	{
		return headers;
	}
	
	@Override
	public Class<?> getType(Object propertyId)
	{
		return RemoteServerInfoItem.getType(propertyId);
	}
	
	@Override
	public Collection<?> getItemIds()
	{
		return remoteServers.keySet();
	}
	
	@Override
	public int size()
	{
		return getItemIds().size();
	}
	
	@Override
	public boolean containsId(Object itemId)
	{
		return remoteServers.containsKey(itemId);
	}
	
	@Override
	public RemoteServerInfoItemInner getItem(Object itemId)
	{
		return remoteServers.get(itemId);
	}

	@Override
	public Property<? extends Object> getContainerProperty(Object itemId, Object propertyId)
	{
		return getItem(itemId).getItemProperty(propertyId);
	}

	// ------------------------------------------------------------------------------------------------
	// OPTIONAL:
	
	@Override
	public boolean addContainerProperty(Object propertyId, Class<?> type, Object defaultValue) throws UnsupportedOperationException
	{
		return false;
	}

	@Override
	public boolean removeContainerProperty(Object propertyId) throws UnsupportedOperationException
	{
		return false;
	}
	
	@Override
	public Object addItem() throws UnsupportedOperationException
	{
		return null;
	}
	
	@Override
	public Item addItem(Object itemId) throws UnsupportedOperationException
	{
		return null;
	}
	
	@Override
	public boolean removeItem(Object itemId) throws UnsupportedOperationException
	{
		return false;
	}

	@Override
	public boolean removeAllItems() throws UnsupportedOperationException
	{
		return false;
	}
	
	// ------------------------------------------------------------------------------------------------
	// INNER ITEM CLASS:
	
	private class RemoteServerInfoItemInner implements Item
	{
		private final RemoteServerInfoItem serverInfoProperties;
		
		public RemoteServerInfoItemInner(RemoteServerInfoItem serverInfoProperties)
		{
			this.serverInfoProperties = serverInfoProperties;
		}
		
		@Override
		public Collection<?> getItemPropertyIds()
		{
			return headers;
		}
		
		@Override
		public Property<? extends Object> getItemProperty(Object id)
		{
			return serverInfoProperties.getProperty(id);
		}
		
		// ------------------------------------------------------------------------------------------------
		// OPTIONAL:

		@SuppressWarnings("rawtypes")
		@Override
		public boolean addItemProperty(Object id, Property property) throws UnsupportedOperationException
		{
			return false;
		}

		@Override
		public boolean removeItemProperty(Object id) throws UnsupportedOperationException
		{
			return false;
		}
	}
}