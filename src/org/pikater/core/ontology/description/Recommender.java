package org.pikater.core.ontology.description;

import jade.content.Concept;

import java.util.ArrayList;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class Recommender implements Concept {

    String recommenderClass;
    ArrayList<Parameter> parameters;

    public String getRecommenderClass() {
        return recommenderClass;
    }

    public void setRecommenderClass(String recommenderClass) {
        this.recommenderClass = recommenderClass;
    }

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<Parameter> parameters) {
        this.parameters = parameters;
    }
}
