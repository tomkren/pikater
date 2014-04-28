package org.pikater.core.ontology.description;

import org.pikater.core.ontology.messages.Option;
import org.pikater.shared.database.experiment.UniversalConnector;
import org.pikater.shared.database.experiment.UniversalElement;
import org.pikater.shared.database.experiment.UniversalElementWrapper;
import org.pikater.shared.database.experiment.UniversalGui;

import jade.util.leap.ArrayList;


/**
 * Created by Martin Pilat on 28.12.13.
 */
public class CARecSearchComplex extends AbstractDataProcessing implements IComputingAgent, IDataProvider {

	ArrayList options;
    ArrayList errors;

    Search search;
    Recommend recommender;
    IComputingAgent computingAgent;

    public ArrayList getErrors() {
        return errors;
    }
    public void setErrors(ArrayList errors) {
        this.errors = errors;
    }

    public IComputingAgent getComputingAgent() {
        return computingAgent;
    }
    public void setComputingAgent(IComputingAgent computingAgent) {
        this.computingAgent = computingAgent;
    }
    
    public Search getSearch() {
        return search;
    }
    public void setSearch(Search search) {
        this.search = search;
    }

    public Recommend getRecommender() {
        return recommender;
    }
    public void setRecommender(Recommend recommender) {
        this.recommender = recommender;
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

		UniversalElement element = new UniversalElement();
		element.setType(this.getClass());
		element.setOptions(options);
		element.setErrors(errors);

		if (computingAgent != null) {

			UniversalElementWrapper computingAgentInputSlot =
					((AbstractDataProcessing) computingAgent).exportUniversalElement();

			UniversalConnector universalComputingAgent = new UniversalConnector();
			universalComputingAgent.setInputDataType("computingAgent");
			universalComputingAgent.setUniversalDataProvider(computingAgentInputSlot);
			
			element.addInputSlot(universalComputingAgent);
		}
		if (search != null) {
			
			UniversalElementWrapper searchInputSlot =
					search.exportUniversalElement();
					
			UniversalConnector universalSearch = new UniversalConnector();
			universalSearch.setInputDataType("search");
			universalSearch.setUniversalDataProvider(searchInputSlot);
			
			element.addInputSlot(universalSearch);
		}

		if (recommender != null) {

			UniversalElementWrapper recommenderInputSlot =
					recommender.exportUniversalElement();

			UniversalConnector universalRecommend = new UniversalConnector();
			universalRecommend.setInputDataType("recommend");
			universalRecommend.setUniversalDataProvider(recommenderInputSlot);

			element.addInputSlot(universalRecommend);
		}
		
		
		UniversalElementWrapper wrapper =
				new UniversalElementWrapper();
		wrapper.setElement(element);
		
		return wrapper;
	}

}
