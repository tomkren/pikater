package org.pikater.core.ontology.subtrees.batchdescription;

import java.util.List;
import java.util.ArrayList;

import org.pikater.core.CoreConstant;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;
import org.pikater.shared.util.collections.CollectionUtils;

/**
 * Ontology represents the special part of Batch which is connecting
 * {@link ComputingAgent}, {@link Search} and {@link Recommend}.
 * 
 * @author Martin Pilat
 * @date 28.12.13
 * 
 */
public class CARecSearchComplex extends DataProcessing implements
		IComputingAgent, IDataProvider {

	private static final long serialVersionUID = -913470799010962236L;

	private List<NewOption> options;
	private List<ErrorSourceDescription> errors;

	private Search search;
	private Recommend recommender;
	private IComputingAgent computingAgent;

	/**
	 * Constructor
	 */
	public CARecSearchComplex() {

		this.options = new ArrayList<NewOption>();
		this.errors = new ArrayList<ErrorSourceDescription>();
	}

	/**
	 * Get errors
	 */
	public List<ErrorSourceDescription> getErrors() {
		return errors;
	}

	/**
	 * Set errors
	 */
	public void setErrors(List<ErrorSourceDescription> errors) {
		if (errors == null) {
			throw new IllegalArgumentException("Argument errors can't be null");
		}
		this.errors = errors;
	}

	/**
	 * Add a error
	 */
	public void addError(ErrorSourceDescription error) {
		if (error == null) {
			throw new IllegalArgumentException("Argument error can't be null");
		}
		this.errors.add(error);
	}

	/**
	 * Get the {@link ComputingAgent}
	 */
	public IComputingAgent getComputingAgent() {
		return computingAgent;
	}

	/**
	 * Set the {@link ComputingAgent}
	 */
	public void setComputingAgent(IComputingAgent computingAgent) {
		this.computingAgent = computingAgent;
	}

	/**
	 * Get the {@link Search}
	 */
	public Search getSearch() {
		return search;
	}

	/**
	 * Set the {@link Search}
	 */
	public void setSearch(Search search) {
		this.search = search;
	}

	/**
	 * Get the {@link Recommender}
	 */
	public Recommend getRecommender() {
		return recommender;
	}

	/**
	 * Set the {@link Recommender}
	 */
	public void setRecommender(Recommend recommender) {
		this.recommender = recommender;
	}

	/**
	 * Get the {@link Options}
	 */
	public List<NewOption> getOptions() {
		return options;
	}

	/**
	 * Set the {@link Options}
	 */
	public void setOptions(List<NewOption> options) {
		this.options = options;
	}

	/**
	 * Add the {@link Option}
	 */
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

		DataSourceDescription searchSlot = new DataSourceDescription();
		searchSlot.setInputType(CoreConstant.SlotContent.SEARCH.getSlotName());
		searchSlot.setOutputType(CoreConstant.SlotContent.SEARCH.getSlotName());
		searchSlot.setDataProvider(search);

		DataSourceDescription recommenderSlot = new DataSourceDescription();
		recommenderSlot.setInputType(
				CoreConstant.SlotContent.RECOMMEND.getSlotName());
		recommenderSlot.setOutputType(
				CoreConstant.SlotContent.RECOMMEND.getSlotName());
		recommenderSlot.setDataProvider(recommender);

		DataSourceDescription computingAgentSlot = new DataSourceDescription();
		computingAgentSlot.setInputType(
				CoreConstant.SlotContent.COMPUTATIONAGENT.getSlotName());
		computingAgentSlot.setOutputType(
				CoreConstant.SlotContent.COMPUTATIONAGENT.getSlotName());
		
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
	public void importAllDataSourceDescriptions(
			List<DataSourceDescription> dataSourceDescriptions) {

		String searchSlot = CoreConstant.SlotContent.SEARCH.getSlotName();
		String recommenderSlot =
				CoreConstant.SlotContent.RECOMMEND.getSlotName();
		String comAgentSlot =
				CoreConstant.SlotContent.COMPUTATIONAGENT.getSlotName();

		for (DataSourceDescription slotI : dataSourceDescriptions) {

			if (slotI.getInputType().equals(searchSlot)) {
				search = (Search) slotI.getDataProvider();
			}
			if (slotI.getInputType().equals(recommenderSlot)) {
				recommender = (Recommend) slotI.getDataProvider();
			}
			if (slotI.getInputType().equals(comAgentSlot)) {
				computingAgent = (IComputingAgent) slotI.getDataProvider();
			}
		}

	}

	@Override
	public CARecSearchComplex clone() {

		Search searchClone = null;
		if (search != null) {
			searchClone = search.clone();
		}

		Recommend recommendClone = null;
		if (recommender != null) {
			recommendClone = recommender.clone();
		}

		IComputingAgent computingAgentClone = null;
		if (computingAgent != null) {
			computingAgentClone = computingAgent.clone();
		}

		CARecSearchComplex result = (CARecSearchComplex) super.clone();
		result.setId(this.getId());
		result.setOptions(CollectionUtils.deepCopy(options));
		result.setErrors(CollectionUtils.deepCopy(errors));
		result.setSearch(searchClone);
		result.setRecommender(recommendClone);
		result.setComputingAgent(computingAgentClone);
		return result;
	}

}
