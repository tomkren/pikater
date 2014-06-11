package org.pikater.web.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.shared.experiment.webformat.BoxType;
import org.pikater.shared.util.SimpleIDGenerator;

public class AgentInfoCollection
{
	private final SimpleIDGenerator idGenerator;
	private final Map<Integer, AgentInfo> boxes;
	
	public AgentInfoCollection()
	{
		this.idGenerator = new SimpleIDGenerator();
		this.boxes = new HashMap<Integer, AgentInfo>(); 
	}
	
	public boolean addDefinition(AgentInfo info)
	{
		if(boxes.values().contains(info))
		{
			return false;
		}
		else
		{
			boxes.put(idGenerator.getAndIncrement(), info);
			return true;
		}
	}
	
	public AgentInfo getByID(Integer id)
	{
		return boxes.get(id);
	}
	
	public AgentInfo getByOntologyClass(Class<?> ontologyClass)
	{
		for(AgentInfo info : boxes.values())
		{
			if(info.getOntologyClassName().equalsIgnoreCase(ontologyClass.getName()))
			{
				return info;
			}
		}
		return null;
	}
	
	public List<AgentInfo> getByType(BoxType type)
	{
		List<AgentInfo> result = new ArrayList<AgentInfo>();
		for(AgentInfo info : boxes.values())
		{
			if(BoxType.fromAgentInfo(info) == type)
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