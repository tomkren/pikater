package org.pikater.shared.experiment.webformat;

import java.util.ArrayList;
import java.util.Collection;

import org.pikater.shared.experiment.webformat.BoxInfo.BoxType;
import org.pikater.shared.util.BidiMap;

public class WebBoxInfoProvider
{
	private static BidiMap<String, BoxInfo> boxInfo = new BidiMap<String, BoxInfo>();

	public static void setBoxDefinitions(Collection<BoxInfo> definitions)
	{
		boxInfo.clear();
		for(BoxInfo definition : definitions)
		{
			boxInfo.put(definition.ontology, definition);
		}
	}
	
	public Collection<BoxInfo> getByType(BoxType type)
	{
		Collection<BoxInfo> result = new ArrayList<BoxInfo>();
		for(BoxInfo info : boxInfo.valueSet())
		{
			if(info.type == type)
			{
				result.add(info);
			}
		}
		return result;
	}
	
	public BoxInfo getByID()
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
		
		return null;
	}
}