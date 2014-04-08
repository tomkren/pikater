package org.pikater.core.ontology.description;

import java.util.ArrayList;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class CARecSearchComplex implements IComputingAgent, IDataProvider {

    ArrayList<ErrorDescription> errors;
    Search search;
    Recommender recommender;
    IComputingAgent computingAgent;

    ArrayList<Parameter> parameters;

    public ArrayList<ErrorDescription> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<ErrorDescription> errors) {
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

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<Parameter> parameters) {
        this.parameters = parameters;
    }

    public IComputingAgent getComputingAgent() {
        return computingAgent;
    }

    public void setComputingAgent(IComputingAgent computingAgent) {
        this.computingAgent = computingAgent;
    }
}
