package org.pikater.shared.experiment.universalformat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.pikater.core.ontology.subtrees.batchDescription.ErrorDescription;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;

public class UniversalOntology {
	
	private int id;
	private Class<?> ontologyClass;
	private String agentClass;

	/**
	 * List of edges that lead to the {@link UniversalElement element} containing
	 * this instance.
	 */
	private final Collection<UniversalConnector> inputSlots;

	private final Collection<ErrorDescription> errors;
	private NewOptions options;

	public UniversalOntology() {
		this.inputSlots = new ArrayList<UniversalConnector>();
		this.errors = new ArrayList<ErrorDescription>();
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

	public Collection<ErrorDescription> getErrors() {
		return errors;
	}

	public void setErrors(Collection<ErrorDescription> errors) {
		this.errors.clear();
		this.errors.addAll(errors);
	}

	/**
	 * Gets edges that lead to the {@link UniversalElement element} containing
	 * this instance.
	 */
	public Collection<UniversalConnector> getInputSlots() {
		return inputSlots;
	}

	public void addInputSlot(UniversalConnector connector) {
		inputSlots.add(connector);
	}

	public void addInputSlots(List<UniversalConnector> connectors) {
		inputSlots.addAll(connectors);
	}
}