package org.pikater.shared.experiment.webformat.server;

import java.util.HashMap;
import java.util.Map;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.Slot;

public class BoxInfoServer
{
	private final AgentInfo associatedAgent;
	private final Map<Slot, BoxSlotConnection> slotConnections;
	
	public BoxInfoServer(AgentInfo associatedAgent)
	{
		this.associatedAgent = associatedAgent;
		this.slotConnections = new HashMap<Slot, BoxSlotConnection>();
	}
	
	public AgentInfo getAssociatedAgent()
	{
		return associatedAgent;
	}
	
	public void connect(Slot localSlot, BoxSlotConnection externalSlot)
	{
		// TODO:
	}
	
	public void disconnect(Slot localSlot)
	{
		// TODO:
	}
	
	public boolean isLocalSlotConnected(Slot slot)
	{
		if(associatedAgent.getInputSlots().contains(slot) || associatedAgent.getOutputSlots().contains(slot))
		{
			return slotConnections.containsKey(slot);
		}
		else
		{
			throw new IllegalArgumentException("The given slot does NOT belong to this box.");
		}
	}
}