package org.pikater.shared.experiment.webformat;

import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.batchDescription.CARecSearchComplex;
import org.pikater.core.ontology.subtrees.batchDescription.ComputingAgent;
import org.pikater.core.ontology.subtrees.batchDescription.DataProcessing;
import org.pikater.core.ontology.subtrees.batchDescription.EvaluationMethod;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataProvider;
import org.pikater.core.ontology.subtrees.batchDescription.FileDataSaver;
import org.pikater.core.ontology.subtrees.batchDescription.Recommend;
import org.pikater.core.ontology.subtrees.batchDescription.Search;
import org.pikater.shared.logging.PikaterLogger;

public enum BoxType
{
	INPUT(FileDataProvider.class),
	DATAPROCESSING(DataProcessing.class),
	CHOOSE(Recommend.class),
	SEARCH(Search.class),
	EVALUATION(EvaluationMethod.class),
	COMPUTE(ComputingAgent.class),
	TRIBOX(CARecSearchComplex.class),
	OUTPUT(FileDataSaver.class),
	MISCELLANEOUS(BoxType.class);
	
	private final Class<?> mappedOntologyClass;
	
	private BoxType(Class<?> mappedOntologyClass)
	{
		this.mappedOntologyClass = mappedOntologyClass;
	}
	
	public Class<?> toOntologyClass()
	{
		return mappedOntologyClass;
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
		return BoxType.MISCELLANEOUS;
	}
}