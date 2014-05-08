package org.pikater.shared.experiment.webformat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoxInfoCollection implements Serializable
{
	private static final long serialVersionUID = 3532490361387785163L;
	
	public Map<String, BoxInfo> boxInfo;
	
	/**
	 * Default constructor keeps GWT and Vaadin happy.
	 */
	protected BoxInfoCollection()
	{
		this.boxInfo = new HashMap<String, BoxInfo>(); 
	}
	
	public BoxInfoCollection(Collection<BoxInfo> definitions)
	{
		this();
		
		this.boxInfo.clear();
		for(BoxInfo definition : definitions)
		{
			this.boxInfo.put(definition.getOntology(), definition);
		}
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
	
	public BoxInfo getByID(String ontologyClassName)
	{
		// TODO:
		
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
		
		return this.boxInfo.get(ontologyClassName);
	}
}