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
	
	/**
	 * This method is guaranteed to return the request object or throw an exception
	 * otherwise.
	 * @param ontologyClassName
	 * @param agentClassName
	 * @return
	 */
	public AgentInfo getUnique(String ontologyClassName, String agentClassName)
	{
		List<AgentInfo> result = new ArrayList<AgentInfo>(); 
		for(AgentInfo info : boxes)
		{
			if(info.getOntologyClassName().equals(ontologyClassName) && 
					info.getAgentClassName().equals(agentClassName))
			{
				result.add(info);
			}
		}
		switch(result.size())
		{
			case 0:
				throw new IllegalStateException(String.format("No agent info was found for ontology class "
						+ "name '%s' and agent class name '%s'.", ontologyClassName, agentClassName));
			case 1:
				return result.get(0);
			default:
				throw new IllegalStateException("Duplicate AgentInfo found by ontology and agent class names.");
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