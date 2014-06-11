package org.pikater.core.ontology.subtrees.batchDescription;

import java.util.List;
import java.util.ArrayList;

import org.pikater.core.ontology.subtrees.option.Option;
import org.pikater.shared.experiment.universalformat.UniversalComputationDescription;
import org.pikater.shared.experiment.universalformat.UniversalConnector;
import org.pikater.shared.experiment.universalformat.UniversalElement;
import org.pikater.shared.experiment.universalformat.UniversalOntology;



/**
 * Created by Martin Pilat on 28.12.13.
 */
public class CARecSearchComplex extends AbstractDataProcessing implements IComputingAgent, IDataProvider {

	private static final long serialVersionUID = -913470799010962236L;

	private List<Option> options;
    private List<ErrorDescription> errors;

    private Search search;
    private Recommend recommender;
    private IComputingAgent computingAgent;

    public List<ErrorDescription> getErrors() {
        return errors;
    }
    public void setErrors(List<ErrorDescription> errors) {
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

    public List<Option> getOptions() {
    	if (this.options == null) {
    		return new ArrayList<Option>();
    	}
        return options;
    }
    public void setOptions(List<Option> options) {
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

		UniversalOntology ontologyInfo = new UniversalOntology();
		ontologyInfo.setType(this.getClass());
		ontologyInfo.setOptions(options);
		ontologyInfo.setErrors(errors);

		if (computingAgent != null) {

			UniversalElement computingAgentInputSlot =
					((AbstractDataProcessing) computingAgent).exportUniversalElement(uModel);

			UniversalConnector universalComputingAgent = new UniversalConnector();
			universalComputingAgent.setInputDataType("computingAgent");
			universalComputingAgent.setFromElement(computingAgentInputSlot);
			
			ontologyInfo.addInputSlot(universalComputingAgent);
		}
		if (search != null) {
			
			UniversalElement searchInputSlot =
					search.exportUniversalElement(uModel);
					
			UniversalConnector universalSearch = new UniversalConnector();
			universalSearch.setInputDataType("search");
			universalSearch.setFromElement(searchInputSlot);
			
			ontologyInfo.addInputSlot(universalSearch);
		}

		if (recommender != null) {

			UniversalElement recommenderInputSlot =
					recommender.exportUniversalElement(uModel);

			UniversalConnector universalRecommend = new UniversalConnector();
			universalRecommend.setInputDataType("recommend");
			universalRecommend.setFromElement(recommenderInputSlot);

			ontologyInfo.addInputSlot(universalRecommend);
		}
		
		
		UniversalElement wrapper = new UniversalElement();
		wrapper.setOntologyInfo(ontologyInfo);
		uModel.addElement(wrapper);
		
		return wrapper;
	}

}
