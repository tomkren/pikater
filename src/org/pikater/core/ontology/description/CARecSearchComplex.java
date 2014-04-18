package org.pikater.core.ontology.description;

import jade.util.leap.ArrayList;


/**
 * Created by Martin Pilat on 28.12.13.
 */
public class CARecSearchComplex implements IComputingAgent, IDataProvider {

    ArrayList errors;
    Search search;
    Recommender recommender;
    IComputingAgent computingAgent;

    ArrayList options;

    public ArrayList getErrors() {
        return errors;
    }

    public void setErrors(ArrayList errors) {
        this.errors = errors;
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    public Recommender getRecommender() {
        return recommender;
    }

    public void setRecommender(Recommender recommender) {
        this.recommender = recommender;
    }

    public ArrayList getOptions() {
        return options;
    }

    public void setOptions(ArrayList options) {
        this.options = options;
    }

    public IComputingAgent getComputingAgent() {
        return computingAgent;
    }

    public void setComputingAgent(IComputingAgent computingAgent) {
        this.computingAgent = computingAgent;
    }
}
