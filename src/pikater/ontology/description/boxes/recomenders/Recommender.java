package pikater.ontology.description.boxes.recomenders;

import java.util.ArrayList;

import pikater.ontology.description.Box;
import pikater.ontology.description.elements.Parameter;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class Recommender extends Box {

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
