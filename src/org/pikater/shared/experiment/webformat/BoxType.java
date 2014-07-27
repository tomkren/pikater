package org.pikater.shared.experiment.webformat;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataProvider;
import org.pikater.core.ontology.subtrees.batchDescription.Recommend;
import org.pikater.core.ontology.subtrees.batchDescription.Search;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.web.sharedresources.ThemeResources;

public enum BoxType
{
	INPUT(FileDataProvider.class, ThemeResources.relPath_IMG_boxInputIcon), // TODO: simple file icon
	CHOOSE(Recommend.class, ThemeResources.relPath_IMG_boxRecommenderIcon),
	COMPUTE(ComputingAgent.class, ThemeResources.relPath_IMG_boxComputingIcon),
	SEARCH(Search.class, ThemeResources.relPath_IMG_boxSearcherIcon),
	// SAVE(FileDataSaver.class, MyResources.), // TODO: simple file icon
	
	/*
	TROJKRABICKA
	EVALUATION
	DATAPROCESSING
	*/
	
	MULTIBOX(BoxType.class, ThemeResources.relPath_IMG_boxWrapperIcon); // wrappers are never going to have their own ontologies anyway
	
	private final Class<?> mappedOntologyClass;
	private final String pictureURL;
	
	private BoxType(Class<?> mappedOntologyClass, String relativePicturePath)
	{
		this.mappedOntologyClass = mappedOntologyClass;
		this.pictureURL = ThemeResources.getVaadinRelativePathForResource(relativePicturePath);
	}
	
	public String toOntology()
	{
		return mappedOntologyClass.getName();
	}
	
	public String toPictureURL()
	{
		return pictureURL;
	}
	
	public static String[] getAllPictureURLs()
	{
		List<String> result = new ArrayList<String>();
		for(BoxType type : values())
		{
			result.add(type.toPictureURL());
		}
		return result.toArray(new String[0]);
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