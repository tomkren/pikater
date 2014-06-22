package org.pikater.shared.experiment.webformat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataProvider;
import org.pikater.core.ontology.subtrees.batchDescription.Recommend;
import org.pikater.core.ontology.subtrees.batchDescription.Search;
import org.pikater.shared.logging.PikaterLogger;
import org.pikater.web.vaadin.MyResources;
import org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.ExpEditor;

public enum BoxType
{
	INPUT(FileDataProvider.class, MyResources.relPath_IMG_boxInputIcon),
	SEARCH(Search.class, MyResources.relPath_IMG_boxSearcherIcon),
	COMPUTE(ComputingAgent.class, MyResources.relPath_IMG_boxComputingIcon),
	CHOOSE(Recommend.class, MyResources.relPath_IMG_boxRecommenderIcon),
	METHOD(Method.class, MyResources.relPath_IMG_boxMethodIcon),
	DISPLAY(ExpEditor.class, MyResources.relPath_IMG_boxVizualizerIcon), // TODO: change this properly
	MULTIBOX(BoxType.class, MyResources.relPath_IMG_boxWrapperIcon); // wrappers are never going to have their own ontologies anyway
	
	private final Class<?> mappedOntologyClass;
	private final String pictureURL;
	
	private BoxType(Class<?> mappedOntologyClass, String relativePicturePath)
	{
		this.mappedOntologyClass = mappedOntologyClass;
		this.pictureURL = MyResources.getVaadinRelativePathForResource(relativePicturePath);
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