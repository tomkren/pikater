package org.pikater.core.ontology.subtrees.batchDescription;

import java.util.List;
import java.util.ArrayList;

import org.pikater.core.ontology.subtrees.batchDescription.export.Slot;
import org.pikater.core.ontology.subtrees.option.Option;



/**
 * Created by Martin Pilat on 28.12.13.
 */
public class CARecSearchComplex extends AbstractDataProcessing implements IComputingAgent, IDataProvider {

	private static final long serialVersionUID = -913470799010962236L;

	private List<Option> options = new ArrayList<Option>();
    private List<ErrorDescription> errors = new ArrayList<ErrorDescription>();

    private Search search;
    private Recommend recommender;
    private IComputingAgent computingAgent;

    public List<ErrorDescription> getErrors() {
        return errors;
    }
    public void setErrors(List<ErrorDescription> errors) {
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

    public Recommend getRecommender() {
        return recommender;
    }
    public void setRecommender(Recommend recommender) {
        this.recommender = recommender;
    }

    public List<Option> getOptions() {
        return options;
    }
    public void setOptions(List<Option> options) {
        this.options = options;
    }
    public void addOption(Option option) {
    	if (option == null) {
    		throw new NullPointerException("Argument option can't be null");
    	}
        this.options.add(option);
    }
	
	@Override
	public List<Option> getUniversalOptions() {
		return this.options;
	}
	@Override
	public void setUniversalOptions(List<Option> options) {
		this.options = options;	
	}
	
	@Override
	public List<ErrorDescription> getUniversalErrors() {
		return this.errors;
	}
	@Override
	public void setUniversalErrors(List<ErrorDescription> errors) {
		this.errors = errors;
	}
	
	@Override
	public List<Slot> getInputSlots() {
		
		Slot searchSlot = new Slot();
		searchSlot.setInputDataType("search");
		searchSlot.setOutputDataType("search");
		searchSlot.setAbstractDataProcessing(search);

		Slot recommenderSlot = new Slot();
		recommenderSlot.setInputDataType("recommender");
		recommenderSlot.setOutputDataType("recommender");
		recommenderSlot.setAbstractDataProcessing(recommender);

		Slot computingAgentSlot = new Slot();
		computingAgentSlot.setInputDataType("computingAgent");
		computingAgentSlot.setOutputDataType("computingAgent");
		computingAgentSlot.setAbstractDataProcessing(computingAgent);

		List<Slot> slots = new ArrayList<Slot>();
		slots.add(searchSlot);
		slots.add(recommenderSlot);
		slots.add(computingAgentSlot);
		
		return slots;
	}
	@Override
	public void setUniversalInputSlots(List<Slot> universalInputSlots) {
		
		for (Slot slotI : universalInputSlots) {
			if (slotI.getInputDataType().equals("search")) {
				search = (Search) slotI.getAbstractDataProcessing();
			}
			if (slotI.getInputDataType().equals("recommender")) {
				recommender = (Recommend) slotI.getAbstractDataProcessing();
			}
			if (slotI.getInputDataType().equals("computingAgent")) {
				computingAgent = (IComputingAgent) slotI.getAbstractDataProcessing();
			}
		}
		
	}

}
