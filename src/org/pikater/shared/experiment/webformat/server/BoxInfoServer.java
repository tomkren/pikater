package org.pikater.shared.experiment.webformat.server;

import java.util.HashMap;
import java.util.Map;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.Slot;
import org.pikater.shared.experiment.webformat.IBoxInfo;
import org.pikater.shared.experiment.webformat.client.BoxInfoClient;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.ExpEditor;

public class BoxInfoServer implements IBoxInfo<Integer>
{
	private Integer generatedUniqueID;
	private int posX;
	private int posY;
	private boolean registered;
	
	private final BoxType boxType;
	private final AgentInfo associatedAgent;
	private final Map<Slot, BoxSlotConnection> slotConnections;
	
	public BoxInfoServer(AgentInfo associatedAgent, int posX, int posY)
	{
		this.posX = posX;
		this.posY = posY;
		this.registered = true;
		this.boxType = BoxType.fromAgentInfo(associatedAgent);
		this.associatedAgent = associatedAgent;
		this.slotConnections = new HashMap<Slot, BoxSlotConnection>();
	}
	
	@Override
	public Integer getID()
	{
		return generatedUniqueID;
	}
	
	@Override
	public void setID(Integer id)
	{
		generatedUniqueID = id;
	}
	
	public int getPosX()
	{
		return posX;
	}

	public void setPosX(int posX)
	{
		this.posX = posX;
	}
	
	public int getPosY()
	{
		return posY;
	}

	public void setPosY(int posY)
	{
		this.posY = posY;
	}
	
	public boolean isRegistered()
	{
		return registered;
	}

	public void setRegistered(boolean registered)
	{
		this.registered = registered;
	}
	
	public AgentInfo getAssociatedAgent()
	{
		return associatedAgent;
	}
	
	public BoxType getBoxType()
	{
		return boxType;
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
	
	public void connect(Slot localSlot, BoxSlotConnection externalSlot)
	{
		// TODO:
	}
	
	public void disconnect(Slot localSlot)
	{
		// TODO:
	}
	
	public boolean isValid()
	{
		return true; // TODO
	}
	
	public BoxInfoClient toClientFormat()
	{
		return new BoxInfoClient(
				generatedUniqueID,
				boxType.name(),
				associatedAgent.getName(),
				posX,
				posY,
				ExpEditor.getBoxPictureURL(boxType)
		);
	}
}