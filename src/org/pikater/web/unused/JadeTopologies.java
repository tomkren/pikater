package org.pikater.web.unused;

import java.util.ArrayList;
import java.util.Collection;

import org.pikater.shared.TopologyModel;

public class JadeTopologies
{
	private final Collection<TopologyModel> connectedTopologies;

	public JadeTopologies()
	{
		this.connectedTopologies = new ArrayList<TopologyModel>();
	}
	
	public void topologyWasConnected(TopologyModel model)
	{
		if(!connectedTopologies.contains(model))
		{
			connectedTopologies.add(model);
		}
	}
	
	public Collection<TopologyModel> getConnectedTopologies()
	{
		return connectedTopologies;
	}
}
