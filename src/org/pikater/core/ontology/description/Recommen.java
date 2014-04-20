package org.pikater.core.ontology.description;

import jade.content.Concept;
import jade.util.leap.ArrayList;


/**
 * Created by Martin Pilat on 28.12.13.
 */
public class Recommen extends AbstractDataProcessing {

    String recommenderClass;
    ArrayList options;

    public String getRecommenderClass() {
        return recommenderClass;
    }

    public void setRecommenderClass(String recommenderClass) {
        this.recommenderClass = recommenderClass;
    }

    public ArrayList getOptions() {
        return options;
    }

    public void setParameters(ArrayList options) {
        this.options = options;
    }
}
