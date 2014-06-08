package org.pikater.core.ontology.subtrees.batchDescription;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.option.Option;
import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;
import org.pikater.shared.experiment.universalformat.UniversalElement;
import org.pikater.shared.experiment.universalformat.UniversalOntology;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class Search extends AbstractDataProcessing {

	private static final long serialVersionUID = 7856131679884259768L;
	
	private String searchClass;
    private List<Option> options;

    public String getSearchClass() {
        return searchClass;
    }
    public void setSearchClass(String searchClass) {
        this.searchClass = searchClass;
    }

    public List<Option> getOptions() {
    	if (this.options == null) {
    		return new ArrayList<Option>();
    	}
        return options;
    }
    public void setOptions(ArrayList<Option> options) {
        this.options = options;
    }
    public void addOption(Option option) {
    	if (this.options == null) {
    		this.options = new ArrayList<Option>();
    	}
        this.options.add(option);
    }

	@Override
	UniversalElement exportUniversalElement(
			UniversalComputationDescription uModel) {

		Option searchClassOption = new Option();
		searchClassOption.setName("searchClass");
		searchClassOption.setValue(searchClass);
		
		List<Option> options = new ArrayList<Option>();
		options.add(searchClassOption);

		UniversalOntology element = new UniversalOntology();
		element.setType(this.getClass());
		element.setOptions(options);
		
		UniversalElement wrapper = new UniversalElement(uModel);
		wrapper.setElement(element);
		
		return wrapper;
	}
}
