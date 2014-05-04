package org.pikater.shared.experiment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.pikater.core.ontology.agentInfo.AgentInfo;
import org.pikater.core.ontology.agentInfo.Slot;
import org.pikater.core.ontology.description.ComputingAgent;
import org.pikater.core.ontology.description.FileDataProvider;
import org.pikater.core.ontology.description.Recommend;
import org.pikater.core.ontology.description.Search;
import org.pikater.core.ontology.messages.option.Option;
import org.pikater.shared.experiment.webformat.BoxInfo;
import org.pikater.shared.experiment.webformat.WebBoxInfoProvider;
import org.pikater.shared.experiment.webformat.BoxInfo.BoxType;

public class CoreToWebBoxDefinitionsConverter
{
	private static Map<String, BoxType> ontologyToBoxTypeMapping = new HashMap<String, BoxType>();
	static
	{
		ontologyToBoxTypeMapping.put(FileDataProvider.class.getSimpleName(), BoxType.INPUT);
		ontologyToBoxTypeMapping.put(ComputingAgent.class.getSimpleName(), BoxType.COMPUTING);
		ontologyToBoxTypeMapping.put(Search.class.getSimpleName(), BoxType.SEARCHER);
		ontologyToBoxTypeMapping.put(Recommend.class.getSimpleName(), BoxType.RECOMMENDER);
		ontologyToBoxTypeMapping.put(Method.class.getSimpleName(), BoxType.METHOD);
		ontologyToBoxTypeMapping.put(FileDataProvider.class.getSimpleName(), BoxType.VISUALIZER); // TODO: an error?
	}
	
	public static void convert(AgentInfo... boxDefinitionsFromCore)
	{
		Collection<BoxInfo> boxInfoColl = new ArrayList<BoxInfo>();
		for (AgentInfo box : boxDefinitionsFromCore)
		{
			boxInfoColl.add(coreBoxToWebBox(box, ontologyToBoxTypeMapping.get(box.getOntologyClass())));
		}
		WebBoxInfoProvider.setBoxDefinitions(boxInfoColl);
	}
	
	@SuppressWarnings("unchecked")
	private static BoxInfo coreBoxToWebBox(AgentInfo coreBox, BoxType type)
	{
		BoxInfo box = new BoxInfo(coreBox.getOntologyClass(), coreBox.getAgentClass(), coreBox.getName(), type, coreBox.getPicture(), coreBox.getDescription());
		
		for(Option option : (Collection<Option>) coreBox.getOptions())
		{
			// TODO:
		}
		for(Slot slot : (Collection<Slot>) coreBox.getInputSlots())
		{
			// TODO:
		}
		for(Slot slot : (Collection<Slot>) coreBox.getOutputSlots())
		{
			// TODO:
		}
		return box;
	}
}
