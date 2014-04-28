package org.pikater.core.ontology.description;

import org.pikater.core.ontology.messages.Option;
import org.pikater.shared.database.experiment.UniversalElement;
import org.pikater.shared.database.experiment.UniversalElementWrapper;

import jade.content.Concept;
import jade.util.leap.ArrayList;


/**
 * Created by Martin Pilat on 28.12.13.
 */
public class Recommend extends AbstractDataProcessing {

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

	@Override
	UniversalElementWrapper exportUniversalElement() {
		
		Option recommenderClassOption = new Option();
		recommenderClassOption.setName("recommenderClass");
		recommenderClassOption.setValue(recommenderClass);
		
		ArrayList options = new ArrayList();
		options.add(recommenderClassOption);
		
		UniversalElement element = new UniversalElement();
		element.setType(this.getClass());
		element.setOptions(options);
		
		UniversalElementWrapper wrapper =
				new UniversalElementWrapper();
		wrapper.setElement(element);
		
		return wrapper;
	}
}
