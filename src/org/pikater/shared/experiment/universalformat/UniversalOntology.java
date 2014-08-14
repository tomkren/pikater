package org.pikater.shared.experiment.universalformat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
			throw new NullPointerException("Argument options can't be null");
		}
		else
		{
			this.options = options;
		}
	}

	public Collection<UniversalConnector> getErrors() {
		return inputErrorSlots;
	}

	public void setErrors(Collection<UniversalConnector> errors) {
		this.inputErrorSlots.clear();
		this.inputErrorSlots.addAll(errors);
	}

	/**
	 * Gets edges that lead to the {@link UniversalElement element} containing
	 * this instance.
	 */
	public Collection<UniversalConnector> getInputDataSlots() {
		return inputDataSlots;
	}

	public void addInputDataSlot(UniversalConnector connector) {
		inputDataSlots.add(connector);
	}

	public void addInputDataSlots(List<UniversalConnector> connectors) {
		inputDataSlots.addAll(connectors);
	}
}