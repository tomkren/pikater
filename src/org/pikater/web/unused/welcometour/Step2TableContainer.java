package org.pikater.web.unused.welcometour;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.pikater.web.unused.welcometour.RemoteServerInfoItem.Header;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

@SuppressWarnings("serial")
public class Step2TableContainer implements Container
{
	private static final Collection<Header> headers = new ArrayList<Header>()
	{
		{
			add(Header.SERVERTYPE); // read-only
			add(Header.HOSTNAME);
			add(Header.USERNAME);
			add(Header.PASSWORD);
			add(Header.DIRECTORY);
		}
	};
		
	private String filterOnlyMachinesFromThisTopology;
	private boolean filterFilled;
	private final Map<Integer, RemoteServerInfoItemInner> data;
	
	public Step2TableContainer(Collection<RemoteServerInfoItem> allServers)
	{
		this.filterOnlyMachinesFromThisTopology = null;
		this.filterFilled = Step2UI.filterFilledDefaultValue;
		
		int id = 1;
		this.data = new HashMap<Integer, RemoteServerInfoItemInner>();
		for(RemoteServerInfoItem serverProperties : allServers)
		{
			data.put(id, new RemoteServerInfoItemInner(serverProperties));
			id++;
		}
	}
	
	public void setFilterOnlyMachinesFromModel(String topologyName)
	{
		filterOnlyMachinesFromThisTopology = topologyName;
	}
	
	public void setFilterFilled(boolean enabled)
	{
		filterFilled = enabled;
	}
	
	public void setDisplayPasswordsInPlainText(boolean enabled)
	{
		for(Integer id : data.keySet())
		{
			getItem(id).serverInfoProperties.setDisplayPasswordsInPlainText(enabled);
		}
	}
	
	public void batchSetValues(Set<Integer> ids, Header header, String newValue)
	{
		if(header.supportsBatchSet())
		{
			for(Integer itemID : ids)
			{
				getItem(itemID).serverInfoProperties.setValueForProperty(header, newValue);
			}
		}
		else
		{
			throw new UnsupportedOperationException();
		}
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
		Collection<Integer> result = new ArrayList<Integer>();
		if(filterOnlyMachinesFromThisTopology != null)
		{
			for(Entry<Integer, RemoteServerInfoItemInner> entry : data.entrySet())
			{
				if(entry.getValue().serverInfoProperties.getProperty(Header.TOPOLOGYNAME).getValue().equals(filterOnlyMachinesFromThisTopology))
				{
					result.add(entry.getKey());
				}
			}
		}
		else
		{
			result.addAll(data.keySet());
		}
		
		if(filterFilled)
		{
			Collection<Integer> toRemove = new ArrayList<Integer>();
			for(Integer idToCheck : result)
			{
				if(!data.get(idToCheck).serverInfoProperties.isHostnameUsernameOrPasswordMissing()) // nothing is missing
				{
					toRemove.add(idToCheck);
				}
			}
			result.removeAll(toRemove);
		}
		
		return result;
	}
	
	@Override
	public int size()
	{
		return getItemIds().size();
	}
	
	@Override
	public boolean containsId(Object itemId)
	{
		return data.containsKey(itemId);
	}
	
	@Override
	public RemoteServerInfoItemInner getItem(Object itemId)
	{
		return data.get(itemId);
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
			return Step2TableContainer.headers;
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