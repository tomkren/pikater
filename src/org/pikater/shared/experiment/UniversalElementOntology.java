package org.pikater.shared.experiment;

import java.util.ArrayList;
import java.util.Collection;

import org.pikater.core.ontology.subtrees.newOption.NewOptions;

/**
 * Additional experiment related information to {@link UniversalElement}.
 * 
 * TODO: fields of this class should be declared directly in {@link UniversalElement} 
 * 
 * @author SkyCrawl
 */
public class UniversalElementOntology
{
	/**
	 * Id the parent {@link UniversalElement element}.
	 */
	private int id;
	
	/**
	 * Ontology associated with the parent {@link UniversalElement element}.
	 */
	private Class<?> ontologyClass;
	
	/**
	 * Agent associated with the parent {@link UniversalElement element}.
	 */
	private String agentClass;

	/**
	 * Collection of edges that lead to the parent {@link UniversalElement element}. 
	 */
	private final Collection<UniversalElementConnector> inputDataSlots;
	
	/**
	 * Collection of error edges that lead to the parent {@link UniversalElement element}. 
	 */
	private final Collection<UniversalElementConnector> inputErrorSlots;
	
	/**
	 * Collection of options for the parent {@link UniversalElement element}. 
	 */
	private NewOptions options;

	public UniversalElementOntology()
	{
		this.inputDataSlots = new ArrayList<UniversalElementConnector>();
		this.inputErrorSlots = new ArrayList<UniversalElementConnector>();
		this.options = new NewOptions();
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public Class<?> getOntologyClass()
	{
		return ontologyClass;
	}

	public void setOntologyClass(Class<?> type)
	{
		this.ontologyClass = type;
	}

	public String getAgentClass()
	{
		return agentClass;
	}

	public void setAgentClass(String agentClass)
	{
		this.agentClass = agentClass;
	}

	public NewOptions getOptions()
	{
		return options;
	}

	public void setOptions(NewOptions options)
	{
		if (options == null)
		{
			throw new IllegalArgumentException("Argument options can't be null");
		}
		else
		{
			this.options = options;
		}
	}

	/**
	 * Gets data edges that lead to the parent {@link UniversalElement element}.
	 */
	public Collection<UniversalElementConnector> getInputDataSlots()
	{
		return inputDataSlots;
	}

	/**
	 * Gets error edges that lead to the parent {@link UniversalElement element}.
	 */
	public Collection<UniversalElementConnector> getInputErrorSlots()
	{
		return inputErrorSlots;
	}

	public void addInputDataSlot(UniversalElementConnector connector)
	{
		inputDataSlots.add(connector);
	}

	public void addInputErrorSlot(UniversalElementConnector connector)
	{
		inputErrorSlots.add(connector);
	}
}