package org.pikater.shared.experiment.webformat;

import java.util.Collection;

import org.pikater.shared.util.BidiMap;

public class WebBoxInfoProvider
{
	private static BidiMap<String, BoxInfo> boxInfo = new BidiMap<String, BoxInfo>();

	public static void setBoxDefinitions(Collection<BoxInfo> definitions)
	{
		boxInfo.clear();
		for(BoxInfo definition : definitions)
		{
			boxInfo.put(definition.ontology.getSimpleName(), definition);
		}
	}
	
	
	
	public BoxInfo getByType()
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