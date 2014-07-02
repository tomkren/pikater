package org.pikater.core.ontology.subtrees.batchDescription;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.ontology.subtrees.batchDescription.export.Slot;
import org.pikater.core.ontology.subtrees.option.Option;


/**
 * Created by Martin Pilat on 28.12.13.
 */
public class Recommend extends AbstractDataProcessing {

	private static final long serialVersionUID = -1204258141585020540L;

	private String recommenderClass;
    private List<Option> options;

    public String getRecommenderClass() {
        return recommenderClass;
    }

    public void setRecommenderClass(String recommenderClass) {
        this.recommenderClass = recommenderClass;
    }

    public List<Option> getOptions() {
    	if (this.options == null) {
    		return new ArrayList<Option>();
    	}
        return options;
    }
    public void setOptions(ArrayList<Option> options) {
        this.options = options;
    }
    public void addOption(Option option) {
    	if (this.options == null) {
    		this.options = new ArrayList<Option>();
    	}
        this.options.add(option);
    }

	@Override
	public List<Option> getUniversalOptions() {
		
		Option recommenderClassOption = new Option();
		recommenderClassOption.setName("recommenderClass");
		recommenderClassOption.setValue(recommenderClass);
		
		List<Option> options = new ArrayList<Option>();
		options.addAll(this.options);
		return options;
	}
	@Override
	public void setUniversalOptions(List<Option> options) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ErrorDescription> getUniversalErrors() {
		return new ArrayList<ErrorDescription>();
	}
	@Override
	public void setUniversalErrors(List<ErrorDescription> errors) {
		
		if (errors != null && !errors.isEmpty()) {
			new IllegalArgumentException("Argument errors can be only null");
		}
	}

	@Override
	public List<Slot> getInputSlots() {
		return new ArrayList<Slot>();
	}
	@Override
	public void setUniversalInputSlots(List<Slot> universalInputSlots) {
		
		if (universalInputSlots != null && !universalInputSlots.isEmpty()) {
			new IllegalArgumentException("Argument universalInputSlots can be only null");
		}
	}

}
