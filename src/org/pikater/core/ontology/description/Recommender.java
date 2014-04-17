package org.pikater.core.ontology.description;

import jade.content.Concept;

import java.util.ArrayList;

import org.pikater.core.dataStructures.options.StepanuvOption;
import org.pikater.core.ontology.messages.Option;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class Recommender implements Concept {

    String recommenderClass;
    ArrayList<Option> options;

    public String getRecommenderClass() {
        return recommenderClass;
    }

    public void setRecommenderClass(String recommenderClass) {
        this.recommenderClass = recommenderClass;
    }

    public ArrayList<Option> getOptions() {
        return options;
    }

    public void setParameters(ArrayList<Option> options) {
        this.options = options;
    }
}
