package org.pikater.core.ontology.subtrees.batchdescription;

import org.pikater.core.ontology.subtrees.newoption.base.NewOption;
import org.pikater.shared.util.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin Pilat on 28.12.13.
 */
public class Recommend extends DataProcessing {

	private static final long serialVersionUID = -1204258141585020540L;

	private String recommenderClass;
	private List<NewOption> options;
	private List<ErrorSourceDescription> errors;

	public Recommend() {
		this.options = new ArrayList<NewOption>();
		this.errors = new ArrayList<ErrorSourceDescription>();
	}

	public String getAgentType() {
		return recommenderClass;
	}

	public void setAgentType(String recommenderClass) {
		this.recommenderClass = recommenderClass;
	}

	public List<NewOption> getOptions() {
		return options;
	}

	public void setOptions(List<NewOption> options) {
		this.options = options;
	}

	public void addOption(NewOption option) {

		if (option == null) {
			throw new IllegalArgumentException("Argument option can't be null");
		}
		this.options.add(option);
	}

	public List<ErrorSourceDescription> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorSourceDescription> errors) {
		this.errors = errors;
	}

	@Override
	public List<NewOption> exportAllOptions() {
		return this.options;
	}

	@Override
	public void importAllOptions(List<NewOption> options) {

		if (options == null) {
			throw new IllegalArgumentException("Argument options can't be null");
		}

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
		return new ArrayList<DataSourceDescription>();
	}

	@Override
	public void importAllDataSourceDescriptions(
			List<DataSourceDescription> dataSourceDescriptions) {

		if (dataSourceDescriptions != null && !dataSourceDescriptions.isEmpty()) {
			throw new IllegalArgumentException(
					"Argument dataSourceDescriptions can be only null");
		}
	}

	@Override
	public Recommend clone() {
		Recommend recommend = (Recommend) super.clone();
		recommend.setId(this.getId());
		recommend.setAgentType(this.recommenderClass);
		recommend.setOptions(CollectionUtils.deepCopy(options));
		recommend.setErrors(CollectionUtils.deepCopy(errors));
		return recommend;
	}

}
