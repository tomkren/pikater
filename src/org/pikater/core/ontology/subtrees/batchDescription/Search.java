package org.pikater.core.ontology.subtrees.batchDescription;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.newOption.NewOptionList;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.values.StringValue;


/**
 * Created by Martin Pilat on 28.12.13.
 */
public class Search extends DataProcessing {

	private static final long serialVersionUID = 7856131679884259768L;
	
	private String searchClass;
    private List<NewOption> options = new ArrayList<NewOption>();

    public String getSearchClass() {
        return searchClass;
    }
    public void setSearchClass(String searchClass) {
        this.searchClass = searchClass;
    }

    public List<NewOption> getOptions() {
        return options;
    }
    
    public void setOptions(ArrayList<NewOption> options) {
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
		
		NewOption searchClassOption = new NewOption( "searchClass",searchClass);
		
		List<NewOption> options = new ArrayList<NewOption>();
		options.add(searchClassOption);
		options.addAll(this.options);
		
		return options;
	}
	@Override
	public void importAllOptions(List<NewOption> options) {
		
		NewOptionList optionsOnt = new NewOptionList(options);
		
		NewOption optSearchClass = optionsOnt.getOptionByName("searchClass");
		StringValue value = (StringValue) optSearchClass.toSingleValue().getCurrentValue();
		this.searchClass = value.getValue();

		options.remove(optSearchClass);
		this.options = options;
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
