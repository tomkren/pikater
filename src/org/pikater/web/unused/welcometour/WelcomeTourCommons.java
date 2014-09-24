package org.pikater.web.unused.welcometour;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.pikater.shared.RemoteServerInfo;
import org.pikater.shared.TopologyModel;
import org.pikater.shared.TopologyModel.ServerType;
import org.pikater.web.vaadin.gui.server.components.wizards.IWizardCommon;

public class WelcomeTourCommons implements IWizardCommon
{
	private final Map<String, TopologyModel> parsedModels;
	public Collection<RemoteServerInfoItem> wrappedModels;
	public boolean allModelsParsed;
	
	public WelcomeTourCommons()
	{
		this.parsedModels = new HashMap<String, TopologyModel>();
		this.wrappedModels = null;
		this.allModelsParsed = false;
	}
	
	public void addParsedModel(String topologyName, TopologyModel model)
	{
		if(!allModelsParsed)
		{
			parsedModels.put(topologyName, model);
		}
	}
	
	public void allModelsParsedCallback()
	{
		// actually close adding new topologies
		allModelsParsed = true;
		
		// wrap the parsed topologies into properties
		wrappedModels = new ArrayList<RemoteServerInfoItem>();
		for(Entry<String, TopologyModel> entry : parsedModels.entrySet())
		{
			for(RemoteServerInfo server : entry.getValue().getMasters())
			{
				wrappedModels.add(new RemoteServerInfoItem(entry.getKey(), ServerType.MASTER, server));
			}
			for(RemoteServerInfo server : entry.getValue().getSlaves())
			{
				wrappedModels.add(new RemoteServerInfoItem(entry.getKey(), ServerType.SLAVE, server));
			}
		}
	}
	
	public Map<String, TopologyModel> getParsedModels()
	{
		return parsedModels;
	}
	
	public Collection<RemoteServerInfoItem> getWrappedModels()
	{
		return wrappedModels;
	}
}