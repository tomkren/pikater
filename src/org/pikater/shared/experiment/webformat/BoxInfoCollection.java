package org.pikater.shared.experiment.webformat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoxInfoCollection implements Serializable
{
	private static final long serialVersionUID = 3532490361387785163L;
	
	public Map<Integer, BoxInfo> boxInfo;
	
	/**
	 * Default constructor keeps GWT and Vaadin happy.
	 */
	public BoxInfoCollection()
	{
		this.boxInfo = new HashMap<Integer, BoxInfo>(); 
	}
	
	public boolean addDefinition(BoxInfo info)
	{
		Integer id = info.getID();
		if(boxInfo.containsKey(id))
		{
			return false;
		}
		else
		{
			this.boxInfo.put(id, info);
			return true;
		}
	}
	
	public BoxInfo getByOntology(Class<?> ontology)
	{
		for(BoxInfo info : boxInfo.values())
		{
			if(info.getOntology().equals(ontology.getName()))
			{
				return info;
			}
		}
		return null;
	}

	public List<BoxInfo> getByType(BoxType type)
	{
		List<BoxInfo> result = new ArrayList<BoxInfo>();
		for(BoxInfo info : boxInfo.values())
		{
			if(info.getType() == type)
			{
				result.add(info);
			}
		}
		return result;
	}
	
	public BoxInfo getByID(Integer id)
	{
		return this.boxInfo.get(id);
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