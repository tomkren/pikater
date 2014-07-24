package org.pikater.core.ontology.subtrees.batchDescription;


import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.values.StringValue;

public class EvaluationMethod  extends DataProcessing implements IDataProvider {

	private static final long serialVersionUID = -9024769565945696142L;
	private String type;

	private List<NewOption> options;

	public EvaluationMethod() {
		this.type = "Standard";
		this.options = new ArrayList<NewOption>();
	}
	public EvaluationMethod(String type) {
		this.type = type;
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

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public List<NewOption> exportAllOptions() {

		NewOption typeOption =
				new NewOption("type",type);
		
		List<NewOption> options = new ArrayList<NewOption>();
		options.add(typeOption);
		options.addAll(this.options);
		return options;
	}
	@Override
	public void importAllOptions(List<NewOption> options) {
		
		NewOptions optionsOntol = new NewOptions(options);
		NewOption optMethod = optionsOntol.getOptionByName("type");
		StringValue valueMethod = (StringValue)
				optMethod.toSingleValue().getCurrentValue();
		this.type = valueMethod.getValue();
		
		options.remove(optMethod);
		this.options = options;
	}
	
	@Override
	public List<ErrorDescription> exportAllErrors() {
		return new ArrayList<ErrorDescription>();
	}
	@Override
	public void importAllErrors(List<ErrorDescription> errors) {
	}
	
	@Override
	public List<DataSourceDescription> exportAllDataSourceDescriptions() {
		return new ArrayList<DataSourceDescription>();
	}
	@Override
	public void importAllDataSourceDescriptions(
			List<DataSourceDescription> dataSourceDescriptions) {
	}

}
