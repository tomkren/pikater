package org.pikater.web.experiment.server;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.web.experiment.IBoxInfoCommon;
import org.pikater.web.experiment.client.BoxInfoClient;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.ExpEditor;

public class BoxInfoServer implements IBoxInfoCommon<Integer>
{
	/*
	 * Read-only fields (except ID, which is only set once and then it is read-only).
	 */
	
	private Integer generatedUniqueID;
	private final BoxType boxType;
	private final AgentInfo associatedAgent;
	
	/*
	 * Mutable fields.
	 */
	private int posX;
	private int posY;
	private boolean registered;
	
	public BoxInfoServer(AgentInfo associatedAgent, int posX, int posY) throws CloneNotSupportedException
	{
		this.generatedUniqueID = null;
		this.boxType = BoxType.fromAgentInfo(associatedAgent);
		this.associatedAgent = associatedAgent.clone(); // each box needs to have a unique instance (to preserve original instances intact)
		this.posX = posX;
		this.posY = posY;
		this.registered = true;
	}
	
	//--------------------------------------------------
	// SOME BASIC GETTERS/SETTERS
	
	@Override
	public Integer getID()
	{
		return generatedUniqueID;
	}
	
	@Override
	public void setID(Integer id)
	{
		if(generatedUniqueID == null)
		{
			generatedUniqueID = id;
		}
		else
		{
			throw new IllegalStateException("ID has already been set.");
		}
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
	
	//--------------------------------------------------
	// SLOT-RELATED ROUTINES
	
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