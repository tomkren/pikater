package org.pikater.core.ontology.subtrees.batchDescription;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;


/**
 * Created by Martin Pilat on 28.12.13.
 */
public class Search extends DataProcessing implements IErrorProvider {

	private static final long serialVersionUID = 7856131679884259768L;
	
	private String searchClass;
    private List<NewOption> options;

    public Search() {
    	this.options = new ArrayList<NewOption>();
    }
    
    public String getAgentType() {
        return searchClass;
    }
	public void setAgentType(String agentType) {
        this.searchClass = agentType;
    }

    public List<NewOption> getOptions() {
        return options;
    }
    
    public void setOptions(List<NewOption> options) {
    	if (options == null) {
    		throw new NullPointerException("Argument options can't be null");
    	}
        this.options = options;
    }
    
    public void addOption(NewOption option) {
    	if (option == null) {
    		throw new NullPointerException("Argument option can't be null");
    	}
        this.options.add(option);
    }

	@Override
	public List<NewOption> exportAllOptions() {		
		return this.options;
	}
	@Override
	public void importAllOptions(List<NewOption> options) {
		if (options == null) {
			throw new IllegalArgumentException("Argument options is not null");
		}
		this.options = options;
	}
	
	@Override
	public List<ErrorSourceDescription> exportAllErrors() {
		return new ArrayList<ErrorSourceDescription>();
	}
	@Override
	public void importAllErrors(List<ErrorSourceDescription> errors) {
		
		if (errors != null && !errors.isEmpty()) {
			throw new IllegalArgumentException("Argument errors can be only null");
		}
	}
	
	@Override
	public List<DataSourceDescription> exportAllDataSourceDescriptions() {
		return new ArrayList<DataSourceDescription>();
	}
	@Override
	public void importAllDataSourceDescriptions(List<DataSourceDescription> dataSourceDescriptions) {
		
		if (dataSourceDescriptions != null && !dataSourceDescriptions.isEmpty()) {
			throw new IllegalArgumentException("Argument universalInputSlots can be only null");
		}
	}
	
	public Search clone() {
		
		NewOptions optionsOnt = new NewOptions(this.options);
		
		Search search = new Search();
		search.setId(this.getId());
		search.setAgentType(searchClass);
		search.setOptions(optionsOnt.clone().getOptions());
		return search;
	}
}
