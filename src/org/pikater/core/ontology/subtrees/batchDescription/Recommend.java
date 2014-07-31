package org.pikater.core.ontology.subtrees.batchDescription;

import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.values.StringValue;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Martin Pilat on 28.12.13.
 */
public class Recommend extends DataProcessing {

	private static final long serialVersionUID = -1204258141585020540L;

	private String recommenderClass;
    private List<NewOption> options;
    private List<ErrorDescription> errors;    
   

	public Recommend() {
    	this.options = new ArrayList<>();
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

    public List<ErrorDescription> getErrors() {
		return errors;
	}
	public void setErrors(List<ErrorDescription> errors) {
		this.errors = errors;
	} 
    
	@Override
	public List<NewOption> exportAllOptions() {
		
		NewOption recommenderClassOption = new NewOption("recommenderClass", getAgentType());
		
		List<NewOption> options = new ArrayList<>();
		options.add(recommenderClassOption);
		options.addAll(this.options);
		return options;
	}
	@Override
	public void importAllOptions(List<NewOption> options) {
		
    	if (options == null) {
    		throw new IllegalArgumentException("Argument options can't be null");
    	}
    	
    	NewOptions importedOptions = new NewOptions(options);
    	NewOption recommenderClassOption =
    			importedOptions.getOptionByName("recommenderClass");
    	
    	StringValue recValue = (StringValue) recommenderClassOption.toSingleValue().getCurrentValue();
    	this.recommenderClass = recValue.getValue();
    	
    	options.remove(recommenderClassOption);
    	
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
		return new ArrayList<DataSourceDescription>();
	}
	@Override
	public void importAllDataSourceDescriptions(List<DataSourceDescription> dataSourceDescriptions) {
		
		if (dataSourceDescriptions != null && !dataSourceDescriptions.isEmpty()) {
			new IllegalArgumentException("Argument dataSourceDescriptions can be only null");
		}
	}

	public Recommend clone() {
		
		Recommend recommend = new Recommend();
		recommend.setAgentType(this.recommenderClass);
		NewOptions optionsOnt = new NewOptions(this.options);
		recommend.setOptions(optionsOnt.clone().getOptions());
		ErrorDescriptions errorOnt = new ErrorDescriptions(this.errors);
		recommend.setErrors(errorOnt.clone().getErrors());
		
		return recommend;
	}

}
