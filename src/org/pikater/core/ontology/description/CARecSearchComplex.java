package org.pikater.core.ontology.description;

import org.pikater.core.ontology.messages.Option;
import org.pikater.shared.database.experiment.UniversalComputationDescription;
import org.pikater.shared.database.experiment.UniversalConnector;
import org.pikater.shared.database.experiment.UniversalOntology;
import org.pikater.shared.database.experiment.UniversalElement;

import jade.util.leap.ArrayList;


/**
 * Created by Martin Pilat on 28.12.13.
 */
public class CARecSearchComplex extends AbstractDataProcessing implements IComputingAgent, IDataProvider {

	private static final long serialVersionUID = -913470799010962236L;

	private ArrayList options;
    private ArrayList errors;

    private Search search;
    private Recommend recommender;
    private IComputingAgent computingAgent;

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
	UniversalElement exportUniversalElement(
			UniversalComputationDescription uModel) {

		UniversalOntology element = new UniversalOntology();
		element.setType(this.getClass());
		element.setOptions(options);
		element.setErrors(errors);

		if (computingAgent != null) {

			UniversalElement computingAgentInputSlot =
					((AbstractDataProcessing) computingAgent).exportUniversalElement(uModel);

			UniversalConnector universalComputingAgent = new UniversalConnector();
			universalComputingAgent.setInputDataType("computingAgent");
			universalComputingAgent.setUniversalDataProvider(computingAgentInputSlot);
			
			element.addInputSlot(universalComputingAgent);
		}
		if (search != null) {
			
			UniversalElement searchInputSlot =
					search.exportUniversalElement(uModel);
					
			UniversalConnector universalSearch = new UniversalConnector();
			universalSearch.setInputDataType("search");
			universalSearch.setUniversalDataProvider(searchInputSlot);
			
			element.addInputSlot(universalSearch);
		}

		if (recommender != null) {

			UniversalElement recommenderInputSlot =
					recommender.exportUniversalElement(uModel);

			UniversalConnector universalRecommend = new UniversalConnector();
			universalRecommend.setInputDataType("recommend");
			universalRecommend.setUniversalDataProvider(recommenderInputSlot);

			element.addInputSlot(universalRecommend);
		}
		
		
		UniversalElement wrapper =
				new UniversalElement(uModel);
		wrapper.setElement(element);
		
		return wrapper;
	}

}
