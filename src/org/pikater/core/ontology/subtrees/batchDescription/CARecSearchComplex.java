package org.pikater.core.ontology.subtrees.batchDescription;

import java.util.List;
import java.util.ArrayList;

import org.pikater.core.CoreConstant;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.shared.util.collections.CollectionUtils;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class CARecSearchComplex extends DataProcessing implements IComputingAgent, IDataProvider {

	private static final long serialVersionUID = -913470799010962236L;

	private List<NewOption> options;
    private List<ErrorSourceDescription> errors;

    private Search search;
    private Recommend recommender;
    private IComputingAgent computingAgent;

    public CARecSearchComplex() {
    	
    	this.options = new ArrayList<NewOption>();
    	this.errors = new ArrayList<ErrorSourceDescription>();
    }
    
    public List<ErrorSourceDescription> getErrors() {
        return errors;
    }
    public void setErrors(List<ErrorSourceDescription> errors) {
    	if (errors == null) {
    		throw new IllegalArgumentException("Argument errors can't be null");
    	}
        this.errors = errors;
    }
    public void addError(ErrorSourceDescription error) {
    	if (error == null) {
    		throw new IllegalArgumentException("Argument error can't be null");
    	}
        this.errors.add(error);
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

    public List<NewOption> getOptions() {
        return options;
    }
    public void setOptions(List<NewOption> options) {
        this.options = options;
    }
    public void addOption(NewOption option) {
    	if (option == null) {
    		throw new NullPointerException("Argument option can't be null");
    	}
        this.options.add(option);
    }
	
	@Override
	public List<NewOption> exportAllOptions() {
		return this.options;
	}
	@Override
	public void importAllOptions(List<NewOption> options) {
		this.options = options;	
	}
	
	@Override
	public List<ErrorSourceDescription> exportAllErrors() {
		return this.errors;
	}
	@Override
	public void importAllErrors(List<ErrorSourceDescription> errors) {
		this.errors = errors;
	}
	
	@Override
	public List<DataSourceDescription> exportAllDataSourceDescriptions() {
		
		DataSourceDescription searchSlot =
				new DataSourceDescription();
		searchSlot.setInputType(
				CoreConstant.SlotContent.SEARCH.getSlotName());
		searchSlot.setOutputType(
				CoreConstant.SlotContent.SEARCH.getSlotName());
		searchSlot.setDataProvider(search);
		
		DataSourceDescription recommenderSlot =
				new DataSourceDescription();
		recommenderSlot.setInputType(
				CoreConstant.SlotContent.RECOMMEND.getSlotName());
		recommenderSlot.setOutputType(
				CoreConstant.SlotContent.RECOMMEND.getSlotName());
		recommenderSlot.setDataProvider(recommender);
		
		DataSourceDescription computingAgentSlot =
				new DataSourceDescription();
		computingAgentSlot.setInputType(
				CoreConstant.SlotContent.COMPUTATION_AGENT.getSlotName());
		computingAgentSlot.setOutputType(
				CoreConstant.SlotContent.COMPUTATION_AGENT.getSlotName());
		computingAgentSlot.setDataProvider((IDataProvider) computingAgent);

		List<DataSourceDescription> slots = new ArrayList<DataSourceDescription>();
		if (search != null) {
			slots.add(searchSlot);
		}
		if (recommender != null) {
			slots.add(recommenderSlot);
		}
		if (computingAgent != null) {
			slots.add(computingAgentSlot);
		}
		return slots;
	}
	
	@Override
	public void importAllDataSourceDescriptions(List<DataSourceDescription> dataSourceDescriptions) {
		
		for (DataSourceDescription slotI : dataSourceDescriptions) {
			
			if (slotI.getInputType().equals(CoreConstant.SlotContent.SEARCH.getSlotName())) {
				search = (Search) slotI.getDataProvider();
			}
			if (slotI.getInputType().equals(CoreConstant.SlotContent.RECOMMEND.getSlotName())) {
				recommender = (Recommend) slotI.getDataProvider();
			}
			if (slotI.getInputType().equals(CoreConstant.SlotContent.COMPUTATION_AGENT.getSlotName())) {
				computingAgent = (IComputingAgent) slotI.getDataProvider();
			}
		}
		
	}

	@Override
	public CARecSearchComplex clone()
	{
		CARecSearchComplex result = (CARecSearchComplex) super.clone();
		result.setId(this.getId());
		result.setOptions(CollectionUtils.deepCopy(options));
		result.setErrors(CollectionUtils.deepCopy(errors));
		result.setSearch(search != null ? search.clone() : null);
		result.setRecommender(recommender != null ? recommender.clone() : null);
		result.setComputingAgent(computingAgent != null ? computingAgent.clone() : null);
		return result;
	}
	
}
