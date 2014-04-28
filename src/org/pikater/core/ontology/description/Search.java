package org.pikater.core.ontology.description;

import org.pikater.core.ontology.messages.Option;
import org.pikater.shared.database.experiment.UniversalElement;
import org.pikater.shared.database.experiment.UniversalElementWrapper;

import jade.content.Concept;
import jade.util.leap.ArrayList;


/**
 * Created by Martin Pilat on 28.12.13.
 */
public class Search extends AbstractDataProcessing {

	String searchClass;
    ArrayList options;

    public String getSearchClass() {
        return searchClass;
    }
    public void setSearchClass(String searchClass) {
        this.searchClass = searchClass;
    }

    public ArrayList getOptions() {
        return options;
    }
    public void setOptions(ArrayList options) {
        this.options = options;
    }
    public void addOption(Option option) {
        this.options.add(option);
    }

	@Override
	UniversalElementWrapper exportUniversalElement() {

		Option searchClassOption = new Option();
		searchClassOption.setName("searchClass");
		searchClassOption.setValue(searchClass);
		
		ArrayList options = new ArrayList();
		options.add(searchClassOption);

		UniversalElement element = new UniversalElement();
		element.setType(this.getClass());
		element.setOptions(options);
		
		UniversalElementWrapper wrapper = new UniversalElementWrapper();
		wrapper.setElement(element);
		
		return wrapper;
	}

}
