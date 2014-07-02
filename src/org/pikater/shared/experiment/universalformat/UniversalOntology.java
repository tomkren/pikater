package org.pikater.shared.experiment.universalformat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.pikater.core.ontology.subtrees.batchDescription.ErrorDescription;
import org.pikater.core.ontology.subtrees.option.Option;

public class UniversalOntology {
	private Class<?> type;
	private int id;

	private final Collection<UniversalConnector> inputSlots;

	private final Collection<ErrorDescription> errors;
	private final Collection<Option> options;

	public UniversalOntology() {
		this.inputSlots = new ArrayList<UniversalConnector>();
		this.errors = new ArrayList<ErrorDescription>();
		this.options = new ArrayList<Option>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public Collection<Option> getOptions() {
		return options;
	}

	public void setOptions(Collection<Option> options) {
		if (options == null) {
			throw new NullPointerException("Argument options can't be null");
		}
		this.options.clear();
		this.options.addAll(options);
	}

	public Option getOptionByName(String name) {

		for (Option optionI : getOptions()) {
			if (optionI.getName().equals(name)) {
				return optionI;
			}
		}
		return null;
	}

	public Collection<ErrorDescription> getErrors() {
		return errors;
	}

	public void setErrors(Collection<ErrorDescription> errors) {
		this.errors.clear();
		this.errors.addAll(errors);
	}

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