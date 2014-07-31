package org.pikater.web.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.shared.experiment.webformat.BoxType;

public class AgentInfoCollection implements Iterable<AgentInfo>
{
	private final Set<AgentInfo> boxes;
	
	public AgentInfoCollection()
	{
		this.boxes = new HashSet<AgentInfo>(); 
	}
	
	@Override
	public Iterator<AgentInfo> iterator()
	{
		return boxes.iterator();
	}
	
	public boolean addDefinition(AgentInfo info)
	{
		return boxes.add(info);
	}
	
	public AgentInfo getUniqueByAgentName(String agentClassName)
	{
		List<AgentInfo> result = new ArrayList<AgentInfo>(); 
		for(AgentInfo info : boxes)
		{
			if(info.getAgentClassName().equals(agentClassName))
			{
				result.add(info);
			}
		}
		switch(result.size())
		{
			case 0:
				return null;
			case 1:
				return result.get(0);
			default:
				throw new IllegalStateException("Duplicate AgentInfo found by agent class name. Use ontology class name too to distinguish them.");
		}
	}
	
	public List<AgentInfo> getListByType(BoxType type)
	{
		List<AgentInfo> result = new ArrayList<AgentInfo>(); 
		for(AgentInfo info : boxes)
		{
			if(type.toOntologyClass().getName().equals(info.getOntologyClassName()))
			{
				result.add(info);
			}
		}
		return result;
	}

	/*
	private ArrayList<BoxInfo> getWrapperBoxes()
	{
		LogicalBoxDescription treeLogicalBox = new LogicalBoxDescription(
				"Complex", CARecSearchComplex.class,
				"Wraper for tree boxes, Computing, Search and Recomend");

		treeLogicalBox.setPicture("complex.jpg");

		BoxInfo treeBox = transformation(treeLogicalBox, BoxType.WRAPPER);

		ArrayList<BoxInfo> wrappers = new ArrayList<BoxInfo>();
		wrappers.add(treeBox);

		return wrappers;
	}
	*/
}