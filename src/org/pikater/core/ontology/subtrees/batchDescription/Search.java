package org.pikater.core.ontology.subtrees.batchDescription;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.option.Option;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class Search extends DataProcessing {

	private static final long serialVersionUID = 7856131679884259768L;
	
	private String searchClass;
    private List<Option> options = new ArrayList<Option>();

    public String getSearchClass() {
        return searchClass;
    }
    public void setSearchClass(String searchClass) {
        this.searchClass = searchClass;
    }

    public List<Option> getOptions() {
        return options;
    }
    
    public void setOptions(ArrayList<Option> options) {
    	if (options == null) {
    		throw new NullPointerException("Argument options can't be null");
    	}
        this.options = options;
    }
    
    public void addOption(Option option) {
    	if (option == null) {
    		throw new NullPointerException("Argument option can't be null");
    	}
        this.options.add(option);
    }

	@Override
	public List<Option> exportAllOptions() {
		
		Option searchClassOption = new Option();
		searchClassOption.setName("searchClass");
		searchClassOption.setValue(searchClass);
		
		List<Option> options = new ArrayList<Option>();
		options.add(searchClassOption);
		options.addAll(this.options);
		
		return options;
	}
	@Override
	public void importAllOptions(List<Option> options) {
		
		for (Option optionI : options) {
			if (optionI.getName().equals("searchClass")) {
				this.searchClass = optionI.getValue();
			}
		}
	}
	
	@Override
	public List<ErrorDescription> exportAllErrors() {
		return new ArrayList<ErrorDescription>();
	}
	@Override
	public void importAllErrors(List<ErrorDescription> errors) {
		
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
	
}
