package org.pikater.core.ontology.subtrees.batchDescription;


import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.batchDescription.evaluationMethod.Standart;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;

public class EvaluationMethod  extends DataProcessing implements IDataProvider {

	private static final long serialVersionUID = -9024769565945696142L;
	
	private String agentType;
	private List<NewOption> options;

	public EvaluationMethod() {
		this.agentType = Standart.class.getName();
		this.options = new ArrayList<NewOption>();
	}
	public EvaluationMethod(String type) {
		this.agentType = type;
		this.options = new ArrayList<NewOption>();
	}

	public void setOptions(List<NewOption> options) {
		this.options = options;
	}
	public List<NewOption> getOptions() {
		return options;
	}
	
    public void addOption(NewOption option) {
        this.options.add(option);
    }

	public String getAgentType() {
		return agentType;
	}
	public void setAgentType(String type) {
		this.agentType = type;
	}
	
	@Override
	public List<NewOption> exportAllOptions() {
		return this.options;
	}
	@Override
	public void importAllOptions(List<NewOption> options) {
		this.options = options;
	}
	
	@Override
	public List<ErrorSourceDescription> exportAllErrors() {
		return new ArrayList<ErrorSourceDescription>();
	}
	@Override
	public void importAllErrors(List<ErrorSourceDescription> errors) {
	}
	
	@Override
	public List<DataSourceDescription> exportAllDataSourceDescriptions() {
		return new ArrayList<DataSourceDescription>();
	}
	@Override
	public void importAllDataSourceDescriptions(
			List<DataSourceDescription> dataSourceDescriptions) {
	}

	public EvaluationMethod clone() throws CloneNotSupportedException
	{
		NewOptions optionsOnt = new NewOptions(this.options);
		
		EvaluationMethod evaluation = (EvaluationMethod) super.clone();
		evaluation.setId(this.getId());
		evaluation.setAgentType(this.getAgentType());
		evaluation.setOptions(optionsOnt.clone().getOptions());
		
		return evaluation;
	}
}
