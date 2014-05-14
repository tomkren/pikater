package org.pikater.shared.experiment.webformat;

import java.io.Serializable;

public class BoxInfo implements Serializable
{
	private static final long serialVersionUID = 7775620949481137845L;
	
	public String name;
	public BoxType type;
	public String picture;
	public String description;
	
	public String ontology;
	public String agentClass;
	
	// private final Collection<AbstractOption> options;
	// private final Collection<AbstractSlot> inputSlots;
	// private final Collection<AbstractSlot> outputSlots;
	
	/** Protected Ctor keeps GWT happy */
	protected BoxInfo()
	{
	}
	
	public BoxInfo(String ontology, String agentClass, String name, BoxType type, String picture, String description)
	{
		this.name = name;
		this.type = type;
		this.picture = picture;
		this.description = description;
		
		this.ontology = ontology;
		this.agentClass = agentClass;
		
		/*
		this.options = new ArrayList<AbstractOption>();
		this.inputSlots = new ArrayList<AbstractSlot>();
		this.outputSlots = new ArrayList<AbstractSlot>();
		*/
	}
	
	public int getID()
	{
		return this.hashCode();
	}

	public String getOntology()
	{
		return ontology;
	}

	public String getAgentClass()
	{
		return agentClass;
	}

	public String getName()
	{
		return name;
	}

	public BoxType getType()
	{
		return type;
	}

	public String getPicture()
	{
		return picture;
	}

	public String getDescription()
	{
		return description;
	}
	
	/*
	public void addOption(AbstractOption option)
	{
		this.options.add(option);
	}
	
	public void addInputSlot(AbstractSlot slot)
	{
		this.inputSlots.add(slot);
	}
	
	public void addOutputSlot(AbstractSlot slot)
	{
		this.outputSlots.add(slot);
	}
	*/
}
