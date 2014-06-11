package org.pikater.shared.experiment.webformat;

import java.lang.reflect.Method;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataProvider;
import org.pikater.core.ontology.subtrees.batchDescription.Recommend;
import org.pikater.core.ontology.subtrees.batchDescription.Search;
import org.pikater.shared.logging.PikaterLogger;

public enum BoxType
{
	INPUT(FileDataProvider.class, null),
	SEARCHER(Search.class, null),
	COMPUTING(ComputingAgent.class, null),
	RECOMMENDER(Recommend.class, null),
	METHOD(Method.class, null),
	VISUALIZER(null, null), // TODO: change this properly
	WRAPPER(null, null);
	
	private final Class<?> mappedOntologyClass;
	private final String pictureURL;
	
	private BoxType(Class<?> mappedOntologyClass, String pictureURL)
	{
		this.mappedOntologyClass = mappedOntologyClass;
		this.pictureURL = pictureURL;
	}
	
	public String toOntology()
	{
		return mappedOntologyClass.getName();
	}
	
	public String toPictureURL()
	{
		return pictureURL;
	}
	
	public static BoxType fromAgentInfo(AgentInfo info)
	{
		try
		{
			return fromOntologyClass(Class.forName(info.getOntologyClassName()));
		}
		catch (ClassNotFoundException e)
		{
			PikaterLogger.logThrowable(String.format("No box type is mapped to '%s'.", info.getOntologyClassName()), e);
			return null;
		}
	}
	
	public static BoxType fromOntologyClass(Class<?> ontologyClass)
	{
		for(BoxType result : values())
		{
			if(result.mappedOntologyClass.equals(ontologyClass))
			{
				return result;
			}
		}
		return null;
	}
}