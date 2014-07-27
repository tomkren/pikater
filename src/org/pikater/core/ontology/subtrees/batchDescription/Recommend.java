package org.pikater.core.ontology.subtrees.batchDescription;

import org.pikater.core.ontology.subtrees.newOption.base.NewOption;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Martin Pilat on 28.12.13.
 */
public class Recommend extends DataProcessing {

	private static final long serialVersionUID = -1204258141585020540L;

	private String recommenderClass;
    private List<NewOption> options = new ArrayList<NewOption>();
    private ErrorDescription errorDescription;

    public String getRecommenderClass() {
        return recommenderClass;
    }

    public void setRecommenderClass(String recommenderClass) {
        this.recommenderClass = recommenderClass;
    }

    public List<NewOption> getOptions() {
        return options;
    }
    public void setOptions(ArrayList<NewOption> options) {
        this.options = options;
    }
    public void addOption(NewOption option) {
		
    	if (option == null) {
			throw new IllegalArgumentException("Argument option can't be null");
		}
        this.options.add(option);
    }

	@Override
	public List<NewOption> exportAllOptions() {
		
		NewOption recommenderClassOption = new NewOption("recommenderClass",recommenderClass);
		
		List<NewOption> options = new ArrayList<NewOption>();
		options.add(recommenderClassOption);
		options.addAll(this.options);
		return options;
	}
	@Override
	public void importAllOptions(List<NewOption> options) {
		
    	if (options == null) {
    		throw new IllegalArgumentException("Argument options can't be null");
    	}
    	
		this.options = options;
	}

	@Override
	public List<ErrorDescription> exportAllErrors() {
		return new ArrayList<ErrorDescription>();
	}
	@Override
	public void importAllErrors(List<ErrorDescription> errors) {
		
		if (errors != null && !errors.isEmpty()) {
			new IllegalArgumentException("Argument errors can be only null");
		}
	}

	@Override
	public List<DataSourceDescription> exportAllDataSourceDescriptions() {
		return new ArrayList<DataSourceDescription>();
	}
	@Override
	public void importAllDataSourceDescriptions(List<DataSourceDescription> dataSourceDescriptions) {
		
		if (dataSourceDescriptions != null && !dataSourceDescriptions.isEmpty()) {
			new IllegalArgumentException("Argument dataSourceDescriptions can be only null");
		}
	}

    public ErrorDescription getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(ErrorDescription errorDescription) {
        this.errorDescription = errorDescription;
    }
}
