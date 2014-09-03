package org.pikater.shared.experiment;

import java.util.ArrayList;
import java.util.Collection;

import org.pikater.core.ontology.subtrees.newOption.NewOptions;

public class UniversalOntology {
	
	private int id;
	private Class<?> ontologyClass;
	private String agentClass;

	/**
	 * List of edges that lead to the {@link UniversalElement element} containing
	 * this instance.
	 */
	private final Collection<UniversalConnector> inputDataSlots;
	private final Collection<UniversalConnector> inputErrorSlots;
	private NewOptions options;

	public UniversalOntology() {
		this.inputDataSlots = new ArrayList<UniversalConnector>();
		this.inputErrorSlots = new ArrayList<UniversalConnector>();
		this.options = new NewOptions();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Class<?> getOntologyClass() {
		return ontologyClass;
	}

	public void setOntologyClass(Class<?> type) {
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
		if(options == null)
		{
			throw new IllegalArgumentException("Argument options can't be null");
		}
		else
		{
			this.options = options;
		}
	}

	/**
	 * Gets edges that lead to the {@link UniversalElement element} containing
	 * this instance and connect data slots.
	 */
	public Collection<UniversalConnector> getInputDataSlots() {
		return inputDataSlots;
	}
	
	/**
	 * Gets edges that lead to the {@link UniversalElement element} containing
	 * this instance and connect error slots.
	 */
	public Collection<UniversalConnector> getInputErrorSlots() {
		return inputErrorSlots;
	}

	public void addInputDataSlot(UniversalConnector connector) {
		inputDataSlots.add(connector);
	}
	
	public void addInputErrorSlot(UniversalConnector connector) {
		inputErrorSlots.add(connector);
	}
}