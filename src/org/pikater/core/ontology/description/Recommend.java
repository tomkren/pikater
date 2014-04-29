package org.pikater.core.ontology.description;

import org.pikater.core.ontology.messages.Option;
import org.pikater.shared.database.experiment.UniversalComputationDescription;
import org.pikater.shared.database.experiment.UniversalOntology;
import org.pikater.shared.database.experiment.UniversalElement;

import jade.util.leap.ArrayList;


/**
 * Created by Martin Pilat on 28.12.13.
 */
public class Recommend extends AbstractDataProcessing {

	private static final long serialVersionUID = -1204258141585020540L;

	private String recommenderClass;
    private ArrayList options;

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
	UniversalElement exportUniversalElement(
			UniversalComputationDescription uModel) {
		
		Option recommenderClassOption = new Option();
		recommenderClassOption.setName("recommenderClass");
		recommenderClassOption.setValue(recommenderClass);
		
		ArrayList options = new ArrayList();
		options.add(recommenderClassOption);
		
		UniversalOntology element = new UniversalOntology();
		element.setType(this.getClass());
		element.setOptions(options);
		
		UniversalElement wrapper =
				new UniversalElement(uModel);
		wrapper.setElement(element);
		
		return wrapper;
	}
}
