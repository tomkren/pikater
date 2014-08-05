package org.pikater.shared.experiment.webformat.server;

import org.pikater.core.ontology.subtrees.agentInfo.Slot;

public class BoxSlotConnection
{
	private final BoxInfoServer box;
	private final Slot slot;
	
	public BoxSlotConnection(BoxInfoServer box, Slot slot)
	{	
		this.box = box;
		this.slot = slot;
	}

	public BoxInfoServer getBox()
	{
		return box;
	}

	public Slot getSlot()
	{
		return slot;
	}
}