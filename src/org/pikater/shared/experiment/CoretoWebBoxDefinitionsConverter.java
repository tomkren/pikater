package org.pikater.shared.experiment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.pikater.core.dataStructures.options.OptionDefault;
import org.pikater.core.dataStructures.slots.Slot;
import org.pikater.core.ontology.description.ComputingAgent;
import org.pikater.core.ontology.description.FileDataProvider;
import org.pikater.core.ontology.description.Recommend;
import org.pikater.core.ontology.description.Search;
import org.pikater.core.options.LogicalBoxDescription;
import org.pikater.shared.experiment.webformat.BoxInfo;
import org.pikater.shared.experiment.webformat.WebBoxInfoProvider;
import org.pikater.shared.experiment.webformat.BoxInfo.BoxType;

public class CoretoWebBoxDefinitionsConverter
{
	private static Map<Class<? extends Object>, BoxType> ontologyToBoxTypeMapping = new HashMap<Class<? extends Object>, BoxType>();
	static
	{
		ontologyToBoxTypeMapping.put(FileDataProvider.class, BoxType.INPUT);
		ontologyToBoxTypeMapping.put(ComputingAgent.class, BoxType.COMPUTING);
		ontologyToBoxTypeMapping.put(Search.class, BoxType.SEARCHER);
		ontologyToBoxTypeMapping.put(Recommend.class, BoxType.RECOMMENDER);
		ontologyToBoxTypeMapping.put(Method.class, BoxType.METHOD);
		ontologyToBoxTypeMapping.put(FileDataProvider.class, BoxType.VISUALIZER); // TODO: an error?
	}
	
	public static void convert(LogicalBoxDescription... boxDefinitionsFromCore)
	{
		Collection<BoxInfo> boxInfoColl = new ArrayList<BoxInfo>();
		for (LogicalBoxDescription box : boxDefinitionsFromCore)
		{
			boxInfoColl.add(coreBoxToWebBox(box, ontologyToBoxTypeMapping.get(box.getOntology())));
		}
		WebBoxInfoProvider.setBoxDefinitions(boxInfoColl);
	}
	
	private static BoxInfo coreBoxToWebBox(LogicalBoxDescription coreBox, BoxType type)
	{
		BoxInfo box = new BoxInfo(coreBox.getOntology(), coreBox.getAgentClass(), coreBox.getName(), type, coreBox.getPicture(), coreBox.getDescription());
		for(OptionDefault option : coreBox.getOptions())
		{
			// TODO:
		}
		for(Slot slot : coreBox.getInputSlots())
		{
			// TODO:
		}
		for(Slot slot : coreBox.getOutputSlots())
		{
			// TODO:
		}
		return box;
	}
}
