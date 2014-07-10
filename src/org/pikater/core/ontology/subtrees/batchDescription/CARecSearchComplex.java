package org.pikater.core.ontology.subtrees.batchDescription;

import java.util.List;
import java.util.ArrayList;

import org.pikater.core.ontology.subtrees.newOption.NewOption;




/**
 * Created by Martin Pilat on 28.12.13.
 */
public class CARecSearchComplex extends DataProcessing implements IComputingAgent, IDataProvider {

	private static final long serialVersionUID = -913470799010962236L;

	private List<NewOption> options = new ArrayList<NewOption>();
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
	public List<ErrorDescription> exportAllErrors() {
		return this.errors;
	}
	@Override
	public void importAllErrors(List<ErrorDescription> errors) {
		this.errors = errors;
	}
	
	@Override
	public List<DataSourceDescription> exportAllDataSourceDescriptions() {
		
		DataSourceDescription searchSlot =
				new DataSourceDescription();
		searchSlot.setDataInputType("search");
		searchSlot.setDataOutputType("search");
		searchSlot.setDataProvider(search);
		
		DataSourceDescription recommenderSlot =
				new DataSourceDescription();
		recommenderSlot.setDataInputType("recommender");
		recommenderSlot.setDataOutputType("recommender");
		recommenderSlot.setDataProvider(recommender);
		
		DataSourceDescription computingAgentSlot =
				new DataSourceDescription();
		computingAgentSlot.setDataInputType("computingAgent");
		computingAgentSlot.setDataOutputType("computingAgent");
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
			
			if (slotI.getDataInputType().equals("search")) {
				search = (Search) slotI.getDataProvider();
			}
			if (slotI.getDataInputType().equals("recommender")) {
				recommender = (Recommend) slotI.getDataProvider();
			}
			if (slotI.getDataInputType().equals("computingAgent")) {
				computingAgent = (IComputingAgent) slotI.getDataProvider();
			}
		}
		
	}

}
