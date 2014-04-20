package org.pikater.core.ontology.description;

import org.pikater.core.ontology.messages.Option;

import jade.util.leap.ArrayList;


/**
 * Created by Martin Pilat on 28.12.13.
 */
public class CARecSearchComplex extends AbstractDataProcessing implements IComputingAgent, IDataProvider {

	ArrayList options;
    ArrayList errors;

    Search search;
    Recommen recommender;
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

    public Recommen getRecommender() {
        return recommender;
    }
    public void setRecommender(Recommen recommender) {
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

}
