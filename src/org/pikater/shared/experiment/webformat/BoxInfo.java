package org.pikater.shared.experiment.webformat;

import java.util.ArrayList;
import java.util.Collection;

import org.pikater.shared.experiment.webformat.options.AbstractOption;
import org.pikater.shared.experiment.webformat.slots.AbstractSlot;

public class BoxInfo
{
	// TODO: merge this with the core types?
	public enum BoxType
	{
		INPUT,
		SEARCHER,
		COMPUTING,
		RECOMMENDER,
		METHOD,
		VISUALIZER,
		WRAPPER
	}

	public final String ontology;
	public final String agentClass;
	
	public final String name;
	public final BoxType type;
	public final String picture;
	public final String description;
	
	private final Collection<AbstractOption> options;
	private final Collection<AbstractSlot> inputSlots;
	private final Collection<AbstractSlot> outputSlots;
	
	public BoxInfo(String ontology, String agentClass, String name, BoxType type, String picture, String description)
	{
		this.ontology = ontology;
		this.agentClass = agentClass;
		this.name = name;
		this.type = type;
		this.picture = picture;
		this.description = description;
		
		this.options = new ArrayList<AbstractOption>();
		this.inputSlots = new ArrayList<AbstractSlot>();
		this.outputSlots = new ArrayList<AbstractSlot>();
	}
	
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
}
